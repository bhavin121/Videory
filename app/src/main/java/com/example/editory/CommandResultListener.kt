package com.example.editory

interface CommandResultListener {
    fun onSuccess(message: String)
    fun onFailure(message: String)
}
