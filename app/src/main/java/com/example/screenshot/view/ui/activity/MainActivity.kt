package com.example.screenshot.view.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.screenshot.R
import com.example.screenshot.databinding.ActivityMainBinding
import com.example.screenshot.view.ui.fragment.DeleteFragment
import com.example.screenshot.view.ui.fragment.InfoFragment
import com.example.screenshot.view.ui.fragment.ShareFragment
import com.example.screenshot.viewmodel.ScreenShotViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: ScreenShotViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted
                viewModel.getAllScreenShots()
            } else {
                // Permission denied, handle accordingly
                showPermissionSnackbar()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (checkPermission()) {
            // Permission is already granted
            viewModel.getAllScreenShots()
        } else {
            // Request permissions
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        replaceFragment(ShareFragment.newInstance())
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.share -> {
                    replaceFragment(ShareFragment.newInstance())
                    return@setOnItemSelectedListener true
                }

                R.id.info -> {
                    replaceFragment(
                        InfoFragment.newInstance()
                    )
                    return@setOnItemSelectedListener true
                }

                R.id.delete -> {
                    replaceFragment(DeleteFragment.newInstance())
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionSnackbar() {
        val snackbar = Snackbar.make(
            binding.root,
            R.string.error,
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction(R.string.settings) {
            // Open app settings
            openAppSettings()
        }
        snackbar.show()
    }

    private fun openAppSettings() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = android.net.Uri.parse("package:$packageName")
        startActivity(intent)
    }
}