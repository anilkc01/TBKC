package com.example.tbkc

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tbkc.view.DashboardActivity
import com.example.tbkc.view.LoginActivity
import com.example.tbkc.view.RegisterActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<LoginActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun loginToDashboard() {

        composeRule.onNodeWithTag("emailField").performTextInput("anil@gmail.com")
        composeRule.onNodeWithTag("passwordField").performTextInput("Hello@123")

        composeRule.onNodeWithTag("LoginButton").performClick()

        Thread.sleep(3000)

        Intents.intended(hasComponent(DashboardActivity::class.java.name))
    }

    @Test
    fun NavigateToRegistration() {

        composeRule.onNodeWithTag("goToRegister").performClick()

        Intents.intended(hasComponent(RegisterActivity::class.java.name))
    }
}