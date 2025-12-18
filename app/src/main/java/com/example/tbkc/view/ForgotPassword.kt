package com.example.tbkc.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tbkc.repository.UserRepoImplementation
import com.example.tbkc.view.ui.theme.TBKCTheme
import com.example.tbkc.viewModel.UserViewModel

class ForgotPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TBKCTheme {
                ForgotPasswordScreen()
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ForgotPasswordScreen() {
    var email by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity


    val userViewModel = UserViewModel(UserRepoImplementation())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6B4E71),
                        Color(0xFF4A3B6B),
                        Color(0xFF2D1B3D)
                    )
                )
            )
    ) {
        // Background decorative circles
        Box(
            modifier = Modifier
                .size(280.dp)
                .offset(x = (-70).dp, y = 100.dp)
                .background(
                    color = Color(0x40A07B8A),
                    shape = RoundedCornerShape(50)
                )
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = 180.dp, y = 400.dp)
                .background(
                    color = Color(0x403D2C4D),
                    shape = RoundedCornerShape(50)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Back button
            IconButton(
                onClick = { activity?.finish() },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text(
                text = "Forgot\nPassword?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "Don't worry! It happens. Please enter the email address associated with your account and we will send you your reset link.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Email Input
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Send OTP Button
            Button(
                onClick = {
                    userViewModel.forgetPassword(email) { success, message ->
                        if(success){
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            activity?.finish()
                        }else{
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C5CE7)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Email Reset Link",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
@Preview
fun ForgotPassScreen(){
    ForgotPasswordScreen()
}