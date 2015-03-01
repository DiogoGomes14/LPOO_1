import java.util.*;


public class Main {

	public static int heroRow = 1;
	public static int heroColumn = 1;
	public static int dragonRow = 3;
	public static int dragonColumn = 1;
	public static int swordRow = 8;
	public static int swordColumn = 1;
	public static char hero = 'H';
	public static boolean dragon = true;

	public static void main(String[] args) {

		//char[][] map = mapGeneration(10);
		char[][] map = {{'X','X','X','X','X','X','X','X','X','X'},
						{'X',' ',' ',' ',' ',' ',' ',' ',' ','X'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ',' ',' ',' ',' ',' ','X',' ','S'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ','X','X',' ','X',' ','X',' ','X'},
						{'X',' ','X','X',' ',' ',' ',' ',' ','X'},
						{'X','X','X','X','X','X','X','X','X','X'}};
		mapGeneration(7);
        /*
		printMap(map,10);

		while(!(heroRow == 5 && heroColumn == 9 && hero == 'A')){
			// ask for the next player move
			newPosition(map);

			// if the player is in the same position as the sword change the 'H' to 'A'
			if(heroRow == swordRow && heroColumn == swordColumn)
				hero = 'A';

			// if the dragon is alive calculate next dragon position
			if(dragon)
				dragonMovement(map);

			// if the hero doesn't have a sword when next to the dragon print the map and end the game (die)
			if(
				((heroRow == dragonRow + 1 && heroColumn == dragonColumn)
					|| (heroRow == dragonRow - 1 && heroColumn == dragonColumn)
					|| (heroRow == dragonRow && heroColumn == dragonColumn + 1)
					|| (heroRow == dragonRow && heroColumn == dragonColumn - 1))
				&& hero != 'A')
			{
				printMap(map,10);
				break;
			}

			// if the has a sword next to the dragon, kill it (dragon = false)
			else if(
				((heroRow == dragonRow + 1 && heroColumn == dragonColumn)
					|| (heroRow == dragonRow - 1 && heroColumn == dragonColumn)
					|| (heroRow == dragonRow && heroColumn == dragonColumn + 1)
					|| (heroRow == dragonRow && heroColumn == dragonColumn - 1))
				&& hero == 'A')
			{
				dragon = false;
			}

			printMap(map,10);
		}
		System.out.println(dragon ? "You died." : "You have reached the exit");
		System.out.println("The end.");*/
	}
	
	public static void newPosition(char[][] map){
		System.out.println("Make a move (left,right,up,down):");

		char ch = getMove();
		System.out.println(ch);	
		
		switch (ch){
			case 'l':
				if(map[heroRow][heroColumn - 1] == ' ')
					heroColumn--;
				break;
			case 'r':
				if(map[heroRow][heroColumn + 1] == ' ' || (map[heroRow][heroColumn + 1] == ' ' && hero == 'A' && !dragon))
					heroColumn++;
				break;
			case 'u':
				if(map[heroRow - 1][heroColumn] == ' ')
					heroRow--;
				break;
			case 'd':
				if(map[heroRow + 1][heroColumn] == ' ')
					heroRow++;
				break;
			default:
				System.out.println("Invalid string. Type again");
				break;
		}		
	}

	public static void printMap(char[][] map, int n){
		// insert the hero on the map
		map[heroRow][heroColumn] = hero;

		// insert the sword on the map
		if(hero != 'A')
			map[swordRow][swordColumn] = 'E';

		// insert the dragon on the map
		if(dragon)
			map[dragonRow][dragonColumn] = 'D';

		// check if the dragon is in the same place as the sword and change that location to 'F'
		if(dragonRow == swordRow && dragonColumn == swordColumn)
			map[dragonRow][dragonColumn] = 'F';

		// print the map
        System.out.print("  ");
        for(int i = 0; i < n; i++){
            System.out.print(i + " ");
        }
        System.out.print('\n');
		for(int i = 0; i < n; i++){
            System.out.print(i + " ");
			for (int j = 0; j < n; j++){
				System.out.print(map[i][j] + " ");
			}
			System.out.print('\n');
		}

		// reset the board
		map[dragonRow][dragonColumn] = ' ';
		map[swordRow][swordColumn] = ' ';
		map[heroRow][heroColumn] = ' ';
	}
	
	public static char getMove(){
		char ch = '0';
		Scanner s = new Scanner(System.in);
		try {
			ch = s.findInLine("[lrud]").charAt(0);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return ch;
	}
	
	public static void dragonMovement(char[][] map){
		// generate a random number and if equivalent move made it go to the wall don't move it

		Random rand = new Random();
	    int randomNum = rand.nextInt(3);
	    
		switch (randomNum){
			case 0: // Move left
				if(map[dragonRow][dragonColumn - 1] == ' ')
					dragonColumn--;
				break;
			case 1: // Move right
				if(map[dragonRow][dragonColumn + 1] == ' ')
					dragonColumn++;
				break;
			case 2: // Move up
				if(map[dragonRow - 1][dragonColumn] == ' ')
					dragonRow--;
				break;
			case 3: // Move down
				if(map[dragonRow + 1][dragonColumn] == ' ')
					dragonRow++;
				break;
			default:
				break;
		}
	}

	public static char[][] mapGeneration(int n){
        int randomNum, visitedCellsSize = (n-1)/2;
		char map[][] = new char[n][n];
        char visitedCells[][] = new char[visitedCellsSize][visitedCellsSize];
        Stack<Integer> stack = new Stack<Integer>();
        Random rand = new Random();


        // used to fill a 2d array with odd cells empty
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i%2 == 0 || j%2 == 0){
                    map[i][j] = 'X';
                    continue;
                }
                visitedCells[(i-1)/2][(j-1)/2] = '.';
                map[i][j] = ' ';
            }
        }


        // Selecting initial position and exit
        randomNum = rand.nextInt(visitedCellsSize - 1);

        if (rand.nextInt(2) == 0){
            int y = rand.nextInt(2) * (visitedCellsSize - 1);
            visitedCells[y][randomNum] = '+';

            stack.push(randomNum);
            stack.push(y);

            map[y*2 + 1][randomNum*2 + 1] = '+';

            if (2*y == 0){
                map[0][randomNum*2 + 1] = 'S';
            }
            else {
                map[2*y + 2][randomNum*2 + 1] = 'S';
            }
        }
        else {
            int x = rand.nextInt(2) * (visitedCellsSize - 1);
            visitedCells[randomNum][x] = '+';

            stack.push(x);
            stack.push(randomNum);

            map[randomNum*2 + 1][x*2 + 1] = '+';

            if (2*x == 0){
                map[randomNum*2 + 1][0] = 'S';
            }
            else {
                map[randomNum*2 + 1][2*x + 2] = 'S';
            }
        }

        int x, y;
        boolean flag = false;
        while (!stack.empty()){
            y = stack.peek();
            stack.pop();
            x = stack.peek();
            stack.push(y);
            switch (rand.nextInt(4)){
                case 0:
                    if(visitedCells[y][x + 1] == '.'){ // bugs here repeatedly
                        visitedCells[y][x + 1] = '+';
                        map[y * 2 + 1][x * 2 + 2] = ' ';
                        flag = true;
                        stack.push(x + 1);
                        stack.push(y);
                    }
                    break;
                case 1:
                    if(visitedCells[y][x - 1] == '.'){
                        visitedCells[y][x - 1] = '+';
                        map[y * 2 + 1][x * 2] = ' ';
                        flag = true;
                        stack.push(x - 1);
                        stack.push(y);
                    }
                    break;
                case 2:
                    if(visitedCells[y + 1][x] == '.'){
                        visitedCells[y + 1][x] = '+';
                        map[y * 2 + 2][x * 2 + 1] = ' ';
                        flag = true;
                        stack.push(x);
                        stack.push(y + 1);
                    }
                    break;
                case 3:
                    if(visitedCells[y - 1][x] == '.'){
                        visitedCells[y - 1][x] = '+';
                        map[y * 2][x * 2 + 1] = ' ';
                        flag = true;
                        stack.push(x);
                        stack.push(y - 1);
                    }
                    break;
            }

            //possivelmente mudar de lugar
            if (!flag){
                continue;
            }

            // rever
            if (visitedCells[y - 1][x] == '+' && visitedCells[y + 1][x] == '+' && visitedCells[y][x + 1] == '+' && visitedCells[y][x - 1] == '+'){
                stack.pop();
                stack.pop();
            }

            // para continuar
        }

        // maze generation auxiliary prints
        System.out.print("  ");
        for(int i = 0; i < n; i++){
            System.out.print(i + " ");
        }
        System.out.print('\n');
        for(int i = 0; i < n; i++){
            System.out.print(i + " ");
            for (int j = 0; j < n; j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.print('\n');
        }

        System.out.print("\n  ");
        for(int i = 0; i < (n-1)/2; i++){
            System.out.print(i + " ");
        }
        System.out.print('\n');
        for(int i = 0; i < (n-1)/2; i++){
            System.out.print(i + " ");
            for (int j = 0; j < (n-1)/2; j++){
                System.out.print(visitedCells[i][j] + " ");
            }
            System.out.print('\n');
        }

		return map;
	}
}
