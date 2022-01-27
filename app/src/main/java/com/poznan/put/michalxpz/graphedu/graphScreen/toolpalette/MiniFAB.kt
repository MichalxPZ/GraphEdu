package com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MiniFAB(
    item: MultiFabItem,
    buttonScale: Float,
    iconAlpha: Float,
    showLabel: Boolean,
) {
    val buttonColor = MaterialTheme.colors.secondary
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 12.dp)
    ) {
        if (showLabel) {
            Text(
                item.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .alpha(alpha = iconAlpha)
                    .background(color = MaterialTheme.colors.surface)
                    .padding(start = 6.dp, end = 6.dp, top = 4.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Canvas(
                modifier = Modifier
                    .size(32.dp)
                    .clickable(
                        onClick = { item.onClick.invoke(item) },
                        indication = rememberRipple(
                            bounded = false,
                            radius = 20.dp,
                            color = MaterialTheme.colors.onSecondary
                        ),
                        interactionSource = remember { MutableInteractionSource() }
                    )
            ) {
                drawCircle(color = buttonColor, buttonScale)
                drawImage(
                    item.icon,
                    topLeft = Offset(
                        (this.center.x) - (item.icon.width / 2),
                        (this.center.y) - (item.icon.width / 2)
                    ),
                    alpha = iconAlpha
                )
            }
        }
    }
}
