package ir.andromeda.cartoonabad.feature.contacts

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.disposables.CompositeDisposable
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.common.asyncNetworkRequest
import ir.andromeda.cartoonabad.data.CartoonAbadEvent
import ir.andromeda.cartoonabad.data.message.MessageResponse
import ir.andromeda.cartoonabad.databinding.FragmentContactsBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class ContactsFragment : CartoonAbadFragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactsViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private var selectedTitle: String? = null
    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCanSendMessage()

        setAutoTextViewValues()

        binding.autoTvTopic.setOnItemClickListener { parent, _, position, _ ->
            selectedTitle = parent.getItemAtPosition(position) as String
        }

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        binding.btnSend.setOnClickListener {
            binding.etlEmail.error = null
            binding.etlMessage.error = null

            val email = binding.etlEmail.editText?.text.toString().trim()
            val message = binding.etlMessage.editText?.text.toString().trim()

            if (selectedTitle.isNullOrEmpty()) {
                Snackbar.make(
                    activity?.findViewById(R.id.contentRootView) as View,
                    getString(R.string.choose_topic),
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            if (email.isEmpty()) {
                binding.etlEmail.error = getString(R.string.do_not_leave_fields_empty)
            }

            if (message.isEmpty()) {
                binding.etlMessage.error = getString(R.string.do_not_leave_fields_empty)
            }

            if (!selectedTitle.isNullOrEmpty() && email.isNotEmpty() && message.isNotEmpty()) {
                viewModel.sendMessage(selectedTitle!!, message, email)
                    .asyncNetworkRequest()
                    .subscribe(object :
                        CartoonAbadSingleObserver<MessageResponse>(compositeDisposable) {
                        override fun onSuccess(t: MessageResponse) {
                            Timber.i(t.message)
                            Snackbar.make(
                                activity?.findViewById(R.id.contentRootView) as View,
                                t.message,
                                Snackbar.LENGTH_SHORT
                            ).show()

                            sharedPreferences.edit()
                                .putLong(
                                    "messageTime",
                                    Calendar.getInstance().timeInMillis + (86400000L * 2)
                                )
                                .apply()

                            findNavController().popBackStack()

                        }
                    })
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(cartoonAbadEvent: CartoonAbadEvent) {
        when (cartoonAbadEvent.type) {
            CartoonAbadEvent.Type.SIMPLE -> snackBar(
                cartoonAbadEvent.stringMessage
                    ?: getString(cartoonAbadEvent.resMessage)
            )

            else -> {}
        }

    }

    private fun checkCanSendMessage() {
        val time = sharedPreferences.getLong("messageTime", 0)
        if (time > Calendar.getInstance().timeInMillis) {
            binding.scrollView.visibility = View.GONE
            binding.messageView.visibility = View.VISIBLE
        }
    }

    private fun snackBar(message: String) {
        Snackbar.make(
            activity?.findViewById(R.id.contentRootView) as View, message, Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun setAutoTextViewValues() {
        val topics: MutableList<String> = ArrayList()
        topics.add(getString(R.string.error_in_purchase))
        topics.add(getString(R.string.error_in_app))
        topics.add(getString(R.string.suggest_cartoon))

        val adapter = ArrayAdapter(
            requireContext(), R.layout.item_topic, topics
        )
        binding.autoTvTopic.setAdapter(adapter)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()


        FirebaseAnalytics.getInstance(requireContext())
            .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, "ContactsFragment")
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.javaClass.simpleName)
            })
    }

}