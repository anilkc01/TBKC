package com.example.tbkc.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tbkc.repository.TrekRepoImpl
import com.example.tbkc.viewModel.TrekViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyTreks(onEditClick: (String) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val currentUid = auth.currentUser?.uid ?: ""

    val trekViewModel = remember { TrekViewModel(TrekRepoImpl()) }
    val userTreks by trekViewModel.userTreks.observeAsState(initial = null)

    // Fetch user-specific data
    LaunchedEffect(currentUid) {
        if (currentUid.isNotEmpty()) {
            trekViewModel.getTreksByUserId(currentUid)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF6B4E71), Color(0xFF2D1B3D))))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(60.dp))
            Text("My Adventures", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Your personal trek history", color = Color.White.copy(0.6f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(20.dp))

            if (userTreks == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (userTreks!!.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("You haven't posted any adventures yet!", color = Color.White.copy(0.5f))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(userTreks!!) { trek ->
                        TrekListItem(trek = trek, onClick = { onEditClick(trek.trekId) })
                    }
                }
            }
        }
    }
}