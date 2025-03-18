package cat.itb.m78.exercices.RememberMe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.itb.m78.exercices.Settings.ViewModelRememberMe
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RememberMe()
{
    val viewModelRememberMe = viewModel { ViewModelRememberMe() }
    val nameState by viewModelRememberMe.name.collectAsState()
    RememberMe(nameState) { viewModelRememberMe.saveName(it) }
}

@Composable
fun RememberMe(nameToRemember : String, saveName: (String)->Unit)
{
    var input = remember {mutableStateOf("")}
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
                text = "Hello ${nameToRemember}",
                color = Color.White
            )
            TextField(
                value = input.value,
                onValueChange = {input.value = it}
            )
            Button(
                onClick = {
                    saveName(input.value)
                }
            )
            {
                Text(
                    text = "Guardar"
                )
            }
        }
    }
}