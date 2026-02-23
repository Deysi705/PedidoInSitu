package com.example.pedidoinsitu.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore



object CameraUtils {
    const val REQUEST_IMAGE_CAPTURE = 1
    fun abrirCamara(activity: Activity): Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
}
