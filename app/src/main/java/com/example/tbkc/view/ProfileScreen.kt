package com.example.tbkc.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.tbkc.model.UserModel
import com.example.tbkc.repository.ImageRepoImpl
import com.example.tbkc.repository.UserRepoImplementation
import com.example.tbkc.viewModel.ImageViewModel
import com.example.tbkc.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

// ─── Theme Colors ────────────────────────────────────────────────────────────
private val GradTop      = Color(0xFF6B4E71)
private val GradMid      = Color(0xFF4A3B6B)
private val GradBottom   = Color(0xFF2D1B3D)
private val AccentPurple = Color(0xFF6C5CE7)
private val CardBg       = Color(0x30FFFFFF)
private val CardBgHover  = Color(0x50FFFFFF)
private val DangerRed    = Color(0xFFE74C3C)

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUid = auth.currentUser?.uid ?: ""

    // CRITICAL: Remember the ViewModel so it isn't recreated on every keystroke
    val userViewModel = remember { UserViewModel(UserRepoImplementation()) }
    val ImageViewModel = remember { ImageViewModel(ImageRepoImpl()) }

    // Observe DB state
    val userFromDb by userViewModel.users.observeAsState(initial = null)

    // Local UI state
    var fullName by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var gender   by remember { mutableStateOf("") }
    var dob      by remember { mutableStateOf("") }


    var cloudinaryLink by remember { mutableStateOf("") }

    var isEditing        by remember { mutableStateOf(false) }
    var isSaving         by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDatePicker   by remember { mutableStateOf(false) }
    var showGenderMenu   by remember { mutableStateOf(false) }

    // 1. Fetch data on load
    LaunchedEffect(currentUid) {
        if (currentUid.isNotEmpty()) {
            userViewModel.getUserById(currentUid)
        }
    }

    // 2. Sync DB data to Local State when it arrives
    LaunchedEffect(userFromDb) {
        userFromDb?.let {
            fullName = it.fullName
            email    = it.email
            gender   = it.gender
            dob      = it.dob
            cloudinaryLink    = it.dp
        }
    }

    // Image Picker Logic
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            ImageViewModel.uploadImage(context, it) { success, imageUrl ->
                if (success) {
                    cloudinaryLink = imageUrl.toString()
                } else {
                    Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradTop, GradMid, GradBottom)))
    ) {
        // Decorative background elements
        Box(modifier = Modifier.size(280.dp).offset(150.dp, (-40).dp).background(Color(0x40A07B8A), CircleShape))

        if (userFromDb == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(56.dp))

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("My\nProfile", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

                    Button(
                        onClick = {
                            if (isEditing) {
                                isSaving = true
                                val updatedUser = UserModel(
                                    id = currentUid,
                                    fullName = fullName,
                                    email = email,
                                    gender = gender,
                                    dob = dob,
                                    dp = cloudinaryLink)
                                userViewModel.editProfile(currentUid, updatedUser) { success, msg ->
                                    isSaving = false
                                    if (success) isEditing = false
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                isEditing = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isEditing) AccentPurple else CardBgHover),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Icon(if (isEditing) Icons.Default.Save else Icons.Default.Edit, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(if (isEditing) "Save" else "Edit")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Avatar
                Box(contentAlignment = Alignment.BottomEnd) {
                    if (cloudinaryLink.isNotEmpty()) {
                        AsyncImage(
                            model = cloudinaryLink,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp).clip(CircleShape).border(2.dp, AccentPurple, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier.size(100.dp).clip(CircleShape).background(CardBgHover).border(2.dp, AccentPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(fullName.take(1).uppercase(), fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (isEditing) {
                        IconButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier.size(32.dp).background(AccentPurple, CircleShape)
                        ) {
                            Icon(Icons.Default.CameraAlt, null, Modifier.size(16.dp), Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(fullName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(email, fontSize = 14.sp, color = Color.White.alpha(0.6f))

                Spacer(modifier = Modifier.height(32.dp))

                // Fields Card
                Box(modifier = Modifier.fillMaxWidth().background(CardBg, RoundedCornerShape(24.dp)).padding(20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ProfileFieldRow("Full Name", fullName, isEditing, KeyboardType.Text) { fullName = it }
                        Divider(color = Color.White.copy(0.1f))
                        ProfileFieldRow("Email", email, false, KeyboardType.Email) { email = it }
                        Divider(color = Color.White.copy(0.1f))
                        ProfileClickableRow("Date of Birth", dob, isEditing, "Select Date") { showDatePicker = true }
                        Divider(color = Color.White.copy(0.1f))
                        ProfileClickableRow("Gender", gender, isEditing, "Select Gender") { showGenderMenu = true }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                ActionButton("Log Out", Icons.Default.Logout, CardBgHover) { showLogoutDialog = true }
                Spacer(modifier = Modifier.height(12.dp))
                ActionButton("Delete Account", Icons.Default.Delete, DangerRed.copy(0.8f)) { showDeleteDialog = true }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Dialogs
    if (showGenderMenu) {
        Dialog(onDismissRequest = { showGenderMenu = false }) {
            Card(colors = CardDefaults.cardColors(containerColor = GradMid)) {
                Column(Modifier.padding(16.dp)) {
                    listOf("Male", "Female", "Other").forEach {
                        Text(it, Modifier.fillMaxWidth().clickable { gender = it; showGenderMenu = false }.padding(12.dp), color = Color.White)
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        ConfirmDialog("Log Out", "Are you sure?", "Log Out", AccentPurple, {
            auth.signOut()
            context.startActivity(Intent(context, com.example.tbkc.view.LoginActivity::class.java))
        }, { showLogoutDialog = false })
    }



    if (showDeleteDialog) {
        ConfirmDialog(
            "Delete Account",
            "This is permanent. Are you sure?",
            "Delete",
            DangerRed,
            onConfirm = {
                userViewModel.deleteAccount(currentUid) { success, msg ->
                    if (success) {
                        auth.currentUser?.delete()
                        context.startActivity(Intent(context, com.example.tbkc.view.LoginActivity::class.java))
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileFieldRow(label: String, value: String, isEditing: Boolean, keyboard: KeyboardType, onValueChange: (String) -> Unit) {
    Column {
        Text(label, fontSize = 11.sp, color = Color.White.copy(0.5f), fontWeight = FontWeight.Bold)
        if (isEditing) {
            TextField(
                value = value, onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
        } else {
            Text(value.ifEmpty { "—" }, fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
private fun ProfileClickableRow(label: String, value: String, isEditing: Boolean, placeholder: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable(enabled = isEditing) { onClick() }) {
        Text(label, fontSize = 11.sp, color = Color.White.copy(0.5f), fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(value.ifEmpty { placeholder }, fontSize = 16.sp, color = if(value.isEmpty()) Color.Gray else Color.White)
            if (isEditing) Icon(Icons.Default.Edit, null, Modifier.size(14.dp), Color.White.copy(0.5f))
        }
    }
}

@Composable
private fun ActionButton(label: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().height(54.dp), colors = ButtonDefaults.buttonColors(containerColor = color), shape = RoundedCornerShape(28.dp)) {
        Icon(icon, null, Modifier.size(18.dp), Color.White)
        Spacer(Modifier.width(10.dp))
        Text(label, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ConfirmDialog(title: String, message: String, confirmText: String, confirmColor: Color, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = GradBottom)) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                Spacer(Modifier.height(8.dp))
                Text(message, textAlign = TextAlign.Center, color = Color.White.copy(0.7f))
                Spacer(Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(onClick = onConfirm, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = confirmColor)) { Text(confirmText) }
                }
            }
        }
    }
}

// Extension to make alpha easier
fun Color.alpha(alpha: Float) = this.copy(alpha = alpha)