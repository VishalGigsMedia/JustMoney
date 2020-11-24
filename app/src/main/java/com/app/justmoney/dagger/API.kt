package com.app.justmoney.dagger

import com.app.justmoney.available.FAQResponse
import com.app.justmoney.available.TestModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface API {

    @POST("signup")
    fun getHeroes(@Body loginParams: String): Call<TestModel>

    @POST("GetFaq")
    fun getFaq(@Body loginParams: InputParams): Call<FAQResponse>
}