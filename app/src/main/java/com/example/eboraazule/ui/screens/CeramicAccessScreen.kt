package com.example.eboraazule.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.eboraazule.R

@Composable
fun CeramicAccessScreen(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Ceramic Detail Image (ID: 4b85a3e04d3949088d6a770e52f74d48)
            AsyncImage(
                model = "https://images.unsplash.com/photo-1578321272176-b7bac0429b5a?auto=format&fit=crop&q=80&w=1000", // Detail of ceramics/tiles
                contentDescription = null,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.placeholder)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Patrimonio de la Humanidad",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "La cerámica de Talavera no es solo arte; es el alma de nuestra ciudad. Un legado vivo que ahora puedes explorar en la palma de tu mano.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.3
            )

            Spacer(modifier = Modifier.weight(0.2f))

            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary // Terra Cotta
                )
            ) {
                Text(
                    "Entrar a EboraAzule",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            TextButton(onClick = onBack) {
                Text("Revisar Introducción", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
