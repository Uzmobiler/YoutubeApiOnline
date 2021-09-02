package uz.mobiler.youtubeapionline.repository

import uz.mobiler.youtubeapionline.retrofit.ApiService

class YoutubeRepository(val apiService: ApiService) {

    suspend fun getData() = apiService.getData()
}