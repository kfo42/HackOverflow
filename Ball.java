import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Ball extends GraphicsProgram{
	private GOval ballImg;
	private Color ballColor;
	
	public Ball (Color color, double x, double y) {
		ballColor=color;
		ballImg = new GOval(10,10);
		ballImg.setColor(ballColor);
		ballImg.setVisible(true);
		ballImg.setLocation(x, y);
	}
	public GOval getBall(){
		return ballImg;
	}

}