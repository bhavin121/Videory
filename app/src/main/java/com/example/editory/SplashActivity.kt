package com.example.editory

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.editory.databinding.ActivitySplashBinding
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.utils.ContentUriUtils

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.backgroundColor)

        binding.linearLayoutCompat.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.logo_reveal)
        )

        binding.choose.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.button_reveal)
        )

        binding.choose.setOnClickListener {
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
            val path = data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)?.get(0)
            val absPath = ContentUriUtils.getFilePath(this, path!!)
            println(absPath)

            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra(Constants.KEY_VIDEO_PATH, absPath)
            })
        }
    }
}