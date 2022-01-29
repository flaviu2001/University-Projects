package ro.ubb.flaviu.client.data

import androidx.room.*
import ro.ubb.flaviu.client.data.models.Thing

@Dao
interface AppDatabaseDao {
    @Insert
    suspend fun insertThing(vararg thing: Thing)

    @Delete
    suspend fun deleteThing(vararg thing: Thing)

    @Query("DELETE FROM thing")
    suspend fun deleteAll()
}
