package ir.andromeda.cartoonabad.feature.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadFragment
import ir.andromeda.cartoonabad.common.CartoonAbadSingleObserver
import ir.andromeda.cartoonabad.data.message.MessageResponse
import ir.andromeda.cartoonabad.databinding.FragmentContactsBinding
import ir.andromeda.cartoonabad.feature.main.DrawerLocker

import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ContactsFragment : CartoonAbadFragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactsViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    private var selectedTitle: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object :
                        CartoonAbadSingleObserver<MessageResponse>(compositeDisposable) {
                        override fun onSuccess(t: MessageResponse) {
                            Timber.i(t.message)
                            Snackbar.make(
                                activity?.findViewById(R.id.contentRootView) as View,
                                t.message,
                                Snackbar.LENGTH_SHORT
                            ).show()

                            emptyAllFields()
                        }
                    })
            }


        }
        (activity as DrawerLocker).setDrawerLocked(true)
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


    override fun onStop() {
        super.onStop()
        (activity as DrawerLocker).setDrawerLocked(false)
    }

    fun emptyAllFields() {
        binding.etlEmail.editText?.text?.clear()
        binding.etlMessage.editText?.text?.clear()
        binding.autoTvTopic.text.clear()
        selectedTitle = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}