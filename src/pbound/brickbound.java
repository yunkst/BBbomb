package pbound;

import model.ball;
import model.brick;
import model.myVector;

public class brickbound extends bound {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public brickbound(myVector p1, myVector p2,brick owner) {
		super(p1, p2);
		this.owner = owner;
	}
	public brickbound(myVector p1, myVector p2) {
		super(p1, p2);
	}
	public brickbound(){
		
	}
	@Override
	public bound getTransClone(){
		return TransClone(new brickbound()) ;
	}
	//拥有这个边界的砖块
	public brick owner = null;
	@Override
	public int calculate(ball checkedBall) {
		if (!makeBallReflect(checkedBall))
			return 0;
		if (owner!=null)
			owner.bitted();
		return 1;
	}
	

}
