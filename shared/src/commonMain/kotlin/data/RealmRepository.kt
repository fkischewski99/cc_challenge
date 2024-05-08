package data

import data.local.GitRepository
import data.local.Language
import data.local.RealmEmployee
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.isManaged
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object RealmRepository {
    private lateinit var realm: Realm

    init {
        configureRealm()
    }

    private fun configureRealm() {
        val config = RealmConfiguration.create(
            schema = setOf(
                RealmEmployee::class,
                Language::class,
                GitRepository::class
            ),
        )
        realm = Realm.open(config)
    }

    fun findEmployeeByUsername(username: String): RealmEmployee? {
        return realm.query<RealmEmployee>(query = "githubUsername = $0", username).find()
            .firstOrNull()
    }

    suspend fun insertEmployee(realmEmployee: RealmEmployee) {
        // Only newly created RealmObjects (not managed) can be copied to Realm
        if (realmEmployee.isManaged()) {
            return
        }
        //Employee already exists in DB
        if (findEmployeeByUsername(realmEmployee.githubUsername) != null) {
            return
        }
            realm.write {
                copyToRealm(realmEmployee)
        }
    }

    fun getEmployees(): Flow<List<RealmEmployee>> {
        return realm.query<RealmEmployee>()
            .asFlow().map { it.list }
    }

    fun getLanguages(): Flow<List<Language>> {
        return realm.query<Language>()
            .asFlow().map { it.list }
    }

    suspend fun updateRealmEmployeeRepositories(realmEmployee: RealmEmployee, gitRepositoryList: RealmList<GitRepository>){
            realm.write {
                findLatest(realmEmployee)?.let { unfrozenEmployee ->
                    unfrozenEmployee.repositories = gitRepositoryList
                }
            }

    }

    suspend fun getOrInsertLanguage(strLanguage: String): Language {
        var language =
            realm.query<Language>(query = "name = $0", strLanguage).find().firstOrNull()
        if (language == null) {
                realm.write {
                    language = Language().apply { name = strLanguage }
                    copyToRealm(language!!)
                }
        }
        return language!!
    }
}