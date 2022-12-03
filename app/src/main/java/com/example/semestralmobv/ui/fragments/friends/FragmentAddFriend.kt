package com.example.semestralmobv.ui.fragments.friends

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.semestralmobv.R
import com.example.semestralmobv.databinding.FragmentAddFriendBinding
import com.example.semestralmobv.ui.viewmodels.AddFriendViewModel
import com.example.semestralmobv.utils.PreferencesData
import com.example.semestralmobv.utils.SystemUtils
import com.example.semestralmobv.utils.ViewModelFactoryProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentAddFriend : Fragment() {
    private lateinit var addFriendViewModel: AddFriendViewModel

    private var _binding: FragmentAddFriendBinding? = null
    private val binding get() = _binding!!

    private lateinit var nav: NavController
    private lateinit var spinner: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        addFriendViewModel = ViewModelProvider(
            this, ViewModelFactoryProvider.provideViewModelFactory(requireContext())
        )[AddFriendViewModel::class.java]
        super.onCreate(savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility =
            View.VISIBLE

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFriendBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nav = view.findNavController()
        initializeToolbar()


        val foundUser = PreferencesData.getInstance().getUserItem(requireContext())
        if ((foundUser?.uid ?: "").isBlank()) {
            nav.navigate(R.id.action_to_login)
            return
        }

        addFriendViewModel.message.observe(viewLifecycleOwner) {
            if (PreferencesData.getInstance().getUserItem(requireContext()) == null) {
                nav.navigate(R.id.action_to_login)
            }
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = addFriendViewModel
        }.also { bnd ->
            bnd.add.setOnClickListener {
                SystemUtils.closeKeyboard(view)
                val friendName = bnd.userName.text.toString()
                if (friendName.isNotBlank()) {
                    addFriendViewModel.addFriend(friendName)
                } else {
                    addFriendViewModel.setMessage("You should enter your friend's name")
                }
            }

            addFriendViewModel.isSuccessful.observe(viewLifecycleOwner) {
                if (it) {
                    bnd.checkmark.playAnimation()
                    bnd.checkmark.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            nav.popBackStack(R.id.fragmentFriends, false)
                        }

                        override fun onAnimationCancel(animation: Animator) {
                        }

                        override fun onAnimationRepeat(animation: Animator) {
                        }


                    })
                }
            }
        }
    }

    private fun stopLoading(
    ) {
        spinner.cancelAnimation()
    }

    private fun startLoading() {
        spinner.playAnimation()
    }


    @SuppressLint("SetTextI18n")
    private fun initializeToolbar() {
        activity?.findViewById<ImageView>(R.id.back_button)?.visibility = View.VISIBLE
        activity?.findViewById<TextView>(R.id.screen_title)?.text = "Add friend"
        val logoutButton = activity?.findViewById<ImageView>(R.id.logout)
        logoutButton?.visibility = View.VISIBLE
        logoutButton?.setOnClickListener {
            PreferencesData.getInstance().clearData(context)
            nav.navigate(R.id.action_to_login)
        }
    }
}