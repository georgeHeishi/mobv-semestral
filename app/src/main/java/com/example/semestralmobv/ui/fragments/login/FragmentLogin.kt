package com.example.semestralmobv.ui.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.semestralmobv.R
import com.example.semestralmobv.databinding.FragmentLoginBinding
import com.example.semestralmobv.ui.viewmodels.AuthorizationViewModel
import com.example.semestralmobv.utils.PasswordUtils
import com.example.semestralmobv.utils.PreferencesData
import com.example.semestralmobv.utils.ViewModelFactoryProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentLogin : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var nav: NavController
    private lateinit var authorizationViewModel: AuthorizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        authorizationViewModel = ViewModelProvider(
            this, ViewModelFactoryProvider.provideViewModelFactory(requireContext())
        )[AuthorizationViewModel::class.java]
        super.onCreate(savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nav = view.findNavController()
        val foundUser = PreferencesData.getInstance().getUserItem(requireContext())
        if ((foundUser?.uid ?: "").isNotBlank()) {
            Navigation.findNavController(view).navigate(R.id.action_to_pubs)
            return
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = authorizationViewModel
        }.also { bnd ->
            bnd.login.setOnClickListener {
                if (bnd.usernameInput.text.toString()
                        .isNotBlank() && bnd.passwordInput.text.toString().isNotBlank()
                ) {
                    val hashPass = PasswordUtils.hash(bnd.passwordInput.text.toString())
                    authorizationViewModel.login(
                        binding.usernameInput.text.toString(),
                        String(hashPass)
                    )
                } else {
                    authorizationViewModel.setMessage("Fill in name and password")
                }
            }

            bnd.signup.setOnClickListener {
                nav.navigate(R.id.action_to_signup)
            }
        }

        authorizationViewModel.user.observe(viewLifecycleOwner) {
            it?.let {
                PreferencesData.getInstance().putUserItem(requireContext(), it)
                nav.navigate(R.id.action_to_pubs)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}