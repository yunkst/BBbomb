package transferInfo;

import java.util.Vector;

import model.ball;
import model.brick;
import model.gameTool;
import pbound.bound;
import pbound.startBound;

//action��ͼ��Ҫ����Ϣ��
public class Transfer_Action {
	public int ballCount;//��ǰ������
	public int restBallCount ;//ʣ�ඪ��ȥ������
	public int hittedBallCount;
	public ball ejector;
	
	public Vector<brick>vailableBricks =new Vector<>(); 
	public Vector<ball> vailableBall=new Vector<>();
	public Vector<bound> vailableBound=new Vector<>();
	
}
