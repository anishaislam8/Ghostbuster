import java.util.*;

public class Main {
    public static int[][] board;
    public static int size;
    public static boolean status;
    public static int ghostRow;
    public static int ghostColumn;
    public static String sensedColor;
    public static int redLastRange;
    public static int orangeLastRange;

    public static void main(String[] args) {

        status = true;
        sensedColor="";

        Scanner input = new Scanner(System.in);
        System.out.println("Enter a number for board size : ");
        String s = input.nextLine();
        System.out.println();
        System.out.println();

        size = Integer.parseInt(s);
        board = new int[size][size];
        
        if(size != 2) {
            int limit = (size - 1) * 2;
            redLastRange = limit / 4;
            orangeLastRange = redLastRange * 2;
        }else{
            redLastRange = 0;
            orangeLastRange = 1;
        }


        //assignInitialParticles
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                board[i][j] = 2;
            }
        }
        //assign a random position for a ghost
        Random random = new Random(System.currentTimeMillis());
        ghostRow = random.nextInt(size);
        ghostColumn = random.nextInt(size);


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
                    moveParticle();
                    printMatrix();
                    //total();
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
                    int reduced = recalculateBasedOnSensor(row, column);
                    int added = resample(reduced);
                    if(added != reduced){
                        normalize(added, reduced);
                    }

                    printMatrix();
                    //total();


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

    public static void total(){
        int total = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                total += board[i][j];
            }
        }

        System.out.println("Total particles : " + total);
    }

    public static void normalize(int added, int reduced){

        PriorityQueue<ComparatorClass> priorityQueue = new PriorityQueue<>(new QueueComparator());
        PriorityQueue<ComparatorClass> priorityQueue2 = new PriorityQueue<>(new QueueComparatorReverse());
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(board[i][j] != 0){
                    priorityQueue.add(new ComparatorClass(positionToString(i,j), board[i][j]));
                    priorityQueue2.add(new ComparatorClass(positionToString(i,j), board[i][j]));
                }
            }
        }
        int temp = added;
        ComparatorClass obj;

        if(added > reduced) {
            while (temp > reduced) {
                obj = priorityQueue.poll();
                String key = obj.getKey();
                int[] positions = stringToPosition(key);
                board[positions[0]][positions[1]] -= 1;
                temp -= 1;
            }
        }else{
            while (temp < reduced) {
                obj = priorityQueue2.poll();
                String key = obj.getKey();
                int[] positions = stringToPosition(key);
                board[positions[0]][positions[1]] += 1;
                temp += 1;
            }
        }
    }

    public static int resample(int reduced){
        HashMap<String, Integer> ratio = new HashMap<>();
        int divideBy = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(board[i][j] != 0){
                    ratio.put(positionToString(i,j), board[i][j]);
                    divideBy += board[i][j];
                }
            }
        }

        int adding = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                int toAdd = 0;
                if(board[i][j] != 0){
                    double fraction = (reduced * 1.00 / divideBy* 1.00) * (ratio.get(positionToString(i,j))*1.00);

                    String s = Double.toString(fraction);
                    String[] splitArray = s.split("\\.");
                    char temp = splitArray[1].charAt(0);
                    int fractionVal = Integer.parseInt(String.valueOf(temp));
                    if(fractionVal >= 5){
                        toAdd = Integer.parseInt(splitArray[0]) + 1;
                    }else{
                        toAdd = Integer.parseInt(splitArray[0]);
                    }
                    board[i][j] += toAdd;
                    adding += toAdd;
                }
            }
        }

        return adding;
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
                System.out.print(board[i][j] + "\t");
            }
            System.out.println();

        }
        System.out.println();
        System.out.println();
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

    public static void moveParticle(){

        int[][] particleNumber = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                particleNumber[i][j] = board[i][j];
            }
        }

        Random random = new Random(System.currentTimeMillis());

        for(int row = 0; row < size; row ++){
            for(int column =0; column < size; column++) {
                int newRow = row;
                int newColumn = column;
                int fixed = board[row][column];
                for (int j = 0; j < fixed; j++) {
                    double probability = random.nextDouble();
                    if (row == 0) {
                        if (column == 0) {
                            //right neighbor
                            if (probability > 0.75) {
                                newRow = row;
                                newColumn = column + 1;
                            }
                            //down neighbor
                            else if (probability > 0.50 && probability <= 0.75) {
                                newRow = row + 1;
                                newColumn = column;
                            }
                            //lower right neighbor
                            else if (probability > 0.25 && probability <= 0.50) {
                                newRow = row + 1;
                                newColumn = column + 1;
                            }
                            //own
                            else if (probability <= 0.25) {
                                //do nothing
                                particleNumber[newRow][newColumn] -= 1;
                                particleNumber[row][column] += 1;
                            }

                            particleNumber[newRow][newColumn] += 1;
                            particleNumber[row][column] -= 1;

                        } else if (column == size - 1) {
                            //left neighbor
                            if (probability > 0.75) {
                                newRow = row;
                                newColumn = column - 1;
                            }
                            //down neighbor
                            else if (probability > 0.50 && probability <= 0.75) {
                                newRow = row + 1;
                                newColumn = column;
                            }
                            //lower left neighbor
                            else if (probability > 0.25 && probability <= 0.50) {
                                newRow = row + 1;
                                newColumn = column - 1;
                            }
                            //own
                            else if (probability <= 0.25) {
                                //do nothing
                                particleNumber[newRow][newColumn] -= 1;
                                particleNumber[row][column] += 1;
                            }

                            particleNumber[newRow][newColumn] += 1;
                            particleNumber[row][column] -= 1;

                        } else {
                            //right neighbor
                            if (probability > 0.85) {
                                newRow = row;
                                newColumn = column + 1;
                            }
                            //left
                            else if (probability > 0.68 && probability <= 0.85) {
                                newRow = row;
                                newColumn = column - 1;
                            }
                            //down neighbor
                            else if (probability > 0.51 && probability <= 0.68) {
                                newRow = row + 1;
                                newColumn = column;
                            }
                            //lower right neighbor
                            else if (probability > 0.34 && probability <= 0.51) {
                                newRow = row + 1;
                                newColumn = column + 1;
                            }
                            //lower left neighbor
                            else if (probability > 0.17 && probability <= 0.34) {
                                newRow = row + 1;
                                newColumn = column - 1;
                            }
                            //own
                            else if (probability <= 0.17) {
                                //
                                particleNumber[newRow][newColumn] -= 1;
                                particleNumber[row][column] += 1;
                            }
                            particleNumber[newRow][newColumn] += 1;
                            particleNumber[row][column] -= 1;
                        }
                    } else if (row == size - 1) {
                        if (column == 0) {
                            //right neighbor
                            if (probability > 0.75) {
                                newRow = row;
                                newColumn = column + 1;
                            }
                            //up neighbor
                            else if (probability > 0.50 && probability <= 0.75) {
                                newRow = row - 1;
                                newColumn = column;
                            }
                            //upper right neighbor
                            else if (probability > 0.25 && probability <= 0.50) {
                                newRow = row - 1;
                                newColumn = column + 1;
                            }
                            //own
                            else if (probability <= 0.25) {
                                //do nothing
                                particleNumber[newRow][newColumn] -= 1;
                                particleNumber[row][column] += 1;
                            }
                            particleNumber[newRow][newColumn] += 1;
                            particleNumber[row][column] -= 1;

                        } else if (column == size - 1) {
                            //left
                            if (probability > 0.75) {
                                newRow = row;
                                newColumn = column - 1;
                            }
                            //up neighbor
                            else if (probability > 0.50 && probability <= 0.75) {
                                newRow = row - 1;
                                newColumn = column;
                            }
                            //upper left neighbor
                            else if (probability > 0.25 && probability <= 0.50) {
                                newRow = row - 1;
                                newColumn = column - 1;
                            }
                            //own
                            else if (probability <= 0.25) {
                                //do nothing
                                particleNumber[newRow][newColumn] -= 1;
                                particleNumber[row][column] += 1;
                            }
                            particleNumber[newRow][newColumn] += 1;
                            particleNumber[row][column] -= 1;

                        } else {
                            //right neighbor
                            if (probability > 0.85) {
                                newRow = row;
                                newColumn = column + 1;
                            }
                            //left
                            else if (probability > 0.68 && probability <= 0.85) {
                                newRow = row;
                                newColumn = column - 1;
                            }
                            //up neighbor
                            else if (probability > 0.51 && probability <= 0.68) {
                                newRow = row - 1;
                                newColumn = column;
                            }
                            //upper Right
                            else if (probability > 0.34 && probability <= 0.51) {
                                newRow = row - 1;
                                newColumn = column + 1;
                            }
                            //upper Left
                            else if (probability > 0.17 && probability <= 0.34) {
                                newRow = row - 1;
                                newColumn = column - 1;
                            }
                            //own
                            else if (probability <= 0.17) {
                                //do nothing
                                particleNumber[newRow][newColumn] -= 1;
                                particleNumber[row][column] += 1;
                            }
                            particleNumber[newRow][newColumn] += 1;
                            particleNumber[row][column] -= 1;

                        }
                    } else if (column == 0) { //4 ta corner case already covered
                        //right
                        if (probability > 0.85) {
                            newRow = row;
                            newColumn = column + 1;
                        }
                        //up
                        else if (probability > 0.68 && probability <= 0.85) {
                            newRow = row - 1;
                            newColumn = column;
                        }
                        //down neighbor
                        else if (probability > 0.51 && probability <= 0.68) {
                            newRow = row + 1;
                            newColumn = column;
                        }
                        //lower Right
                        else if (probability > 0.34 && probability <= 0.51) {
                            newRow = row + 1;
                            newColumn = column + 1;
                        }
                        //upper right
                        else if (probability > 0.17 && probability <= 0.34) {
                            newRow = row - 1;
                            newColumn = column + 1;
                        }
                        //own
                        else if (probability <= 0.17) {
                            //do nothing
                            particleNumber[newRow][newColumn] -= 1;
                            particleNumber[row][column] += 1;
                        }
                        particleNumber[newRow][newColumn] += 1;
                        particleNumber[row][column] -= 1;

                    } else if (column == size - 1) { //4 ta corner case already covered
                        //left
                        if (probability > 0.85) {
                            newRow = row;
                            newColumn = column - 1;
                        }
                        //up
                        else if (probability > 0.68 && probability <= 0.85) {
                            newRow = row - 1;
                            newColumn = column;
                        }
                        //down neighbor
                        else if (probability > 0.51 && probability <= 0.68) {
                            newRow = row + 1;
                            newColumn = column;
                        }
                        //lower left
                        else if (probability > 0.34 && probability <= 0.51) {
                            newRow = row + 1;
                            newColumn = column - 1;
                        }
                        //upper Left
                        else if (probability > 0.17 && probability <= 0.34) {
                            newRow = row - 1;
                            newColumn = column - 1;
                        }
                        //own
                        else if (probability <= 0.17) {
                            //do nothing
                            particleNumber[newRow][newColumn] -= 1;
                            particleNumber[row][column] += 1;
                        }
                        particleNumber[newRow][newColumn] += 1;
                        particleNumber[row][column] -= 1;

                    } else {
                        //right neighbor
                        if (probability > 0.88) {
                            newRow = row;
                            newColumn = column + 1;
                        }
                        //left
                        else if (probability > 0.77 && probability <= 0.88) {
                            newRow = row;
                            newColumn = column - 1;
                        }
                        //up neighbor
                        else if (probability > 0.66 && probability <= 0.77) {
                            newRow = row - 1;
                            newColumn = column;
                        }
                        //down neighbor
                        else if (probability > 0.55 && probability <= 0.66) {
                            newRow = row + 1;
                            newColumn = column;
                        }
                        //lower right neighbor
                        else if (probability > 0.44 && probability <= 0.55) {
                            newRow = row + 1;
                            newColumn = column + 1;
                        }
                        //lower left neighbor
                        else if (probability > 0.33 && probability <= 0.44) {
                            newRow = row + 1;
                            newColumn = column - 1;
                        }
                        //upper Right
                        else if (probability > 0.22 && probability <= 0.33) {
                            newRow = row - 1;
                            newColumn = column + 1;
                        }
                        //upper Left
                        else if (probability > 0.11 && probability <= 0.22) {
                            newRow = row - 1;
                            newColumn = column - 1;
                        }
                        //own
                        else if (probability <= 0.11) {
                            //do nothing
                            particleNumber[newRow][newColumn] -= 1;
                            particleNumber[row][column] += 1;
                        }
                        particleNumber[newRow][newColumn] += 1;
                        particleNumber[row][column] -= 1;

                    }


                }
            }
        }

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                board[i][j] = particleNumber[i][j];
            }
        }

    }

    public static int recalculateBasedOnSensor(int sensedRow, int sensedColumn){

        int reduced = 0;
        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                int tempGhostRow = i;
                int tempGhostColumn = j;

                String color = colorOfCell(sensedRow, sensedColumn, tempGhostRow, tempGhostColumn);
                if(!color.equalsIgnoreCase(sensedColor)){
                    reduced += board[i][j];
                    board[i][j] = 0;
                }
            }
        }

        return reduced;
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



}

class ComparatorClass{
    String key;
    int val;

    ComparatorClass(String key, int val){
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}

class QueueComparator implements Comparator<ComparatorClass> {

    @Override
    public int compare(ComparatorClass o1, ComparatorClass o2) {
        //ascending order
        return o1.getVal() > o2.getVal() ? 1 : -1;

    }
}

class QueueComparatorReverse implements Comparator<ComparatorClass> {

    @Override
    public int compare(ComparatorClass o1, ComparatorClass o2) {
        //descending order
        return o1.getVal() > o2.getVal() ? -1 : 1;

    }
}
