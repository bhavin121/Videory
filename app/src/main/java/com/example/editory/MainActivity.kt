package com.example.editory

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.editory.databinding.ActivityMainBinding
import com.example.editory.databinding.AddAudioDialogBinding
import com.example.editory.databinding.ExportVideoDialogBinding
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.greentoad.turtlebody.mediapicker.MediaPicker
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var addMusicBinding: AddAudioDialogBinding
    private lateinit var addMusicDialog: BottomSheetDialog
    private lateinit var exportOptionsDialog: AlertDialog
    private lateinit var loading: AlertDialog
    private lateinit var exportVideoDialogBinding: ExportVideoDialogBinding
    private lateinit var binding: ActivityMainBinding
    private lateinit var ffmpeg: FFmpeg

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.backgroundColor)

        initFfmpeg()
        buildDialogs()

        binding.video.setVideoPath(
            intent.getStringExtra(Constants.KEY_VIDEO_PATH)
        )
        binding.video.setMediaController(MediaController(this))
        binding.video.start()

        binding.audio.setOnClickListener {
            addMusicDialog.show()
        }
    }

    private fun initFfmpeg() {
        ffmpeg = FFmpeg.getInstance(this)
        ffmpeg.loadBinary(object:LoadBinaryResponseHandler(){
            override fun onFailure() {
                Toast.makeText(applicationContext, "This app dose not support on your device", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun executeFFmpegCommand(cmd: Array<String>, listener: CommandResultListener) {
        try {
            ffmpeg.execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onSuccess(message: String) {
                    super.onSuccess(message)
                    listener.onSuccess(message)
                }

                override fun onFailure(message: String) {
                    super.onFailure(message)
                    listener.onFailure(message)
                }
            })
        } catch (e: FFmpegCommandAlreadyRunningException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission", "CheckResult")
    private fun buildDialogs() {
        loading = Helper.buildLoadingDialog(this)
//        messageDialog = AlertDialog.Builder(this)
//            .setTitle("Error")
//            .setPositiveButton("Ok", null)
//            .create()
        exportVideoDialogBinding = ExportVideoDialogBinding.inflate(layoutInflater)
        exportOptionsDialog = AlertDialog.Builder(this)
            .setTitle("Export")
            .setView(exportVideoDialogBinding.root)
            .create()
        addMusicDialog = BottomSheetDialog(this)
        addMusicBinding = AddAudioDialogBinding.inflate(layoutInflater)
        addMusicDialog.setContentView(addMusicBinding.root)
        addMusicBinding.add.setOnClickListener { view ->
            addMusicDialog.dismiss()
//            applyEffect(MainActivity.ADD_AUDIO)
        }
        addMusicBinding.cancel.setOnClickListener { view -> addMusicDialog.dismiss() }
        addMusicBinding.choose.setOnClickListener { view ->

            val pickerConfig = MediaPickerConfig()
                .setUriPermanentAccess(true)
                .setAllowMultiSelection(false)
                .setShowConfirmationDialog(false)
                .setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

            MediaPicker.with(this, MediaPicker.MediaTypes.AUDIO)
                .setConfig(pickerConfig)
                .onResult()
                .subscribe(
                    {
                        val fileName: String = File(it[0].toString()).name
                        addMusicBinding.fileName.text = fileName
                    },
                    {
                        println("error: $it")
                    },
                )

        }
//        effectDialogBinding = EffectDialogBinding.inflate(layoutInflater)
//        addEffectDialog = BottomSheetDialog(this)
//        addEffectDialog.setContentView(effectDialogBinding.getRoot())
//        effectDialogBinding.fadeIn.setOnClickListener { view ->
//            applyEffect(MainActivity.FADE_IN)
//            addEffectDialog.dismiss()
//        }
//        effectDialogBinding.fadeOut.setOnClickListener { view ->
//            applyEffect(MainActivity.FADE_OUT)
//            addEffectDialog.dismiss()
//        }
//        effectDialogBinding.reverse.setOnClickListener { view ->
//            applyEffect(MainActivity.REVERSE)
//            addEffectDialog.dismiss()
//        }
//        speedDialogBinding = SpeedDialogBinding.inflate(layoutInflater)
//        speedDialog = BottomSheetDialog(this)
//        speedDialog.setContentView(speedDialogBinding.getRoot())
//        speedDialogBinding.s2x.setOnClickListener { view ->
//            applyEffect(MainActivity.SPEED_2X)
//            speedDialog.dismiss()
//        }
//        speedDialogBinding.s05x.setOnClickListener { view ->
//            applyEffect(MainActivity.SPEED_0_5X)
//            speedDialog.dismiss()
//        }
//        addTextDialog = BottomSheetDialog(this)
//        textDialogBinding = TextDialogBinding.inflate(layoutInflater)
//        addTextDialog.setContentView(textDialogBinding.getRoot())
//        textDialogBinding.apply.setOnClickListener { view ->
//            applyEffect(MainActivity.ADD_FONT)
//            addTextDialog.dismiss()
//        }
    }
}