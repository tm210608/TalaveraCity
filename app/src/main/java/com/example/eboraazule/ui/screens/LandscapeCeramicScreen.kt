package com.example.eboraazule.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.eboraazule.R

@Composable
fun LandscapeCeramicScreen(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Landscape and Ceramic image (ID: 365b675457b143be899c3d608ac5f530)
        AsyncImage(
            model = "https://images.unsplash.com/photo-1590059345033-91223961b72e?auto=format&fit=crop&q=80&w=2000", // Artistic Spain view
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.placeholder)
        )

        // Overlay with ceramic pattern feel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        ),
                        startY = 500f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Volver", color = MaterialTheme.colorScheme.primary)
            }

            Column {
                Text(
                    text = "Sinergia Eterna",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Contempla cómo el horizonte histórico de Talavera se funde con la maestría de su alfarería, reconocida por la UNESCO.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.3
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                LargeFloatingActionButton(
                    onClick = onNext,
                    modifier = Modifier.align(Alignment.End),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Continuar"
                    )
                }
            }
        }
    }
}
