package cat.itb.m78.exercices.APIGamePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GamePage(backToHomePage: () -> Unit, message: Long) {

    val gamePageViewModel = viewModel { ViewModelGamePage(message) }
    val gameInfo by gamePageViewModel.gameData.collectAsState()
    if (gameInfo == null) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        )
        {
            Column {
                gamePageViewModel.gameData.value?.name?.let { Text(it, color = Color.White) }
                Button(
                    onClick = backToHomePage
                )
                {

                }
            }
        }
    }

}