import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ================================================================================================
 * Lvlr Solver
 * @author Daniel Wu
 * March 24, 2017
 * Java, Version 7+
 * ================================================================================================
 * PROBLEM DEFNITION
 * Given a particular graph, your program will find the optimal number of moves needed to merge the 
 * entire grid into a single group.
 * 
 *  
 * 
 */
public class Assignment_001 {
	
	/**main method:
	 * This procedural method is called automatically and is used to organize the calling of other
	 * methods defined in the class. This method also checks if the user wants to input another
	 * file or exit entirely.
	 * 
	 * List of Local Variables:
	 * max - Integer variable that stores the largest number in the grid
	 * minMove - Integer variable that stores the minimum amount of moves
	 * move - Integer variable that stores the current moves to compare with the minimum moves
	 * grid - 2 Dimensional array that stores the input in readFile();
	 * status - Boolean variable that checks if the user has inputed a correct file
	 * invalid - Boolean variable that checks if the user has inputed a correct input
	 * A1 - Variable that allows access to call for other methods in this class
	 * peak - 2 dimensional array that stores the position of the highest values
	 * trough - 2 dimensional array that stores the position of the lowest values
	 * master - 2 dimensional array that stores the absolute value of grid to be copied
	 * 
	 * 
	 * @param args
	 * @throws IOException
	 * @return void
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Boolean exit = true;
		while(exit){
			int max = 0;
			int minMove = 1000;
			int move = 0;
			int[][] grid;
			Boolean status = true;
			Boolean invalid = true;
			String answer = "";
			Assignment_001 A1 = new Assignment_001();
			
			do{
				grid = A1.readFile();
				if(grid != null){
					status = false;
				}
			}while(status);
			
			int[][] peak = new int[grid.length][grid.length];
			int[][] trough = new int[grid.length][grid.length];
			int[][] master = new int[grid.length][grid.length];
			long startTime = System.currentTimeMillis();
			A1.findPeakAndTrough(peak, trough, grid);
			
			for (int i = 0; i < grid.length; i++) {
			    for (int j = 0; j < grid.length; j++){
			    	master[i][j] = grid[i][j];
			    	if(grid[i][j] > max){
			    		max = grid[i][j];
			    	}
			    }
			}	

			for (int i = 0; i < grid.length; i++) {
			    for (int j = 0; j < grid.length; j++){
			    	if(peak[i][j] == 1){
			    		move =A1.solve(i,j, grid,0,grid[i][j], 0, false, max);
			    		if(move < minMove){
			    			minMove = move;
			    		}
			    		A1.renew(master, grid);
			    	}
			    	if(trough[i][j] == 1){
			    		move = A1.solve(i,j, grid,0,grid[i][j], 0, true, max);
			    		if(move < minMove){
			    			minMove = move;
			    		}
			    		A1.renew(master, grid);
			    	}
			    }
			}	
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("RUN TIME: " + totalTime + "ms");
			System.out.println("MINIMUM MOVES: " + minMove);
			System.out.println("------------------------------");
			while(invalid){
				try{
					System.out.println("WOULD YOU LIKE TO TRY ANOTHER");
					System.out.println("Y=Yes / N=No");
					answer = br.readLine();
					invalid = false;
					if(answer.equals("Y")){
						exit = true;
					}else if(answer.equals("N")){
						exit = false;
					}else{
						invalid = true;
					}
				}catch(IOException ex) {
					invalid = true;
		            System.out.println("INVALID INPUT");
		            System.out.println("------------------------------");
		        }
			}
		}
	}//end main method
	
	
	/**solve method:
	 * This functional method takes in 8 inputs to solve for the amount of moves in the grid
	 * 
	 * List of Local Variables
	 * breaker - Boolean that stores true or false and checks if the grid values are constant
	 * bridged - Integer Value that adds on the recursive call that indicates an additional move was made
	 * a - Integer value that keeps track if val should increase or decrease by 1
	 * og - Integer value that stores the original val before being increment or decreased
	 * 
	 * 
	 * @param i - <int> holds y position of element
	 * @param j - <int> holds x position of element
	 * @param grid - <int[][]> holds a 2 dimensional array that has the full input
	 * @param step - <int> holds the amount of steps in this call
	 * @param val - <int> holds the current value to check for
	 * @param back - <int> holds the previous number to prevent back tracking
	 * @param dir - <Boolean> holds true or false to indicate to increase or decrease of val
	 * @param max - <int> holds the largest value in grid
	 * @return int - returns the amount of steps
	 */
	public int solve(int i, int j, int [][]grid, int step, int val, int back, Boolean dir, int max){
		Boolean breaker = false;
		int bridged = 0;
		print(grid);
		for (int row = 1; row < grid.length-1; row++){
	        for (int col = 1; col < grid[row].length-1; col++){
	            if(grid[1][1] != grid[row][col]){
	            	breaker = true;
	            	break;
	            }
	        }
	    }
		if(breaker == false){
			return 0;
		}else{
			int a = 0;
			if(val == max){
				dir = false;
			}
			if(val == 1){
				dir = true;
			}
			if(dir == true){
				a = 1;
			}else{
				a = -1;
			}
			int og = val;
			if(bridge(grid, val, dir) == true){
				step++;
				System.out.println(step);
				print(grid);
				bridged = 1;
			};
			val += a;
			System.out.println(step+1);
			search(i,j, grid, step, val, back, og);
			step++;
			return (1 + bridged +solve(i,j, grid, step, val, back, dir, max));
		}
	}// end solve method
	
	/**search method:
	 * This procedural method is the main brain that calculates the connections in groups and
	 * when incremented does the entire group
	 * 
	 * List of Local Variables
	 * none
	 * 
	 * @param i - <int> holds y position of element
	 * @param j - <int> holds x position of element
	 * @param grid - <int[][]> holds a 2 dimensional array that has the full input
	 * @param step - <int> holds the amount of steps in this call
	 * @param val - <int> holds the current value to check for
	 * @param back - <int> holds the previous number to prevent back tracking
	 * @param og - <int> holds the original value before being incremented or decremented
	 * @return void
	 */
	public void search(int i, int j, int [][]grid, int step, int val, int back, int og){
		grid[i][j] = val;
		if(grid[i+1][j] == og && back != 2){
			grid[i+1][j] = val;
			search(i+1, j, grid,step,val, 1, og);
		}
		if(grid[i-1][j] == og && back != 1){
			grid[i-1][j] = val;
			search(i-1, j, grid,step,val, 2, og);
		}
		if(grid[i][j+1] == og && back != 4){
			grid[i][j+1] = val;
			search(i, j+1, grid,step,val, 3, og);
		}
		if(grid[i][j-1] == og && back != 3){
			grid[i][j-1] = val;
			search(i, j-1, grid,step,val, 4, og);
		}	
	}// end of search method
	
	/**bridge method:
	 * functional method that checks if there is a possible bridge in the current state of the
	 * board
	 * 
	 * List of Variables
	 * copy - 2 dimensional array that stores a copy of grid without the current value
	 * reference - 2 dimensional array that stores a copy of grid with the current value
	 * 
	 * @param grid - <int[][]> 2 dimensional array that holds the current board state
	 * @param val - <int> integer that holds the current value to check for 
	 * @param dir- <int> integer that determines whether to increment or decrement
	 * @return Boolean - returns true if bridge is found
	 */
	
	public Boolean bridge(int [][]grid, int val, Boolean dir){
		int[][] copy = new int[grid.length][grid.length];
		int[][] reference = new int[grid.length][grid.length];
		for(int i = 1; i < grid.length-1; i++){
			for (int j = 1; j < grid.length-1; j++){
				if(grid[i][j] != val && dir == false){
					copy[i][j] = grid[i][j] + 1;
				}else if(grid[i][j] != val && dir == true){
					copy[i][j] = grid[i][j] - 1;
				}else{
					reference [i][j] = 1;
				}
				
		    }
		}
		groupify(reference, 1);
		for(int i = 1; i < grid.length-1; i++){
			for (int j = 1; j < grid.length-1; j++){
				if(copy[i][j] == val){
		        	int[][] blank = new int[grid.length][grid.length];
		        	blank[i][j]=1;
					if(bridgeMagic(i,j, grid, val, 0, dir, blank, reference) == true){;
						System.out.println("BRIDGE!" + i + " " + j);
						grid[i][j] = val;
						print(grid);
						return true;
					}
				}	
		    }
		}
		return false;	
	}// end of bridge method
	
	/**bridgeMagic method
	 * Functional method that searches through groups and looks for connections between those 
	 * groups
	 * 
	 * List of Variables
	 * a - Integer value that keeps track if val should increase or decrease by 1
	 * 
	 * @param i - <int> integer that holds the y position of element in the grid
	 * @param j - <int> integer that holds the x position of element in the grid
	 * @param grid - <int[][]> 2 dimensional array that holds the current board state
	 * @param val - <int> integer that holds the current value to check for 
	 * @param counter - <int> integer that holds the amount of occurrences of connected groups
	 * @param dir - <int> integer that determines whether to increment or decrement
	 * @param blank - <int[][]> 2 dimensional array that holds a blank state of grid for back track 
	 * @param reference - <int[][]> 2 dimensional array that stores a copy of grid with the current value
	 * @return Boolean - returns if true if a bridge has been found and is connected
	 */
	public Boolean bridgeMagic(int i, int j, int [][]grid, int val, int counter, Boolean dir, int [][]blank, int [][]reference){
		int group = 2;
		int a=0;
		if(dir == true){
			a = 1;
		}else{
			a = -1;
		}
		if(grid[i+1][j] == val && reference[i+1][j] == group){
			group++;
			counter++;
		}
		if(grid[i-1][j] == val && reference[i-1][j] == group){
			group++;
			counter++;
		}
		if(grid[i][j+1] == val && reference[i][j+1] == group){
			group++;
			counter++;
		}
		if(grid[i][j-1] == val && reference[i][j-1] == group){
			group++;
			counter++;
		}
		if(counter == 2){
			return true;
		}else{
			if(grid[i+1][j] == val+a && blank[i+1][j] != 1){
				blank[i+1][j] = 1;
				bridgeMagic(i+1, j, grid, val, counter, dir, blank, reference);
			}
			if(grid[i-1][j] == val+a && blank[i-1][j] != 1){
				blank[i-1][j] = 1;
				bridgeMagic(i-1, j, grid, val, counter, dir, blank, reference);
			}
			if(grid[i][j+1] == val+a && blank[i][j+1] != 1){
				blank[i][j+1] = 1;
				bridgeMagic(i, j+1, grid, val, counter, dir, blank, reference);
			}
			if(grid[i][j-1] == val+a && blank[i][j-1] != 1){
				blank[i][j-1] = 1;
				bridgeMagic(i, j-1, grid, val, counter, dir, blank, reference);
			}
			return false;
		}
	}//end bridgeMagic method
	
	/**print method
	 * this procedural method prints out a 2 dimensional array in a readable format
	 * List of Variables
	 * none
	 * 
	 * @param array - <int[][]> 2 dimensional array that holds a sequence
	 * @return void
	 */
	public void print(int [][] array){
		for (int i = 0; i < array.length; i++) {
		    for (int j = 0; j < array.length; j++){
		        System.out.print(array[i][j]);
		    }
		    System.out.println();
		} 	
		System.out.println();
	}//end of print method
	
	/**findPeakAndTrough method
	 * this procedural method finds the highest and lowest values in the given sequence
	 * 
	 * List of Variables
	 * minValue - this integer holds the minimum value in the sequence
	 * maxValue - this integer holds the maximum value in the sequence
	 * 
	 * @param peak - <int[][]> 2 dimensional array that holds the position of the highest values
	 * @param trough - <int[][]> 2 dimensional array that holds the position of the lowest values
	 * @param grid - <int[][]> 2 dimensional array that holds the current board state
	 */
	public void findPeakAndTrough (int [][] peak, int [][]trough, int [][] grid){
		int minValue = grid[1][1];
		int maxValue = 0;
		for (int i = 0; i < grid.length; i++) {
		    for (int j = 0; j < grid.length; j++){
		        if (grid[i][j] > maxValue) {
		            maxValue = grid[i][j];
		        }
		        if (grid[i][j] < minValue && grid[i][j] != 0) {
		            minValue = grid[i][j];
		        }
		    }
		}	
		for (int i = 0; i < grid.length; i++) {
		    for (int j = 0; j < grid.length; j++){
		        if(grid[i][j] == minValue){
		        	trough[i][j] =1;
		        }
		        if(grid[i][j] == maxValue){
		        	peak[i][j] = 1;
		        }
		    }
		}
		groupify(peak, 0);
		groupify(trough, 0);
	}//end of findPeakAndTrough method
	
	/**groupify method
	 * this procedural method sets up to find groups and set them together.
	 * 
	 * List of Variables
	 * none
	 * 
	 * @param raw - <int[][]> 2 dimensional array that holds a temporary sequence of an array 
	 * @param mode - <int> integer that holds which mode to group them as
	 */
	public void groupify(int [][] raw, int mode){
		for (int i = 0; i < raw.length; i++) {
		    for (int j = 0; j < raw.length; j++){
		        if(raw[i][j] == 1){
		        	int[][] blank = new int[raw.length][raw.length];
		        	blank[i][j]=1;
		        	if(mode == 0){
		        		groupifyMagic(i,j, raw, blank, mode);
		        	}else{
		        		raw[i][j] = mode;
		        		groupifyMagic(i,j, raw, blank, mode);
		        		mode++;
		        	}
		        }
		    }
		}	
	}//end of groupify method
	
	/**groupifyMagic method
	 * this procedural method traverses through the raw array to identify groups
	 * 
	 * List of Variables
	 * none
	 * 
	 * @param i - <int> integer that holds the y position of element in the grid
	 * @param j - <int> integer that holds the x position of element in the grid
	 * @param raw - <int[][]> 2 dimensional array that holds a temporary sequence of an array 
	 * @param blank - <int[][]> 2 dimensional array that holds a blank state of grid for back track 
	 * @param mode - <int> integer that holds which mode to group them as
	 */
	public void groupifyMagic(int i, int j, int [][] raw, int [][]blank, int mode){
		if(raw[i+1][j] == 1 && blank[i+1][j] != 1){
			blank[i+1][j]=1;
			groupifyMagic(i+1, j, raw, blank, mode);
			raw[i+1][j] = mode;
		}
		if(raw[i-1][j] == 1 && blank[i-1][j] != 1){
			blank[i-1][j]=1;
			groupifyMagic(i-1, j, raw, blank, mode);
			raw[i-1][j] = mode;
		}
		if(raw[i][j+1] == 1 && blank[i][j+1] != 1){
			blank[i][j+1]=1;
			groupifyMagic(i, j+1, raw, blank, mode);
			raw[i][j+1] = mode;
		}
		if(raw[i][j-1] == 1 && blank[i][j-1] != 1){
			blank[i][j-1]=1;
			groupifyMagic(i, j-1, raw, blank, mode);
			raw[i][j-1] = mode;
		}	
	}//end of groupifyMagic method
	
	/**renew method
	 * this is a procedural method that copies a sequence into another 2 dimensional array to create
	 * a identical copy 
	 * 
	 * List of Variables
	 * none
	 * 
	 * @param master - <int[][]> 2 dimensional array that holds the original board state
	 * @param grid - <int[][]> 2 dimensional array that holds the current board state
	 */
	public void renew(int [][]master, int [][]grid){
		for (int i = 0; i < grid.length; i++) {
		    for (int j = 0; j < grid.length; j++){
		    	grid[i][j] = master[i][j];
		    }
		}	
	}//end of renew method
	
	/**readFile method
	 * this functional method reads a text file and converts it into a grid
	 * 
	 * List of Variables
	 * fileName - String variable that stores the file name
	 * line - String variable that stores the current characters of the specific line in the file
	 * lineNumb - Integer that stores the current line being processed
	 * br - BufferedReader that allows access to input
	 * fileReader - FileReader that allows access to reading text file
	 * bufferedReader - BufferedReader that allows access to input file
	 * parts - array that splits the current line text by character
	 * arr - 2 dimensional array that stores the sequence from the parts
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @return int[][] - returns a 2 dimensional array with the input sequence
	 */
	public int[][] readFile() throws IOException{
		String fileName ="";
        String line = null;
        int lineNumb = 1;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("PLEASE ENTER THE FILE NAME...");
		System.out.println("ex: temp.txt");
		fileName = fileName + br.readLine();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            String[] parts = line.split("");
            int [][] arr = new int [parts.length+2][parts.length+2];
            for(int i = 0; i < parts.length; i++){
            	arr[lineNumb][i+1] = Integer.parseInt(parts[i]);
            }
            while((line = bufferedReader.readLine()) != null) {
            	lineNumb++;
            	parts = line.split("");
            	for(int n = 0; n < parts.length; n++) {
            	   arr[lineNumb][n+1] = Integer.parseInt(parts[n]);
            	}
            }   
            bufferedReader.close();
            return arr;
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");         
            System.out.println("------------------------------");
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");  
            System.out.println("------------------------------");
        }
		return null;
	}//end of readFile method
}//end of Assignment_001 class