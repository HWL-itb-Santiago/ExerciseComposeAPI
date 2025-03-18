package cat.itb.m78.exercices.Settings

import androidx.lifecycle.ViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

private const val COUNTER_VIEW_K = "count"
class ViewModelCounter : ViewModel()
{
    private val settings: Settings = Settings()
    val countViews = settings.getInt(COUNTER_VIEW_K, 0)

    init {
        settings[COUNTER_VIEW_K] = countViews+1
    }
}