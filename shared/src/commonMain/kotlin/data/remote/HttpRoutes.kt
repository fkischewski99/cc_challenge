package data.remote

object HttpRoutes {

    private const val BASE_URL = "https://api.github.com"
    const val MEMBERS = "$BASE_URL/orgs/codecentric/members"

    fun getUserRepos(user: String): String {
        return "$BASE_URL/users/$user/repos"
    }

    fun getUserRepoLanguage(user: String, repo: String): String {
        return "$BASE_URL/repos/$user/$repo/languages"
    }
}
