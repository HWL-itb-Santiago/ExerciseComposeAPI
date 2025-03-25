package cat.itb.m78.exercices.APIAgents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cat.itb.m78.exercices.APICountries.ViewModelAgents
import coil3.compose.AsyncImage

@Composable
fun Agents()
{
    val viewModel = viewModel { ViewModelAgents() }

    if (viewModel.data.isEmpty())
        CircularProgressIndicator()
    else
        LazyColumn {
            items(viewModel.data) { index ->
                if (index != null) {
                    Row {
                        Column(modifier = Modifier.weight(0.2f)) {
                            Text(text = index.data.first().displayName, fontWeight = FontWeight.Bold)
                            Text(text = index.data.first().description)
                        }
                        AsyncImage(
                            modifier = Modifier.size(80.dp),
                            model = index.data.first().background,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                        )
                    }
                }
            }
        }
}