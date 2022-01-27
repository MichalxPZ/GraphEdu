package com.poznan.put.michalxpz.graphedu.graphScreen.toolpalette

import androidx.compose.ui.graphics.ImageBitmap

class MultiFabItem(
    val identifier: String,
    val icon: ImageBitmap,
    val label: String,
    val onClick: (item: MultiFabItem) -> Unit
)