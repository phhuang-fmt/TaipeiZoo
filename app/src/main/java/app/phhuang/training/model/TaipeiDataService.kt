package app.phhuang.training.model

import retrofit2.Call
import retrofit2.http.GET

interface TaipeiDataService {
    //@GET("#/dataset/detail?id=1ed45a8a-d26a-4a5f-b544-788a4071eea2")
    @GET("opendata/datalist/apiAccess?scope=resourceAquire&rid=5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a")
    fun listArea(): Call<ApiResult>

    //@GET("#/dataset/detail?id=48c4d6a7-4b09-4d1f-9739-ee837d302bd1")
    @GET("opendata/datalist/apiAccess?scope=resourceAquire&rid=f18de02f-b6c9-47c0-8cda-50efad621c14")
    fun listPlant(): Call<ApiPlantResult>
}