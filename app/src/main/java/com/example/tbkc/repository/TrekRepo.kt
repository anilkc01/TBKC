package com.example.tbkc.repository

import com.example.tbkc.model.TrekModel

interface TrekRepo {
    fun addTrek(trekModel: TrekModel, callback: (Boolean, String) -> Unit)

    fun updateTrek(trekId: String, trekModel: TrekModel, callback: (Boolean, String) -> Unit)

    fun deleteTrek(trekId: String, callback: (Boolean, String) -> Unit)

    fun getAllTreks(callback: (Boolean, String, List<TrekModel>?) -> Unit)

    fun getTreksByUserId(userId: String, callback: (Boolean, String, List<TrekModel>?) -> Unit)
}