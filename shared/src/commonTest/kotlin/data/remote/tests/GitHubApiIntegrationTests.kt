package data.remote.tests

import kotlinx.coroutines.runBlocking
import services.GitHubApi
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GitHubApiIntegrationTests {
    @Test
    fun testMembersCall() {
        runBlocking {
            val result = GitHubApi.fetchEmployees()
            assertFalse { result.isEmpty() }
        }
    }
}