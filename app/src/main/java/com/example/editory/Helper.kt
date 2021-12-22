package com.example.editory

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.editory.databinding.LoadingBinding

object Helper {
    fun buildLoadingDialog(context: Context): AlertDialog{
        val binding: LoadingBinding = LoadingBinding.inflate(LayoutInflater.from(context))
        return AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()
    }
}