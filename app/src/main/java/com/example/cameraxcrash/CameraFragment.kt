package com.example.cameraxcrash

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.camera.view.CameraView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class CameraFragment : Fragment(R.layout.fragment_camera) {

    interface Callbacks {
        fun onPictureTaken()
    }

    private val callbacks: Callbacks
        get() = requireContext() as Callbacks

    private var cameraView: CameraView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraView = view.findViewById(R.id.camera)

        view.findViewById<View>(R.id.take_picture).setOnClickListener {
            // taking an actual picture is not relevant to this bug report
            callbacks.onPictureTaken()
        }
    }

    override fun onResume() {
        super.onResume()

        if (requireContext().hasCameraPermission()) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (requireContext().hasCameraPermission()) {
                    startCamera()
                } else {
                    Toast.makeText(requireContext(), "Camera permission required", Toast.LENGTH_SHORT).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun startCamera() {
        cameraView?.bindToLifecycle(this)
    }

    companion object {

        private const val PERMISSION_REQUEST_CODE = 1

        private fun Context.hasCameraPermission() =
            checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        fun newInstance() = CameraFragment()
    }
}
