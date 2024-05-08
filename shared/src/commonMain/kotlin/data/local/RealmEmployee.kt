package data.local

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmEmployee: RealmObject {
    @PrimaryKey
    var githubUsername: String = "";
    var id: Int = 0;
    var repositories: RealmList<GitRepository> = realmListOf()
}