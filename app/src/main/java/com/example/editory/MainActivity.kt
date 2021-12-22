package com.example.editory

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
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