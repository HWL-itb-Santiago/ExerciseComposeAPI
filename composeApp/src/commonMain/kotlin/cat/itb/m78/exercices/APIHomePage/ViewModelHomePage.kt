package cat.itb.m78.exercices.APIHomePage

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.APIGamePage.Game
import cat.itb.m78.exercices.database
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class GameResponse(
    val count: Long? = null,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Result>? = null,
)

@Serializable
data class Result(
    val id: Long? = null,
    val slug: String? = null,
    val name: String? = null,
    val released: String? = null,
    val tba: Boolean? = null,
    @SerialName("background_image")
    val backgroundImage: String? = null,
    val rating: Double? = null,
    val metacritic: Long? = null,
    @SerialName("esrb_rating")
    val esrbRating: EsrbRating? = null,
)

@Serializable
data class EsrbRating(
    val id: Long? = null,
    val slug: String? = null,
    val name: String? = null,
)

object AgentAPI {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun fetchGames(currentPage: Int): GameResponse {
        val pageQuery = if (currentPage > 0) "&page=$currentPage" else ""
        val response: HttpResponse = client.get("https://api.rawg.io/api/games?key=f065fafb79d44605a13510e272fc4e75$pageQuery")
        return response.body()
    }

    suspend fun fetchGamePerSlug(slug: String): GameResponse
    {
        val response: HttpResponse = client.get("https://api.rawg.io/api/games?search=${slug}&key=f065fafb79d44605a13510e272fc4e75")
        return response.body()
    }
}

class ViewModelAgents: ViewModel()
{
    private val _textFieldSate = MutableStateFlow(TextFieldState())
    val textFieldState: StateFlow<TextFieldState> = _textFieldSate.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _data = MutableStateFlow<List<Result>>(emptyList())
    val data: StateFlow<List<Result>> = _data.asStateFlow()

    private var _currentPage = MutableStateFlow(1)
    var currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private var _favoriteList = MutableStateFlow<List<Long>>(emptyList())
    val favoriteList: StateFlow<List<Long>> = _favoriteList.asStateFlow()

    private val _filteredGames = MutableStateFlow<List<Result>>(emptyList())
    val filteredGames: StateFlow<List<Result>> = _filteredGames.asStateFlow()


    fun cleanSearch()
    {
        _textFieldSate.value.clearText()
        _filteredGames.value = data.value
        _isSearching.value = false
    }

    fun onSearch(query: String) {
        if (query.isBlank()) {
            _isSearching.value = false
            _filteredGames.value = data.value
        } else {
            _isSearching.value = true
            val filteredGameList = _filteredGames.value.toMutableList()
            viewModelScope.launch {
                val response = AgentAPI.fetchGamePerSlug(query)
                _filteredGames.value = response.results ?: emptyList()
            }
            _filteredGames.value = filteredGameList.filter { it.name?.contains(query, ignoreCase = true) == true }
        }
    }

    private fun loadGames(page: Int) {
        viewModelScope.launch {
            val result = AgentAPI.fetchGames(page)
            result.results?.let {gamesData ->
                val currentData = _data.value.toMutableList()
                currentData.addAll(gamesData)
                _data.value = currentData
            }
        }
    }

    fun MoreGames()
    {
        _currentPage.value++
        loadGames(currentPage.value)
    }

    fun addGameToFavorite(game: Result?)
    {
        val currentList = _favoriteList.value.toMutableList()
        if (game != null && !currentList.contains(game.id)) {
            game.id?.let { currentList.add(it) }
            _favoriteList.value = currentList
        }
//        viewModelScope.launch {
//            withContext(Dispatchers.Default)
//            {
//                database.myTableQueries.insert(
//                    id = game?.id,
//                    gameName = game?.name ?: "",
//                    gameDescription = game?.description ?: "",
//                    gameDataRealesed = game?.released ?: "",
//                    gameImage = game?.backgroundImage ?: "",
//                    gameRanking = game?.rating ?: 0.0,
//                    gameType = game?.genres?.joinToString(",") { it.name.toString() }
//                        ?: "",
//                    gameDevelopedBy = game?.developers?.firstOrNull()?.name
//                        ?: "",
//                    gamePublisherBy = game?.publishers?.firstOrNull()?.name
//                        ?: "",
//                    gamePlatform1 = game?.platforms?.getOrNull(0)?.platform?.name,
//                    gamePlatform2 = game?.platforms?.getOrNull(1)?.platform?.name,
//                    gamePlatform3 = game?.platforms?.getOrNull(2)?.platform?.name
//                )
//            }
//        }
    }

    fun removeGameFromFavorite(game: Result) {
        val currentList = _favoriteList.value.toMutableList()
        if (currentList.contains(game.id)) {
            game.id?.let { currentList.remove(it) }
            _favoriteList.value = currentList
        }
    }

    init {
        if (_data.value.isEmpty())
        {
            loadGames(currentPage.value)
        }
    }
}