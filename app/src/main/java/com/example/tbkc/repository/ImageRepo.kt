package com.example.tbkc.repository

import android.content.Context
import android.net.Uri

interface ImageRepo {
    fun  uploadImage(context: Context, imageUri: Uri?, callback: (Boolean,String?)-> Unit)

    fun getFileNameFromUri(context: Context,imageUri: Uri?):String?
}