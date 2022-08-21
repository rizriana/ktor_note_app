package com.learn.ktornoteapp.ui.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.databinding.FragmentUserInfoBinding

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding: FragmentUserInfoBinding?
        get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserInfoBinding.bind(view)

        binding?.btnCreateAccount?.setOnClickListener {
            findNavController().navigate(R.id.action_userInfoFragment_to_registerAccountFragment)
        }
        binding?.btnLogin?.setOnClickListener {
            findNavController().navigate(R.id.action_userInfoFragment_to_loginAccountFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}