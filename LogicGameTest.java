import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class LogicGameTest extends GraphicsProgram {

	/* How many ms to pause between "heartbeats" */
	private static final int DELAY = 30;
	/* How much to reduce velocity after a collision */
	private static final double DAMPING = 0.85;
	/* Initial speed in the x direction */
	private static final double INITIAL_VY = -1;
	/* Initial speed in the y direction */
	private static final double INITIAL_VX = 1;
	/* The amount of acceleration in the y direction */
	private static final double DELTA_VY = 0.05;
	/* The size of the ball (in pixels) */
	private static final int BALL_RADIUS = 10;
	/* The color of the ball */
	private static final Color BALL_COLOR = Color.BLUE;
	/* The damping from the force of static friction */
	private static final double STATIC_FRICTION = 0.05;
	/* The damping from the force of friction */
	private static final double FRICTION = 0.999;

	private RandomGenerator rgen = RandomGenerator.getInstance();

	private double[] vx = new double[300];
	private double[] vy = new double[300];

	private GRect bucket = new GRect (50,10);
	private GRect bucketFrame = new GRect( 60, 20);
	private double blueHits = 1;
	private double greenHits = 1;

	public void run() {	
		Ball[] ball = new Ball[200];
		
		add(bucketFrame,getWidth()/2-bucketFrame.getWidth()/2, getHeight()/2-bucketFrame.getHeight()/2);
 
		bucket.setFillColor(Color.CYAN);
		add(bucket, getWidth()/2-bucket.getWidth()/2, getHeight()/2-bucket.getHeight()/2);

		for(int j=0; j<ball.length; j++ ){
			
			vy[j] = rgen.nextDouble(1.0, 3.0);
			if (j<100){
				vx[j] = rgen.nextDouble(1.0,3.0);
				ball[j] = new Ball(BALL_COLOR, rgen.nextDouble(100, getWidth()/2), rgen.nextDouble(0, getHeight()/4));
			}else{
				vx[j] = -1*Math.abs(rgen.nextDouble(1.0,3.0));
				ball[j] = new Ball(Color.GREEN, rgen.nextDouble(getWidth()/2, getWidth()-100), rgen.nextDouble(0, getHeight()/4));	
			}
			add(ball[j].getBall());	
		}


		waitForClick();

		while(greenHits<400&blueHits<400) {

			for(int i=0; i<ball.length; i++){


				// update visualization
				move(ball[i], vx[i], vy[i]);


				if(getCollidingObject(ball[i].getBall().getX(), ball[i].getBall().getY())==bucket){
					if(ball[i].getBall().getColor()==Color.BLUE){
						blueHits++;
					}else{
						greenHits++;
					}
					
					remove(ball[i].getBall());
					
					if ((bucket.getHeight()+.2)<getHeight()){
						bucket.setSize(bucket.getWidth(), bucket.getHeight()+.2);
						if ((getHeight()-(bucket.getHeight()+.2))/2>0){
							bucket.setLocation(getWidth()/2-bucket.getWidth()/2, (getHeight()/2-(bucket.getHeight()+.2)));
						}
					}
				}


				vy[i] += DELTA_VY;
				// update parameters
				if(hitLeftWall(ball[i], vx[i]) || hitRightWall(ball[i], vx[i])) {
					vx[i] = -(vx[i] * DAMPING);
				}
				if(hitTopWall(ball[i], vy[i])) {
					vy[i] = -(vy[i] * DAMPING);
				}
				if(hitBottomWall(ball[i], vy[i])) {
					vy[i] = -(vy[i] * DAMPING);
					// if the ball is rolling on the ground.
					if(Math.abs(vy[i]) < 0.5) {
						vy[i] = 0.0;
						vx[i] = vx[i] * FRICTION;
						if(Math.abs(vx[i]) < STATIC_FRICTION) {
							vx[i] = 0.0;
						}
					}
				}

				if (blueHits>400 && greenHits>400){
					removeAll();
				}
			}
			// pause
			pause(DELAY);

		}

		pause(100);
		removeAll();
		GImage magicKey = new GImage("key.jpg");
		magicKey.setSize(getWidth(), getHeight());
		add(magicKey, getWidth()/2-magicKey.getWidth()/2,getHeight()/2-magicKey.getHeight()/2);
		pause(100);
		
		GLabel winText = new GLabel("You have unlocked AND");
		winText.setFont("Comic Sans MS-30");
		add(winText, getWidth()/2, getHeight()*3/4);
	

	}



	private GObject getCollidingObject(double x, double y){
		if (getElementAt (x,y) !=null){
			GObject collider = getElementAt (x,y);
			return collider;
		}else if (getElementAt (x*BALL_RADIUS,y) !=null){
			GObject collider = getElementAt (x+2*BALL_RADIUS,y);
			return collider;
		}else if (getElementAt (x,y+BALL_RADIUS) !=null){
			GObject collider = getElementAt (x,y+2*BALL_RADIUS);
			return collider;
		}else if (getElementAt (x+BALL_RADIUS,y+BALL_RADIUS) !=null){
			GObject collider = getElementAt (x+BALL_RADIUS,y+BALL_RADIUS);
			return collider;
		}else{
			return null;
		}
	}


	public void move(Ball ball, double vx, double vy){
		ball.getBall().move(vx, vy);
	}

	/**
	 * Method: Hit Bottom Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the bottom wall of the window.
	 */
	public boolean hitBottomWall(Ball ball, double vy) {
		if(vy < 0) return false;
		return ball.getBall().getY() > getHeight() - ball.getBall().getHeight();
	}

	/**
	 * Method: Hit Top Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the top wall of the window.
	 */
	public boolean hitTopWall(Ball ball, double vy) {
		if(vy > 0) return false;
		return ball.getBall().getY() <= 0;
	}

	/**
	 * Method: Hit Right Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the right wall of the window.
	 */
	public boolean hitRightWall(Ball ball, double vx) {
		if(vx < 0) return false;
		return ball.getBall().getX() >= getWidth() - ball.getBall().getWidth();
	}

	/**
	 * Method: Hit Left Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the left wall of the window.
	 */
	public boolean hitLeftWall(Ball ball, double vx) {
		if(vx > 0) return false;
		return ball.getBall().getX() <= 0;
	}




}


