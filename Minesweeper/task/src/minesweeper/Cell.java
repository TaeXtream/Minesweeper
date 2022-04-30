package minesweeper;

import java.util.Set;

enum Cell {
    ZERO('/'),
    ONE('1'),
    TWO('2'),
    TREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    MINE('X'),
    MARK('*'),
    UNKNOWN('.');

    private final char symbol;

    static final Set<Cell> UNEXPLORED = Set.of(UNKNOWN, MARK);


    Cell(final char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
