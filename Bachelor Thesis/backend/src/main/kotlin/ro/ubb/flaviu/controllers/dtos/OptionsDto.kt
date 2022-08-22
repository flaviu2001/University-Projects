package ro.ubb.flaviu.controllers.dtos

data class OptionsDto(
    val alphaBeta: Boolean = true,
    val quiescence: Boolean = false,
    val transposition: Boolean = true,
    val parallelization: Boolean = false,
    val lowCutoff: Long = 3000,
    val highCutoff: Long = 30000
)
