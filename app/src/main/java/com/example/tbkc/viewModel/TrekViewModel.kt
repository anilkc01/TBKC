package com.example.tbkc.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tbkc.model.TrekModel
import com.example.tbkc.repository.TrekRepo

class TrekViewModel(val repo: TrekRepo) : ViewModel() {

    private val _allTreks = MutableLiveData<List<TrekModel>?>()
    val allTreks: MutableLiveData<List<TrekModel>?> get() = _allTreks

    private val _userTreks = MutableLiveData<List<TrekModel>?>()
    val userTreks: MutableLiveData<List<TrekModel>?> get() = _userTreks

    fun addTrek(trekModel: TrekModel, callback: (Boolean, String) -> Unit) {
        repo.addTrek(trekModel, callback)
    }

    fun updateTrek(trekId: String, trekModel: TrekModel, callback: (Boolean, String) -> Unit) {
        repo.updateTrek(trekId, trekModel, callback)
    }

    fun deleteTrek(trekId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteTrek(trekId, callback)
    }

    fun getAllTreks() {
        repo.getAllTreks { success, message, treks ->
            if (success) {
                _allTreks.postValue(treks)
            }
        }
    }

    fun getTreksByUserId(userId: String) {
        repo.getTreksByUserId(userId) { success, message, treks ->
            if (success) {
                _userTreks.postValue(treks)
            }
        }
    }
}