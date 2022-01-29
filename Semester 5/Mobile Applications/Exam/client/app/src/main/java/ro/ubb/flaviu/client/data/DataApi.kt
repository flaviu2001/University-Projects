package ro.ubb.flaviu.client.data

import android.util.Log
import retrofit2.http.*
import ro.ubb.flaviu.client.data.models.Text
import ro.ubb.flaviu.client.data.models.ToSend

object DataApi {
    private interface DataService {
        companion object {
            private const val prefix = "message"
        }

        @Headers("Content-Type: application/json")
        @POST("/item")
        suspend fun update(@Body item: ToSend): Text
    }

    private val DATA_SERVICE: DataService = Api.retrofit.create(DataService::class.java)


    suspend fun update(item: ToSend): String {
        return try {
            DATA_SERVICE.update(item).text
        } catch (e: Exception) {
            Log.i("anthrax", e.toString())
            "can't connect"
        }
    }
}
