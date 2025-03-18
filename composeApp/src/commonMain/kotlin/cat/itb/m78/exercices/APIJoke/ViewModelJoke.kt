package cat.itb.m78.exercices.APIJoke

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class Joke
    (
    val id: Int,
    val type: String,
    val setup: String,
    val punchline: String
)

object JokeApi
{
    private val url = "https://api.sampleapis.com/jokes/goodJokes"
    private val client = HttpClient(){
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun jokeList() = client.get(url).body<List<Joke>>().random()
}
class ViewModelJoke :ViewModel()
{
    val data = mutableStateOf<Joke?>(null)
    init {
        viewModelScope.launch(Dispatchers.Default) {
            data.value = JokeApi.jokeList()
        }
    }
}