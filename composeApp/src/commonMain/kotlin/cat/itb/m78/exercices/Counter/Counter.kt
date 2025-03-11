package cat.itb.m78.exercices.Counter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.itb.m78.exercices.Settings.ViewModelCounter

@Composable
fun Counter()
{
    val viewModelCounter = viewModel{ViewModelCounter()}

    var counter by remember {mutableStateOf(0)}
    Box(
        modifier = Modifier.background(color = Color.Black)
            .fillMaxSize()
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Text(
                text = "Usted ha visitado esta pagina un total de ${counter} veces",
                color = Color.White
            )
            Button(
                onClick = {
                    counter += 1
                }
            )
            {
                Text(
                    text = "Sumar Counter"
                )
            }
        }
    }
}