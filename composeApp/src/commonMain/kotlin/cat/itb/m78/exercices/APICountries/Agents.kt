package cat.itb.m78.exercices.APIAgents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cat.itb.m78.exercices.APICountries.AgentAPI
import cat.itb.m78.exercices.APICountries.GameResponse
import cat.itb.m78.exercices.APICountries.ViewModelAgents
import coil3.compose.AsyncImage
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Agents() {
    val viewModel = viewModel { ViewModelAgents() }
    val currentPage by viewModel.currentPage.collectAsState()
    var _currentPage = mutableStateOf(currentPage)
    println(_currentPage)
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
        floatingActionButton = {
            Row {
                FloatingActionButton(onClick = {
                    viewModel.PreviousPage()
                    _currentPage.value = viewModel.currentPage.value
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Add")
                }
                FloatingActionButton(onClick = {
                    viewModel.NextPage()
                    _currentPage.value = viewModel.currentPage.value
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Add")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (viewModel.data.isEmpty())
                CircularProgressIndicator()
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    items(viewModel.data) { index ->
                        if (index != null) {
                            Row {
                                Column(modifier = Modifier.weight(0.2f)) {
                                    Text(text = index.name, fontWeight = FontWeight.Bold)
                                    Text(text = index.updated)
                                }
                                AsyncImage(
                                    modifier = Modifier.size(80.dp),
                                    model = index.backgroundImage,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
