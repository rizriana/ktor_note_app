package com.learn.ktornoteapp.ui.users

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.databinding.FragmentRegisterAccountBinding
import com.learn.ktornoteapp.ui.users.viewmodel.UserViewModel
import com.learn.ktornoteapp.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.learn.ktornoteapp.utils.*

@AndroidEntryPoint
class RegisterAccountFragment : Fragment(R.layout.fragment_register_account) {
    private var _binding: FragmentRegisterAccountBinding? = null
    private val binding: FragmentRegisterAccountBinding?
        get() = _binding

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterAccountBinding.bind(view)

        subscribeToRegisterEvents()
        binding?.btnCreateAccount?.setOnClickListener {
            val name = binding?.etUsername?.text.toString().trim()
            val email = binding?.etEmail?.text.toString().trim()
            val password = binding?.etPassword?.text.toString().trim()
            val confirmPassword = binding?.etConfirmPassword?.text.toString().trim()

            userViewModel.registerUser(
                name,
                email,
                password,
                confirmPassword
            )
        }
    }

    private fun subscribeToRegisterEvents() = lifecycleScope.launch {
        userViewModel.registerState.collect { result ->
            when (result) {
                is Result.Success -> {
                    binding?.progressBar?.makeInvisible()
                    Toast.makeText(requireContext(),
                        "Account Successfully Created!",
                        Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is Result.Error -> {
                    binding?.progressBar?.makeInvisible()
                    context?.showToast(result.errorMessage)
                }
                is Result.Loading -> binding?.progressBar?.makeVisible()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}