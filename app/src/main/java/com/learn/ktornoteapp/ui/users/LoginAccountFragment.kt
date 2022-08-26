package com.learn.ktornoteapp.ui.users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.databinding.FragmentLoginAccountBinding
import com.learn.ktornoteapp.ui.users.viewmodel.UserViewModel
import com.learn.ktornoteapp.utils.Result
import com.learn.ktornoteapp.utils.makeInvisible
import com.learn.ktornoteapp.utils.makeVisible
import com.learn.ktornoteapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginAccountFragment : Fragment(R.layout.fragment_login_account) {
    private var _binding: FragmentLoginAccountBinding? = null
    private val binding: FragmentLoginAccountBinding?
        get() = _binding

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginAccountBinding.bind(view)

        subscribeToLoginEvents()
        binding?.btnLogin?.setOnClickListener {
            val name = binding?.etUserName?.text.toString().trim()
            val email = binding?.etEmail?.text.toString().trim()
            val password = binding?.etPassword?.text.toString().trim()

            userViewModel.loginUser(
                name,
                email,
                password
            )
        }
    }

    private fun subscribeToLoginEvents() = lifecycleScope.launch {
        userViewModel.loginState.collect { result ->
            when (result) {
                is Result.Success -> {
                    binding?.progressBar?.makeInvisible()
                    context?.showToast("Login Successfully!")
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