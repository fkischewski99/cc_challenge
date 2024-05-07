package data.remote

import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    val name: String
)