package com.example.mousegamefx.model;

public class MouseCheeseGame {
    private GameCell[][] gameBoard;
    private int totalEarnedPoints = 0;
    private int colMousePosition;
    private int rowMousePosition;
    private boolean won;
    private boolean lost;
    private boolean testMode;

    /**
     * Crear un tablero de rows x cols y colocar el gato, el queso, una celda propicia y una celda no propicia.
     * @param rows nombre de files
     * @param cols nombre de columnes
     */
    public MouseCheeseGame(int rows, int cols,boolean testMode){
        this.testMode = testMode;
        this.gameBoard = new GameCell[rows][cols];
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = new GameCell();
            }
        }

        //añadimos el gato
        while(true) {
            int x = (int) (Math.random() * rows);
            int y = (int) (Math.random() * cols);
            if ((x > 0 || y > 0) && (x < rows || y > cols) && !(x == rows-1 && y == cols-1)  && !(gameBoard[x][y] instanceof Questionable)){
                gameBoard[x][y].setCat();
                break;
            }
        }

        //añadimos celda de pregunta propicia
        while(true) {
            int x = (int) (Math.random() * rows);
            int y = (int) (Math.random() * cols);
            if ((x > 0 || y > 0) && (x < rows || y > cols) && !(x == rows-1 && y == cols-1) && !(gameBoard[x][y] instanceof Questionable) && !gameBoard[x][y].isCat()){
                gameBoard[x][y] = new PropitiousCell("Di el nombre de un IDE para programar en JAVA:","Intellij Idea,Intellij,NetBeans,Eclipse");
                break;
            }
        }

        //añadimos celda de pregunta no propicia
        while(true) {
            int x = (int) (Math.random() * rows);
            int y = (int) (Math.random() * cols);
            if ((x > 0 || y > 0) && (x < rows || y > cols) && !(x == rows-1 && y == cols-1) && !(gameBoard[x][y] instanceof Questionable) && !gameBoard[x][y].isCat()){
                gameBoard[x][y] = new UnpropitiousCell();
                break;
            }
        }
        //añadimos el queso
        gameBoard[rows-1][cols-1].setCheese();

        if(testMode) {
            printSolutionBoard(gameBoard);
        }

    }

    public void start() {
        colMousePosition = 0;
        rowMousePosition = 0;
        getCurrentCell().setDiscovered();
        completeMouseMovement();
    }

    public boolean hasWon() {
        return won;
    }

    public boolean hasLost() {
        return lost;
    }

    public int getTotalEarnedPoints() {
        return totalEarnedPoints;
    }

    public int getColMousePosition() {
        return colMousePosition;
    }

    public int getRowMousePosition() {
        return rowMousePosition;
    }

    public String startMouseMovement(String movement) {
        switch (movement){
            case "l":
                if (colMousePosition == 0) {
                    return null;
                }
                if(gameBoard[rowMousePosition][colMousePosition-1].isDiscovered()) {
                    return null;
                }
                colMousePosition--;
                break;
            case "r":
                if (colMousePosition == gameBoard[rowMousePosition].length-1) {
                    return null;
                }
                if(gameBoard[rowMousePosition][colMousePosition+1].isDiscovered()) {
                    return null;
                }
                colMousePosition++;
                break;
            case "u":
                if (rowMousePosition == 0) {
                    return null;
                }
                if(gameBoard[rowMousePosition-1][colMousePosition].isDiscovered())
                    return null;
                rowMousePosition--;
                break;
            case "d":
                if (rowMousePosition == gameBoard.length-1) {
                    return null;
                }
                if(gameBoard[rowMousePosition+1][colMousePosition].isDiscovered()) {
                    return null;
                }
                rowMousePosition++;
                break;
        }
        getCurrentCell().setDiscovered();

        if (rowMousePosition == gameBoard.length-1 && colMousePosition == gameBoard[rowMousePosition].length-1 ){
            this.won = true;
            return null;
        }else if(getCurrentCell().isCat()){
            this.lost = true;
            return null;
        }else if(getCurrentCell() instanceof Questionable){
            return ((Questionable)getCurrentCell()).getQuestion();
        }
        completeMouseMovement();
        return null;
    }

    private GameCell getCurrentCell() {
        return gameBoard[rowMousePosition][colMousePosition];
    }


    public int completeMouseMovement(String userAnswer) {
        if(getCurrentCell() instanceof PropitiousCell) {
            if (((PropitiousCell) getCurrentCell()).submitAnswer(userAnswer)) {
                this.totalEarnedPoints += 50;
                return 50;
            }
        }
        if(getCurrentCell() instanceof UnpropitiousCell) {
            if (!(((UnpropitiousCell) getCurrentCell()).submitAnswer(userAnswer))) {
                this.totalEarnedPoints -= 50;
                return -50;
            }
        }
        return 0;
    }

    public int completeMouseMovement() {
        this.totalEarnedPoints += getCurrentCell().getPoints();
        return getCurrentCell().getPoints();
    }

    public String getCurrentFigure() {
        return this.getCurrentCell().toString();
    }


    public static void printSolutionBoard(GameCell[][] gameBoard){
        for(int x = 0; x < gameBoard.length; x++){
            for(int y = 0; y < gameBoard[0].length ; y++){
                if(y > 0 && y < gameBoard[0].length)
                    System.out.print(" ");
                System.out.print(gameBoard[x][y]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
