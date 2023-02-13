package co.dh.wings.network.response

data class LoginResponse(
    val kode: Int,
    val pesan: String,
    val user: User
) {
    class User (
        val user: String
    )
}