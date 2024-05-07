package services

import data.remote.Employee
import data.remote.Repository
import data.remote.HttpRoutes
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.*

object GitHubApi {

    suspend fun fetchEmployees(gitApiToken: String = ""): List<Employee> {
        val resp = getHttpClient(gitApiToken).get(HttpRoutes.MEMBERS)
        return resp.body()
    }

    suspend fun fetchRepositories(user: String, gitApiToken: String = ""): List<Repository> {
        return getHttpClient(gitApiToken).get(HttpRoutes.getUserRepos(user)).body()
    }

    private fun getHttpClient(authToken: String): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            if(authToken != "") {
                defaultRequest {
                    header("Authorization", "Bearer $authToken")
                }
            }
        }
    }

    suspend fun fetchLanguages(
        user: String,
        repo: String,
        gitApiToken: String = ""
    ): Map<String, Int> {
        val response = getHttpClient(gitApiToken).get(HttpRoutes.getUserRepoLanguage(user, repo))
        if (response.status.isSuccess()) {
            val responseBody = response.bodyAsText()
            return Json.decodeFromString<Map<String, Int>>(responseBody)
        }
        return emptyMap()
    }
}