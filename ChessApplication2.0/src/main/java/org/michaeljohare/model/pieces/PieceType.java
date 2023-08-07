package org.michaeljohare.model.pieces;

public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    public static PieceType convertIntToPieceType(int choice) {
        switch (choice) {
            case 0: return QUEEN;
            case 1: return ROOK;
            case 2: return BISHOP;
            case 3: return KNIGHT;
            default: throw new IllegalArgumentException("Invalid choice for promotion");
        }
    }
}
