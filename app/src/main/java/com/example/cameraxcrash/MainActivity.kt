package com.example.cameraxcrash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), CameraFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, CameraFragment.newInstance(), "Root fragment")
            .commit()
    }

    override fun onPictureTaken() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PressBackToCrashFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}
