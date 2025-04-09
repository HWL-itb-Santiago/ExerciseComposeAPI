package cat.itb.m78.exercices.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cat.itb.m78.exercices.APIAgents.HomePage
import cat.itb.m78.exercices.APIGamePage.GamePage
import kotlinx.serialization.Serializable

object Destination
{
    @Serializable
    data object HomePage
    @Serializable
    data class GamePage(val gameData: Long)
}

@Composable
fun Navigation()
{
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destination.HomePage
    )
    {
        composable<Destination.HomePage>
        {
            HomePage { gameData ->
                navController.navigate(Destination.GamePage(gameData))
            }
        }
        composable<Destination.GamePage>
        { backStack ->
            val message = backStack.toRoute<Destination.GamePage>().gameData
            GamePage({navController.navigate(Destination.HomePage)}, message)
        }
    }
}