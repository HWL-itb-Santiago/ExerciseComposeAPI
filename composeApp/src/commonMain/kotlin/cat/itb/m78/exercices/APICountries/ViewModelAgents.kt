package cat.itb.m78.exercices.APICountries

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
data class Root(
    val status: Long,
    val data: List<Agent>,
)
@Serializable
data class Agent(
    val uuid: String,
    val displayName: String,
    val description: String,
    val developerName: String,
    val releaseDate: String,
    val characterTags: List<String>?,
    val displayIcon: String,
    val displayIconSmall: String?,
    val bustPortrait: String?,
    val fullPortrait: String?,
    val fullPortraitV2: String?,
    val killfeedPortrait: String,
    val background: String?,
    val backgroundGradientColors: List<String>,
    val assetPath: String,
    val isFullPortraitRightFacing: Boolean,
    val isPlayableCharacter: Boolean,
    val isAvailableForTest: Boolean,
    val isBaseContent: Boolean,
    val role: Role?,
    val recruitmentData: RecruitmentData?,
    val abilities: List<Ability>,
    val voiceLine: String?,
)
@Serializable
data class Role(
    val uuid: String,
    val displayName: String,
    val description: String,
    val displayIcon: String,
    val assetPath: String,
)
@Serializable
data class RecruitmentData(
    val counterId: String,
    val milestoneId: String,
    val milestoneThreshold: Long,
    val useLevelVpCostOverride: Boolean,
    val levelVpCostOverride: Long,
    val startDate: String,
    val endDate: String,
)
@Serializable
data class Ability(
    val slot: String,
    val displayName: String,
    val description: String,
    val displayIcon: String?,
)

object AgentAPI
{
    private val url = "https://valorant-api.com/v1/agents"
    private val client = HttpClient(){
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun agentList() = client.get(url).body<List<Root>>()
}

class ViewModelAgents: ViewModel()
{
    val data = mutableStateListOf<Root?>()
    init {
        viewModelScope.launch(Dispatchers.Default)
        {
            AgentAPI.agentList().forEach { it ->
                data.add(it)
            }
        }
    }
}