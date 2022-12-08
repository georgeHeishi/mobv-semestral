package com.example.semestralmobv.ui.fragments.nearbyPubs

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.semestralmobv.GeofenceBroadcastReceiver
import com.example.semestralmobv.R
import com.example.semestralmobv.databinding.FragmentNearbyPubsBinding
import com.example.semestralmobv.ui.viewmodels.NearbyPub
import com.example.semestralmobv.ui.viewmodels.NearbyPubsViewModel
import com.example.semestralmobv.utils.LatLongLocation
import com.example.semestralmobv.utils.LocationUtils
import com.example.semestralmobv.utils.PreferencesData
import com.example.semestralmobv.utils.ViewModelFactoryProvider
import com.google.android.gms.location.*


interface SelectPubAction {
    fun selectPub(pub: NearbyPub)
}

class FragmentNearbyPubs : Fragment() {
    private lateinit var nearbyPubsViewModel: NearbyPubsViewModel

    private var _binding: FragmentNearbyPubsBinding? = null
    private val binding get() = _binding!!

    private lateinit var nav: NavController
    private lateinit var spinner: LottieAnimationView
    private lateinit var checkInAnimation: LottieAnimationView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                // Precise location access granted.
            }
            else -> {
                nearbyPubsViewModel.setMessage("Background location access denied.")
                // No location access granted.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!LocationUtils.checkPermissions(requireContext())) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        nearbyPubsViewModel = ViewModelProvider(
            this, ViewModelFactoryProvider.provideViewModelFactory(requireContext())
        )[NearbyPubsViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geofencingClient = LocationServices.getGeofencingClient(requireActivity())

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearbyPubsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<ImageView>(R.id.back_button)?.visibility = View.GONE
        activity?.findViewById<TextView>(R.id.screen_title)?.text = "Nearby Pubs"
        val logoutButton = activity?.findViewById<ImageView>(R.id.logout)
        logoutButton?.visibility = View.VISIBLE

        super.onViewCreated(view, savedInstanceState)
        nav = view.findNavController()

        val foundUser = PreferencesData.getInstance().getUserItem(requireContext())
        if ((foundUser?.uid ?: "").isBlank()) {
            nav.navigate(R.id.action_to_login)
            return
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = nearbyPubsViewModel
        }.also { bnd ->

            bnd.recycleView.selectPubAction = object : SelectPubAction {
                override fun selectPub(
                    pub: NearbyPub,
                ) {
                    if (checkBackgroundPermissions()) {
                        nearbyPubsViewModel.checkIntoPub(pub)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            permissionDialog()
                        }
                    }
                }
            }

            spinner = bnd.spinner
            checkInAnimation = bnd.checkIn

            nearbyPubsViewModel.checkedInPub.observe(viewLifecycleOwner) { sP ->
                bnd.nearestPubItemName.text = sP?.name
                bnd.nearestPubItemType.text = sP?.type
            }

            nearbyPubsViewModel.isCheckedIn.observe(viewLifecycleOwner) {
                if (it) {
                    checkInAnimation.frame = checkInAnimation.maxFrame.toInt()
                    nearbyPubsViewModel.setMessage("Checked in.")
                    nearbyPubsViewModel.deviceLocation.value?.let { location ->
                        createFence(location.lat, location.long)
                    }
                    bnd.recycleView.adapter?.notifyDataSetChanged()
                }
            }
        }

        nearbyPubsViewModel.message.observe(viewLifecycleOwner) {
            if (PreferencesData.getInstance().getUserItem(requireContext()) == null) {
                nav.navigate(R.id.action_to_login)
            }
        }



        nearbyPubsViewModel.loading.observe(viewLifecycleOwner) {
            if (it == true) {
                spinner.playAnimation()
            } else {
                spinner.cancelAnimation()
            }
        }

        if (LocationUtils.checkPermissions(requireContext())) {
            loadData()
        } else {
            nav.navigate(R.id.action_to_pubs)
        }

        logoutButton?.setOnClickListener {
            PreferencesData.getInstance().clearData(context)
            nav.navigate(R.id.action_to_login)
        }
    }

    @SuppressLint("MissingPermission")
    private fun loadData() {
        nearbyPubsViewModel.loading.postValue(true)
        fusedLocationClient.getCurrentLocation(
            CurrentLocationRequest.Builder().setDurationMillis(30000).setMaxUpdateAgeMillis(60000)
                .build(), null
        ).addOnSuccessListener {
            it?.let {
                nearbyPubsViewModel.deviceLocation.postValue(
                    LatLongLocation(
                        it.latitude, it.longitude
                    )
                )
            } ?: nearbyPubsViewModel.loading.postValue(false)
        }
    }


    @SuppressLint("MissingPermission")
    private fun createFence(lat: Double, lon: Double) {
        if (!checkPermissions()) {
            nearbyPubsViewModel.setMessage("Geofence failed, permissions not granted.")
        }
        val geofenceIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            Intent(requireContext(), GeofenceBroadcastReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val request = GeofencingRequest.Builder().apply {
            addGeofence(
                Geofence.Builder().setRequestId("geofence").setCircularRegion(lat, lon, 300F)
                    .setExpirationDuration(1000L * 60 * 60 * 24)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT).build()
            )
        }.build()

        geofencingClient.addGeofences(request, geofenceIntent).run {
            addOnFailureListener {
                nearbyPubsViewModel.setMessage("Geofence failed to create.") //permission is not granted for All times.
                it.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun permissionDialog() {
        val alertDialog: AlertDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Background location needed")
                setMessage("Allow background location (All times) for detecting when you leave bar.")
                setPositiveButton(
                    "OK"
                ) { _, _ ->
                    locationPermissionRequest.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    )
                }
                setNegativeButton(
                    "Cancel"
                ) { _, _ ->
                    // User cancelled the dialog
                }
            }
            // Create the AlertDialog
            builder.create()
        }
        alertDialog.show()
    }


    private fun checkBackgroundPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}