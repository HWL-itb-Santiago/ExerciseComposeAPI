package cat.itb.m78.exercices.APICountries

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Image
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Country(
    val abbreviation: String,
    val capital: String,
    val currency: String,
    val name: String,
    val phone: String,
    val population: Long? = null,
    val media: Media,
    val id: Long,
)

@Serializable
data class Media(
    val flag: String,
    val emblem: String,
    val orthographic: String,
)

object CountriesAPI
{
    private val url = "https://api.sampleapis.com/countries/countries"
    private val client = HttpClient(){
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun countriesList() = client.get(url).body<List<Country>>()
}

class ViewModelCountries: ViewModel()
{
    val data = mutableStateListOf<Country?>()
    init {
        viewModelScope.launch(Dispatchers.Default)
        {
            CountriesAPI.countriesList().forEach { it ->
                data.add(it)
            }
        }
    }
}