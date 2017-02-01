/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

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
	private static final int PADDLE_WIDTH = 60;
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
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;


	/* Method: run() */
	/** Runs the Breakout program. */
	GRect paddle = new GRect (PADDLE_WIDTH, PADDLE_HEIGHT);
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private Color[] color = {Color.RED, Color.RED, 
			Color.ORANGE, Color.ORANGE,
			Color.YELLOW, Color.YELLOW, 
			Color.GREEN, Color.GREEN,
			Color.CYAN, Color.CYAN};

	private double vx = 0;
	private double vy = 3;
	private int lives = 5;

	public void run() {
		AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");

		addMouseListeners();
		//Prevents the paddle from leaving a "trail"
		paddle.setVisible(false);

		//Creates all rows of bricks
		setUpBricks();

		waitForClick();
		//Initializes the ball
		GOval ball= makeBall();
		add (ball, getWidth()/2, getHeight()/2);

		//Initializes the horizontal speed of the ball
		vx=rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}	

		while(lives>0){
			int bricksRemaining = NBRICKS_PER_ROW*NBRICK_ROWS;
			if(hitLeftWall(ball) || hitRightWall(ball)) {
				vx=-vx;
				bounceClip.play();

			}
			if(hitTopWall(ball) || hitBottomWall(ball)) {
				vy = -vy;
				bounceClip.play();

			}
			if(hitBottomWall(ball)){
				lives+=-1;
				bounceClip.play();
				if (lives>0){
					GLabel tryAgain = new GLabel ("TRY AGAIN");
					tryAgain.setVisible(true);
					tryAgain.setFont("Courier New-Bold-40");
					add (tryAgain, getWidth()/2-tryAgain.getWidth()/2, getHeight()/2);
					pause(500);
					remove(tryAgain);
				}


			}

			// update visualization
			ball.move(vx, vy);
			pause(6);
			double x = ball.getX();
			double y = ball.getY();
			GObject collider = getCollidingObject(x,y);
			if (collider ==paddle){
				vy = -vy;
				bounceClip.play();

			}else if (collider != null){
				vy=-vy;
				bounceClip.play();

				remove(collider);
				bricksRemaining +=-1;
			}

		}
		GLabel end = new GLabel ("GAME OVER");
		end.setVisible(true);
		end.setFont("Courier New-Bold-40");
		add (end, getWidth()/2-end.getWidth()/2, getHeight()/2);


	}




	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = getHeight()-PADDLE_Y_OFFSET;
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		paddle.setVisible(true);
		paddle.setLocation(x, y);
		add (paddle);

	}


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
				x_brick = midpoint-((width)*(brickCols/2)+(BRICK_SEP)*(brickCols/2-4/5));
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
	private boolean hitBottomWall(GOval ball) {
		return ball.getY() > getHeight() - ball.getHeight();
	}

	/**
	 * Method: Hit Top Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the top wall of the window.
	 */
	private boolean hitTopWall(GOval ball) {
		return ball.getY() <= 0;
	}

	/**
	 * Method: Hit Right Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the right wall of the window.
	 */
	private boolean hitRightWall(GOval ball) {
		return ball.getX() >= getWidth() - ball.getWidth();
	}

	/**
	 * Method: Hit Left Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the left wall of the window.
	 */
	private boolean hitLeftWall(GOval ball) {
		return ball.getX() <= 0;
	}

	/**
	 * Method: Make Ball
	 * -----------------------
	 * Creates a ball, adds it to the screen, and returns it so
	 * that the ball can be used for animation.
	 */
	public GOval makeBall() {
		double size = BALL_RADIUS * 2;
		GOval ball = new GOval(size, size);
		ball.setFilled(true);
		ball.setColor(Color.BLACK);
		return ball;
	}



	private GObject getCollidingObject(double x, double y){
		if (getElementAt (x,y) !=null){
			GObject collider = getElementAt (x,y);
			return collider;
		}else if (getElementAt (x+2*BALL_RADIUS,y) !=null){
			GObject collider = getElementAt (x+2*BALL_RADIUS,y);
			return collider;
		}else if (getElementAt (x,y+2*BALL_RADIUS) !=null){
			GObject collider = getElementAt (x,y+2*BALL_RADIUS);
			return collider;
		}else if (getElementAt (x+2*BALL_RADIUS,y+2*BALL_RADIUS) !=null){
			GObject collider = getElementAt (x+2*BALL_RADIUS,y+2*BALL_RADIUS);
			return collider;
		}else{
			return null;
		}
	}

}
