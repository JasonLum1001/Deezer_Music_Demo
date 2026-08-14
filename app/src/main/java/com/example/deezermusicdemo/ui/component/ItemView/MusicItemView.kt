package com.example.deezermusicdemo.ui.component.ItemView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deezermusicdemo.R
import com.example.deezermusicdemo.common.component.IconButton
import com.example.deezermusicdemo.common.model.DropdownItem
import com.example.deezermusicdemo.domain.model.MusicItem

@Composable
fun MusicItemView (
    modifier: Modifier = Modifier,
    item: MusicItem,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.albumArt,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Black,
                color = colorResource(R.color.white_100),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.artist,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Thin,
                color = colorResource(R.color.white_80),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        MoreButton(
            dropdownItemList = listOf(
                DropdownItem(
                    icon = if (item.isBookmarked)
                        Icons.Default.FavoriteBorder
                    else
                        Icons.Default.Favorite,
                    text = if (item.isBookmarked)
                        stringResource(R.string.remove_from_favorite)
                    else
                        stringResource(R.string.add_to_fav),
                    onClicked = onBookmarkClick
                )
            )
        )
    }
}

@Composable
private fun MoreButton(
    modifier: Modifier = Modifier,
    dropdownItemList: List<DropdownItem>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 10.dp),
            iconTint = colorResource(R.color.white_100),
            iconSize = 24.dp,
            imageVector = Icons.Default.MoreVert,
            onClick = { expanded = true }
        )

        DropdownMenu(
            modifier = Modifier.defaultMinSize(minWidth = 200.dp),
            shape = RoundedCornerShape(12.dp),
            containerColor = colorResource(R.color.white_100),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dropdownItemList.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorResource(R.color.black_90),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = {
                        expanded = false
                        item.onClicked.invoke()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = item.icon,
                            tint = colorResource(R.color.black_90),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}