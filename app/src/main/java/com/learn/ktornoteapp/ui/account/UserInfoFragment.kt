package com.learn.ktornoteapp.ui.account

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.databinding.FragmentUserInfoBinding
import com.learn.ktornoteapp.ui.account.viewmodel.UserViewModel
import com.learn.ktornoteapp.utils.Result
import com.learn.ktornoteapp.utils.makeInvisible
import com.learn.ktornoteapp.utils.makeVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserInfoFragment : Fragment(R.layout.fragment_user_info) {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding: FragmentUserInfoBinding?
        get() = _binding

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserInfoBinding.bind(view)

        binding?.apply {
            btnCreateAccount.setOnClickListener {
                findNavController().navigate(R.id.action_userInfoFragment_to_registerAccountFragment)
            }
            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_userInfoFragment_to_loginAccountFragment)
            }

            btnLogout.setOnClickListener {
                userViewModel.logout()
            }
        }
        subscribeToCurrentUserEvents()
    }

    override fun onStart() {
        super.onStart()
        userViewModel.getCurrentUser()
    }

    private fun subscribeToCurrentUserEvents() = lifecycleScope.launch {
        userViewModel.currentUserState.collect() { result ->
            when (result) {
                is Result.Success -> {
                    userLoggedIn()
                    binding?.apply {
                        tvUsername.text = result.data?.name ?: "No Name"
                        tvUserEmail.text = result.data?.email ?: "No Email"
                    }
                }
                is Result.Error -> {
                    userNotLoggedIn()
                    binding?.tvUsername?.text = "Not Logged In!"
                }
                is Result.Loading -> binding?.progressBar?.makeVisible()
            }
        }
    }

    private fun userLoggedIn() {
        binding?.apply {
            progressBar.makeInvisible()
            btnLogin.makeInvisible()
            btnCreateAccount.makeInvisible()

            btnLogout.makeVisible()
            tvUserEmail.makeVisible()
        }
    }

    private fun userNotLoggedIn() {
        binding?.apply {
            progressBar.makeInvisible()
            btnLogin.makeVisible()
            btnCreateAccount.makeVisible()

            btnLogout.makeInvisible()
            tvUserEmail.makeInvisible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}