package com.learn.ktornoteapp.ui.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.learn.ktornoteapp.R
import com.learn.ktornoteapp.databinding.FragmentRegisterAccountBinding

class RegisterAccountFragment : Fragment(R.layout.fragment_register_account) {
    private var _binding: FragmentRegisterAccountBinding? = null
    private val binding: FragmentRegisterAccountBinding?
        get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterAccountBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}