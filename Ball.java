import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Ball extends GraphicsProgram{
	public GOval ballImg;
	
	public Ball (Color ballColor) {
		
		ballImg = new GOval(10,10);
		ballImg.setColor(ballColor);
	
	}
	public void move(double vx, double vy){
		ballImg.move(vx, vy);
	}
	/**
	 * Method: Hit Bottom Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the bottom wall of the window.
	 */
	private boolean hitBottomWall(GOval ballImg, double vy) {
		if(vy < 0) return false;
		return ballImg.getY() > getHeight() - ballImg.getHeight();
	}

	/**
	 * Method: Hit Top Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the top wall of the window.
	 */
	private boolean hitTopWall(GOval ballImg, double vy) {
		if(vy > 0) return false;
		return ballImg.getY() <= 0;
	}

	/**
	 * Method: Hit Right Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the right wall of the window.
	 */
	private boolean hitRightWall(GOval ballImg, double vx) {
		if(vx < 0) return false;
		return ballImg.getX() >= getWidth() - ballImg.getWidth();
	}

	/**
	 * Method: Hit Left Wall
	 * -----------------------
	 * Returns whether or not the given ball should bounce off
	 * of the left wall of the window.
	 */
	private boolean hitLeftWall(GOval ballImg, double vx) {
		if(vx > 0) return false;
		return ballImg.getX() <= 0;
	}

}
