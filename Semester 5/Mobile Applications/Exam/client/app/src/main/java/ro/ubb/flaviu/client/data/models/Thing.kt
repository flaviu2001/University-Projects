package ro.ubb.flaviu.client.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thing")
data class Thing (
    @PrimaryKey
    var name: String,
    var quantity: String
)
