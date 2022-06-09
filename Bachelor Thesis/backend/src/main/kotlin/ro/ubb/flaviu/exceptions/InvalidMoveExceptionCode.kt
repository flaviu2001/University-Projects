package ro.ubb.flaviu.exceptions

enum class InvalidMoveExceptionCode {
    NoPieceAtPosition,
    EatingPieceOfSameColor,
    MovingOtherColorsPiece,
    InvalidMoveType,
    UnpromotablePieceType,
}