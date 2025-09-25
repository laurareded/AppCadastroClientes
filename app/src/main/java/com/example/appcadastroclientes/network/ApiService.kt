package com.example.appcadastroclientes.network

import retrofit2.http.GET
import retrofit2.Call
import com.example.appcadastroclientes.data.Cliente

interface ApiService {
    @GET("users")
    fun getClientes(): Call<List<Cliente>>
}