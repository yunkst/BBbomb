package pbound;

import model.ball;
import model.gameMap;
import model.myVector;

public class startBound extends bound {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ball ejector = null;
	public boolean ejectorFleshed = false;
	gameMap _owner =null;
	public startBound(myVector p1, myVector p2,gameMap owner) {
		super(p1, p2);
		_owner = owner;
	}
	public startBound() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public bound getTransClone(){
		return TransClone(new startBound()) ;
	}
	@Override
	public int calculate(ball checkedBall) {
		if (!isAvailableHit(checkedBall))
			return 0;
		_owner.vailableBall.remove(checkedBall);
		if (ejectorFleshed)
			return 2;
		ejector.location.x =checkedBall.location.x;
		ejectorFleshed = true;
		return 2;
	}

}
