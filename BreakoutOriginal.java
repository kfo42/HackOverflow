/*
 * File: BreakoutOriginal.java
 * -------------------
 * Name: Katie Fo
 * Section Leader: Armin Namavari
 * 
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class BreakoutOriginal extends GraphicsProgram {

	/** Width and height of application window in pixels.  IMPORTANT NOTE:
	 * ON SOME PLATFORMS THESE CONSTANTS MAY **NOT** ACTUALLY BE THE DIMENSIONS
	 * OF THE GRAPHICS CANVAS.  Use getWidth() and getHeight() to get the 
	 * dimensions of the graphics canvas. */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board.  IMPORTANT NOTE: ON SOME PLATFORMS THESE 
	 * CONSTANTS MAY **NOT** ACTUALLY BE THE DIMENSIONS OF THE GRAPHICS
	 * CANVAS.  Use getWidth() and getHeight() to get the dimensions of
	 * the graphics canvas. */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 40;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
			(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;


	/* Method: run() */
	/** Runs the Breakout program. */

	//Initializes the paddle.
	GRect paddle = new GRect (PADDLE_WIDTH, PADDLE_HEIGHT);


	//Initializes the random generator.
	private RandomGenerator rgen = RandomGenerator.getInstance();

	//Provides the set of all colors for the rows of bricks.
	private Color[] color = {Color.RED, Color.RED, 
			Color.ORANGE, Color.ORANGE,
			Color.YELLOW, Color.YELLOW, 
			Color.GREEN, Color.GREEN,
			Color.CYAN, Color.CYAN};

	private double vx = 0;
	private double vy = 2.5;
	private int lives = 0;
	private double bricksHit = 0;
	private int ballRadius = 10;


	public void run() {
		//Placing the entire game sequence within a while loop
		//allows for the game to restart when the player loses.
		while(true){
			addMouseListeners();
			paddle.setVisible(true);

			//Creates all rows of bricks simultaneously
			setUpBricks();

			//Waits for the player to click before launching the ball.
			waitForClick();

			//Initializes the ball
			GOval ball= makeBall();
			add (ball, getWidth()/2, getHeight()/2);

			//Initializes the (random) horizontal speed of the ball
			vx=rgen.nextDouble(1.0, 3.0);
			if (rgen.nextBoolean(0.5)) {
				vx = -vx;
			}	

			while(lives>0){
				//Allows the ball to reverse direction (vertically)
				//upon colliding with the walls.

				if(hitLeftWall(ball) || hitRightWall(ball)) {
					vx=-vx;
				}
				if(hitTopWall(ball) || hitBottomWall(ball)) {
					vy = -vy;
					if(hitBottomWall(ball)){

						if (lives>0){
							//Pauses between lives.
							pause(500);
							lives-=1;
						}

					}

				}

				double prevPadX=paddle.getX();
				// Updates the position of the ball.

				ball.move(vx, vy);
				pause(7);

				//Finds the location of the paddle.
				double newPadX=paddle.getX();

				//Finds the location of the ball.
				double x = ball.getX();
				double y = ball.getY();


				//Finds the magnitude of the paddle's current speed.
				double paddleSpeed = Math.sqrt((prevPadX-newPadX)*(prevPadX-newPadX));

				//Controls collisions between the ball, the paddle,
				//and the bricks.
				GObject collider = getCollidingObject(x,y);
				if (collider ==paddle){
					//The change in speed depends on the speed of 
					//the paddle.

					if (paddleSpeed<2) {
						vy = paddleSpeed*2-vy;
					}else{
						vy =-paddleSpeed/20-vy;
					}

					//The ball's motion also reverses in the x-direction if 
					//the ball collides with the side of the paddle.
					if (paddle.getY()<(ball.getY()+PADDLE_HEIGHT/2)){
						vx=-vx;
						}
					

				}else if (collider !=null ){

					if (collider != paddle){
						remove(collider);
						vy=-vy;
					}					
				}

				//Ends the game if all bricks have been hit.
				if (bricksHit==620){
					lives=0;
				}
			}


			//Clears the game for the next round (of 3 lives).
			removeAll();
			lives=0;
			bricksHit=0;
			ballRadius=10;
			paddle.setVisible(false);

		}
	}



	//Creates the MouseEvent for moving the paddle.
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = getHeight()-PADDLE_Y_OFFSET;
		int yCoin =  e.getY();
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		paddle.setLocation(x, y);
		add (paddle);
		coin.setLocation(x, yCoin);


	}

	//Creates the layout of colorful bricks.
	private void setUpBricks(){
		double brickCols = NBRICKS_PER_ROW;
		//Total number of bricks in the current row.
		double brickRows = NBRICK_ROWS;
		double midpoint = (getWidth())/2;
		double width = BRICK_WIDTH;
		double x_brick = 0;
		double y_brick = BRICK_Y_OFFSET;
		int i=0;

		while (brickRows != 0){
			while (brickCols != 0) {
				x_brick = midpoint-((width)*(brickCols/2)+(BRICK_SEP)*(brickCols/2-0.5));
				while (brickCols > 0){
					GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);

					brick.setFilled(true);
					brick.setFillColor(color[i]);
					brick.setColor(color[i]);
					add (brick, x_brick, y_brick);
					brickCols = brickCols-1 ;	
					x_brick = x_brick + width+BRICK_SEP;

				}
			}
			brickCols = NBRICKS_PER_ROW;
			brickRows += -1;
			y_brick = y_brick+BRICK_HEIGHT+BRICK_SEP;
			i++;
		}
	}

	//Determines whether the ball should bounce off of each wall.
	private boolean hitBottomWall(GOval ball) {
		return ball.getY() > getHeight() - ball.getHeight();
	}

	private boolean hitTopWall(GOval ball) {
		return ball.getY() <= 0;
	}

	private boolean hitRightWall(GOval ball) {
		return ball.getX() >= getWidth() - ball.getWidth();
	}

	private boolean hitLeftWall(GOval ball) {
		return ball.getX() <= 0;
	}

	//Initializes  the ball.
	public GOval makeBall() {
		double size = ballRadius * 2;
		GOval ball = new GOval(size, size);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		return ball;
	}


	//Determines whether the ball collides with another object
	//at any of its four "corners."
	private GObject getCollidingObject(double x, double y){
		if (getElementAt (x,y) !=null){
			GObject collider = getElementAt (x,y);
			return collider;
		}else if (getElementAt (x+2*ballRadius,y) !=null){
			GObject collider = getElementAt (x+2*ballRadius,y);
			return collider;
		}else if (getElementAt (x,y+2*ballRadius) !=null){
			GObject collider = getElementAt (x,y+2*ballRadius);
			return collider;
		}else if (getElementAt (x+2*ballRadius,y+2*ballRadius) !=null){
			GObject collider = getElementAt (x+2*ballRadius,y+2*ballRadius);
			return collider;
		}else{
			return null;
		}
	}



}
