package com.example.editory

import android.R.attr
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.content.PermissionChecker
import com.example.editory.databinding.ActivityMainBinding
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.utils.ContentUriUtils
import java.util.jar.Manifest
import droidninja.filepicker.FilePickerConst

import android.R.attr.data

import android.app.Activity
import android.net.Uri
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import droidninja.filepicker.FilePickerConst.KEY_SELECTED_MEDIA


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ffmpeg: FFmpeg
    private lateinit var launcher: ActivityResultLauncher<String>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFfmpeg()

        MovableViewOnTouch.moveOnTouchWithBoundary(binding.text, binding.view)

//        launcher.launch("video/*")

        binding.video.setOnClickListener {
            pickFile()
        }

    }

    private fun pickFile() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            FilePickerBuilder.instance
                .enableVideoPicker(true)
                .enableCameraSupport(false)
                .enableImagePicker(false)
                .setActivityTitle("Select a video")
                .setMaxCount(1)
                .pickPhoto(this)
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            pickFile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val path = data.getParcelableArrayListExtra<Uri>(KEY_SELECTED_MEDIA)?.get(0)
            binding.video.setMediaController(MediaController(this))
            binding.video.setVideoURI(path)
            binding.video.start()
            println(ContentUriUtils.getFilePath(this, path!!))
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
}