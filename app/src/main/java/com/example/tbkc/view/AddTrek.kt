package com.example.tbkc.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tbkc.model.TrekModel
import com.example.tbkc.repository.ImageRepoImpl
import com.example.tbkc.repository.TrekRepoImpl
import com.example.tbkc.viewModel.ImageViewModel
import com.example.tbkc.viewModel.TrekViewModel
import com.google.firebase.auth.FirebaseAuth

// Using your theme colors
private val GradTop      = Color(0xFF6B4E71)
private val GradBottom   = Color(0xFF2D1B3D)
private val AccentPurple = Color(0xFF6C5CE7)
private val CardBg       = Color(0x30FFFFFF)
private val DangerRed    = Color(0xFFE74C3C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrek(onTrekAdded: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    // ViewModels
    val trekViewModel = remember { TrekViewModel(TrekRepoImpl()) }
    val imageViewModel = remember { ImageViewModel(ImageRepoImpl()) }

    // Form States
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var noOfDays by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf("") }
    var cloudinaryLink by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    // Dynamic Itinerary State
    val itinerary = remember { mutableStateListOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isUploading = true
            imageViewModel.uploadImage(context, it) { success, imageUrl ->
                isUploading = false
                if (success) {
                    cloudinaryLink = imageUrl.toString()
                } else {
                    Toast.makeText(context, "Image Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradTop, GradBottom)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Share Your Adventure",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // --- Image Selection ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBg)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (cloudinaryLink.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.White.copy(0.7f)
                        )
                        Text("Add Trek Photo", color = Color.White.copy(0.7f))
                    }
                } else {
                    AsyncImage(
                        model = cloudinaryLink,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                if (isUploading) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            // --- Basic Info ---
            TrekOutlinedTextField(value = title, onValueChange = { title = it }, label = "Trek Title")
            TrekOutlinedTextField(value = location, onValueChange = { location = it }, label = "Location")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TrekOutlinedTextField(
                    value = noOfDays,
                    onValueChange = { noOfDays = it },
                    label = "Total Days",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )
                TrekOutlinedTextField(
                    value = budget,
                    onValueChange = { budget = it },
                    label = "Budget (Rs.)",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Number
                )
            }

            TrekOutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = "Trek Description",
                minLines = 3
            )

            // --- Dynamic Itinerary Section ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Day by Day Itinerary",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                itinerary.forEachIndexed { index, dayDetail ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TrekOutlinedTextField(
                            value = dayDetail,
                            onValueChange = { itinerary[index] = it },
                            label = "Day ${index + 1} Details",
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { itinerary.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = DangerRed)
                        }
                    }
                }

                TextButton(
                    onClick = { itinerary.add("") },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.Cyan)
                    Spacer(Modifier.width(4.dp))
                    Text("Add Day", color = Color.Cyan)
                }
            }

            TrekOutlinedTextField(
                value = recommendations,
                onValueChange = { recommendations = it },
                label = "Recommendations / Tips",
                minLines = 2
            )

            // --- Submit Button ---
            Button(
                onClick = {
                    if (title.isEmpty() || location.isEmpty() || cloudinaryLink.isEmpty()) {
                        Toast.makeText(context, "Title, Location, and Image are required", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val currentUserId = auth.currentUser?.uid ?: ""
                    val trek = TrekModel(
                        userId = currentUserId,
                        title = title,
                        location = location,
                        days = noOfDays,
                        description = description,
                        itinerary = itinerary.toList(),
                        budget = budget,
                        recommendations = recommendations,
                        image = cloudinaryLink
                    )

                    trekViewModel.addTrek(trek) { success, message ->
                        if (success) {
                            Toast.makeText(context, "Trek Posted Successfully!", Toast.LENGTH_SHORT).show()
                            onTrekAdded() // Navigate back or refresh
                        } else {
                            Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                enabled = !isUploading
            ) {
                Text("POST TREK", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrekOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(0.6f)) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        minLines = minLines,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = CardBg,
            unfocusedContainerColor = CardBg.copy(alpha = 0.5f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White.copy(0.3f),
            cursorColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    )
}