package cat.itb.m78.exercices.APICountries

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class GameResponse(
    val count: Long,
    val next: String,
    val previous: String? = null,
    val results: List<Result>,
)

@Serializable
data class Result(
    val id: Long,
    val slug: String,
    val name: String,
    val released: String,
    val tba: Boolean,
    @SerialName("background_image")
    val backgroundImage: String,
    val rating: Double? = 0.0,
    @SerialName("rating_top")
    val ratingTop: Long,
    //val ratings: Map<String>,
    @SerialName("ratings_count")
    val ratingsCount: Long,
    @SerialName("reviews_text_count")
    val reviews_text_count: Int? = 0, //  Asegura que sea
    val added: Long,
    //@SerialName("added_by_status")
    //val addedByStatus: Map<String, String>,
    val metacritic: Long,
    val playtime: Long,
    @SerialName("suggestions_count")
    val suggestionsCount: Long,
    val updated: String,
    @SerialName("esrb_rating")
    val esrbRating: EsrbRating,
    val platforms: List<Platform>? = null,
)

@Serializable
data class EsrbRating(
    val id: Long,
    val slug: String,
    val name: String,
)

@Serializable
data class Platform(
    val platform: Platform2,
    @SerialName("released_at")
    val releasedAt: String,
    val requirements: Requirements? = null
)

@Serializable
data class Platform2(
    val id: Long,
    val slug: String,
    val name: String,
)

@Serializable
data class Requirements(
    val minimum: String,
    val recommended: String,
)

object AgentAPI
{
    suspend fun fetchGames(): GameResponse
    {
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json
                {
                    ignoreUnknownKeys = true
                })
            }
        }

        val response: HttpResponse = client.get("https://api.rawg.io/api/games?key=f065fafb79d44605a13510e272fc4e75")

        return response.body<GameResponse>()
    }
}

class ViewModelAgents: ViewModel()
{
    val data = mutableStateListOf<Result?>()
    init {
        viewModelScope.launch(Dispatchers.Default)
        {
            AgentAPI.fetchGames().results.forEach {it ->
                data.add(it)
            }
        }
    }
}