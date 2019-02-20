/******************************************************************************
 *  Compilation:  javac BlockBounce.java
 *  Execution:    java BlockBounce N -d
 *  Dependencies: StdDraw.java
 *
 *  Implementation of a 2-d bouncing ball in the box from (-xScale,-yScale) to
 *  (xScale, yScale).
 *  Same program but cleaned-up from the Princeton version.
 ******************************************************************************/

import java.util.ArrayList;
public class BlockBounce {
    // Constants                                                                                                     
    private final static double xScale = 10.0;
    private final static double yScale = 10.0;
    private final static double border = 0.2;
 
    // These variables are shared across all methods                                                                 
    private static double rx; // x-position of the ball                                                              
    private static double ry; // y-position of the ball                                                              
    private static double radius; // of the ball 
 
    private static double vx; // x-velocity                                                                          
    private static double vy; // y-velocity 
	private static boolean[][] grid = new boolean[20][20]; // tracks what type of block is filled
	private static ArrayList<Double> trueBlocksX = new ArrayList<Double>(); // keeps track of the x coordinate for filled blocks
	private static ArrayList<Double> trueBlocksY = new ArrayList<Double>(); // keeps track of the y coordinate for filled blocks
	private static boolean flag = false;
	private static boolean quit = true;
 
    private static void setup() {
        // prepare the canvas                                                                                        
        // set the scale of the coordinate system                                                                    
        StdDraw.setXscale(-xScale-border, xScale+border);
        StdDraw.setYscale(-yScale-border, yScale+border);
        StdDraw.enableDoubleBuffering();   // smooths the animation
        // initial values of the animnation                                                                          
        rx = 0.480;             // ball position                                                                     
        ry = 0.860;
        vx = 0.015 * xScale;    // velocity                                                                          
        vy = 0.023 * yScale;
        radius = xScale / 20;   // radius                                                                            
    }
    private static void updateBallPosition() {
        // bounce off wall according to law of elastic collision                                                     
	 if ((Math.abs(rx + vx) > xScale - radius)) vx = -vx;
        if (Math.abs(ry + vy) > yScale - radius) vy = -vy;
		// detect block 
		detectBlock(); // detection method is called
        // update position                                                                                           
        rx = rx + vx;
        ry = ry + vy;
    }
	private static void detectBlock(){ // Detects if a ball is near a box and manipulates the direction of the ball upon detection
		for(int i = 0; i < trueBlocksX.size(); i++){ 
			if((((trueBlocksX.get(i) + 1.5*radius) > (rx + vx)) && (trueBlocksX.get(i) < (rx + vx))) && // detects right side of box
			((trueBlocksY.get(i) + 1.5*radius) > (ry + vy) && (trueBlocksY.get(i) - 1.5*radius) < (ry + vy))){
				vx = -vx;
				if(flag){
					disappear(i);
					return;
				}
			}
			if((((trueBlocksX.get(i) - 1.5*radius) < (rx + vx)) && (trueBlocksX.get(i) > (rx + vx))) && // detects left side of box
			((trueBlocksY.get(i) + 1.5*radius) > (ry + vy) && (trueBlocksY.get(i) - 1.5*radius) < (ry + vy))){
				vx = -vx;
				if(flag){
					disappear(i);
					return;
				}
			}
			if((((trueBlocksY.get(i) + 1.5*radius) > (ry + vy)) && (trueBlocksY.get(i) < (ry + vy))) && // detects top of box
			((trueBlocksX.get(i) + 1.5*radius) > (rx + vx) && (trueBlocksX.get(i) - 1.5*radius) < (rx + vx))){
				vy = -vy;
				if(flag){
					disappear(i);
					return;
				}
			}
			if((((trueBlocksY.get(i) - 1.5*radius) < (ry + vy)) && (trueBlocksY.get(i) > (ry + vy))) && // detects bottom of box
			((trueBlocksX.get(i) + 1.5*radius) > (rx + vx) && (trueBlocksX.get(i) - 1.5*radius) < (rx + vx))){
				vy = -vy;
				if(flag){
					disappear(i);
					return;
				}
			}
			
		}	
	}

	private static void disappear(int i){ // When flagged the program causes a box to disappear upon detection
		if(flag){
			int m = (int)Math.rint(trueBlocksX.get(i)/(2*radius) +xScale - 2.5*border);
			int n = (int)Math.rint(trueBlocksY.get(i)/(2*radius) +yScale - 2.5*border);
			grid[m][n] = false;
			trueBlocksX.clear();
			trueBlocksY.clear();
		}
	}
	private static void generateBlocks(int N){ 
		// createes boxes at a random location on the grid
		for(int i = 0; i < N; i++){
			int tempX = (int)(Math.random()*20);
			int tempY = (int)(Math.random()*20);
			if(grid[tempX][tempY]){
				i--;
				continue;
			}else grid[tempX][tempY] = true;
			}
		}
	private static void grid(){
		//generate a grid on top of the canvas
		int count = 0;
		for(int i = 0; i < 20; i++)
			for(int j = 0; j < 20; j++){
				if(!grid[j][i]){
				StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
				StdDraw.filledRectangle(2*radius*(j-xScale+2.5*border), 2*radius*(i-yScale+2.5*border), radius, radius);
				count++;
					if (count == 400){ // Flag for whether the squares have been cleared
					quit = false;
					StdDraw.setPenColor(StdDraw.BLUE);
					StdDraw.text(0, 0, "MISSION COMPLETE!");
					}
				}else{
					StdDraw.setPenColor(StdDraw.BLUE);
					StdDraw.filledRectangle(2*radius*(j-xScale+2.5*border), 2*radius*(i-yScale+2.5*border), radius, radius);
					trueBlocksX.add(2*radius*(j-xScale+2.5*border));
					trueBlocksY.add(2*radius*(i-yScale+2.5*border));
				}
			}
	}
    private static void repaint() {
	// clear the background                                                                                      
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        StdDraw.filledRectangle(0, 0, xScale, yScale);
        // draw ball on the screen
		grid();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledCircle(rx, ry, radius);
        StdDraw.show();
    }
    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.out.println("Usage: java BlockBounce N");
			System.out.println("Usage: java BlockBounce N -d");
            System.exit(0);
        }
		int N = 0;
		try{
			N = Integer.parseInt(args[0]);
		} catch(NumberFormatException e){
				System.out.print("Sorry, you did not enter a number.");
				System.exit(1);
		}
		if(N < 1){
			System.out.println("Sorry, you have entered a number less than 1.");
			System.exit(1);
		}
		if(args.length == 2)
			if(args[1].equals("-d")){
				flag = true;
			}else{
				System.out.print("Sorry, you did not enter flag, -d, for the second argument.");
				System.exit(1);
			}
		// canvas and initial values                                                                                 
        setup();
		generateBlocks(N);
        // main animation loop                                                                                       
        while (quit)  {
            updateBallPosition();
            repaint();
            StdDraw.pause(20);
			if(!quit){ // Flag to quit the program
				StdDraw.pause(3000);
				System.exit(1);
			}
        }
    }
}
