package com.clonecoding.recorder_clonecoding

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    /**
     * Record button
     */
    private val recordButton: RecordButton by lazy {
        findViewById(R.id.recordButton)
    }

    /**
     * Record state
     */
    private var state: State = State.BEFORE_RECORDING

    private val requiredPermissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    /**
     * onCreate
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.requestAudioPermission()

        this.initViews()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestAudioPermission() {

        requestPermissions(this.requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                    grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) {
            finish()
        }
    }

    /**
     * Init views
     */
    private fun initViews() {

        this.recordButton.updateIconWithState(state)
    }

    companion object {

        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}