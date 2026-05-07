package com.example.eboraazule.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.eboraazule.R

@Composable
fun WelcomeScreen(
    onExplore: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // High-resolution landscape image (ID: 0bc5f10ee4e2464f951ab455ff94b522)
        // Using Coil for potential URL or local placeholder
        AsyncImage(
            model = "https://images.unsplash.com/photo-1543783230-050414a6003b?auto=format&fit=crop&q=80&w=2000", // Representative landscape of Spain
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.placeholder)
        )
        
        // Gradient overlay for readability and premium feel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        ),
                        startY = 300f
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "EboraAzule",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.secondary, // Ceramic Yellow
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Bienvenida Talavera",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Descubre la ciudad donde el Tajo susurra historias a través de su cerámica milenaria.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = onExplore,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary // Azure Blue
                )
            ) {
                Text(
                    text = "Explorar Patrimonio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}
