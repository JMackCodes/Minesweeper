package minesweeper;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        playGameBeginner();
}
    public static void playGameBeginner() {
        System.out.print("How many mines do you want on the field? ");
        Scanner in = new Scanner(System.in);
        int numberOfMines = in.nextInt();
        MineSweeper game = new MineSweeper();
        game.makeMap(9, 9);
        game.makeMines(numberOfMines);
        game.checkAroundMines();
        game.printMap();
        while (!game.checkWin() && !game.youLost) {
            System.out.print("Set/unset mines marks or claim a cell as free:");
            System.out.println();
            MineSweeper.GetInput input = new MineSweeper.GetInput();
            if (input.getCommand()) {
                game.markLocation(input.getX(), input.getY());
            } else {
                game.exploreLocation(input.getX(), input.getY());
                if (!game.checkWin() && !game.youLost) {
                    game.printMap();
                    System.out.println();
                }
            }
        }
    }
}
class MineSweeper {
    private int height;
    private int width;
    private MapLocation[][] mapDetail;
    public void makeMap(int height, int width) {
        this.height = height;
        this.width = width;
        this.mapDetail = new MapLocation[this.height][this.width];

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.mapDetail[i][j] = new MapLocation();
            }
        }
    }
    public void printMap() {
        // comments are to make it look better, not to pass the automated checker
        System.out.print(" |");
        for (int i = 0; i < this.width; i++) {
//            System.out.print(i + 1);
            System.out.print(i + 1 + " ");
        }
        System.out.println("|");
//        System.out.println("-|---------|");

        for (int i = 0; i < this.height; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < this.width; j++) {
//                System.out.print(this.mapDetail[i][j].toChar());
                System.out.print(this.mapDetail[i][j].toChar() + " ");
            }
            System.out.println("|");
        }
//        System.out.println("-|---------|");
    }
    private int numMines;
    public void makeMines(int numMines) {
        this.numMines = numMines;
        int randomHeight;
        int randomWidth;

        for (int i = 0; i < this.numMines; i++) {
            randomHeight = new Random().nextInt(this.height);
            randomWidth = new Random().nextInt(this.width);
            if (!mapDetail[randomHeight][randomWidth].isMine) {
                mapDetail[randomHeight][randomWidth].setMine(true);
            } else {
                i--;
            }
        }
    }
    public void exploreLocation(int x, int y) {
        x--;
        y--;

        if (mapDetail[y][x].isMine) {
            mapDetail[y][x].setExplored(true);
            this.youLost = true;
            this.printMap();
            System.out.println("You stepped on a mine and failed!");
        } else {
            mapDetail[y][x].setExplored(true);
//            if (y > 0 && y < this.height && x > 0 && x < this.height) {
//                this.expandExploration(y, x);
//            }

            try {
                this.expandExploration(y, x);
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.println("Whoops");
            }
        }
    }

    class ThisMightWork {
        int y;
        int x;

        public ThisMightWork(int y, int x) {
            this.y = y;
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }
    }

    public void expandExploration(int i, int j) {
        ThisMightWork coordinateStorage = new ThisMightWork(i, j);
        ArrayList<ThisMightWork> checkNext = new ArrayList<>();

        mapDetail[i][j].setExplored(true);
        mapDetail[i][j].setMarked(false);

        int upI = i + 1;

        int downI = i - 1;

        int upJ = j + 1;

        int downJ = j - 1;

        if (upI < this.height && upJ < this.width && mapDetail[i + 1][j + 1].getNeighboringMines() == 0
                && !mapDetail[i + 1][j + 1].isExplored) {
            checkNext.add(new ThisMightWork(i + 1, j + 1));
        } else if (upI < this.height && upJ < this.width
                && !mapDetail[i + 1][j + 1].isExplored && !mapDetail[i + 1][j + 1].isMine) {
                mapDetail[upI][upJ].setExplored(true);
            mapDetail[upI][upJ].setMarked(false);

        }

        if (upI < this.height && mapDetail[i + 1][j].getNeighboringMines() == 0
                        && !mapDetail[i + 1][j].isExplored) {
                    checkNext.add(new ThisMightWork(i + 1, j));
        } else if (upI < this.height && !mapDetail[i + 1][j].isExplored && !mapDetail[i + 1][j].isMine) {
            mapDetail[upI][j].setExplored(true);
            mapDetail[upI][j].setMarked(false);
        }

        if (upI < this.height && downJ >= 0 && mapDetail[i + 1][j - 1].getNeighboringMines() == 0
                        && !mapDetail[i + 1][j - 1].isExplored) {
                    checkNext.add(new ThisMightWork(i + 1, j - 1));
        } else if (upI < this.height && downJ >= 0 && !mapDetail[i + 1][j - 1].isExplored
                && !mapDetail[i + 1][j - 1].isMine) {
            mapDetail[upI][downJ].setExplored(true);
            mapDetail[upI][downJ].setMarked(false);
        }

        if (upJ < this.width && mapDetail[i][j + 1].getNeighboringMines() == 0
                        && !mapDetail[i][j + 1].isExplored) {
                    checkNext.add(new ThisMightWork(i, j + 1));
        } else if (upJ < this.width && !mapDetail[i][j + 1].isExplored && !mapDetail[i][j + 1].isMine) {
            mapDetail[i][upJ].setExplored(true);
            mapDetail[i][upJ].setMarked(false);
        }

        if (downJ >= 0 && mapDetail[i][j - 1].getNeighboringMines() == 0 && !mapDetail[i][j - 1].isExplored) {
                    checkNext.add(new ThisMightWork(i, j - 1));
        } else if (downJ >= 0 && !mapDetail[i][j - 1].isExplored && !mapDetail[i][j - 1].isMine) {
            mapDetail[i][downJ].setExplored(true);
            mapDetail[i][downJ].setMarked(false);
        }

        if (downI >= 0 && upJ < this.width && mapDetail[i - 1][j + 1].getNeighboringMines() == 0
                        && !mapDetail[i - 1][j + 1].isExplored) {
                    checkNext.add(new ThisMightWork(i - 1, j + 1));
        } else if (downI >= 0 && upJ < this.width && !mapDetail[i - 1][j + 1].isExplored
                && !mapDetail[i - 1][j + 1].isMine) {
            mapDetail[downI][upJ].setExplored(true);
            mapDetail[downI][upJ].setMarked(false);
        }

        if (downI >= 0 && mapDetail[i - 1][j].getNeighboringMines() == 0 && !mapDetail[i - 1][j].isExplored) {
                    checkNext.add(new ThisMightWork(i - 1, j));
        } else if (downI >= 0 && !mapDetail[i - 1][j].isExplored && !mapDetail[i - 1][j].isMine) {
            mapDetail[downI][j].setExplored(true);
            mapDetail[downI][j].setMarked(false);
        }

        if (downI >= 0 && downJ >= 0 && mapDetail[i - 1][j - 1].getNeighboringMines() == 0
                        && !mapDetail[i - 1][j - 1].isExplored) {
                    checkNext.add(new ThisMightWork(i - 1, j - 1));
        } else if (downI >= 0 && downJ >= 0 && !mapDetail[i - 1][j - 1].isExplored && !mapDetail[i - 1][j - 1].isMine) {
            mapDetail[downI][downJ].setExplored(true);
            mapDetail[downI][downJ].setMarked(false);
        }

        for (int k = 0; k < checkNext.size(); k++) {
                try {
                    expandExploration(checkNext.get(k).getY(), checkNext.get(k).getX());
                } catch (ArrayIndexOutOfBoundsException exception) {
                    System.out.println(9);
                }
            }
    }
    public void markLocation(int y, int x) {
        boolean keepGoing = true;
        while (keepGoing) {
            x--;
            y--;
            try {
                System.out.println();
//                if (!mapDetail[x][y].isMine && mapDetail[x][y].neighboringMines > 0 && !mapDetail[x][y].isExplored()) {
//                    System.out.println("There is a number here!");
//                } else
                    if (!mapDetail[x][y].isMarked) {
                    mapDetail[x][y].setMarked(true);
                    keepGoing = false;
                    this.printMap();
                } else if (mapDetail[x][y].isMarked) {
                    mapDetail[x][y].setMarked(false);
                    keepGoing = false;
                    this.printMap();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("ENTER GOOD NUMBERS ONLY END USER PEASANT");
                System.out.println();
                MineSweeper.GetInput input = new MineSweeper.GetInput();
                this.markLocation(input.getX(), input.getY());
                keepGoing = false;
            }
        }
    }
    public boolean youLost = false;
    public boolean checkWin() {
        int score = 0;
        boolean isWin = false;
        int numberOfMarks = 0;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (mapDetail[i][j].isMine && mapDetail[i][j].isMarked) {
                    score++;
                } else if (!mapDetail[i][j].isMine && mapDetail[i][j].isMarked) {
                    score--;
                }
            }
        }
        int emptyRevealed = 0;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (mapDetail[i][j].isExplored) {
                    emptyRevealed++;
                }
            }
        }
        if (emptyRevealed == ((this.height * this.width) - numMines)) {
            isWin = true;
        }

        if (score == this.numMines) {
            isWin = true;
        }
        if (isWin) {
            this.printMap();
            System.out.println("Congratulations! You found all mines!");
        }
        return isWin;
    }
    public void checkAroundMines() {
        for (int i = 1; i < this.height - 1; i++) {//checks all inner cells
            for (int j = 1; j < width - 1; j++) {
                if (mapDetail[i][j].isMine) {
                    mapDetail[i + 1][j + 1].incrementNeighboringMines();
                    mapDetail[i + 1][j].incrementNeighboringMines();
                    mapDetail[i + 1][j - 1].incrementNeighboringMines();

                    mapDetail[i][j + 1].incrementNeighboringMines();
                    mapDetail[i][j].incrementNeighboringMines();
                    mapDetail[i][j - 1].incrementNeighboringMines();

                    mapDetail[i - 1][j + 1].incrementNeighboringMines();
                    mapDetail[i - 1][j].incrementNeighboringMines();
                    mapDetail[i - 1][j - 1].incrementNeighboringMines();
                }
            }
        }
        for (int j = 1; j < width - 1; j++) {//top row check
            int i = 0;
            if (mapDetail[i][j].isMine) {
                mapDetail[i][j + 1].incrementNeighboringMines();
                mapDetail[i][j].incrementNeighboringMines();
                mapDetail[i][j - 1].incrementNeighboringMines();

                mapDetail[i + 1][j + 1].incrementNeighboringMines();
                mapDetail[i + 1][j].incrementNeighboringMines();
                mapDetail[i + 1][j - 1].incrementNeighboringMines();
            }
        }
        for (int j = 1; j < width - 1; j++) {//bottom row check
            int i = this.height - 1;
            if (mapDetail[i][j].isMine) {
                mapDetail[i][j + 1].incrementNeighboringMines();
                mapDetail[i][j].incrementNeighboringMines();
                mapDetail[i][j - 1].incrementNeighboringMines();

                mapDetail[i - 1][j + 1].incrementNeighboringMines();
                mapDetail[i - 1][j].incrementNeighboringMines();
                mapDetail[i - 1][j - 1].incrementNeighboringMines();
            }
        }
        for (int i = 1; i < height - 1; i++) {//checks left column
            int j = 0;
            if (mapDetail[i][j].isMine) {
                mapDetail[i + 1][j + 1].incrementNeighboringMines();
                mapDetail[i + 1][j].incrementNeighboringMines();

                mapDetail[i][j + 1].incrementNeighboringMines();
                mapDetail[i][j].incrementNeighboringMines();

                mapDetail[i - 1][j + 1].incrementNeighboringMines();
                mapDetail[i - 1][j].incrementNeighboringMines();
            }
        }
        for (int i = 1; i < height - 1; i++) {//checks right column
            int j = this.width - 1;
            if (mapDetail[i][j].isMine) {
                mapDetail[i + 1][j].incrementNeighboringMines();
                mapDetail[i + 1][j - 1].incrementNeighboringMines();

                mapDetail[i][j].incrementNeighboringMines();
                mapDetail[i][j - 1].incrementNeighboringMines();

                mapDetail[i - 1][j].incrementNeighboringMines();
                mapDetail[i - 1][j - 1].incrementNeighboringMines();
            }
        }
        if (mapDetail[0][0].isMine) {//checks top left corner
            mapDetail[1][1].incrementNeighboringMines();
            mapDetail[0][1].incrementNeighboringMines();
            mapDetail[1][0].incrementNeighboringMines();
        }
        if (mapDetail[0][width -1].isMine) {//checks top right corner
            mapDetail[0][width - 2].incrementNeighboringMines();
            mapDetail[1][width - 2].incrementNeighboringMines();
            mapDetail[1][width - 1].incrementNeighboringMines();
        }
        if (mapDetail[height - 1][0].isMine) {//checks bottom left corner
            mapDetail[height -1][1].incrementNeighboringMines();
            mapDetail[height - 2][1].incrementNeighboringMines();
            mapDetail[height -2][0].incrementNeighboringMines();
        }
        if (mapDetail[height - 1][width - 1].isMine) {//checks bottom right corner
            mapDetail[height - 1][width - 2].incrementNeighboringMines();
            mapDetail[height - 2][width - 2].incrementNeighboringMines();
            mapDetail[height - 2][width - 1].incrementNeighboringMines();
        }
    }
    public int getNumMines() {
        return numMines;
    }
    class MapLocation {
        private boolean isMine = false;
        private boolean isMarked = false;
        private boolean isExplored = false;
        private int neighboringMines = 0;
        private char neighboringMinesDisplay = '0';
        public boolean isExplored() {
            return isExplored;
        }
        public void setExplored(boolean explored) {
            isExplored = explored;
        }
        public boolean isMine() {
            return isMine;
        }
        public void setMine(boolean mine) {
            isMine = mine;
        }
        public boolean isMarked() {
            return isMarked;
        }
        public void setMarked(boolean marked) {
            isMarked = marked;
        }
        public int getNeighboringMines() {
            return neighboringMines;
        }
        public void incrementNeighboringMines() {
            this.neighboringMines++;
            this.neighboringMinesDisplay++;
        }
        public char toChar() {
            if (this.isMarked) {
                return '*';
            } else if (this.isMine && this.isExplored) {
                return 'X';
            } else if (this.neighboringMines > 0 && !this.isMine && this.isExplored) {
                return this.neighboringMinesDisplay;
            } else if (this.isExplored) {
                return '/';
            } else {
                return '.';
            }
        }
    }
     static class GetInput {
        private int x;
        private int y;
        private String command;
        GetInput() {
            Scanner in = new Scanner(System.in);
            this.x = in.nextInt();
            this.y = in.nextInt();
            String temp = in.next();
            if (temp.equalsIgnoreCase("mine")) {
                this.command = "mine";
            } else if (temp.equalsIgnoreCase("free")) {
                this.command = "free";
            } else {
                System.out.println("command unrecognized");
                //probably do something else here
            }
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        public Boolean getCommand() {
            boolean placeMark = false;

            if (this.command.equalsIgnoreCase("mine")) {
                placeMark = true;
            } else if (this.command.equalsIgnoreCase("free")) {
                placeMark = false;
            }
            return placeMark;
        }
    }
}