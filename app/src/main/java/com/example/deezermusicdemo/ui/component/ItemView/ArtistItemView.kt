package com.example.deezermusicdemo.ui.component.ItemView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.domain.model.ArtistItem

@Composable
fun ArtistItemView (
    modifier: Modifier = Modifier,
    item: ArtistItem,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color = colorResource(R.color.black_100))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Album Image
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = item.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        // Mask
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.45f to colorResource(R.color.transparent),
                            1.0f to colorResource(R.color.black_90)
                        )
                    )
                )
        )

        // Album Name
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun ArtistItemViewPreview() {
    ArtistItemView(
        item = ArtistItem(
            id = 0,
            name = "Album",
            thumbnail = ""
        ),
        onClick = {}
    )
}