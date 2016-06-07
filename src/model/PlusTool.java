package model;

import gameBody.action;
import pbound.toolBound;

public class PlusTool  extends gameTool {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public PlusTool(action a, gameMap m,myVector leftRightLocation) {
		super(a, m);
		location = leftRightLocation.plus(new myVector(gameMap.brickWidthSize/2,gameMap.brickHeightSize/2));
		myVector[] v = new myVector[8];
		v[0] = new myVector(location.x-gameMap.brickWidthSize*toolsize_w/2,location.y-thick/2);
		v[1] = new myVector(location.x+gameMap.brickWidthSize*toolsize_w/2,location.y-thick/2);
		v[2] = new myVector(location.x+gameMap.brickWidthSize*toolsize_w/2,location.y+thick/2);
		v[3] = new myVector(location.x-gameMap.brickWidthSize*toolsize_w/2,location.y+thick/2);
		v[4] = new myVector(location.x-thick/2,location.y-gameMap.brickWidthSize*toolsize_h/2);
		v[5] = new myVector(location.x+thick/2,location.y-gameMap.brickWidthSize*toolsize_h/2);
		v[6] = new myVector(location.x+thick/2,location.y+gameMap.brickWidthSize*toolsize_h/2);
		v[7] = new myVector(location.x-thick/2,location.y+gameMap.brickWidthSize*toolsize_h/2);
		for (myVector myVector : v) {
			points.add(myVector);
		}
		toolBound[] b = new toolBound[8];
		//初始化边界
		b[0] = new toolBound(v[0],v[1], this);
		b[1] = new toolBound(v[1],v[2], this);
		b[2] = new toolBound(v[2],v[3], this);
		b[3] = new toolBound(v[3],v[0], this);
		b[4] = new toolBound(v[4],v[5], this);
		b[5] = new toolBound(v[5],v[6], this);
		b[6] = new toolBound(v[6],v[7], this);
		b[7] = new toolBound(v[7],v[4], this);
		for (toolBound bound : b) {
			bounds.add(bound);
		} 
		
	}

	myVector location;
	//相对于砖块的大小
	static final float toolsize_w =(float)(1)/4; 
	static final float toolsize_h =(float)(1)/4;
	static final float thick = 5;
	

	@Override
	public void reaction(ball checkedBall) {
		// TODO Auto-generated method stub
		_action.ballCount+=1;
		_GameMap.vailableTool.remove(this);
		for (toolBound toolBound : bounds) {
			_GameMap.vailableBound.remove(toolBound);
		}
	}
}
