package transferInfo;

import java.io.Serializable;
import java.util.Vector;

import model.ball;
import model.brick;
import model.gameTool;
import pbound.bound;
import pbound.startBound;

//action绘图必要的信息包
public class Transfer_Action implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int ballCount;//当前的球数
	public int restBallCount ;//剩余丢出去的球数
	public int hittedBallCount;
	public ball ejector;
	public boolean isReadyForFresh;
	public Vector<brick>vailableBricks =new Vector<>(); 
	public Vector<ball> vailableBall=new Vector<>();
	public Vector<bound> vailableBound=new Vector<>();
	
}
