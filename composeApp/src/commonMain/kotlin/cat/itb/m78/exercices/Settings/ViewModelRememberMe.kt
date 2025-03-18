package cat.itb.m78.exercices.Settings

import androidx.lifecycle.ViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val NAMEREMEMBER_KEY = "name"
class ViewModelRememberMe : ViewModel()
{
    val settings: Settings = Settings()

    private val _name = MutableStateFlow(settings.getString(NAMEREMEMBER_KEY, ""))
    var name: StateFlow<String> = _name.asStateFlow()

    fun saveName(newName: String)
    {
        settings[NAMEREMEMBER_KEY] = newName
        _name.value = settings.getString(NAMEREMEMBER_KEY, "")
    }
}