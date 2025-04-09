package cat.itb.m78.exercices.BBDD

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.itb.m78.exercices.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuerysViewModel : ViewModel()
{
    init {
//        viewModelScope.launch {
//            withContext(Dispatchers.Default){
//                database.myTableQueries.insert("some text")
//            }
            // do after insert
        }
    //}
}