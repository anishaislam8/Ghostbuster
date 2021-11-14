import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static double[][] board;
    public static int size;
    public static boolean status;
    public static DecimalFormat df = new DecimalFormat("#.###");
    public static int ghostRow;
    public static int ghostColumn;
    public static String sensedColor;
    public static HashMap<String,  double[][]> transitionProbability;
    public static int redLastRange;
    public static int orangeLastRange;

    public static void main(String[] args) {
        df.setRoundingMode(RoundingMode.DOWN);

        status = true;
        sensedColor="";
        transitionProbability = new HashMap<>();

        Scanner input = new Scanner(System.in);
        System.out.println("Enter a number for board size : ");
        String s = input.nextLine();
        System.out.println();
        System.out.println();

        size = Integer.parseInt(s);
        board = new double[size][size];
        double initialProbability = 1.00 / ((size * size) * 1.00);

        if(size != 2) {
            int limit = (size - 1) * 2;
            redLastRange = limit / 4;
            orangeLastRange = redLastRange * 2;
        }else{
            redLastRange = 0;
            orangeLastRange = 1;
        }


        //assignInitialProbability
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                board[i][j] = initialProbability;
            }
        }
        //assign a random position for a ghost
        Random random = new Random(System.currentTimeMillis());
        ghostRow = random.nextInt(size);
        ghostColumn = random.nextInt(size);

        //assignTransitionProbability
        setTransitionProbability();

        String choice = "";

        System.out.println("********Starting GHOSTBUSTER**********\n\n");
        printMatrix();

        while(status){
            System.out.println("Choose an option : ");
            System.out.println("1) Advance Timestamp");
            System.out.println("2) Select a box");
            System.out.println("3) Catch the ghost");


            choice = input.nextLine();

            switch (choice){
                case "1" :
                    moveGhost();
                    recalculateBasedOnTransitionProbability();
                    normalize();
                    printMatrix();
                    break;
                case "2" :
                    System.out.println("Enter row number : (Starting from 0)");
                    String temp = input.nextLine();
                    int row = Integer.parseInt(temp);
                    System.out.println("Enter column number : (Starting from 0)");
                    temp = input.nextLine();
                    int column = Integer.parseInt(temp);

                    String color = colorOfCell(row, column, ghostRow, ghostColumn);
                    sensedColor = color;
                    System.out.println("Color : " + color);
                    recalculateBasedOnSensor(row, column);
                    normalize();
                    printMatrix();


                    break;
                case "3" :
                    System.out.println("Enter row number : (Starting from 0)");
                    temp = input.nextLine();
                    row = Integer.parseInt(temp);
                    System.out.println("Enter column number : (Starting from 0)");
                    temp = input.nextLine();
                    column = Integer.parseInt(temp);

                    if(row == ghostRow && column == ghostColumn){
                        System.out.println();
                        System.out.println("Hit!!!");
                        status = false;
                    }else{
                        System.out.println();
                        System.out.println("Miss!!!");
                        System.out.println("Ghost was in row : " + ghostRow + " and column : " + ghostColumn);
                        status = false;
                    }
                    break;
                default:
                    System.out.println("Not a valid option");
                    break;
            }
        }
    }


    public static int[] stringToPosition(String s) {
        String[] arr = s.split(" ");
        int result[] = new int[2];
        result[0] = Integer.parseInt(arr[0]);
        result[1] = Integer.parseInt(arr[1]);

        return result;
    }
    public static String positionToString(int i, int j) {
        String s = i + " " + j;
        return s;
    }


    public static void printMatrix(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                System.out.print(df.format(board[i][j]) + "\t");
            }
            System.out.println();

        }
        System.out.println();
        System.out.println();
    }

    public static void normalize(){
        double summation = 0.0;
        for(int i = 0; i < size; i++){
            for(int j  = 0; j < size; j++){
                summation += board[i][j];
            }
        }
        for(int i = 0; i < size; i++){
            for(int j  = 0; j < size; j++){
               board[i][j] /= summation;
            }
        }

    }

    public static void moveGhost(){
        int row = ghostRow;
        int column = ghostColumn;

        Random random = new Random(System.currentTimeMillis());
        double probability = random.nextDouble();

        if(row == 0){
            if(column == 0){
                //right neighbor
                if(probability > 0.75){
                    ghostRow = row;
                    ghostColumn = column + 1;
                }
                //down neighbor
                else if(probability > 0.50 && probability <= 0.75){
                    ghostRow = row + 1;
                    ghostColumn = column;
                }
                //lower right neighbor
                else if(probability > 0.25 && probability <= 0.50){
                    ghostRow = row + 1;
                    ghostColumn = column + 1;
                }
                //own
                else if(probability <= 0.25){
                    //do nothing
                }

            }else if(column == size -1){
                //left neighbor
                if(probability > 0.75){
                    ghostRow = row;
                    ghostColumn = column - 1;
                }
                //down neighbor
                else if(probability > 0.50 && probability <= 0.75){
                    ghostRow = row + 1;
                    ghostColumn = column;
                }
                //lower left neighbor
                else if(probability > 0.25 && probability <= 0.50){
                    ghostRow = row + 1;
                    ghostColumn = column - 1;
                }
                //own
                else if(probability <= 0.25){
                    //do nothing
                }

            }else{
                //right neighbor
                if(probability > 0.85 ){
                    ghostRow = row;
                    ghostColumn = column + 1;
                }
                //left
                else if(probability > 0.68 && probability <= 0.85){
                    ghostRow = row;
                    ghostColumn = column - 1;
                }
                //down neighbor
                else if(probability > 0.51 && probability <= 0.68){
                    ghostRow = row + 1;
                    ghostColumn = column;
                }
                //lower right neighbor
                else if(probability > 0.34 && probability <= 0.51){
                    ghostRow = row + 1;
                    ghostColumn = column + 1;
                }
                //lower left neighbor
                else if(probability > 0.17 && probability <= 0.34){
                    ghostRow = row + 1;
                    ghostColumn = column - 1;
                }
                //own
                else if(probability <= 0.17){
                    //
                }
            }
        } else if(row == size-1){
            if(column == 0){
                //right neighbor
                if(probability > 0.75){
                    ghostRow = row;
                    ghostColumn = column + 1;
                }
                //up neighbor
                else if(probability > 0.50 && probability <= 0.75){
                    ghostRow = row - 1;
                    ghostColumn = column;
                }
                //upper right neighbor
                else if(probability > 0.25 && probability <= 0.50){
                    ghostRow = row - 1;
                    ghostColumn = column + 1;
                }
                //own
                else if(probability <= 0.25){
                    //do nothing
                }

            }else if(column == size -1){
                //left
                if(probability > 0.75){
                    ghostRow = row;
                    ghostColumn = column - 1;
                }
                //up neighbor
                else if(probability > 0.50 && probability <= 0.75){
                    ghostRow = row - 1;
                    ghostColumn = column;
                }
                //upper left neighbor
                else if(probability > 0.25 && probability <= 0.50){
                    ghostRow = row - 1;
                    ghostColumn = column - 1;
                }
                //own
                else if(probability <= 0.25){
                    //do nothing
                }

            }else{
                //right neighbor
                if(probability > 0.85){
                    ghostRow = row;
                    ghostColumn = column + 1;
                }
                //left
                else if(probability > 0.68 && probability <= 0.85){
                    ghostRow = row;
                    ghostColumn = column - 1;
                }
                //up neighbor
                else if(probability > 0.51 && probability <= 0.68){
                    ghostRow = row - 1;
                    ghostColumn = column;
                }
                //upper Right
                else if(probability > 0.34 && probability <= 0.51){
                    ghostRow = row - 1;
                    ghostColumn = column + 1;
                }
                //upper Left
                else if(probability > 0.17 && probability <= 0.34){
                    ghostRow = row - 1;
                    ghostColumn = column - 1;
                }
                //own
                else if(probability <= 0.17){
                    //do nothing
                }

            }
        } else if(column == 0){ //4 ta corner case already covered
            //right
            if(probability > 0.85){
                ghostRow = row;
                ghostColumn = column + 1;
            }
            //up
            else if(probability > 0.68 && probability <= 0.85){
                ghostRow = row - 1;
                ghostColumn = column;
            }
            //down neighbor
            else if(probability > 0.51 && probability <= 0.68){
                ghostRow = row + 1;
                ghostColumn = column;
            }
            //lower Right
            else if(probability > 0.34 && probability <= 0.51){
                ghostRow = row + 1;
                ghostColumn = column + 1;
            }
            //upper right
            else if(probability > 0.17 && probability <= 0.34){
                ghostRow = row - 1;
                ghostColumn = column + 1;
            }
            //own
            else if(probability <= 0.17){
                //do nothing
            }

        } else if(column == size-1){ //4 ta corner case already covered
            //left
            if(probability > 0.85){
                ghostRow = row;
                ghostColumn = column - 1;
            }
            //up
            else if(probability > 0.68 && probability <= 0.85){
                ghostRow = row - 1;
                ghostColumn = column;
            }
            //down neighbor
            else if(probability > 0.51 && probability <= 0.68){
                ghostRow = row + 1;
                ghostColumn = column;
            }
            //lower left
            else if(probability > 0.34 && probability <= 0.51){
                ghostRow = row + 1;
                ghostColumn = column - 1;
            }
            //upper Left
            else if(probability > 0.17 && probability <= 0.34){
                ghostRow = row - 1;
                ghostColumn = column - 1;
            }
            //own
            else if(probability <= 0.17){
                //do nothing
            }

        } else {
            //right neighbor
            if(probability > 0.88){
                ghostRow = row;
                ghostColumn = column + 1;
            }
            //left
            else if(probability > 0.77 && probability <= 0.88){
                ghostRow = row;
                ghostColumn = column - 1;
            }
            //up neighbor
            else if(probability > 0.66 && probability <= 0.77){
                ghostRow = row - 1;
                ghostColumn = column;
            }
            //down neighbor
            else if(probability > 0.55 && probability <= 0.66){
                ghostRow = row + 1;
                ghostColumn = column;
            }
            //lower right neighbor
            else if(probability > 0.44 && probability <= 0.55){
                ghostRow = row + 1;
                ghostColumn = column + 1;
            }
            //lower left neighbor
            else if(probability > 0.33 && probability <= 0.44){
                ghostRow = row + 1;
                ghostColumn = column - 1;
            }
            //upper Right
            else if(probability > 0.22 && probability <= 0.33){
                ghostRow = row - 1;
                ghostColumn = column + 1;
            }
            //upper Left
            else if(probability > 0.11 && probability <= 0.22){
                ghostRow = row - 1;
                ghostColumn = column - 1;
            }
            //own
            else if(probability <= 0.11){
                //do nothing
            }

        }

    }

    public static void setTransitionProbability(){
        for(int row = 0; row < size; row++){
            for(int column = 0; column < size; column++){
                String key = positionToString(row, column);
                double[][] probability = new double[3][3];
                if(row == 0){
                    if(column == 0){
                        probability[0][0] = 0.0;
                        probability[0][1] = 0.0;
                        probability[0][2] = 0.0;

                        probability[1][0] = 0.0;
                        probability[1][1] = 0.01;
                        probability[1][2] = 0.45;

                        probability[2][0] = 0.0;
                        probability[2][1] = 0.45;
                        probability[2][2] = 0.09;
                    }else if(column == size -1){
                        probability[0][0] = 0.0;
                        probability[0][1] = 0.0;
                        probability[0][2] = 0.0;

                        probability[1][0] = 0.45;
                        probability[1][1] = 0.01;
                        probability[1][2] = 0.0;

                        probability[2][0] = 0.09;
                        probability[2][1] = 0.45;
                        probability[2][2] = 0.0;
                    }else{
                        probability[0][0] = 0.0;
                        probability[0][1] = 0.0;
                        probability[0][2] = 0.0;

                        probability[1][0] = 0.24;
                        probability[1][1] = 0.056;
                        probability[1][2] = 0.24;

                        probability[2][0] = 0.112;
                        probability[2][1] = 0.24;
                        probability[2][2] = 0.112;
                    }
                } else if(row == size-1){
                    if(column == 0){
                        probability[0][0] = 0.0;
                        probability[0][1] = 0.45;
                        probability[0][2] = 0.09;

                        probability[1][0] = 0.0;
                        probability[1][1] = 0.01;
                        probability[1][2] = 0.45;

                        probability[2][0] = 0.0;
                        probability[2][1] = 0.0;
                        probability[2][2] = 0.0;
                    }else if(column == size -1){
                        probability[0][0] = 0.09;
                        probability[0][1] = 0.45;
                        probability[0][2] = 0.0;

                        probability[1][0] = 0.45;
                        probability[1][1] = 0.01;
                        probability[1][2] = 0.0;

                        probability[2][0] = 0.0;
                        probability[2][1] = 0.0;
                        probability[2][2] = 0.0;
                    }else{
                        probability[0][0] = 0.112;
                        probability[0][1] = 0.24;
                        probability[0][2] = 0.112;

                        probability[1][0] = 0.24;
                        probability[1][1] = 0.056;
                        probability[1][2] = 0.24;

                        probability[2][0] = 0.0;
                        probability[2][1] = 0.0;
                        probability[2][2] = 0.0;
                    }
                } else if(column == 0){ //4 ta corner case already covered
                    probability[0][0] = 0.0;
                    probability[0][1] = 0.32;
                    probability[0][2] = 0.015;

                    probability[1][0] = 0.0;
                    probability[1][1] = 0.01;
                    probability[1][2] = 0.32;

                    probability[2][0] = 0.0;
                    probability[2][1] = 0.32;
                    probability[2][2] = 0.015;
                } else if(column == size-1){ //4 ta corner case already covered
                    probability[0][0] = 0.015;
                    probability[0][1] = 0.32;
                    probability[0][2] = 0.0;

                    probability[1][0] = 0.32;
                    probability[1][1] = 0.01;
                    probability[1][2] = 0.0;

                    probability[2][0] = 0.015;
                    probability[2][1] = 0.32;
                    probability[2][2] = 0.0;
                } else {
                    probability[0][0] = 0.0275;
                    probability[0][1] = 0.22;
                    probability[0][2] = 0.0275;

                    probability[1][0] = 0.22;
                    probability[1][1] = 0.01;
                    probability[1][2] = 0.22;

                    probability[2][0] = 0.0275;
                    probability[2][1] = 0.22;
                    probability[2][2] = 0.0275;
                }

                transitionProbability.put(key, probability);
            }
        }
    }
    
    public static void recalculateBasedOnTransitionProbability(){
        double[][] tempMatrix = new double[size][size];
        for(int row = 0; row < size; row++){
            for(int column = 0; column < size; column++){
                double newProbability = 0.0;

                if(row == 0){
                    if(column == 0){
                        //right neighbor
                        newProbability += right(row, column);
                        //down neighbor
                        newProbability += down(row, column);
                        //lower right neighbor
                        newProbability += lowerRight(row, column);
                        //own
                        newProbability += own(row,column);

                    }else if(column == size -1){
                        //left neighbor
                        newProbability += left(row, column);
                        //down neighbor
                        newProbability += down(row, column);
                        //lower left neighbor
                        newProbability += lowerLeft(row, column);
                        //own
                        newProbability += own(row,column);
                    }else{
                        //right neighbor
                        newProbability += right(row, column);
                        //left
                        newProbability += left(row, column);
                        //down neighbor
                        newProbability += down(row, column);
                        //lower right neighbor
                        newProbability += lowerRight(row, column);
                        //lower left neighbor
                        newProbability += lowerLeft(row, column);
                        //own
                        newProbability += own(row,column);
                    }
                } else if(row == size-1){
                    if(column == 0){
                        //right neighbor
                        newProbability += right(row, column);
                        //up neighbor
                        newProbability += up(row, column);
                        //upper right neighbor
                        newProbability += upperRight(row, column);
                        //own
                        newProbability += own(row,column);
                    }else if(column == size -1){
                        //left neighbor
                        newProbability += left(row, column);
                        //up neighbor
                        newProbability += up(row, column);
                        //upper left neighbor
                        newProbability += upperLeft(row, column);
                        //own
                        newProbability += own(row,column);
                    }else{
                        //right neighbor
                        newProbability += right(row, column);
                        //left
                        newProbability += left(row, column);
                        //up neighbor
                        newProbability += up(row, column);
                        //upper Right
                        newProbability += upperRight(row,column);
                        //upper Left
                        newProbability += upperLeft(row,column);
                        //own
                        newProbability += own(row,column);
                    }
                } else if(column == 0){ //4 ta corner case already covered
                    //right neighbor
                    newProbability += right(row, column);
                    //up neighbor
                    newProbability += up(row, column);
                    //down neighbor
                    newProbability += down(row, column);
                    //lower right neighbor
                    newProbability += lowerRight(row, column);
                    //upper Right
                    newProbability += upperRight(row,column);
                    //own
                    newProbability += own(row,column);
                } else if(column == size-1){ //4 ta corner case already covered
                    //left
                    newProbability += left(row, column);
                    //up neighbor
                    newProbability += up(row, column);
                    //down neighbor
                    newProbability += down(row, column);
                    //lower left neighbor
                    newProbability += lowerLeft(row, column);
                    //upper Left
                    newProbability += upperLeft(row,column);
                    //own
                    newProbability += own(row,column);
                } else {
                    //right neighbor
                    newProbability += right(row, column);
                    //left
                    newProbability += left(row, column);
                    //up neighbor
                    newProbability += up(row, column);
                    //down neighbor
                    newProbability += down(row, column);
                    //lower right neighbor
                    newProbability += lowerRight(row, column);
                    //lower left neighbor
                    newProbability += lowerLeft(row, column);
                    //upper Right
                    newProbability += upperRight(row,column);
                    //upper Left
                    newProbability += upperLeft(row,column);
                    //own
                    newProbability += own(row,column);

                }

                tempMatrix[row][column] = newProbability;
            }
        }

        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                board[i][j] = tempMatrix[i][j];
            }
        }
    }

    public static void recalculateBasedOnSensor(int sensedRow, int sensedColumn){
        double[][] tempMatrix = new double[size][size];
        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                int tempGhostRow = i;
                int tempGhostColumn = j;

                String color = colorOfCell(sensedRow, sensedColumn, tempGhostRow, tempGhostColumn);
                if(!color.equalsIgnoreCase(sensedColor)){
                    tempMatrix[i][j] = board[i][j] * 0.0;
                }else{
                    tempMatrix[i][j] = board[i][j] * 1.00;
                }
            }
        }
        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                board[i][j] = tempMatrix[i][j];
            }
        }
    }

    public static String colorOfCell(int row, int column, int ghostRow, int ghostColumn){
        String color = "";

        int distance = Math.abs(ghostRow - row) + Math.abs(ghostColumn - column);
        if(distance <= redLastRange){
            color = "Red";
        } else if(distance > redLastRange && distance <= orangeLastRange){
            color = "Orange";
        } else if(distance > orangeLastRange){
            color = "Green";
        }

        return color;
    }

    public static double upperLeft(int row, int column){
        String neighbor = positionToString(row - 1, column - 1);
        double[][] neighborMatrix = transitionProbability.get(neighbor);
        double val = (neighborMatrix[2][2] * board[row - 1][column - 1]);
        return val;
    }
    public static double up(int row, int column){
        String neighbor = positionToString(row-1, column);
        double[][] neighborMatrix = transitionProbability.get(neighbor);
        double val = (neighborMatrix[2][1] * board[row-1][column]);
        return val;
    }
    public static double upperRight(int row, int column){
        String neighbor = positionToString(row - 1, column + 1);
        double[][] neighborMatrix = transitionProbability.get(neighbor);
        double val = (neighborMatrix[2][0] * board[row - 1][column + 1]);
        return val;
    }
    public static double left(int row, int column){
       String neighbor = positionToString(row, column-1);
       double[][] neighborMatrix = transitionProbability.get(neighbor);
       double val = (neighborMatrix[1][2] * board[row][column-1]);
       return val;
    }
    public static double own(int row, int column){
        String neighbor = positionToString(row, column);
        double[][] neighborMatrix = transitionProbability.get(neighbor);
        double val = (neighborMatrix[1][1] * board[row][column]);
        return val;
    }
    public static double right(int row, int column){
        String neighbor = positionToString(row, column +1);
        double[][] neighborMatrix = transitionProbability.get(neighbor);
        double val = (neighborMatrix[1][0] * board[row][column+1]);

        return val;
    }
    public static double lowerLeft(int row, int column){
        String neighbor = positionToString(row + 1, column - 1);
        double[][] neighborMatrix = transitionProbability.get(neighbor);
        double val = (neighborMatrix[0][2] * board[row + 1][column - 1]);
        return val;
    }
    public static double down(int row, int column){
        String neighbor = positionToString(row + 1, column);
        double[][] neighborMatrix = transitionProbability.get(neighbor);
        double val = (neighborMatrix[0][1] * board[row + 1][column]);

        return val;
    }
    public static double lowerRight(int row, int column){
        String neighbor = positionToString(row+1, column +1);
        double[][] neighborMatrix = transitionProbability.get(neighbor);
        double val = (neighborMatrix[0][0] * board[row+1][column+1]);

        return val;
    }
}
