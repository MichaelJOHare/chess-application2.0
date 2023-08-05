package org.michaeljohare.model.board;

public class Square {
    private int row;
    private int col;
    private final String[] legendLetter = { "A", "B", "C", "D", "E", "F", "G", "H" };
    private final String[] legendNumber = { "8", "7", "6", "5", "4", "3", "2", "1" };

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Square) {
            Square square = (Square) o;
            return square.row == this.row && square.col == this.col;
        }
        return false;
    }

    @Override
    public String toString() {
        return legendLetter[col] + legendNumber[row];
    }
}
