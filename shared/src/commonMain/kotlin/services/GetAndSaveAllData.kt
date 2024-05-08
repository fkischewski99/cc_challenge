package services

import data.RealmRepository
import data.local.GitRepository
import data.local.Language
import data.local.RealmEmployee
import data.remote.Repository
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object GetAndSaveAllData {

    fun getAndSaveAllMembers(apiToken: String = "") {
        runBlocking {
            val membersFromApi = GitHubApi.fetchEmployees(apiToken)
            for (employee in membersFromApi) {
                RealmRepository.insertEmployee(RealmEmployee().apply {
                    githubUsername = employee.login
                    id = employee.id
                })
            }
        }
    }

    fun getAndSaveAllRepositoriesAndLanguages(apiToken: String) {
        GlobalScope.launch {
            RealmRepository.getEmployees().collect {
                it.forEach { realmEmployee ->
                    val repositories =
                        GitHubApi.fetchRepositories(realmEmployee.githubUsername, apiToken)
                    val userRepos: RealmList<GitRepository> = realmListOf()
                    for (repo in repositories) {
                        var languages = fetchLanguagesAndSafeInRealm(realmEmployee, repo, apiToken)
                        userRepos.add(GitRepository().apply {
                            name = repo.name
                            languages = languages
                        })
                    }
                    RealmRepository.updateRealmEmployeeRepositories(realmEmployee, userRepos)
                }
            }
        }
    }

    private suspend fun fetchLanguagesAndSafeInRealm(
        realmEmployee: RealmEmployee,
        repo: Repository,
        apiToken: String
    ): RealmList<Language> {
        val languages = GitHubApi.fetchLanguages(realmEmployee.githubUsername, repo.name, apiToken)
        val realmLanguagesList: RealmList<Language> = realmListOf();
        for (language in languages.keys) {
            realmLanguagesList.add(RealmRepository.getOrInsertLanguage(language));
        }
        return realmLanguagesList;
    }
}