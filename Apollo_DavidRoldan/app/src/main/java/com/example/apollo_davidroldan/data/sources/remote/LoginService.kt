package com.example.apollo_davidroldan.data.sources.remote

import com.example.apollo_davidroldan.common.Constantes
import com.example.apollo_davidroldan.data.modelo.LoginTokens
import com.example.apollo_davidroldan.domain.modelo.Credentials
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @POST(Constantes.REGISTER_PATH)
    suspend fun register(@Body credentials: Credentials): Response<Credentials>

    @POST(Constantes.LOGIN_PATH)
    suspend fun login(@Body credentials: Credentials): Response<LoginTokens>

    @GET(Constantes.REFRESH_PATH)
    suspend fun refreshToken(@Query(Constantes.TOKEN) token: String): Response<LoginTokens>
}