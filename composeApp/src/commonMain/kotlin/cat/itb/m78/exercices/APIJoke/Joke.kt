package cat.itb.m78.exercices.APIJoke

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Joke()
{
    val viewModel = viewModel { ViewModelJoke() }

    Column {
        if (viewModel.data.value == null)
            CircularProgressIndicator()
        else
        {
            Column {
                Text(
                    text = viewModel.data.value!!.setup
                )
                Text(
                    text = viewModel.data.value!!.punchline
                )
            }

        }
    }

}