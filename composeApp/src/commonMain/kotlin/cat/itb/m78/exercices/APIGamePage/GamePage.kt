package cat.itb.m78.exercices.APIGamePage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun GamePage(backToHomePage: () -> Unit, gameId: Long) {
    val viewModel = viewModel { ViewModelGamePage(gameId) }
    val game by viewModel.gameData.collectAsState()

    if (game == null) {
        Box(
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
        {
            val isCompact = maxWidth < 500.dp
            val isMedium = maxWidth in 500.dp..800.dp

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start
            ) {

                item {
                    game?.backgroundImage?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = game?.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .fillMaxWidth()
                                .height(
                                    when {
                                        isCompact -> 180.dp
                                        isMedium -> 480.dp
                                        else -> 620.dp
                                    }
                                )
                                .clip(RoundedCornerShape(16.dp)),

                            )
                    }
                }

                item {
                    Text(
                        text = game?.name ?: "Unknown Game",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "⭐ ${game?.rating ?: 0.0}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "📅 ${game?.released ?: "?"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        game?.genres?.take(2)?.forEach {
                            it.name?.let { name ->
                                Text(
                                    text = "🎮 $name",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Description",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = game?.descriptionRaw ?: "No description available.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        )
                    }
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        game?.developers?.take(3)?.forEach {
                            it.name?.let { devName ->
                                Text(
                                    "👨‍💻 Developed by: $devName",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        game?.publishers?.take(2)?.forEach {
                            it.name?.let { pubName ->
                                Text(
                                    "📢 Publisher: $pubName",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        game?.platforms?.take(3)?.forEach {
                            it.platform?.name?.let { platform ->
                                Text(
                                    "👨‍💻 On : $platform",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = backToHomePage,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Back to Home")
                    }
                }
            }
        }


    }

}

