package com.prilki.ntuhostel.presentation.views

import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.EditText
import com.prilki.ntuhostel.R
import com.prilki.ntuhostel.data.*
import com.prilki.ntuhostel.databinding.FragmentLoginBinding
import com.shashank.sony.fancytoastlib.FancyToast

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ADMIN_MODE = false
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            buttonSignIn.setOnClickListener {
                val username = editTextUserName.text.toString()
                val password = editTextPassword.text.toString()
                Log.d(LOG_TAG, password)
                val usernameStatus = checkUserName(username)
                val passwordStatus = checkPassword(password)
                if ( usernameStatus && passwordStatus) {
                    ADMIN_MODE = true
                    toast(requireContext(), "Login successful", FancyToast.SUCCESS)
                    goToMenu()
                }
            }
            buttonGuestMode.setOnClickListener {
                goToMenu()
            }
        }
    }

    private fun goToMenu() {
        parentFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentHolder, MenuFragment()).commit()
    }

    private fun checkUserName(username: String): Boolean {
        if (username.isEmpty()) {
            applyAnimation(binding.editTextUserName)
            toast(requireContext(), "Username is empty", FancyToast.ERROR)
            return false
        }
        if (username != USERNAME) {
            applyAnimation(binding.editTextUserName)
            toast(requireContext(), "Username is wrong", FancyToast.ERROR)
            return false
        }
        return true
    }



    private fun checkPassword(password: String): Boolean {
        if (password.isEmpty()) {
            applyAnimation(binding.editTextPassword)
            toast(requireContext(), "Password is empty", FancyToast.ERROR)
            return false
        }
        if (encodeToBase64(password).trim() != PASSWORD_64.trim()) {
            applyAnimation(binding.editTextPassword)
            toast(requireContext(), "Password is wrong", FancyToast.ERROR)
            return false
        }
        return true
    }

    private fun encodeToBase64(input: String): String {
        return Base64.encodeToString(input.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
    }
    private fun applyAnimation(editText: EditText) {
        editText.startAnimation(
            ScaleAnimation(
                1F,
                0.7F,
                1F,
                0.7F,
                Animation.RELATIVE_TO_SELF,
                0.5F,
                Animation.RELATIVE_TO_SELF,
                0.5F
            ).apply {
                duration = 600
                repeatCount = 1
                repeatMode = Animation.REVERSE
            })
    }
}