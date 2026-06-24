package com.talaveracity.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.talaveracity.app.ui.theme.AmarilloCeramica
import com.talaveracity.app.ui.theme.AzulCobalto
import com.talaveracity.app.ui.theme.VerdeCobre

@Composable
fun GrecaTalavera(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp)
    ) {
        val patternWidth = 40.dp.toPx()
        val steps = (size.width / patternWidth).toInt() + 1
        
        for (i in 0 until steps) {
            val startX = i * patternWidth
            
            // Línea base azul cobalto
            drawLine(
                color = AzulCobalto,
                start = Offset(startX, size.height / 2),
                end = Offset(startX + patternWidth, size.height / 2),
                strokeWidth = 4f
            )
            
            // Rombos decorativos tricolores
            drawCircle(
                color = AmarilloCeramica,
                radius = 6f,
                center = Offset(startX + patternWidth / 4, size.height / 2)
            )
            
            drawCircle(
                color = VerdeCobre,
                radius = 6f,
                center = Offset(startX + (patternWidth * 3) / 4, size.height / 2)
            )
        }
    }
}




