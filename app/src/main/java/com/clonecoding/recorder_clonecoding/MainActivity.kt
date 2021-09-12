package com.clonecoding.recorder_clonecoding

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    /**
     * Sound visualizer view
     */
    private val soundVisualizerView: SoundVisualizerView by lazy {
        findViewById(R.id.soundVisualizerView)
    }

    /**
     * Record button
     */
    private val recordButton: RecordButton by lazy {
        findViewById(R.id.recordButton)
    }

    /**
     * Reset button
     */
    private val resetButton: Button by lazy {
        findViewById(R.id.resetButton)
    }

    /**
     * Record time text view
     */
    private val recordTimeView: CountUpTextView by lazy {
        findViewById(R.id.recordTimeTextView)
    }

    /**
     * Record state
     */
    private var state: State = State.BEFORE_RECORDING
    set(value) {
        field = value
        resetButton.isEnabled = (value == State.AFTER_RECORDING)
                || (value == State.ON_PLAYING)
        recordButton.updateIconWithState(value)
    }

    /**
     * Recording file path
     */
    private val recordingFilePath: String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    /**
     * Media recorder
     */
    private var recorder: MediaRecorder? = null

    /**
     * Media player
     */
    private var player: MediaPlayer? = null

    /**
     * Required permission list
     */
    private val requiredPermissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    /**
     * Record audio permission
     */
    private val RECORD_AUDIO_PERMISSION: String = android.Manifest.permission.READ_EXTERNAL_STORAGE

    /**
     * onCreate
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.requestAudioPermission()

        this.initViews()
        this.bindViews()
        this.initVariables()
    }

    /**
     * Request audio permission
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestAudioPermission() {

        when {
            ContextCompat.checkSelfPermission(
                this,
                this.RECORD_AUDIO_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // DO NOTHING
            }
            shouldShowRequestPermissionRationale(
                this.RECORD_AUDIO_PERMISSION
            ) -> {

                this.showPermissionContextPopUp()
            }
            else -> {

                requestPermissions(
                    this.requiredPermissions,
                    REQUEST_RECORD_AUDIO_PERMISSION)
            }
        }
    }

    /**
     * Show permission dialog
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showPermissionContextPopUp() {

        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("전자액자에 앱에서 사진을 불러오기 위해 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(
                    this.requiredPermissions,
                    REQUEST_RECORD_AUDIO_PERMISSION)
            }
            .setNegativeButton("취소하기") {_, _ -> }
            .create()
            .show()
    }

    /**
     * Permission result
     */
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
     * Init value
     */
    private fun initVariables() {

        this.state = State.BEFORE_RECORDING
    }

    /**
     * Start recording
     */
    private fun startRecording() {

        this.recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }

        this.recorder?.start()
        this.soundVisualizerView.startVisualizing(false)
        this.recordTimeView.startCountUp()
        this.state = State.ON_RECORDING
    }

    /**
     * Stop recording
     */
    private fun stopRecording() {

        this.recorder?.run {
            stop()
            release()
        }
        this.recorder = null
        this.soundVisualizerView.stopVisualizing()
        this.recordTimeView.stopCountUp()
        this.state = State.AFTER_RECORDING
    }

    /**
     * Start playing
     */
    private fun startPlaying() {

        this.player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare()
        }

        this.player?.setOnCompletionListener {
            stopPlaying()
            state = State.AFTER_RECORDING
        }
        this.player?.start()
        this.soundVisualizerView.startVisualizing(true)
        this.recordTimeView.startCountUp()
        this.state = State.ON_PLAYING
    }

    /**
     * Stop playing
     */
    private fun stopPlaying() {

        this.player?.release()
        player = null
        this.soundVisualizerView.stopVisualizing()
        this.recordTimeView.stopCountUp()
        this.state = State.AFTER_RECORDING
    }

    /**
     * Init views
     */
    private fun initViews() {

        this.recordButton.updateIconWithState(this.state)
    }

    /**
     * Bind views
     */
    private fun bindViews() {

        this.soundVisualizerView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude ?: 0
        }

        this.resetButton.setOnClickListener {

            this.stopPlaying()
            this.soundVisualizerView.clearVisualization()
            this.recordTimeView.clearCountTime()
            this.initVariables()
        }

        this.recordButton.setOnClickListener{
            when(this.state) {
                State.BEFORE_RECORDING -> {
                    this.startRecording()
                }
                State.ON_RECORDING -> {
                    this.stopRecording()
                }
                State.AFTER_RECORDING -> {
                    this.startPlaying()
                }
                State.ON_PLAYING -> {
                    this.stopPlaying()
                }
            }
        }
    }

    companion object {

        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}