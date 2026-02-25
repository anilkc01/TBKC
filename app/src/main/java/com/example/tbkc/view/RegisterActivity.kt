package com.example.tbkc.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tbkc.R
import com.example.tbkc.model.UserModel
import com.example.tbkc.repository.UserRepoImplementation
import com.example.tbkc.view.ui.theme.TBKCTheme
import com.example.tbkc.viewModel.UserViewModel

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TBKCTheme {
               SignUpScreen()
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun SignUpScreen() {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showGenderMenu by remember { mutableStateOf(false) }

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
                        Color(0xFF3D2C4D),
                        Color(0xFF2D1B3D)
                    )
                )
            )
    ) {
        // Background decorative circles
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-80).dp, y = 60.dp)
                .background(
                    color = Color(0x40A07B8A),
                    shape = RoundedCornerShape(50)
                )
        )

        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = 200.dp, y = (-50).dp)
                .background(
                    color = Color(0x404A3B6B),
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
                text = "Create\nAccount",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Full Name Input
            CustomTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Full name",
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Input
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))
            //date picker and gender
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Date of Birth Picker
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .background(
                            color = Color(0x30FFFFFF),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .clickable { showDatePicker = true }
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = if (dateOfBirth.isEmpty()) "Date of Birth" else dateOfBirth,
                        color = if (dateOfBirth.isEmpty())
                            Color.White.copy(alpha = 0.5f)
                        else
                            Color.White,
                        fontSize = 14.sp
                    )
                }

                // Gender Dropdown
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color(0x30FFFFFF),
                                shape = RoundedCornerShape(28.dp)
                            )
                            .clickable { showGenderMenu = true }
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = if (gender.isEmpty()) "Gender" else gender,
                            color = if (gender.isEmpty())
                                Color.White.copy(alpha = 0.5f)
                            else
                                Color.White,
                            fontSize = 14.sp
                        )
                    }

                    DropdownMenu(
                        expanded = showGenderMenu,
                        onDismissRequest = { showGenderMenu = false },
                        modifier = Modifier
                            .background(Color(0xFF3D2C4D))
                            .width(150.dp)
                    ) {
                        listOf("Male", "Female").forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option,
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    gender = option
                                    showGenderMenu = false
                                }
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            CustomPasswordField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",

            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            CustomPasswordField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",

            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Button
            Button(
                onClick = {
                    userViewModel.register(email, password) { success, message, userId ->
                        if(success){
                            val user = UserModel(
                                id = userId,
                                fullName = fullName,
                                gender = gender,
                                dob = dateOfBirth,
                                email = email
                                )
                            userViewModel.addUserToDatabase(userId, user) { success, message ->
                                if(success) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    activity?.finish()
                                }else{
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }

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
                    text = "Sign up",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))



            // Login text
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Already have an account ? ",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
                Text(
                    text = "Log in",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        activity?.finish()
                    }
                )
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = { selectedDate ->
                    dateOfBirth = selectedDate
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0x50FFFFFF),
            unfocusedContainerColor = Color(0x30FFFFFF),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White
        ),
        shape = RoundedCornerShape(28.dp),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0x50FFFFFF),
            unfocusedContainerColor = Color(0x30FFFFFF),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White
        ),
        shape = RoundedCornerShape(28.dp),
        singleLine = true,
        visualTransformation =
            if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(
                        if (passwordVisible)
                            R.drawable.baseline_visibility_24
                        else
                            R.drawable.baseline_visibility_off_24
                    ),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = it
        "${calendar.get(java.util.Calendar.DAY_OF_MONTH)}/${calendar.get(java.util.Calendar.MONTH) + 1}/${calendar.get(java.util.Calendar.YEAR)}"
    } ?: ""

    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(selectedDate)
                }
            ) {
                Text("OK", color = Color(0xFF6C5CE7))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White.copy(alpha = 0.7f))
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFF3D2C4D)
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFF3D2C4D),
                titleContentColor = Color.White,
                headlineContentColor = Color.White,
                weekdayContentColor = Color.White,
                subheadContentColor = Color.White,
                yearContentColor = Color.White,
                currentYearContentColor = Color.White,
                selectedYearContentColor = Color.White,
                selectedYearContainerColor = Color(0xFF6C5CE7),
                dayContentColor = Color.White,
                selectedDayContentColor = Color.White,
                selectedDayContainerColor = Color(0xFF6C5CE7),
                todayContentColor = Color(0xFF6C5CE7),
                todayDateBorderColor = Color(0xFF6C5CE7)
            )
        )
    }
}






@Preview
@Composable
fun SignUpScreenPreview() {
        SignUpScreen()
}