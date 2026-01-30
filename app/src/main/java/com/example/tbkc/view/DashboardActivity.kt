package com.example.tbkc.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tbkc.R


data class NavItem(val label: String, val icon: Int)

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardBody()
        }
    }
}

@Composable
fun DashboardBody() {
    val items = listOf(
        NavItem("Home", R.drawable.baseline_home_24),
        NavItem("My Treks", R.drawable.baseline_search_24),
        NavItem("Add", R.drawable.baseline_add_24),
        NavItem("Profile", R.drawable.baseline_person_24)
    )
    var selectedItem by remember { mutableIntStateOf(0) }

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
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                CurvedBottomNavigation(
                    items = items,
                    selectedIndex = selectedItem,
                    onItemSelected = { selectedItem = it }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (selectedItem) {
                    0 -> HomeScreen()
                    1 -> MyTreks()
                    2 -> AddTrek()
                    3 -> ProfileScreen()
                    else -> HomeScreen()
                }
            }
        }
    }
}

@Composable
fun CurvedBottomNavigation(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {

    val backgroundColor = Color(0x50FFFFFF)
    val activeColor = Color(0xFF6C5CE7)

    val animatedIndex = remember { Animatable(selectedIndex.toFloat()) }

    LaunchedEffect(selectedIndex) {
        animatedIndex.animateTo(
            targetValue = selectedIndex.toFloat(),
            animationSpec = TweenSpec(durationMillis = 500)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val itemWidth = width / items.size
            val centerX = (animatedIndex.value * itemWidth) + (itemWidth / 2)

            val humpWidth = 160f
            val humpHeight = -55f

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(centerX - humpWidth, 0f)

                cubicTo(
                    x1 = centerX - (humpWidth / 1.5f), y1 = 0f,
                    x2 = centerX - (humpWidth / 2f), y2 = humpHeight,
                    x3 = centerX, y3 = humpHeight
                )
                cubicTo(
                    x1 = centerX + (humpWidth / 2f), y1 = humpHeight,
                    x2 = centerX + (humpWidth / 1.5f), y2 = 0f,
                    x3 = centerX + humpWidth, y3 = 0f
                )

                lineTo(width, 0f)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(path, backgroundColor)
        }

        Row(modifier = Modifier.fillMaxSize()) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                val iconOffset by animateDpAsState(
                    targetValue = if (isSelected) (-15).dp else 0.dp,
                    animationSpec = TweenSpec(500),
                    label = "iconOffset"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onItemSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Canvas(
                            modifier = Modifier
                                .size(55.dp)
                                .offset(y = iconOffset)
                        ) {
                            drawCircle(color = activeColor)
                        }
                    }

                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(26.dp)
                            .offset(y = iconOffset),
                        tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DashPreview() {
    DashboardBody()
}