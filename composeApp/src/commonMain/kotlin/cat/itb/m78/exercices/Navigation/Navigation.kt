package cat.itb.m78.exercices.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cat.itb.m78.exercices.APIAgents.Agents
import kotlinx.serialization.Serializable

object Destination
{
    @Serializable
    data object Screen1
}

@Composable
fun Navigation()
{
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destination.Screen1
    )
    {
        composable<Destination.Screen1>
        {
            Agents()
        }
    }
}