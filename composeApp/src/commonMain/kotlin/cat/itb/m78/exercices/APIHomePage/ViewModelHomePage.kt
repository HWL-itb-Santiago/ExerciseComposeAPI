package cat.itb.m78.exercices.APIHomePage

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
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
    val rating: Double? = 0.0,
    @SerialName("rating_top")
    val ratingTop: Long? = null,
    //val ratings: Map<String>,
    @SerialName("ratings_count")
    val ratingsCount: Long? = null,
    @SerialName("reviews_text_count")
    val reviews_text_count: Int? = 0, //  Asegura que sea
    val added: Long? = null,
    //@SerialName("added_by_status")
    //val addedByStatus: Map<String, String>,
    val metacritic: Long? = null,
    val playtime: Long? = null,
    @SerialName("suggestions_count")
    val suggestionsCount: Long? = null,
    val updated: String? = null,
    @SerialName("esrb_rating")
    val esrbRating: EsrbRating? = null,
    val platforms: List<Platform>? = null,
)

@Serializable
data class EsrbRating(
    val id: Long? = null,
    val slug: String? = null,
    val name: String? = null,
)

@Serializable
data class Platform(
    val platform: Platform2? = null,
    @SerialName("released_at")
    val releasedAt: String? = null,
    val requirements: Requirements? = null
)

@Serializable
data class Platform2(
    val id: Long? = null,
    val slug: String? = null,
    val name: String? = null,
)

@Serializable
data class Requirements(
    val minimum: String? = null,
    val recommended: String? = null,
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

    private var _gamesPerPage = mutableListOf<List<Result>>()

    private val _filteredGames = MutableStateFlow<List<Result>>(emptyList())
    val filteredGames: StateFlow<List<Result>> = _filteredGames.asStateFlow()


    fun cleanSearch()
    {
        _textFieldSate.value.clearText()
        _filteredGames.value = data.value
        _isSearching.value = false
    }
    fun onSearch(query: String) {
        if (query.isEmpty()) {
            _isSearching.value = false
            _filteredGames.value = data.value
        } else {
            _isSearching.value = true
            _filteredGames.value = data.value.filter { it.name?.contains(query, ignoreCase = true) == true }
        }
    }

    fun clasifiedGamePerPage()
    {
        var page = mutableListOf<Result>()
        for (i in _data.value.indices) {
            page.add(_data.value[i])
            if ((i + 1) % 20 == 0) {
                _gamesPerPage.add(page.toList())
                page.clear()
            }
        }
        if (page.isNotEmpty()) {
            _gamesPerPage.add(page.toList())
        }
    }
    fun loadGames(page: Int) {
        viewModelScope.launch {
            val result = AgentAPI.fetchGames(page)
            result.results?.let {gamesData ->
                val currentData = _data.value.toMutableList()
                currentData.addAll(gamesData)
                _data.value = currentData
            }
        }
    }

    fun NextPage()
    {
        _currentPage.value += 1
    }

    fun PreviousPage()
    {
        if (currentPage.value > 1)
        {
            _currentPage.value -= 1
        }
    }
    init {
        if (_data.value.isEmpty())
        {
            for (page in 1..15) {
                loadGames(page)
            }
        }
    }
}