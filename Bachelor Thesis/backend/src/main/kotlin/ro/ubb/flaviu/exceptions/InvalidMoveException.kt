package ro.ubb.flaviu.exceptions

class InvalidMoveException(messageCode: InvalidMoveExceptionCode) : Exception(messageCodeToString(messageCode)) {
    companion object {
        private fun messageCodeToString(messageCode: InvalidMoveExceptionCode): String {
            return when(messageCode) {
                InvalidMoveExceptionCode.NoPieceAtPosition -> "No piece at the provided position"
                InvalidMoveExceptionCode.EatingPieceOfSameColor -> "Cannot take your own piece"
                InvalidMoveExceptionCode.MovingOtherColorsPiece -> "Cannot move the piece of your enemy"
                InvalidMoveExceptionCode.InvalidMoveType -> "Invalid move type"
                InvalidMoveExceptionCode.UnpromotablePieceType -> "Cannot promote into an unpromotable piece type"
            }
        }
    }
}
