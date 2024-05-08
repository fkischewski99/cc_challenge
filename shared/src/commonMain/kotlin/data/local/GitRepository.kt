package data.local

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList

class GitRepository: EmbeddedRealmObject {
    var name: String = "";
    var languages: RealmList<Language> = realmListOf()
}