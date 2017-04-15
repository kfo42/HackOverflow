import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class LogicGameTest extends GraphicsProgram {

		/* How many ms to pause between "heartbeats" */
		private static final int DELAY = 2;
		/* How much to reduce velocity after a collision */
		private static final double DAMPING = 0.85;
		/* Initial speed in the x direction */
		private static final double INITIAL_VY = 1;
		/* Initial speed in the y direction */
		private static final double INITIAL_VX = 2;
		/* The amount of acceleration in the y direction */
		private static final double DELTA_VY = 0.05;
		/* The size of the ball (in pixels) */
		private static final int BALL_RADIUS = 15;
		/* The color of the ball */
		private static final Color BALL_COLOR = Color.BLUE;
		/* The damping from the force of static friction */
		private static final double STATIC_FRICTION = 0.05;
		/* The damping from the force of friction */
		private static final double FRICTION = 0.999;

		private Ball ball = new Ball(BALL_COLOR);
		
		public void run() {	
			
			waitForClick();
			makeBall(ball);
			double vx = INITIAL_VX;
			double vy = INITIAL_VY;
			while(true) {
				// update visualization
				ball.move(vx, vy);
				vy += DELTA_VY;

				// update parameters
				if(ball.hitLeftWall(vx) || ball.hitRightWall(vx)) {
					vx = -(vx * DAMPING);
				}
				if(ball.hitTopWall(vy)) {
					vy = -(vy * DAMPING);
				}
				if(ball.hitBottomWall(vy)) {
					vy = -(vy * DAMPING);
					// if the ball is rolling on the ground.
					if(Math.abs(vy) < 0.5) {
						vy = 0.0;
						vx = vx * FRICTION;
						if(Math.abs(vx) < STATIC_FRICTION) {
							vx = 0.0;
						}
					}
				}

				// pause
				pause(DELAY);
			}
		}

		/**
		 * Method: Make Ball
		 * -----------------------
		 * Creates a ball, adds it to the screen, and returns it so
		 * that the ball can be used for animation.
		 */
		public void makeBall(Ball ball) {
			ball.addBall();
		}

	}


