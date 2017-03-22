import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Assignment_001 {
	
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
	}
	
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
	}
	
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
	}
	
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
					if(bridgify(i,j, grid, val, 0, dir, blank, reference) == true){;
						System.out.println("BRIDGE!" + i + " " + j);
						grid[i][j] = val;
						print(grid);
						return true;
					}
				}	
		    }
		}
		return false;	
	}
	
	public Boolean bridgify(int i, int j, int [][]grid, int val, int counter, Boolean dir, int [][]blank, int [][]reference){
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
				bridgify(i+1, j, grid, val, counter, dir, blank, reference);
			}
			if(grid[i-1][j] == val+a && blank[i-1][j] != 1){
				blank[i-1][j] = 1;
				bridgify(i-1, j, grid, val, counter, dir, blank, reference);
			}
			if(grid[i][j+1] == val+a && blank[i][j+1] != 1){
				blank[i][j+1] = 1;
				bridgify(i, j+1, grid, val, counter, dir, blank, reference);
			}
			if(grid[i][j-1] == val+a && blank[i][j-1] != 1){
				blank[i][j-1] = 1;
				bridgify(i, j-1, grid, val, counter, dir, blank, reference);
			}
			return false;
		}
	}
	
	public void print(int [][] array){
		for (int i = 0; i < array.length; i++) {
		    for (int j = 0; j < array.length; j++){
		        System.out.print(array[i][j]);
		    }
		    System.out.println();
		} 	
		System.out.println();
	}
	
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
	}	
	
	public void groupify(int [][] raw, int mode){
		for (int i = 0; i < raw.length; i++) {
		    for (int j = 0; j < raw.length; j++){
		        if(raw[i][j] == 1){
		        	int[][] blank = new int[raw.length][raw.length];
		        	blank[i][j]=1;
		        	if(mode == 0){
		        		groupifyMagic(i,j, raw, 0, blank, mode);
		        	}else{
		        		raw[i][j] = mode;
		        		groupifyMagic(i,j, raw, 0, blank, mode);
		        		mode++;
		        	}
		        }
		    }
		}	
	}
	
	public void groupifyMagic(int i, int j, int [][] raw, int back, int [][]blank, int mode){
		if(raw[i+1][j] == 1 && blank[i+1][j] != 1){
			blank[i+1][j]=1;
			groupifyMagic(i+1, j, raw, 1, blank, mode);
			raw[i+1][j] = mode;
		}
		if(raw[i-1][j] == 1 && blank[i-1][j] != 1){
			blank[i-1][j]=1;
			groupifyMagic(i-1, j, raw, 2, blank, mode);
			raw[i-1][j] = mode;
		}
		if(raw[i][j+1] == 1 && blank[i][j+1] != 1){
			blank[i][j+1]=1;
			groupifyMagic(i, j+1, raw, 3, blank, mode);
			raw[i][j+1] = mode;
		}
		if(raw[i][j-1] == 1 && blank[i][j-1] != 1){
			blank[i][j-1]=1;
			groupifyMagic(i, j-1, raw, 4, blank, mode);
			raw[i][j-1] = mode;
		}	
	}		
	
	public void renew(int [][]master, int [][]grid){
		for (int i = 0; i < grid.length; i++) {
		    for (int j = 0; j < grid.length; j++){
		    	grid[i][j] = master[i][j];
		    }
		}	
	}
	
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
	}
}