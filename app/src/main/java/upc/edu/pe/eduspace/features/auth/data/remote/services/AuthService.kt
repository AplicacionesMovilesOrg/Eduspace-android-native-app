package upc.edu.pe.eduspace.features.auth.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import upc.edu.pe.eduspace.features.auth.data.remote.models.AuthenticatedResponseDto
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignInRequestDto
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignUpRequestDto
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignUpResponseDto

interface AuthService {

    @POST("authentication/sign-in")
    suspend fun signIn(@Body request: SignInRequestDto): Response<AuthenticatedResponseDto>

    @POST("administrator-profiles")
    suspend fun signUp(@Body request: SignUpRequestDto): Response<SignUpResponseDto>
}