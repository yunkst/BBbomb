package model;
import java.awt.Color;
import java.io.Serializable;
import java.util.Vector;

import gameBody.action;
import pbound.bound;
import pbound.brickbound;
//砖块模型
public class brick implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Color color;
	//剩余撞击数  TODO 在减少到0的时候触发消失事件
	public int live=0;
	public Vector<bound> bounds = new Vector<>();
	
	public int x,y;
	public action oGameLogical=null;
	
	//点映射
	public Vector <myVector> points = new Vector<>();
	int[] xpts=null;
	int[] ypts=null;
	double mapValue;
	boolean refresh = true;
	private void cutlive() {
		if (live--==0)
			return;
		color = new Color(255-live/10%240, 255-live*2%255, 255-live%255);
		bounds.get(0).color=color;
		bounds.get(1).color=color;
		bounds.get(2).color=color;
		bounds.get(3).color=color;
	}
	
	//砖块受到了撞击
	public void bitted()
	{
		cutlive();
		if (live <=0){//此时砖块失效，
			//1.在map中取消注册
			oGameLogical._gamemap.removeBrick(this);
		}
		oGameLogical.hittedBallCount++; 
	}
	public brick(){}
	//根据左上角坐标进行初始化
	public brick(myVector leftTop,int liveTime){
		x=(int)(leftTop.x+gameMap.brickWidthSize/2);
		y=(int)(leftTop.y+gameMap.brickHeightSize/2);
		
		myVector p1 = leftTop;
		myVector p2 = leftTop.plus(new myVector(gameMap.brickWidthSize,0));
		myVector p3 = leftTop.plus(new myVector(gameMap.brickWidthSize,gameMap.brickHeightSize));
		myVector p4 = leftTop.plus(new myVector(0,gameMap.brickHeightSize));
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		
		bounds.clear();
		bounds.add(new brickbound(p1,p2,this)) ;
		bounds.add(new brickbound(p2,p3,this)) ;
		bounds.add(new brickbound(p3,p4,this)) ;
		bounds.add(new brickbound(p4,p1,this)) ;
		
		live = liveTime+1;
		cutlive();
	}
	//砖块移动
	public void move(myVector director){
		for (myVector p : points) {
			p.plusto(director);
		}
		for (bound bound : bounds) {
			bound.recal();;
		}
		x+=director.x;
		y+=director.y;
		
	}
	public void setBounds(Vector<bound> bounds)
	{
		this.bounds =bounds;
		x = 0;y= 0;
		for (bound bound : bounds) {
			for (myVector vector : bound.endpoint) {
				points.add(vector);
				x+=vector.x;
				y+= vector.y;
			}
		}
		x /=bounds.size();
		y/=bounds.size();
	}
	public int[] get_xpts(double mapvalue){
		if (mapvalue==this.mapValue&&!refresh)
			return xpts;
		xpts = new int[points.size()];
		int i=0;
		for (myVector point :points) {
			xpts[i++] =(int)(point.x*mapvalue);
		}
		
		return xpts;
	}
	public int[] get_ypts(double mapvalue){
		if (mapvalue==this.mapValue&&!refresh)
			return xpts;
		ypts = new int[points.size()];
		int i=0;
		for (myVector point :points) {
			ypts[i++] =(int)(point.y*mapvalue);
		}
		return xpts;
	}
}
