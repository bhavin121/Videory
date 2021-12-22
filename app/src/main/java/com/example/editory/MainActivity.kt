package com.example.editory

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.editory.databinding.ActivityMainBinding
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ffmpeg: FFmpeg

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFfmpeg()

        MovableViewOnTouch.moveOnTouchWithBoundary(binding.text, binding.view)

        binding.video.setVideoPath(
            intent.getStringExtra(Constants.KEY_VIDEO_PATH)
        )
        binding.video.setMediaController(MediaController(this))
        binding.video.start()
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