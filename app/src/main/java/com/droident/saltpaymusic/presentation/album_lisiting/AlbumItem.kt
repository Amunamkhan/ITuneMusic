package com.droident.saltpaymusic.presentation.album_lisiting


import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.droident.saltpaymusic.domain.model.Album




@Composable
fun AlbumItem(
    album: Album,
    modifier: Modifier = Modifier,
    onToggleFavorite: (Int, Boolean) -> Unit,
    isFavorite: Boolean,
) {
    Row(modifier) {
        AlbumImage(album, Modifier.padding(16.dp))
        Column(Modifier
            .width(IntrinsicSize.Max)
            .weight(1f)
            .padding(vertical = 10.dp)) {
            AlbumTitle(album)
            AlbumAuthor(album)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(text = album.price,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground)
            FavoriteButton(isFavorite = isFavorite, onClick = {
                onToggleFavorite(album.albumId, isFavorite)
            })
        }

    }
}


@Composable
fun AlbumAuthor(
    album: Album,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        Text(text = album.author, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun AlbumImage(
    album: Album,
    modifier: Modifier = Modifier,
) {
    Box(modifier = Modifier.size(65.dp)) {
        AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(album.image).build(),
            placeholder = painterResource(com.droident.saltpaymusic.R.drawable.placeholder_1_1),
            contentDescription = null,
            modifier = modifier
                .size(60.dp, 60.dp)
                .clip(MaterialTheme.shapes.small))
    }
}


@Composable
fun AlbumTitle(album: Album) {
    Text(
        text = album.name,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}


@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    IconToggleButton(checked = isFavorite,
        onCheckedChange = { onClick() },
        modifier = modifier.semantics {
            // Use a custom click label that accessibility services can communicate to the user.
            // We only want to override the label, not the actual action, so for the action we pass null.
            this.onClick("", action = null)
        }) {
        Icon(imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
            contentDescription = null // handled by click label of parent
        )
    }
}