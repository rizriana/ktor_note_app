package com.learn.ktornoteapp.ui.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.databinding.FragmentLoginAccountBinding

class LoginAccountFragment : Fragment(R.layout.fragment_login_account) {
    private var _binding: FragmentLoginAccountBinding? = null
    private val binding: FragmentLoginAccountBinding?
        get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginAccountBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}