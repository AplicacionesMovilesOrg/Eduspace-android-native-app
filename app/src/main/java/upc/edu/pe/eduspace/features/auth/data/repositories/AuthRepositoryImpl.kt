package upc.edu.pe.eduspace.features.auth.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import upc.edu.pe.eduspace.core.utils.Resource
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignInRequestDto
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignUpRequestDto
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignUpResponseDto
import upc.edu.pe.eduspace.features.auth.data.remote.services.AuthService
import upc.edu.pe.eduspace.features.auth.domain.models.User
import upc.edu.pe.eduspace.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val service: AuthService) : AuthRepository {
    override suspend fun signIn(
        username: String,
        password: String
    ): Resource<User> = withContext(Dispatchers.IO) {
        try {
            val response = service.signIn(SignInRequestDto(username, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    val user = User(
                        id = authResponse.id,
                        name = authResponse.username,
                        email = authResponse.username,
                    )
                    return@withContext Resource.Success(user)
                }
                return@withContext Resource.Error("No data in response")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun signUp(data: SignUpRequestDto): Resource<SignUpResponseDto> = withContext(Dispatchers.IO) {
        try {
            val response = service.signUp(data)
            if (response.isSuccessful) {
                response.body()?.let { signUpResponse ->
                    return@withContext Resource.Success(signUpResponse)
                }
                return@withContext Resource.Error("No data in response")
            }
            return@withContext Resource.Error(response.message())
        } catch (e: Exception) {
            return@withContext Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

}