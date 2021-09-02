package uz.mobiler.youtubeapionline.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.mobiler.youtubeapionline.models.YoutubeApiData
import uz.mobiler.youtubeapionline.repository.YoutubeRepository
import uz.mobiler.youtubeapionline.retrofit.ApiClient
import uz.mobiler.youtubeapionline.utils.NetworkHelper
import uz.mobiler.youtubeapionline.utils.Resource

class YoutubeViewModel : ViewModel() {

    private val youtubeRepository = YoutubeRepository(ApiClient.apiService)
    private val liveData = MutableLiveData<Resource<YoutubeApiData>>()

    fun getYoutubeData(context: Context): LiveData<Resource<YoutubeApiData>> {
        val networkHelper = NetworkHelper(context)
        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch {
                liveData.postValue(Resource.loading(null))
                val responce = youtubeRepository.getData()
                if (responce.isSuccessful) {
                    liveData.postValue(Resource.success(responce.body()))
                } else {
                    liveData.postValue(
                        Resource.error(
                            responce.errorBody()?.string().toString(),
                            null
                        )
                    )
                }
            }
        } else {
            liveData.postValue(Resource.error("Internet not connection", null))
        }
        return liveData
    }
}