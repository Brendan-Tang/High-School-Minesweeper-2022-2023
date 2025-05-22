import java.util.Scanner;
import java.util.ArrayList;
/**
 * This class represents the Minesweeper game with 3 levels, level 1, 2 and 3. To win you have to flag all mine tiles and eliminate all non mine tiles.
 *
 * @author (Brendan Tang and Gregory Peng)
 * @version (2/2/2023)
 */
public class MineSweeper {
    // Fields:
    private final int EASYROWS = 10;
    private final int EASYCOLS = 10;
    private final int MIDROWS = 15;
    private final int MIDCOLS = 15;
    private final int HARDROWS = 20;
    private final int HARDCOLS = 20;
    private int cols;
    private int rows;
    private int totalMines;
    private Cell[][] hiddenBoard;
    private Cell[][] displayBoard;
    private boolean firstMove = false;
    private int mode;
    private boolean boardGenerated;
    private boolean dead;
    /**
    * Set the parameters for the game mode that the user has chosen.
    * @param m the mode of minesweeper that will be played.
    */
    public MineSweeper(int m) {
        boardGenerated = false;
        dead = false;
        mode = m;
        if (m <= 1) {
            totalMines = 10;
            rows = EASYROWS;
            cols = EASYCOLS;
        } else if (m <= 2) {
            totalMines = 20;
            rows = MIDROWS;
            cols = MIDCOLS;
        } else {
            totalMines = 40;
            rows = HARDROWS;
            cols = HARDCOLS;
        }
        hiddenBoard = new Cell[rows][cols];
        displayBoard = new Cell[rows][cols];
    }
    /**
    * Returns if the user has clicked on a mine(lost the game) or not.
    * @return True if the user has lost, false if the user has not lost.
    */
    public boolean getDead() {
        return dead;
    }
    /**
    * Returns information about how many rows are in this gamemode.
    * @return the number of rows.
    */
    public int getRows() {
        return rows;
    }
    /**
    * Returns information about how many columns are in this gamemode.
    * @return the number of columns.
    */
    public int getCols() {
        return cols;
    }
    /**
    * Returns if the user has won or not. The user will win if they eliminate all free tiles and do not click on any mines in the process.
    * @return True if the user has won, false if the user has lost.
    */
    public boolean hasWon() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (displayBoard[i][j].getContent().equals(".")) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
    * Clicks(reveals) the tile that the user has specified.
    * @param x the row number of the user specified tile.
    * @param y the column number of the user specified tile.
    */
    public void click(int x, int y) {
        if (!boardGenerated) { 
            generateBoard(x, y); // the first click 
        }

        clickBoard(x, y);
    
    }
    /**
    * Sets the specified tile to a flag. If there is already a flag at the tile, then the flag will be removed.
    * @param flagRow the row number of the user specified tile.
    * @param flagCol the column number of the user specified tile.
    */
    public void flag(int flagRow, int flagCol) {
        if (!boardGenerated) {
            generateBoard(flagRow, flagCol);
        }
        if (displayBoard[flagRow][flagCol].getContent().equals(".")) {
            displayBoard[flagRow][flagCol].setContent("🏳️");
        } else if (displayBoard[flagRow][flagCol].getContent().equals("🏳️")) {
            displayBoard[flagRow][flagCol].setContent(".");
        }
    }
    /**
    * Prints the display board.
    */
    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(displayBoard[i][j].getContent());
            }
            System.out.println();
        }
    }
    /**
    * Creates the display board.
    */
    public void preboard() { //display board before first click
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                displayBoard[i][j] = new Cell(i, j, ".");
            }
        }
    }
    /**
    * Creates the board with the parameters from the gamemode the user chose. It also randomly generates the mines on the game board.
    * @param x the row number of the user specified tile, which is used the prevent a mine from being generated there.
    * @param y the column number of the user specified tile, which is used the prevent a mine from being generated there.
    */
    public void generateBoard(int x, int y) {
        boardGenerated = true;
        // the displayBoard is easy - all dots
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                displayBoard[i][j] = new Cell(i, j, ".");
            }
        }

        // now we generate the mines
        int minesGenerated = 0;
        int cellsLeft = rows * cols - 9; // subtract the 9 cells that we dont' want mines in
        double p = totalMines * 1.0 / (rows * cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                hiddenBoard[i][j] = new Cell(i, j, " ");
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) { //code below is checking all directions
                if ((i == x && j == y) || (i == x && j == y - 1) || (i == x && j == y + 1) || (i == x - 1 && j == y) || (i == x - 1 && j == y - 1) || (i == x - 1 && j == y + 1) || (i == x + 1 && j == y) || (i == x + 1 && j == y - 1) || (i == x + 1 && j == y + 1)) {
                    // skipping the first-clicked cell and the other 8 cells around it after first
                    // click, to avoid mines
                    continue;
                } else {

                    if (minesGenerated == totalMines) {
                        break;
                    }
                    if (cellsLeft == totalMines - minesGenerated) {
                        hiddenBoard[i][j].setContent("*");
                        minesGenerated++;
                    } else {
                        double rand = Math.random();
                        if (rand < p) {
                            hiddenBoard[i][j].setContent("*");
                            minesGenerated++;
                        }
                    }
                }
                cellsLeft--;
            }

        }
        generateNum();
    }
    /**
    * Generates the numbers on the tiles for the game and places them on the tiles.
    */
    public void generateNum() {   //generating the numbers in the hiddenBoard
        if (boardGenerated) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (!"*".equals(hiddenBoard[i][j].getContent())) {
                        int mines = 0;
                        if (i != 0) { //checking all the directions
                            if (hiddenBoard[i - 1][j].getContent().equals("*")) { //N
                                mines++;
                            }
                        }
                        if (i != hiddenBoard.length - 1) {
                            if (hiddenBoard[i + 1][j].getContent().equals("*")) { //S
                                mines++;
                            }
                        }
                        if (j != 0) {
                            if (hiddenBoard[i][j - 1].getContent().equals("*")) { //W
                                mines++;
                            }
                            if (i != 0 && hiddenBoard[i - 1][j - 1].getContent().equals("*")) { //NW
                                mines++;
                            }
                            if (i != hiddenBoard.length - 1 && hiddenBoard[i + 1][j - 1].getContent().equals("*")) { //SW
                                mines++;
                            }
                        }
                        if (j != hiddenBoard[0].length - 1) {
                            if (hiddenBoard[i][j + 1].getContent().equals("*")) { //E
                                mines++;
                            }
                            if (i != 0 && hiddenBoard[i - 1][j + 1].getContent().equals("*")) { //NE
                                mines++;
                            }
                            if (i != hiddenBoard.length - 1 && hiddenBoard[i + 1][j + 1].getContent().equals("*")) { //SE
                                mines++;
                            }
                        }
                        hiddenBoard[i][j].setContent(String.valueOf(mines));
                        if (hiddenBoard[i][j].getContent().equals("0")) {
                            hiddenBoard[i][j].setContent(" ");
                        }

                    }
                }
            }
        }
    }
    /**
    * Prints the board with all the mines and tiles revealed. This board is hidden until the player loses since it contains the solution to winning.
    */
    public void printHiddenBoard() {
        // printing the col header
        System.out.print("   ");
        for (int j = 0; j < cols; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        System.out.print("  ");
        for (int j = 0; j < cols; j++) {
            System.out.print("--");
        }
        System.out.println();
        // end of col header

        for (int i = 0; i < rows; i++) {
            if (i < 10) {
                System.out.print(" " + i + "|");
            } else {
                System.out.print(i + "|");
            }
            for (int j = 0; j < cols; j++) {
                System.out.print(hiddenBoard[i][j].getContent() + " ");
            }
            System.out.println();
        }

    }
    /**
    * Prints the board that the user will see while playing the game.
    * @param debug if true it will print the board, if not it will not print.
    */
    public void printBoard(boolean debug) {
        if (debug) {
            System.out.println("---- display board ----");
        }
        // printing the col header
        System.out.print("   ");
        for (int j = 0; j < cols; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        System.out.print("  ");
        for (int j = 0; j < cols; j++) {
            System.out.print("--");
        }
        System.out.println();
        // end of col header
        for (int i = 0; i < rows; i++) {
            if (i < 10) {
                System.out.print(" " + i + "|");
            } else {
                System.out.print(i + "|");
            }

            for (int j = 0; j < cols; j++) {
                System.out.print(displayBoard[i][j].getContent() + " ");
            }
            System.out.println();
        }
    }

    /**
    * Changes the board displayed based on what the user clicks on. 
    * It will also notify the user if they click on a flag and will as the user to try again.
    * @param x the row of the tile that the user wants to click on.
    * @param y the column of the tile that the user wants to click on.
    */
    public void clickBoard(int x, int y) {
        if (displayBoard[x][y].getContent().equals("F")) {
            System.out.print(
                "Dude, you already flagged this tile. Flag it again to unflag it, and click aga" +
                "in, or choose new number."
            );
        } else if (hiddenBoard[x][y].getContent().equals("*")) {
            displayBoard[x][y].setContent(hiddenBoard[x][y].getContent());
            System.out.println("💥You hit a mine! You lose. Below is the real board!💥");
            System.out.println("The * represents a mine.");
            dead = true;
        } else if (displayBoard[x][y].getContent().equals(".")) {
            // first thing: always reveal the clicked cell if it is not a mine
            displayBoard[x][y].setContent(hiddenBoard[x][y].getContent());

            // flood fill algorithm 
            ArrayList<Cell> cellsToSearch = new ArrayList<Cell>();
            cellsToSearch.add(displayBoard[x][y]);

            ArrayList<Cell> zeroCells = new ArrayList<Cell>();
            if (hiddenBoard[x][y].getContent().equals(" ")) 
                zeroCells.add(displayBoard[x][y]);
            
            while (cellsToSearch.size() > 0) {
                Cell c = cellsToSearch.get(0);
                int cx = c.getX();
                int cy = c.getY();
                Cell n;
                // if statements: check the four immediate neighbors and add any zero cells to
                // the list
                if (cx > 0 && hiddenBoard[cx - 1][cy].getContent().equals(" ")) {
                    n = displayBoard[cx - 1][cy];
                    if (zeroCells.indexOf(n) == -1) {
                        cellsToSearch.add(n);
                        zeroCells.add(n);
                    }
                }
                if (cx < rows - 1 && hiddenBoard[cx + 1][cy].getContent().equals(" ")) {
                    n = displayBoard[cx + 1][cy];
                    if (zeroCells.indexOf(n) == -1) {
                        cellsToSearch.add(n);
                        zeroCells.add(n);
                    }
                }
                if (cy > 0 && hiddenBoard[cx][cy - 1].getContent().equals(" ")) {
                    n = displayBoard[cx][cy - 1];
                    if (zeroCells.indexOf(n) == -1) {
                        cellsToSearch.add(n);
                        zeroCells.add(n);
                    }
                }
                if (cy < cols - 1 && hiddenBoard[cx][cy + 1].getContent().equals(" ")) {
                    n = displayBoard[cx][cy + 1];
                    if (zeroCells.indexOf(n) == -1) {
                        cellsToSearch.add(n);
                        zeroCells.add(n);
                    }
                }
                cellsToSearch.remove(0);
            }

            // now I have a list of zero cells and I need to reveal them as well as their
            // immediate neighbors if they are not mines
            while (zeroCells.size() > 0) {
                Cell c = zeroCells.get(0);
                // reveal the zero cell
                int cx = c.getX();
                int cy = c.getY();
                displayBoard[cx][cy].setContent(hiddenBoard[cx][cy].getContent());
                // reveal the neighbors
                if (cx > 0 && !hiddenBoard[cx - 1][cy].getContent().equals("*")) {
                    displayBoard[cx - 1][cy].setContent(hiddenBoard[cx - 1][cy].getContent());
                }
                if (cx < rows - 1 && !hiddenBoard[cx + 1][cy].getContent().equals("*")) {
                    displayBoard[cx + 1][cy].setContent(hiddenBoard[cx + 1][cy].getContent());
                }
                if (cy > 0 && !hiddenBoard[cx][cy - 1].getContent().equals("*")) {
                    displayBoard[cx][cy - 1].setContent(hiddenBoard[cx][cy - 1].getContent());
                }
                if (cy < cols - 1 && !hiddenBoard[cx][cy + 1].getContent().equals("*")) {
                    displayBoard[cx][cy + 1].setContent(hiddenBoard[cx][cy + 1].getContent());
                }

                zeroCells.remove(0);
            }
        } else {
            System.out.print("This tile has already been revealed! Try again!");
        }
    }
}
