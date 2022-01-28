package com.poznan.put.michalxpz.graphedu.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import com.poznan.put.michalxpz.graphedu.Colors.Black
import com.poznan.put.michalxpz.graphedu.Colors.Danger002
import com.poznan.put.michalxpz.graphedu.Colors.Primary001
import com.poznan.put.michalxpz.graphedu.Colors.Primary002

private val LightColors = lightColors(
    primary = Primary002,
    primaryVariant = Primary001,
    onPrimary = White,
    background = White,
    surface = White,
    onBackground = Black,
    onSurface = Black,
    error = Danger002,
    onError = White,
)
private val DarkColors = darkColors(
    primary = Primary002,
    primaryVariant = Primary001,
    onPrimary = White,
    background = White,
    surface = White,
    onBackground = Black,
    onSurface = Black,
    error = Danger002,
    onError = White,
)

@Composable
fun GraphEduTheme(
    content: @Composable () -> Unit
) {
    val colors: Colors
    if (isSystemInDarkTheme()) colors = DarkColors else colors = LightColors
    MaterialTheme(
        colors = colors,
        typography = GraphEduTypography,
        shapes = GraphEduShapes,
        content = content
    )
}