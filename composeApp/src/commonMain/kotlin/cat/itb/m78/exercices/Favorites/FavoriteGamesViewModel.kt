package cat.itb.m78.exercices.Favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.APIGamePage.Game
import cat.itb.m78.exercices.APIHomePage.AgentAPI
import cat.itb.m78.exercices.APIHomePage.Result
import cat.itb.m78.exercices.database
import cat.itb.m78.exercices.db.GameDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteGamesViewModel : ViewModel()
{
    private val _favoriteGames = MutableStateFlow<List<GameDB>>(emptyList())
    val favoriteGames: StateFlow<List<GameDB>> = _favoriteGames.asStateFlow()

    private val _filteredGames = MutableStateFlow<List<GameDB>>(emptyList())
    val filteredGames: StateFlow<List<GameDB>> = _filteredGames.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    fun onSearch(query: String) {
        if (query.isBlank()) {
            _isSearching.value = false
            _filteredGames.value = _favoriteGames.value
        } else {
            _isSearching.value = true
            val filteredGameList = _filteredGames.value.toMutableList()
            _filteredGames.value = filteredGameList.filter { it.gameName.contains(query, ignoreCase = true) }
        }
    }
    fun getFavoriteGames() {
        viewModelScope.launch {
            _favoriteGames.value = database.myTableQueries.selectAll().executeAsList()
        }
    }
    init {
        getFavoriteGames()
    }
}