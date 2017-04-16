import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Ball extends GraphicsProgram{
	private GOval ballImg;
	private Color ballColor;
	
	public Ball (Color color) {
		ballColor=color;
		ballImg = new GOval(20,20);
		ballImg.setColor(ballColor);
		ballImg.setVisible(true);
	}
	public GOval getBall(){
		return ballImg;
	}

}