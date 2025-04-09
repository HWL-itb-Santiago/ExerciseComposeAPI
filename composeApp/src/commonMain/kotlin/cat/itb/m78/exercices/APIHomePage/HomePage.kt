package cat.itb.m78.exercices.APIAgents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.itb.m78.exercices.APIHomePage.Result
import cat.itb.m78.exercices.APIHomePage.ViewModelAgents
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    // Controls expansion state of the search bar
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .background(color = Color.Transparent)
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f }
                .fillMaxWidth(0.45f),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { query ->
                        textFieldState.edit { replace(0, length, query) }
                        onSearch(query)
                        expanded = false
                    },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = false,
                    onExpandedChange = { expanded = false },
                    placeholder = { Text("Search") }
                )
            },
            expanded = false,
            onExpandedChange = { expanded = false },
        )
        {
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//
//            ) {
//                items(searchResults) { result ->
//                    ListItem(
//                        headlineContent = { Text(result) },
//                        modifier = Modifier
//                            .clickable {
//                                textFieldState.edit { replace(0, length, result) }
//                                expanded = false
//                                onSearch(textFieldState.text.toString())
//                            }
//                            .background(color = Color.Black)
//
//                    )
//                }
//            }
        }
        if (expanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            expanded = false // Colapsar la SearchBar al hacer clic fuera
                        }
                    }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(goToGamePage: (Long) -> Unit) {
    val viewModel = viewModel { ViewModelAgents() }
    val currentGames by viewModel.data.collectAsState()
    val filteredGames by viewModel.filteredGames.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val showSearchbar = remember { mutableStateOf(false) }

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
                            "GameDB",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showSearchbar.value = !showSearchbar.value }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Aquí puedes agregar contenido si lo deseas
                }
            }
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                if (showSearchbar.value) {
                    SimpleSearchBar(
                        textFieldState = viewModel.textFieldState.value,
                        onSearch = { query -> viewModel.onSearch(query) },
                        searchResults = filteredGames.map { it.name ?: "" }
                    )
                }
                if (currentGames.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (!isSearching) {
                    GameGrid(innerPadding, 2, currentGames, { viewModel.MoreGames() }, goToGamePage)
                } else {
                    GameGrid(innerPadding, 2, filteredGames, { viewModel.MoreGames() }, goToGamePage)
                }
            }
        }
    }

    println(currentGames.size)
    println(filteredGames.map { it.name ?: "" })
    println(filteredGames.map { it.id ?: "" })
}


@Composable
fun GameGrid(innerPadding: PaddingValues, colSize: Int, games: List<Result>, loadMoreGames: () -> Unit, goToGamePage: (Long) -> Unit)
{
    var visibleGames by remember { mutableStateOf(games.take(games.size)) } // Muestra solo los primeros 10 juegos por defecto
    var isLoading by remember { mutableStateOf(false) } // Controla si se está cargando más contenido

    LazyVerticalGrid(
        columns = GridCells.Fixed(colSize),
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(start = 90.dp, end = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(visibleGames) { game ->
            GameCard(game, goToGamePage = { game.id?.let { goToGamePage(it) } })
        }
        item {
            if (!isLoading) {
                IconButton(onClick = {
                    isLoading = true
                    loadMoreGames()// Llamada a la función para cargar más juegos
                }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "More")
                }

            } else {
                CircularProgressIndicator() // Muestra un indicador de carga mientras se cargan más juegos
            }
        }
    }
    LaunchedEffect(games)
    {
        visibleGames = games.take(games.size)
        isLoading = false
    }
}
@Composable
fun GameCard(game: Result, goToGamePage: () -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        val isCompact = maxWidth < 500.dp
        val isWidth = maxWidth < 800.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { goToGamePage() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = game.backgroundImage,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(if (isCompact) 100.dp else if ((isWidth))150.dp else 250.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                game.name?.let {
                    Text(
                        text = it,
                        fontSize = if (isCompact) 16.sp else if (isWidth)20.sp else 30.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    text = "Rating: ${game.rating ?: ""} ★",
                    fontSize = if (isCompact) 12.sp else if (isWidth)15.sp else 25.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Released: ${game.released ?: ""}",
                    fontSize = if (isCompact) 12.sp else if (isWidth)15.sp else 25.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = game.esrbRating?.name ?: "",
                    fontSize = if (isCompact) 12.sp else if (isWidth)15.sp else 25.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {},
                    modifier = Modifier
                        .defaultMinSize(minHeight = 36.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Añadir a favoritos")
                }
            }
        }
    }
}

