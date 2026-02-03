package com.example.tbkc.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.tbkc.repository.ImageRepo

class ImageViewModel(val repo: ImageRepo) : ViewModel()  {
    fun  uploadImage(context: Context, imageUri: Uri, callback: (Boolean, String?) -> Unit){
        repo.uploadImage(context,imageUri,callback)
    }
}