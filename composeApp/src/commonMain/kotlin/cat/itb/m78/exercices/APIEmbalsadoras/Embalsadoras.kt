package cat.itb.m78.exercices.APIEmbalsadoras

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun Embalssadoras()
{
    val viewModel = viewModel { ViewModelEmbalsadoras() }
    if (viewModel.data.isEmpty())
        CircularProgressIndicator()
    else
        LazyColumn {
            items(viewModel.data) { index ->
                if (index != null) {
                    Row {
                        Column(modifier = Modifier.weight(0.2f)) {
                            Button(
                                onClick = {}
                            )
                            {
                                Text(text = index.estaci, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
}