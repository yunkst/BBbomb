package model;

import java.awt.Color;
import java.io.Serializable;

public class ball implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public myVector location = new myVector();
	public myVector direction = new myVector();
	public float size=20;
	public Color color = Color.white;
	
	public ball(){}
	public ball(ball ej){
		location.x = ej.location.x;
		location.y =ej.location.y;
		direction.x = ej.direction.x;
		direction.y = ej.direction.y;
	}
	public void setDirection(int x,int y)
	{
		direction = new myVector(x,y).unitization();
		
	}
}
