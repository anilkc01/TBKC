package com.example.tbkc

import com.example.tbkc.model.TrekModel
import com.example.tbkc.repository.TrekRepo
import com.example.tbkc.viewModel.TrekViewModel
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class TrekViewModelTest {

    //  Case 1; Add Trek
    @Test
    fun addTrek_success_test() {
        val repo = mock<TrekRepo>()
        val viewModel = TrekViewModel(repo)
        val trek = TrekModel(title = "Everest", budget = "50000")

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Trek Added")
            null
        }.`when`(repo).addTrek(eq(trek), any())

        var successResult = false
        viewModel.addTrek(trek) { success, _ -> successResult = success }

        assertTrue(successResult)
        verify(repo).addTrek(eq(trek), any())
    }

    //  Case 2: Update Trek
    @Test
    fun updateTrek_success_test() {
        val repo = mock<TrekRepo>()
        val viewModel = TrekViewModel(repo)
        val trek = TrekModel(title = "Updated Everest")

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Trek Updated")
            null
        }.`when`(repo).updateTrek(eq("trek_123"), eq(trek), any())

        var successResult = false
        viewModel.updateTrek("trek_123", trek) { success, _ -> successResult = success }

        assertTrue(successResult)
        verify(repo).updateTrek(eq("trek_123"), eq(trek), any())
    }

    //  Case 3-- Delete Trek
    @Test
    fun deleteTrek_success_test() {
        val repo = mock<TrekRepo>()
        val viewModel = TrekViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Trek Deleted")
            null
        }.`when`(repo).deleteTrek(eq("trek_123"), any())

        var successResult = false
        viewModel.deleteTrek("trek_123") { success, _ -> successResult = success }

        assertTrue(successResult)
        verify(repo).deleteTrek(eq("trek_123"), any())
    }
}