package ir.andromeda.movieado.feature.contacts

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable
import ir.andromeda.movieado.R
import ir.andromeda.movieado.common.MovieadoFragment
import ir.andromeda.movieado.common.MovieadoSingleObserver
import ir.andromeda.movieado.common.asyncNetworkRequest
import ir.andromeda.movieado.data.MovieadoEvent
import ir.andromeda.movieado.data.message.MessageResponse
import ir.andromeda.movieado.databinding.FragmentContactsBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class ContactsFragment : MovieadoFragment() {

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

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

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
                snackBar(getString(R.string.choose_topic))
            }

            if (email.isEmpty()) {
                binding.etlEmail.error = getString(R.string.do_not_leave_fields_empty)
            }

            if (message.isEmpty()) {
                binding.etlMessage.error = getString(R.string.do_not_leave_fields_empty)
            }

            if (!selectedTitle.isNullOrEmpty() && email.isNotEmpty() && message.isNotEmpty()) {
                viewModel.sendMessage(JsonObject().apply {
                    addProperty("title", selectedTitle!!)
                    addProperty("text", message)
                    addProperty("email", email)
                })
                    .asyncNetworkRequest()
                    .subscribe(object :
                        MovieadoSingleObserver<MessageResponse>(compositeDisposable) {
                        override fun onSuccess(t: MessageResponse) {
                            snackBar(t.message)
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
    fun showError(movieadoEvent: MovieadoEvent) {
        when (movieadoEvent.type) {
            MovieadoEvent.Type.SIMPLE -> snackBar(
                movieadoEvent.stringMessage
                    ?: getString(movieadoEvent.resMessage)
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
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
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