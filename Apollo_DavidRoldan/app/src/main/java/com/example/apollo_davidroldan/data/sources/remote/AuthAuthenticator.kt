package com.example.apollo_davidroldan.data.sources.remote


import com.example.apollo_davidroldan.common.Constantes
import com.example.apollo_davidroldan.data.modelo.LoginTokens
import com.example.apollo_davidroldan.utils.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val loginService: LoginService,
    private val tokenManager: TokenManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val token = runBlocking {
            tokenManager.getRefreshToken().first()
        }
        return runBlocking {
            val newToken = loginService.refreshToken("$token")

            if (newToken.isSuccessful) {
                val newAccessToken = newToken.body()?.accessToken
                if (newAccessToken != null) {
                    tokenManager.saveAccessToken(newAccessToken)
                }
                response.request.newBuilder()
                    .header(Constantes.AUTHORIZATION, Constantes.BEARER+"$newAccessToken").build()
            } else {
                null
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String?): retrofit2.Response<LoginTokens> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASELOGIN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(LoginService::class.java)
        val response = service.refreshToken("$refreshToken")

        return response
    }
}