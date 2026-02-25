package com.example.tbkc.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.tbkc.model.TrekModel
import com.example.tbkc.repository.TrekRepoImpl
import com.example.tbkc.repository.UserRepoImplementation
import com.example.tbkc.viewModel.TrekViewModel
import com.example.tbkc.viewModel.UserViewModel

@Composable
fun HomeScreen() {
    val trekViewModel = remember { TrekViewModel(TrekRepoImpl()) }

    val userViewModel = remember { UserViewModel(UserRepoImplementation()) }
    val allTreks by trekViewModel.allTreks.observeAsState(initial = null)

    var showDialog by remember { mutableStateOf(false) }
    var selectedTrek by remember { mutableStateOf<TrekModel?>(null) }

    LaunchedEffect(Unit) {
        trekViewModel.getAllTreks()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF6B4E71), Color(0xFF2D1B3D))))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(60.dp))
            Text("Discover Treks", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Find your next adventure", color = Color.White.copy(0.6f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(20.dp))

            if (allTreks == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (allTreks!!.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No treks shared yet.", color = Color.White.copy(0.5f))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(allTreks!!) { trek ->
                        TrekListItem(trek = trek, onClick = {
                            selectedTrek = trek
                            showDialog = true
                        })
                    }
                }
            }
        }


        if (showDialog && selectedTrek != null) {
            TrekDetailDialog(
                trek = selectedTrek!!,
                userViewModel = userViewModel,
                ) {
                showDialog = false
            }
        }
    }
}

@Composable
fun TrekDetailDialog(trek: TrekModel,userViewModel: UserViewModel, onDismiss: () -> Unit) {

    val ownerData by userViewModel.users.observeAsState()

    LaunchedEffect(trek.userId) {
        userViewModel.getUserById(trek.userId)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            color = Color(0xFF2D1B3D),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Image
                Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                    AsyncImage(
                        model = trek.image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(Color.Black.copy(0.4f), RoundedCornerShape(50.dp))
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                    }
                }

                // Info Content
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ownerData?.dp ?: "https://via.placeholder.com/150",
                            contentDescription = "Owner DP",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.Gray),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = ownerData?.fullName ?: "Loading...",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text("Publisher", color = Color.White.copy(0.6f), fontSize = 12.sp)
                        }
                    }

                    Text(trek.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(trek.location, color = Color.Cyan, fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Days and Budget Row
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        InfoChip(label = "${trek.days} Days", value = "Duration")
                        InfoChip(label = "Rs. ${trek.budget}", value = "Budget")
                        InfoChip(label = trek.difficulty, value = "Difficulty")
                    }

                    SectionHeader("Description")
                    Text(trek.description, color = Color.White.copy(0.8f))

                    SectionHeader("Day-by-Day Itinerary")
                    trek.itinerary.forEachIndexed { index, day ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("Day ${index + 1}: ", fontWeight = FontWeight.Bold, color = Color.Cyan)
                            Text(day, color = Color.White.copy(0.8f))
                        }
                    }

                    SectionHeader("Recommendations & tips")
                    Text(trek.recommendations, color = Color.White.copy(0.8f))

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun InfoChip(label: String, value: String) {
    Column {
        Text(value, color = Color.White.copy(0.5f), fontSize = 12.sp)
        Text(label, color = Color.White, fontWeight = FontWeight.Bold)
    }
}