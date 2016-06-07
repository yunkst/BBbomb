package gameBody;

import java.io.Serializable;
import java.util.Date;
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

//维护模型运行
public class action implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//TODO: 还没有决定如何和调用该类的数据交互
	public gameMap _gamemap = new gameMap();
	double v=4;
	int randNum;
	public startBound _sStartBound;
	public double deadline = gameMap.height-gameMap.brickHeightSize;
	public int ballCount=0;//当前的球数
	public int nextBrickCount=0;//下一组砖块的总打击数
	public int restBallCount=0 ;//剩余丢出去的球数
	public int hittedBallCount=0;
	private int unHittedTime = 0;
	private int lastHitted;
	private final static int maxloopTime = 1000;//最长允许的循环次数
	public void launch(){
		//如果可以发射，就发射新球
		if (restBallCount!=0){
			restBallCount--;
			_gamemap.vailableBall.add(new ball(_sStartBound.ejector));
		}
	}
	//让时间前进一个单位
	public void timeGo(){
		//更新所有的球的位置
		for (int i = 0;i<_gamemap.vailableBall.size();++i)
		{
			ball oneball = _gamemap.vailableBall.get(i);
			oneball.location.plusto(oneball.direction.mul(v));
		}
		//获取在网格中可能撞击到的边界,并计算
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
					j--;//球被删除一个
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
	//让回合前进 ，返回false表示游戏结束
	public boolean TurnGo()
	{
		//1.砖块移动一个单位
		for (brick aBrick : _gamemap.vailableBricks) {
			aBrick.move(new myVector(0,gameMap.brickHeightSize));
			if (aBrick.y>deadline-gameMap.brickHeightSize)
				return false;
		}
		//移动道具
		for(int i = 0; i<_gamemap.vailableTool.size();++i){
			gameTool atool = _gamemap.vailableTool.get(i);
			atool.move(new myVector(0,gameMap.brickHeightSize));
			if (atool.bounds.get(0).endpoint[0].y>deadline-gameMap.brickHeightSize)
			{
				_gamemap.removeTool(atool);	
				i--;
			}
		}
		//2.放置新砖块
		makeItems();
		//删除重定向块
		for (int i = 0;i<_gamemap.vailableTool.size();){
			if (_gamemap.vailableTool.get(i) instanceof ReDirTool)
				_gamemap.removeTool(_gamemap.vailableTool.get(i));
			else
				i++;
		}
		//3.刷新各个状态
		restBallCount=ballCount;
		_sStartBound.ejectorFleshed = false;
		lastHitted = 0;
		unHittedTime = 0;
		return true;
	}
	
	//初始化地图
	public void iniTheMap()
	{
		_gamemap.clear();
		//1.添加边界和底线
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
	//建造砖块和道具
	public void makeItems(){
		//1.获取可以放置的位置个数
		int space =(int)(gameMap.width/gameMap.brickWidthSize);
		int randspace = (space+1)*space/2;
		int randseed = (randNum+nextBrickCount+ballCount)%randspace;
		randspace = 0;
		int usefulSpaceCount = 1;//可用的随机空间数
		for (int i = 1;i<=space ;i++)
		{
			randspace+=i;
			if (randseed<=randspace){
				usefulSpaceCount= i;
				break;
				}
		}
		int[] definedSpace=new int[usefulSpaceCount];//确定的安置空间位置
	    for(int i = 0;i<usefulSpaceCount;++i){
	    	int temp;
	    	temp = randseed%space+1;
	    	int j ;
	    	for (j = 0;j<definedSpace.length;j++)//验证一下是否重合
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
		//放置砖块
	    for (int i =0;i<definedSpace.length;++i) {
	    	if (nextBrickCount<=0)
	    		break;
	    	brick newbrick = new brick(new myVector((definedSpace[i]-1)*gameMap.brickWidthSize,gameMap.brickHeightSize), setlive);
	    	newbrick.oGameLogical = this;
	    	_gamemap.addBrick(newbrick);
	    	nextBrickCount-=setlive;
		}
	    //设置增加道具
	    {
		    int tp = randseed%space;
		    myVector p = new myVector((tp+1/2)*gameMap.brickWidthSize,gameMap.brickHeightSize*(3/2));
		    _gamemap.addTool(new PlusTool(this,_gamemap,p));
	    }
	    
	}
}
