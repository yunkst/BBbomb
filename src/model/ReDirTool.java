package model;


import gameBody.action;
import pbound.toolBound;

public class ReDirTool extends gameTool {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double toolSize=5;
	public ReDirTool(action a, gameMap m,myVector location) {
		super(a, m);
		myVector[] p = new myVector[4];
		p[0]=location.plus(new myVector(0,-toolSize));
		p[1]=location.plus(new myVector(0,toolSize));
		p[2]=location.plus(new myVector(toolSize,0));
		p[3]=location.plus(new myVector(-toolSize,0));
		bounds.add(new toolBound(p[0], p[1], this));
		bounds.add(new toolBound(p[0], p[2], this));
		bounds.add(new toolBound(p[0], p[3], this));
	}
	
	@Override
	public void reaction(ball checkedBall) {
		// TODO Auto-generated method stub
		double rand = 1-Math.random()*2;
		checkedBall.direction= new myVector(rand,-Math.sqrt(1-rand*rand));
	}

}
