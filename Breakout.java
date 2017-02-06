/*
 * File: Breakout.java
 * -------------------
 * Name: Katie Fo
 * Section Leader: Armin Namavari
 * 
 * This file will eventually implement the game of Breakout.
 * 
 * Additions:
 * ~Title sequence
 * ~Collision sounds
 * ~A powerup that adds an extra life
 * ~Keeps score and displays the number of points and lives to the player.
 * ~The ball randomly decreases in radius with each turn, increasing difficulty.
 * ~At the end, displays win/loss and number of points attained.
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

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;


	/* Method: run() */
	/** Runs the Breakout program. */
	GRect paddle = new GRect (PADDLE_WIDTH, PADDLE_HEIGHT);
	private RandomGenerator rgen = RandomGenerator.getInstance();

	//Provides the set of all colors for the rows of bricks.
	private Color[] color = {Color.RED, Color.RED, 
			Color.ORANGE, Color.ORANGE,
			Color.YELLOW, Color.YELLOW, 
			Color.GREEN, Color.GREEN,
			Color.CYAN, Color.CYAN};

	private double vx = 0;
	private double vy = 2;
	private int lives = 3;
	private double bricksHit = 0;
	private int ballRadius = 10;


	public void run() {
		//Placing the entire game sequence within a while loop
		//allows for the game to restart when the player loses.
		while(true){
			//Produces a title sequence for Breakout.
			prepSequence();
			//The sound of the ball's bounce.
			AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");

			addMouseListeners();

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

			//Initializes the "extra life powerup" attained by
			//hitting the lives counter.
			GOval powerUp = new GOval (40, 10);
			powerUp.setFilled(true);
			powerUp.setColor(Color.YELLOW);
			add (powerUp, 200, 10);
			powerUp.sendToBack();

			while(lives>0){

				//Initializes the lives and score counters.
				GLabel points = new GLabel("Score: "+bricksHit);

				add (points, 20, PADDLE_Y_OFFSET/3);
				GLabel livesLeft = new GLabel("Lives: "+lives);

				add (livesLeft, 200, PADDLE_Y_OFFSET/3);
				//Determines what the ball does upon hitting each wall.

				if(hitLeftWall(ball) || hitRightWall(ball)) {
					vx=-vx;
					bounceClip.play(); 

				}
				if(hitTopWall(ball) || hitBottomWall(ball)) {
					vy = -vy;
					bounceClip.play();
					if(hitBottomWall(ball)){

						if (lives>0){
							//Randomizes the reduction in the size of the ball's radius,
							//which increases difficulty with each life lost.
							ballRadius-=rgen.nextDouble(1.0, 3.0);
							ball.setSize(ballRadius*2, ballRadius*2);

							//Displays a "TRY AGAIN" message after losing a life.
							GLabel tryAgain = new GLabel ("TRY AGAIN");
							tryAgain.setVisible(true);
							tryAgain.setFont("Courier New-Bold-40");
							add (tryAgain, getWidth()/2-tryAgain.getWidth()/2, getHeight()/2);

							//Pauses between lives.
							pause(500);
							remove(tryAgain);
							lives-=1;
						}

					}

				}

				double prevX=ball.getX();
				double prevY=ball.getY();
				// Updates the position of the ball.
				ball.move(vx, vy);
				pause(5);
				double x = ball.getX();
				double y = ball.getY();

				double paddleSpeed = prevX-x;

				//Controls collisions between the ball, the paddle,
				//and the bricks.
				GObject collider = getCollidingObject(x,y);
				if (collider ==paddle){

					//The change in speed depends on the speed of 
					//the paddle, but whether this is an increase or 
					//decrease is randomized.
					if (rgen.nextBoolean(0.5)) {
						vy = -(paddleSpeed/10)-vy;
					}else{
						vy =(paddleSpeed/10)-vy;
					}
				}
					//The ball's motion also reverses in the x-direction if 
					//the ball collides with the side of the paddle.
					if (collider.getY()<(ball.getY()+2*ballRadius)){
						if (rgen.nextBoolean(0.5)) {
							vx = -(paddleSpeed/10)-vx;
						}else{
							vx =(paddleSpeed/10)-vx;
						}
					bounceClip.play();
				}else if (collider == livesLeft || collider == points ){
					//Nothing occurs if the ball hits the text.

				}else if (collider ==powerUp){
					//The powerup is awarded.
					lives+=1;
					remove(powerUp);
					GLabel extraLife = new GLabel ("EXTRA LIFE!");
					extraLife.setVisible(true);
					extraLife.setFont("Courier New-Bold-40");
					add (extraLife, getWidth()/2-extraLife.getWidth()/2, getHeight()/2);

				}else if (collider !=null ){

					if (collider != paddle){
						remove(collider);
						vy=-vy;
						bounceClip.play();
						//Updates the number of points depending on
						//the color of brick hit.
						bricksHit=colorPoints(collider, bricksHit);

					}					
				}
				//Clears the number of points and lives so that they can be updated.
				remove(points);
				remove(livesLeft);
			}
			//Displays the win/loss and final points to the player.
			endSequence(bricksHit);

			//Clears the game for the next round (of 3 lives).
			removeAll();
			lives=3;
			bricksHit=0;
			ballRadius=10;
			paddle.setVisible(false);
			}
		}
	
	//Determines how many points are awarded for each brick color.
	public double colorPoints(GObject collider, double bricksHit){
		if (collider.getColor() == Color.CYAN){
			bricksHit+=1;
		}
		if (collider.getColor() == Color.GREEN){
			bricksHit+=2;
		}
		if (collider.getColor() == Color.YELLOW){
			bricksHit+=4;
		}
		if (collider.getColor() == Color.ORANGE){
			bricksHit+=8;
		}
		if (collider.getColor() == Color.RED){
			bricksHit+=16;
		}
		return bricksHit;

	}

	//Creates the MouseEvent for moving the paddle.
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = getHeight()-PADDLE_Y_OFFSET;
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		paddle.setVisible(true);
		paddle.setLocation(x, y);
		add (paddle);

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

	//Creates the title sequence for Breakout.
	private void prepSequence(){
		GLabel start = new GLabel ("BREAKOUT!");
		start.setVisible(true);
		start.setFont("Courier New-Bold-60");
		start.setColor(Color.BLUE);
		add (start, getWidth()/2-start.getWidth()/2, getHeight()/2-50);
		GLabel reminder = new GLabel ("Click anywhere to begin");
		reminder.setFont("Courier New-Bold-20");
		add (reminder, getWidth()/2-reminder.getWidth()/2, getHeight()/2+50);
		waitForClick();
		remove(start);
		remove(reminder);
		pause(800);
		GLabel ready = new GLabel ("READY...");
		ready.setFont("Courier New-Bold-60");
		add (ready, getWidth()/2-ready.getWidth()/2, getHeight()/2+50);
		pause(800);
		remove(ready);
		GLabel set = new GLabel ("SET...");
		set.setFont("Courier New-Bold-60");
		add (set, getWidth()/2-set.getWidth()/2, getHeight()/2+50);
		pause(600);
		remove(set);
		GLabel go = new GLabel ("GO!");
		go.setFont("Courier New-Bold-60");
		add (go, getWidth()/2-go.getWidth()/2, getHeight()/2+50);
		pause(500);
		remove(go);

	}

	private void endSequence(double bricksHit){
		GLabel end = new GLabel ("GAME OVER");
		end.setVisible(true);
		end.setFont("Courier New-Bold-40");
		add (end, getWidth()/2-end.getWidth()/2, getHeight()/2);
		GLabel total = new GLabel ("Points: " + bricksHit+" / 620");
		total.setFont("Courier New-Bold-20");
		add (total, getWidth()/2-total.getWidth()/2, getHeight()/2+200);
		pause(800);
		remove(total);

		//The game is won only if all bricks are cleared, which amounts to
		//620 total points.
		if (bricksHit==620){
			GLabel win = new GLabel ("YOU WON!");
			win.setVisible(true);
			win.setFont("Courier New-Bold-40");
			win.setColor(Color.GREEN);
			add (win, getWidth()/2-win.getWidth()/2, getHeight()/2+200);
		}else{
			GLabel loss = new GLabel ("YOU LOST!");
			loss.setVisible(true);
			loss.setFont("Courier New-Bold-40");
			loss.setColor(Color.RED);
			add (loss, getWidth()/2-loss.getWidth()/2, getHeight()/2+200);
		}
	}
}
