package minesweeper;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

public class Board {

    public static final int DEFAULT_SIZE = 9;

    private final int size = DEFAULT_SIZE;

    private final List<Cell> field;

    private final Set<Integer> mines;

    private final Set<Integer> markedMines;

    private final String template;

    private static final Random RNG = new Random();

    private final int minesCount;

    private boolean isExplode;

    private final Stack<Integer> stack;


    public Board(int minesCount) {
        this.minesCount = minesCount;
        this.mines = new HashSet<>(minesCount);
        this.markedMines = new HashSet<>(minesCount);
        this.stack = new Stack<>();
        this.template = createTemplate();
        this.field = new ArrayList<>(Collections.nCopies(size*size, Cell.UNKNOWN));
        this.placeMine();
        this.isExplode = false;
    }


    private int getIndex(int x, int y) {
        return x + size * y;
    }

    public void markCell(int x, int y) {
        if (!(inRange(x) && inRange(y))) {
            System.out.println("Coordinate out of Range!");
            return;
        }
        int index = getIndex(x, y);
        if (field.get(index).equals(Cell.MARK)) {
            field.set(index, Cell.UNKNOWN);
            if (mines.contains(index)) {
                markedMines.remove(index);
            }
            System.out.println(this);
        }
        else if (field.get(index).equals(Cell.UNKNOWN)) {
            field.set(index, Cell.MARK);
            if (mines.contains(index)) {
                markedMines.add(index);
            }
            System.out.println(this);
        }
        else if (field.get(index).equals(Cell.ZERO)) {
            System.out.println("This cell is already explored!");
        }
        else {
            System.out.println("There is a number here!");
        }

    }

    public void freeCell(int x, int y) {
        if (!(inRange(x) && inRange(y))) {
            System.out.println("Coordinate out of Range!");
            return;
        }
        int index = getIndex(x, y);
        if (mines.contains(index)) {
            this.isExplode = true;
            revealMine();
            System.out.println(this);
        }
        else if (isUnexplored(index)) {
            openArea(index);
            System.out.println(this);
        }
        else if (field.get(index).equals(Cell.ZERO)) {
            System.out.println("This cell is already explored!");
        }
        else {
            System.out.println("There is a number here!");
        }
    }

    private void openArea(int index) {
        int mineCount = countMines(index);
        if (mineCount > 0) {
            this.placeNum(index);
        }
        else {
            this.placeNum(index);
            checkAllAround(index);
            while (!stack.empty()) {
                Integer next = stack.pop();
                checkAllAround(next);
            }
        }
    }

    public void checkAllAround(int index) {
        IntStream neighbors = neighbors(index);
        neighbors.forEach(n -> {
            if (isUnexplored(n)) {
                int mineCount = countMines(n);
                if (mineCount == 0) {
                    placeNum(n);
                    stack.push(n);
                }
                else if (mineCount > 0) {
                    placeNum(n);
                }
            }
        });

    }

    public boolean isAllExplored() {
        return range(0, field.size()).parallel().filter(this::isUnexplored).count() == mines.size();
    }

    private boolean isUnexplored(int index) {
        return Cell.UNEXPLORED.contains(field.get(index));
    }

    private void revealMine() {
        mines.forEach(i -> field.set(i, Cell.MINE));
    }


    private void placeMine() {
        int placeCount = 0;
        while (placeCount != minesCount) {
            int p = RNG.nextInt(size*size);
            if (!mines.contains(p)) {
                placeCount += 1;
                mines.add(p);
            }
        }
    }

    private void placeNum(int i) {
        int mineCount = countMines(i);
        switch (mineCount) {
            case 0 -> field.set(i, Cell.ZERO);
            case 1 -> field.set(i, Cell.ONE);
            case 2 -> field.set(i, Cell.TWO);
            case 3 -> field.set(i, Cell.TREE);
            case 4 -> field.set(i, Cell.FOUR);
            case 5 -> field.set(i, Cell.FIVE);
            case 6 -> field.set(i, Cell.SIX);
            case 7 -> field.set(i, Cell.SEVEN);
            case 8 -> field.set(i, Cell.EIGHT);
            default -> field.set(i, Cell.UNKNOWN);
        }
    }

    private int countMines(int index) {
        return (int) neighbors(index).parallel().filter(mines::contains).count();
    }

    private IntStream neighbors(int index) {
        return IntStream
                .of(-size - 1, -size, -size + 1, -1, 1, size - 1, size, size + 1)
                .parallel()
                .filter(offset -> inRange(index, offset))
                .map(offset -> index + offset);
    }

    private boolean inRange(int index, int offset) {
        return inRange(index % size + offset - offset / (size - 1) * size)
                && inRange(index / size + offset / (size - 1));
    }


    private boolean inRange(int x) {
        return x >= 0 && x < size;
    }


    public boolean isAllMineMarked() {
        return markedMines.equals(mines);
    }

    public boolean isBoardExplode() {
        return isExplode;
    }

    @Override
    public String toString() {
        return String.format(template, field.toArray());
    }


    private String createTemplate() {
        return ""
                + " │123456789│%n"
                + "—│—————————│%n"
                + rangeClosed(1, size).mapToObj(row -> row + "│%s%s%s%s%s%s%s%s%s│%n").collect(joining())
                + "—│—————————│";
    }
}

