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
	private void move(double vx, double vy){
		ballImg.move(vx, vy);
	}
}
