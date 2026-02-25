package com.example.tbkc

import com.example.tbkc.model.UserModel
import com.example.tbkc.repository.UserRepo
import com.example.tbkc.viewModel.UserViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserViewModelTest {

    // Test Case 1: Login Success
    @Test
    fun login_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("tek@gmail.com"), eq("123456"), any())

        var successResult = false
        viewModel.login("test@gmail.com", "123456") { success, _ -> successResult = success }

        assertTrue(successResult)
        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }

    // Test Case 2: Register Success
    @Test
    fun register_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String, String) -> Unit>(2)
            callback(true, "Registration success", "user_123")
            null
        }.`when`(repo).register(eq("new@gmail.com"), eq("password"), any())

        var uidResult = ""
        viewModel.register("new@gmail.com", "password") { _, _, uid -> uidResult = uid }

        assertEquals("user_123", uidResult)
        verify(repo).register(eq("new@gmail.com"), eq("password"), any())
    }

    // Test Case 3: Forget Password Success
    @Test
    fun forgetPassword_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Reset link sent")
            null
        }.`when`(repo).forgetPassword(eq("test@gmail.com"), any())

        var messageResult = ""
        viewModel.forgetPassword("test@gmail.com") { _, msg -> messageResult = msg }

        assertEquals("Reset link sent", messageResult)
        verify(repo).forgetPassword(eq("test@gmail.com"), any())
    }

    // Test Case 4: Edit Profile Success
    @Test
    fun editProfile_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)
        val updatedUser = UserModel(fullName = "Sadip Silwal")

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Profile updated")
            null
        }.`when`(repo).editProfile(eq("user_123"), any(), any())

        var successResult = false
        viewModel.editProfile("user_123", updatedUser) { success, _ -> successResult = success }

        assertTrue(successResult)
        verify(repo).editProfile(eq("user_123"), any(), any())
    }
}