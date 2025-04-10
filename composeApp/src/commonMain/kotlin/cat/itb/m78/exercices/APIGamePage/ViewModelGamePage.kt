package cat.itb.m78.exercices.APIGamePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.APIHomePage.AgentAPI
import cat.itb.m78.exercices.APIHomePage.GameResponse
import cat.itb.m78.exercices.APIHomePage.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Game(
    val id: Long? = null,
    val slug: String? = null,
    val name: String? = null,
    @SerialName("name_original")
    val nameOriginal: String? = null,
    val description: String? = null,
    val metacritic: Long? = null,
    val released: String? = null,
    val tba: Boolean? = null,
    val updated: String? = null,
    @SerialName("background_image")
    val backgroundImage: String? = null,
    @SerialName("background_image_additional")
    val backgroundImageAdditional: String? = null,
    val website: String? = null,
    val rating: Double? = null,
    @SerialName("rating_top")
    val ratingTop: Long? = null,
    val ratings: List<Rating>? = null,
    val reactions: Reactions? = null,
    val added: Long? = null,
    @SerialName("added_by_status")
    val addedByStatus: AddedByStatus? = null,
    val playtime: Long? = null,
    @SerialName("screenshots_count")
    val screenshotsCount: Long? = null,
    @SerialName("movies_count")
    val moviesCount: Long? = null,
    @SerialName("creators_count")
    val creatorsCount: Long? = null,
    @SerialName("achievements_count")
    val achievementsCount: Long? = null,
    @SerialName("parent_achievements_count")
    val parentAchievementsCount: Long? = null,
    @SerialName("reddit_url")
    val redditUrl: String? = null,
    @SerialName("reddit_name")
    val redditName: String? = null,
    @SerialName("reddit_description")
    val redditDescription: String? = null,
    @SerialName("reddit_logo")
    val redditLogo: String? = null,
    @SerialName("reddit_count")
    val redditCount: Long? = null,
    @SerialName("twitch_count")
    val twitchCount: Long? = null,
    @SerialName("youtube_count")
    val youtubeCount: Long? = null,
    @SerialName("reviews_text_count")
    val reviewsTextCount: Long? = null,
    @SerialName("ratings_count")
    val ratingsCount: Long? = null,
    @SerialName("suggestions_count")
    val suggestionsCount: Long? = null,
    @SerialName("alternative_names")
    val alternativeNames: List<String?>? = null,
    @SerialName("metacritic_url")
    val metacriticUrl: String? = null,
    @SerialName("parents_count")
    val parentsCount: Long? = null,
    @SerialName("additions_count")
    val additionsCount: Long? = null,
    @SerialName("game_series_count")
    val gameSeriesCount: Long? = null,
    @SerialName("user_game")
    val userGame: String? = null,
    @SerialName("reviews_count")
    val reviewsCount: Long? = null,
    @SerialName("saturated_color")
    val saturatedColor: String? = null,
    @SerialName("dominant_color")
    val dominantColor: String? = null,
    @SerialName("parent_platforms")
    val parentPlatforms: List<ParentPlatform>? = null,
    val platforms: List<Platform2>? = null,
    val stores: List<Store>? = null,
    val developers: List<Developer>? = null,
    val genres: List<Genre>? = null,
    val tags: List<Tag>? = null,
    val publishers: List<Publisher>? = null,
    @SerialName("esrb_rating")
    val esrbRating: EsrbRating? = null,
    val clip: String? = null,
    @SerialName("description_raw")
    val descriptionRaw: String? = null,
)

@Serializable
data class Rating(
    val id: Long? = null,
    val title: String? = null,
    val count: Long? = null,
    val percent: Double? = null,
)

@Serializable
data class Reactions(
    @SerialName("1") val n1: Long? = null,
    @SerialName("2") val n2: Long? = null,
    @SerialName("3") val n3: Long? = null,
    @SerialName("4") val n4: Long? = null,
    @SerialName("6") val n6: Long? = null,
    @SerialName("7") val n7: Long? = null,
    @SerialName("9") val n9: Long? = null,
    @SerialName("10") val n10: Long? = null,
    @SerialName("11") val n11: Long? = null,
    @SerialName("12") val n12: Long? = null,
)

@Serializable
data class AddedByStatus(
    val yet: Long? = null,
    val owned: Long? = null,
    val beaten: Long? = null,
    val toplay: Long? = null,
    val dropped: Long? = null,
    val playing: Long? = null,
)

@Serializable
data class ParentPlatform(val platform: Platform? = null)

@Serializable
data class Platform(
    val id: Long? = null,
    val name: String? = null,
    val slug: String? = null,
)

@Serializable
data class Platform2(
    val platform: Platform3? = null,
    @SerialName("released_at")
    val releasedAt: String? = null,
    val requirements: Requirements? = null,
)

@Serializable
data class Platform3(
    val id: Long? = null,
    val name: String? = null,
    val slug: String? = null,
    val image: String? = null,
    //@SerialName("year_end") val yearEnd: String? = null,
    //@SerialName("year_start") val yearStart: String? = null,
    @SerialName("games_count") val gamesCount: Long? = null,
    @SerialName("image_background") val imageBackground: String? = null,
)

@Serializable
data class Requirements(
    val minimum: String? = null,
    val recommended: String? = null,
)

@Serializable
data class Store(
    val id: Long? = null,
    val url: String? = null,
    val store: Store2? = null,
)

@Serializable
data class Store2(
    val id: Long? = null,
    val name: String? = null,
    val slug: String? = null,
    val domain: String? = null,
    @SerialName("games_count") val gamesCount: Long? = null,
    @SerialName("image_background") val imageBackground: String? = null,
)

@Serializable
data class Developer(
    val id: Long? = null,
    val name: String? = null,
    val slug: String? = null,
    @SerialName("games_count") val gamesCount: Long? = null,
    @SerialName("image_background") val imageBackground: String? = null,
)

@Serializable
data class Genre(
    val id: Long? = null,
    val name: String? = null,
    val slug: String? = null,
    @SerialName("games_count") val gamesCount: Long? = null,
    @SerialName("image_background") val imageBackground: String? = null,
)

@Serializable
data class Tag(
    val id: Long? = null,
    val name: String? = null,
    val slug: String? = null,
    val language: String? = null,
    @SerialName("games_count") val gamesCount: Long? = null,
    @SerialName("image_background") val imageBackground: String? = null,
)

@Serializable
data class Publisher(
    val id: Long? = null,
    val name: String? = null,
    val slug: String? = null,
    @SerialName("games_count") val gamesCount: Long? = null,
    @SerialName("image_background") val imageBackground: String? = null,
)

@Serializable
data class EsrbRating(
    val id: Long? = null,
    val name: String? = null,
    val slug: String? = null,
)

object GamePageAPI
{
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun fetchGameDetails(gameId: Long): Game {
        val response: HttpResponse = client.get("https://api.rawg.io/api/games/$gameId") {
            parameter("key", "f065fafb79d44605a13510e272fc4e75")
        }
        return response.body()
    }
}
class ViewModelGamePage(private val gameId: Long) : ViewModel()
{
    private val _gameData = MutableStateFlow<Game?>(null)
    val gameData: StateFlow<Game?> = _gameData.asStateFlow()

    fun resetGame() {
        _gameData.value = null
    }
    fun getGame(gameId: Long)
    {
        viewModelScope.launch {
            val gameDetails = GamePageAPI.fetchGameDetails(gameId)
            _gameData.value = gameDetails
        }
    }
    init {
        resetGame()
        getGame(gameId)
    }
}