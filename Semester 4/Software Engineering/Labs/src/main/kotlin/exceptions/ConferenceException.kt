package exceptions

class ConferenceException(override val message: String) : RuntimeException(message)