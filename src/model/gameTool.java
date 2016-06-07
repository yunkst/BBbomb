package model;

import java.io.Serializable;
import java.util.Vector;

import gameBody.action;
import pbound.toolBound;

public abstract class gameTool implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Vector<toolBound> bounds = new Vector<>();
	public gameMap _GameMap ;
	public gameBody.action _action ;
	public Vector<myVector> points = new Vector<>();
	public gameTool(action a,gameMap m){
		_action = a;
		_GameMap = m;
	}
	public void move(myVector director){
		for (myVector p: points) {
			p.plusto(director);
		}
		for (toolBound toolBound : bounds) {
			toolBound.recal();
		}
	}
	
	//道具行为
	public abstract void reaction(ball checkedBall);
}
