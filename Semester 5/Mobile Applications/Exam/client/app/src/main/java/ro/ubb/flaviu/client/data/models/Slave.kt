package ro.ubb.flaviu.client.data.models

data class Slave (
    var id: Int,
    var text: String,
    var read: Boolean,
    var sender: String,
    var created: Long,
)