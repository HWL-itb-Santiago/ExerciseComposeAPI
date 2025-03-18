package cat.itb.m78.exercices.APIEmbalsadoras

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.APICountries.CountriesAPI
import cat.itb.m78.exercices.APICountries.Country
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Embalse(
    val dia: String,
    val estaci: String,
    @SerialName("nivell_absolut")
    val nivellAbsolut: String,
    @SerialName("percentatge_volum_embassat")
    val percentatgeVolumEmbassat: String,
    @SerialName("volum_embassat")
    val volumEmbassat: String,
)

object EmbalseAPI
{
    private val url = "https://analisi.transparenciacatalunya.cat/resource/gn9e-3qhr.json"
    private val client = HttpClient(){
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun embalsesList() = client.get(url).body<List<Embalse>>()
}

class ViewModelEmbalsadoras : ViewModel()
{
    val data = mutableStateListOf<Embalse?>()
    init {
        viewModelScope.launch(Dispatchers.Default)
        {
            EmbalseAPI.embalsesList().forEach { it ->
                data.add(it)
            }
        }
    }
}

