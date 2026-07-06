package com.example.deezermusicdemo.common.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deezermusicdemo.R


@Composable
fun TitleBar(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 24.dp, bottom = 8.dp),
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.white_100),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Heading(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 8.dp),
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.white_100),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}