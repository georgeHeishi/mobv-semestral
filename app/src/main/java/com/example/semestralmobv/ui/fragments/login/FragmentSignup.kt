package com.example.semestralmobv.ui.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.semestralmobv.R
import com.example.semestralmobv.databinding.FragmentSignupBinding
import com.example.semestralmobv.ui.viewmodels.AuthorizationViewModel
import com.example.semestralmobv.utils.PasswordUtils
import com.example.semestralmobv.utils.PreferencesData
import com.example.semestralmobv.utils.SystemUtils
import com.example.semestralmobv.utils.ViewModelFactoryProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentSignup : Fragment() {
    private var _binding: FragmentSignupBinding? = null
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<ImageView>(R.id.back_button)?.visibility = View.GONE
        activity?.findViewById<TextView>(R.id.screen_title)?.text = ""
        activity?.findViewById<ImageView>(R.id.logout)?.visibility = View.GONE

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
            bnd.signup.setOnClickListener {
                SystemUtils.closeKeyboard(view)
                if (bnd.usernameInput.text.toString().isNotBlank()
                    && bnd.passwordInput.text.toString().isNotBlank()
                    && bnd.passwordInput.text.toString()
                        .compareTo(bnd.repeatPasswordInput.text.toString()) == 0
                ) {
                    val hashPass = PasswordUtils.hash(bnd.passwordInput.text.toString())
                    authorizationViewModel.signup(
                        bnd.usernameInput.text.toString(), String(hashPass)
                    )
                } else if (bnd.usernameInput.text.toString()
                        .isBlank() || bnd.passwordInput.text.toString().isBlank()
                ) {
                    authorizationViewModel.setMessage("Fill in name and password")
                } else {
                    authorizationViewModel.setMessage("Passwords must be same")
                }
            }

            bnd.backButton.setOnClickListener {
                nav.popBackStack(R.id.fragmentLogin, false)
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