package com.example.tbkc.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TBKCTheme {
                LoginScreen()
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                .offset(x = 150.dp, y = -40.dp)
                .background(
                    color = Color(0x40A07B8A),
                    shape = RoundedCornerShape(50)
                )
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-60).dp, y = 500.dp)
                .background(
                    color = Color(0x402D1F3D),
                    shape = RoundedCornerShape(50)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {


            Spacer(modifier = Modifier.height(80.dp))

            // Title
            Text(
                text = "Welcome\nBack",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(100.dp))

            // Email Input
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            CustomPasswordField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",

            )

            Spacer(modifier = Modifier.height(15.dp))

            // Forgot Password

            Text(
                text = "Forgot Password ?",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        val intent = Intent(
                            context,
                            ForgotPassword::class.java
                        )
                        context.startActivity(intent)
                    }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = {
                    userViewModel.login(email, password) { success, message ->
                        if(success) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(
                                context,
                                DashboardActivity::class.java
                            )
                            context.startActivity(intent)
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
                    text = "Log in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }



            Spacer(modifier = Modifier.weight(1f))

            // Sign Up text
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Don't have an account ? ",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
                Text(
                    text = "Sign in",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        val intent = Intent(
                            context,
                            RegisterActivity::class.java
                        )
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}


@Composable
@Preview
fun LoginScreenPreview() {
    LoginScreen()
}