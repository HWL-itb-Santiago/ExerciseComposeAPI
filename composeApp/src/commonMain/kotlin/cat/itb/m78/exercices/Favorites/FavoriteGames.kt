package cat.itb.m78.exercices.Favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.itb.m78.exercices.APIAgents.SimpleSearchBar
import cat.itb.m78.exercices.APIHomePage.Result
import cat.itb.m78.exercices.APIHomePage.ViewModelAgents
import cat.itb.m78.exercices.db.GameDB
import coil3.compose.AsyncImage

@Composable
fun FavoriteGames(goBackToHomePage: () -> Unit, goToGamePage: (Long) -> Unit) {
    val favoriteGamesViewModel = viewModel { FavoriteGamesViewModel() }
    val currentGames by favoriteGamesViewModel.favoriteGames.collectAsState()
    val filteredGames by favoriteGamesViewModel.filteredGames.collectAsState()

    FavoriteGames(
        goToGamePage = goToGamePage,
        goBackToHomePage = goBackToHomePage,
        currentGames = currentGames,
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteGames(
    goToGamePage: (Long) -> Unit,
    goBackToHomePage: () -> Unit,
    showSearchbar: MutableState<Boolean> = remember { mutableStateOf(false) },
    currentGames: List<GameDB>,
)
{
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {},
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 50.dp)
                            .clickable(onClick = {})
                    ) {
                        Text(
                            "Favorites DB",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "HomePage",
                        modifier = Modifier.clickable(onClick = goBackToHomePage)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                if (currentGames.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                GameGrid(
                    innerPadding,
                    2,
                    currentGames,
                    goToGamePage
                )
            }
        }
    }
}

@Composable
fun GameGrid(innerPadding: PaddingValues, colSize: Int, games: List<GameDB>, goToGamePage: (Long) -> Unit)
{
    LazyVerticalGrid(
        columns = GridCells.Fixed(colSize),
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(start = 90.dp, end = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(games) { game ->
            GameCard(game,goToGamePage = { goToGamePage(game.id) })
        }
    }
}
@Composable
fun GameCard(
    game: GameDB,
    goToGamePage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = viewModel { ViewModelAgents() }
    val gameOnFavorites = remember { mutableStateOf(false) }

    LaunchedEffect(game.id) {
        val exists  = viewModel.favoriteList.value.contains(game.id)
        gameOnFavorites.value = exists
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        val isCompact = maxWidth < 500.dp
        val isMedium = maxWidth < 800.dp

        val imageSize = when {
            isCompact -> 100.dp
            isMedium -> 150.dp
            else -> 250.dp
        }

        val textSize = when {
            isCompact -> 12.sp
            isMedium -> 15.sp
            else -> 25.sp
        }

        val titleSize = when {
            isCompact -> 16.sp
            isMedium -> 20.sp
            else -> 30.sp
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { goToGamePage() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = game.gameImage,
                contentDescription = game.gameName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(imageSize)
            )

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = game.gameName,
                    fontSize = titleSize,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Rating: ${game.gameRanking} ★",
                    fontSize = textSize,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Released: ${game.gameDataRealesed}",
                    fontSize = textSize,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}