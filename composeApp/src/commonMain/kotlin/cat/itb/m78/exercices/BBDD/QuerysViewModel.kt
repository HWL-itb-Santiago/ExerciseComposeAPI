package cat.itb.m78.exercices.BBDD

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.database
import cat.itb.m78.exercices.db.GameDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class QuerysViewModel : ViewModel()
{
    fun addGame(
        gameId: Long,
        gameName: String,
        gameDataRealesed: String,
        gameImage: String,
        gameRanking: Double
    )
    {
        viewModelScope.launch(Dispatchers.IO) {
            database.myTableQueries.insert(
                id = gameId,
                gameName = gameName,
                gameDataRealesed = gameDataRealesed,
                gameImage = gameImage,
                gameRanking = gameRanking
            )
        }
    }

    fun deleteGame(gameId: Long)
    {
        viewModelScope.launch(Dispatchers.IO) {
            database.myTableQueries.deleteById(gameId)
        }
    }
    fun getAllGames(): List<GameDB>
    {
        return database.myTableQueries.selectAll().executeAsList()
    }
}