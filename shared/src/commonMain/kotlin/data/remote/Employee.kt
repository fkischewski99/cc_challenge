package data.remote

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val login: String,
    val id: Int,
    val repos_url: String
)
