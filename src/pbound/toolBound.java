package pbound;

import model.gameTool;
import model.myVector;
import model.ball;

public class toolBound extends bound {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public toolBound(myVector p1, myVector p2 ,gameTool owner) {
		super(p1, p2);
		this.owner = owner;
	}
	public gameTool owner;
	public toolBound() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int calculate(ball checkedBall) {
		if (isAvailableHit(checkedBall)){
			owner.reaction(checkedBall);
			return 1;
		}
		return 0;
	}
	@Override
	public bound getTransClone(){
		return TransClone(new startBound()) ;
	}
}
