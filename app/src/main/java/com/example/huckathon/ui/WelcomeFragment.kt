package com.example.huckathon.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.huckathon.R
import com.example.huckathon.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentWelcomeBinding.bind(view)


        // Fade in/out animasyon döngüsü
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

        fadeIn.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                binding.tapText.startAnimation(fadeOut)
            }
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
        })
        fadeOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                binding.tapText.startAnimation(fadeIn)
            }
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
        })
        binding.tapText.startAnimation(fadeIn)

        // Ekrana tıklanınca LoginFragment'a geç
        binding.root.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

