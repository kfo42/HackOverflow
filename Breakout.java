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

	private double vx = 0;
	private double vy = 3;
	private RandomGenerator rgen = RandomGenerator.getInstance();

	public void run() {
		addMouseListeners();
		paddle.setVisible(false);
		setUpBricks();
		waitForClick();
		GOval ball= makeBall();
		add (ball, APPLICATION_WIDTH/2, APPLICATION_HEIGHT/2);
		while(true){
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;	
		if(hitLeftWall(ball) || hitRightWall(ball)) {
			vx=-vx;
		}
		if(hitTopWall(ball) || hitBottomWall(ball)) {
			vy = -vy;
		}

		// update visualization
		ball.move(vx, vy);
		double x =ball.getX();
		double y = ball.getY();
		}

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

		while (brickRows != 0){
			while (brickCols != 0) {
				x_brick = midpoint-(width*(brickCols/2));
				while (brickCols > 0){
					GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
					add (brick, x_brick, y_brick);
					brickCols = brickCols-1 ;	
					//The y-coordinate does not change, but the x-coordinate
					//increases by one brick-width for each counted brick.
					x_brick = x_brick + width+BRICK_SEP;
				}
			}
			brickCols = NBRICKS_PER_ROW;
			brickRows += -1;
			y_brick = y_brick+BRICK_HEIGHT+BRICK_SEP;
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


	/* You fill this in, along with any subsidiary methods */

}
