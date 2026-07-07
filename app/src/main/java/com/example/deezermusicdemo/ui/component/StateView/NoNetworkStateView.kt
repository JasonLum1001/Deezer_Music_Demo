package com.example.deezermusicdemo.ui.component.StateView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.deezermusicdemo.R

@Composable
fun NoNetworkStateView(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.WifiOff,
            contentDescription = null,
            tint = colorResource(R.color.white_100),
            modifier = Modifier.size(64.dp)
        )

        Text(
            text = stringResource(R.string.network_error_msg),
            color = colorResource(R.color.white_100),
            textAlign = TextAlign.Center
        )

        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}