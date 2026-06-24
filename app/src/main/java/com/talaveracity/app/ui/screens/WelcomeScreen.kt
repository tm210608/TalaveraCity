package com.talaveracity.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.talaveracity.app.R
import java.util.Calendar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WelcomeScreen(
    onExplore: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val currentTime = remember { Calendar.getInstance() }
    val hour = currentTime.get(Calendar.HOUR_OF_DAY)
    
    val (greetingRes, descriptionRes) = when {
        hour in 6..12 -> R.string.greeting_morning to R.string.welcome_desc_morning
        hour in 13..20 -> R.string.greeting_afternoon to R.string.welcome_desc_afternoon
        else -> R.string.greeting_night to R.string.welcome_desc_night
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // High-resolution landscape image
        AsyncImage(
            model = "https://images.unsplash.com/photo-1543783230-050414a6003b?auto=format&fit=crop&q=80&w=2000",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.placeholder)
        )
        
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
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
            with(sharedTransitionScope) {
                Text(
                    text = "TalaveraCity",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.sharedElement(
                        rememberSharedContentState(key = "titulo_app"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
            }
            
            Text(
                text = stringResource(greetingRes),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = stringResource(descriptionRes),
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
                    text = stringResource(R.string.btn_explore),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}




