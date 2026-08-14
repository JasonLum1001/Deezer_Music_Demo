package com.example.deezermusicdemo.common.model

import androidx.compose.ui.graphics.vector.ImageVector

data class DropdownItem(
    val icon: ImageVector,
    val text: String,
    val onClicked: () -> Unit
)