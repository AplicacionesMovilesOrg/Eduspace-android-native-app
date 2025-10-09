package upc.edu.pe.eduspace.features.auth.domain.repositories

import upc.edu.pe.eduspace.core.utils.Resource
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignUpRequestDto
import upc.edu.pe.eduspace.features.auth.data.remote.models.SignUpResponseDto
import upc.edu.pe.eduspace.features.auth.domain.models.User

interface AuthRepository {
    suspend fun signIn(username: String, password: String): Resource<User>
    suspend fun signUp(data: SignUpRequestDto): Resource<SignUpResponseDto>
}