package cat.itb.m78.exercices

import app.cash.sqldelight.db.SqlDriver
import cat.itb.m78.exercices.database
import androidx.compose.runtime.Composable
import cat.itb.m78.exercices.Navigation.Navigation
import cat.itb.m78.exercices.db.Database

@Composable
fun App()
{
    Navigation()
}

expect fun createDriver(): SqlDriver
fun createDatabase(): Database {
    val driver = createDriver()
    return Database(driver)
}
val database by lazy { createDatabase() }


