package gameBody;

import java.awt.Paint;
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import model.PlusTool;
import model.ReDirTool;
import model.ball;
import model.brick;
import model.gameMap;
import model.gameTool;
import model.myVector;
import pbound.bound;
import pbound.brickbound;
import pbound.startBound;
import transferInfo.Transfer_Action;

//ά��ģ������
public class action implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//TODO: ��û�о�����κ͵��ø�������ݽ���
	public gameMap _gamemap = new gameMap();
	double v=4;
	int randNum;
	public startBound _sStartBound;
	public double deadline = gameMap.height-gameMap.brickHeightSize;
	public int ballCount=0;//��ǰ������
	public int nextBrickCount=0;//��һ��ש����ܴ����
	public int restBallCount=0 ;//ʣ�ඪ��ȥ������
	public int hittedBallCount=0;
	private int unHittedTime = 0;
	private int lastHitted;
	private final static int maxloopTime = 1000;//������ѭ������
	//�ɴ���������Ϣ����ˢ������
	public void freshByPack(Transfer_Action pack){
		_gamemap.vailableBall = pack.vailableBall;
		_gamemap.vailableBound=pack.vailableBound;
		_gamemap.vailableBricks = pack.vailableBricks;
		_sStartBound.ejector.direction = pack.ejector.direction;
		_sStartBound.ejector.location = pack.ejector.location;
		ballCount = pack.ballCount;
		hittedBallCount = pack.hittedBallCount;
		restBallCount =pack.restBallCount;
	}
	//��ȡ��Ϣ��
	public Transfer_Action getPack(){
		Transfer_Action new_pack = new Transfer_Action();
		ball neweje = new ball();
		neweje.direction= _sStartBound.ejector.direction;
		neweje.location= _sStartBound.ejector.location;
		new_pack.ejector = neweje;
		
		for (int i =0; i<_gamemap.vailableBall.size();i++) {
			new_pack.vailableBall.add(new ball(_gamemap.vailableBall.get(i)));
		}
		for (int i =0; i<_gamemap.vailableBound.size();i++) {
			new_pack.vailableBound.add(_gamemap.vailableBound.get(i).getTransClone());
		}
		for (int i =0; i<_gamemap.vailableBricks.size();i++) {
			brick temp = new brick();
			temp.x = _gamemap.vailableBricks.get(i).x;
			temp.y = _gamemap.vailableBricks.get(i).y;
			temp.live = _gamemap.vailableBricks.get(i).live;
			temp.color = _gamemap.vailableBricks.get(i).color;
			new_pack.vailableBricks.add(temp);
		}
		new_pack.ballCount = ballCount;
		new_pack.hittedBallCount = hittedBallCount;
		new_pack.restBallCount = restBallCount;
		return new_pack;
	}
	public void launch(){
		//������Է��䣬�ͷ�������
		if (restBallCount!=0){
			restBallCount--;
			_gamemap.vailableBall.add(new ball(_sStartBound.ejector));
		}
	}
	//��ʱ��ǰ��һ����λ
	public void timeGo(){
		//�������е����λ��
		for (int i = 0;i<_gamemap.vailableBall.size();++i)
		{
			ball oneball = _gamemap.vailableBall.get(i);
			oneball.location.plusto(oneball.direction.mul(v));
		}
		//��ȡ�������п���ײ�����ı߽�,������
		for (int j = 0;j<_gamemap.vailableBall.size();j++){
			double mindis = 0;
			bound cloestBound;
			cloestBound = null;
			ball oneball = _gamemap.vailableBall.get(j);
			for (int i = 0 ;i<_gamemap.vailableBound.size();i++)
			{
				bound nowBound = _gamemap.vailableBound.get(i);
				double distance ;
				distance = nowBound.getDistance(oneball.location);
				if (distance<=oneball.size)
				{
					if (cloestBound==null)
					{
						mindis = distance;
						cloestBound = nowBound;
					}
					else {
						if (mindis>distance){
							cloestBound = nowBound;
							mindis = distance;
						}
					}
				}
			}
			if (cloestBound!=null)
				if (cloestBound.calculate(oneball)==2)
					j--;//��ɾ��һ��
		}
		if (lastHitted == hittedBallCount)
		{
			if(++unHittedTime>maxloopTime)
			{	
				if(_gamemap.vailableBall.size()>0)
					_gamemap.addTool(new ReDirTool(this, _gamemap, _gamemap.vailableBall.get(0).location));
				unHittedTime=0;
			}
		}
		else
		{
			lastHitted = hittedBallCount;
			unHittedTime=0;
		}
	}
	//�ûغ�ǰ�� ������false��ʾ��Ϸ����
	public boolean TurnGo()
	{
		//1.ש���ƶ�һ����λ
		for (brick aBrick : _gamemap.vailableBricks) {
			aBrick.move(new myVector(0,gameMap.brickHeightSize));
			if (aBrick.y>deadline-gameMap.brickHeightSize)
				return false;
		}
		//�ƶ�����
		for(int i = 0; i<_gamemap.vailableTool.size();++i){
			gameTool atool = _gamemap.vailableTool.get(i);
			atool.move(new myVector(0,gameMap.brickHeightSize));
			if (atool.bounds.get(0).endpoint[0].y>deadline-gameMap.brickHeightSize)
			{
				_gamemap.removeTool(atool);	
				i--;
			}
		}
		//2.������ש��
		makeItems();
		//ɾ���ض����
		for (int i = 0;i<_gamemap.vailableTool.size();){
			if (_gamemap.vailableTool.get(i) instanceof ReDirTool)
				_gamemap.removeTool(_gamemap.vailableTool.get(i));
			else
				i++;
		}
		//3.ˢ�¸���״̬
		restBallCount=ballCount;
		_sStartBound.ejectorFleshed = false;
		lastHitted = 0;
		unHittedTime = 0;
		return true;
	}
	
	//��ʼ����ͼ
	public void iniTheMap()
	{
		_gamemap.clear();
		//1.��ӱ߽�͵���
		bound b1= new brickbound(new myVector(0,0),new myVector(0,gameMap.height));
		bound b2 = new brickbound(new myVector(0,gameMap.height),new myVector(gameMap.width,gameMap.height));
		bound b3 = new brickbound(new myVector(gameMap.width,gameMap.height),new myVector(gameMap.width,0));
		bound b4 = new brickbound(new myVector(gameMap.width,0),new myVector(0,0));
	    _gamemap.addBound(b1);
	    _gamemap.addBound(b2);
	    _gamemap.addBound(b3);
	    _gamemap.addBound(b4);
	    _sStartBound = new startBound(new myVector(0,deadline),new myVector(gameMap.width,deadline),_gamemap);
	    _gamemap.addBound(_sStartBound);
	    
	    randNum =Math.abs((int)(new Date().getTime())%10000);
	    ballCount = 1;
	    restBallCount = 1;
	    nextBrickCount = 3;
	    _sStartBound.ejector = new ball();
	    _sStartBound.ejector.location.x=gameMap.width/2;
	    _sStartBound.ejector.location.y=deadline-30;
	    _sStartBound.ejector.size = 30;
	    _sStartBound.ejector.direction.x= 1;
	    _sStartBound.ejector.direction.y= 0;
	    
	    makeItems();
	}
	//����ש��͵���
	public void makeItems(){
		//1.��ȡ���Է��õ�λ�ø���
		int space =(int)(gameMap.width/gameMap.brickWidthSize);
		int randspace = (space+1)*space/2;
		int randseed = (randNum+nextBrickCount+ballCount)%randspace;
		randspace = 0;
		int usefulSpaceCount = 1;//���õ�����ռ���
		for (int i = 1;i<=space ;i++)
		{
			randspace+=i;
			if (randseed<=randspace){
				usefulSpaceCount= i;
				break;
				}
		}
		int[] definedSpace=new int[usefulSpaceCount];//ȷ���İ��ÿռ�λ��
	    for(int i = 0;i<usefulSpaceCount;++i){
	    	int temp;
	    	temp = randseed%space+1;
	    	int j ;
	    	for (j = 0;j<definedSpace.length;j++)//��֤һ���Ƿ��غ�
	    	{
	    		if (definedSpace[j]==temp)
	    			break;
	    	}
	    	if (j<definedSpace.length){
    			i--;
    	    	randseed*=37;
    	    	randseed++;
    			continue;
    		}
	    		
	    	definedSpace[i] = temp;
	    	randseed*=37;
	    	randseed++;
	    	randseed%=9813654;
	    }
	    int setlive = nextBrickCount/usefulSpaceCount+1;
		//����ש��
	    for (int i =0;i<definedSpace.length;++i) {
	    	if (nextBrickCount<=0)
	    		break;
	    	brick newbrick = new brick(new myVector((definedSpace[i]-1)*gameMap.brickWidthSize,gameMap.brickHeightSize), setlive);
	    	newbrick.oGameLogical = this;
	    	_gamemap.addBrick(newbrick);
	    	nextBrickCount-=setlive;
		}
	    //�������ӵ���
	    {
		    int tp = randseed%space;
		    myVector p = new myVector((tp+1/2)*gameMap.brickWidthSize,gameMap.brickHeightSize*(3/2));
		    _gamemap.addTool(new PlusTool(this,_gamemap,p));
	    }
	    
	}
}
