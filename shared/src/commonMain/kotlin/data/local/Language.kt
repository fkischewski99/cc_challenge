package data.local

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Language: RealmObject {
    @PrimaryKey
    var name: String = "";
}