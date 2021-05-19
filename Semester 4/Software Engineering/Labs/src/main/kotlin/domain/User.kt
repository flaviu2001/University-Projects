package domain

data class User (val id: Int, val name: String, val password: String, val email: String,
                val fullName: String, val affiliation: String, val personalWebsite: String,
                val domainOfInterest: String) {
    override fun toString(): String {
        return "User(name='$name', email='$email', fullName='$fullName')"
    }
}
