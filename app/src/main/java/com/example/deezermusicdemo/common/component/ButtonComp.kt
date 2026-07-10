package com.example.deezermusicdemo.common.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    iconTint: Color = LocalContentColor.current,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = iconModifier.size(iconSize),
            imageVector = imageVector,
            tint = iconTint,
            contentDescription = null
        )
    }
}