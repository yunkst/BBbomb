package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import pbound.bound;

//��ͼ1.�����¼��Чͼ��Ԫ�أ�2.��¼�߽������ӳ�䣬
public class gameMap implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Vector<brick>vailableBricks  = new Vector<brick>(); 
	public Vector<ball> vailableBall= new Vector<ball>();
	public Vector<bound> vailableBound= new Vector<bound>();
	public Vector<gameTool> vailableTool = new Vector<gameTool>();
	public static int width=570,height=700;//��ǰ��ͼ�Ĵ�С��
	public static double brickHeightSize = height/10,brickWidthSize = width/6;
	
	float griddingsize=1;
	HashMap<myVector,Vector<bound>> gridding = new HashMap<>();
	public void clear(){
		vailableBricks.clear();
		vailableBound.clear();
		vailableTool.clear();
		vailableBall.clear();
		
	}
	public void addTool(gameTool atool){
		atool._GameMap = this;
		vailableTool.add(atool);
		for (bound aBound : atool.bounds) 
			addBound(aBound);	
	}
	
	
	public void removeBrick(brick goal){
		vailableBricks.remove(goal);
		for (bound temp : goal.bounds) {
			vailableBound.remove(temp);
		}
		//freshMapGridding();
	}
	/*
	//��ȡ��ǰ������Χ�ı߽�
	public Vector<bound> mayCrash(myVector po){
		myVector standard = GetGridPoint(po);
		if (gridding.containsKey(standard)){
			return gridding.get(standard);
		}
		return null;
	}
	//*/
	
	//���һ����
	public void addBall(ball newball){
		vailableBall.add(newball);
	}
	
	//���һ��ש��
	public void addBrick(brick newbrick){
		vailableBricks.add(newbrick);
		//��ӱ߽絽������
		for (bound abound : newbrick.bounds) {
			addBound(abound);
		}
		
	}
	//��ӱ߽絽������
	public void addBound(bound newbound){
		//1.��ʼ��һ���ڵ�
		//myVector NowPoint =new myVector(newbound.endpoint[0]) ;
		//double length = 0;
		vailableBound.add(newbound);
		/*
		int singn_x = newbound.dircetor.x>=0?1:-1;
		int singn_y = newbound.dircetor.y>=0?1:-1;
		double direct_x = newbound.dircetor.x==0?1:newbound.dircetor.x;
		double direct_y = newbound.dircetor.y==0?1:newbound.dircetor.y;
		/*
		//1.��ȡ���п��ɵ�����
		for (double i =newbound.endpoint[0].x;i*singn_x<=newbound.endpoint[1].x*singn_x;i+=direct_x)
			for (double j =newbound.endpoint[0].y;j*singn_y<=newbound.endpoint[1].y*singn_y;j+=direct_y){
				myVector gridStandPoint =GetGridPoint(new myVector(i,j)); 
				if (!gridding.containsKey(gridStandPoint))
					gridding.put(gridStandPoint, new Vector<bound>());
				Vector<bound> thelist =gridding.get(gridStandPoint); 
				if (!thelist.contains(newbound))
					thelist.add(newbound);
			}
		//������һ���ڵ�
		myVector gridStandPoint =GetGridPoint(newbound.endpoint[1]); 
		if (!gridding.containsKey(gridStandPoint))
			gridding.put(gridStandPoint, new Vector<bound>());
		Vector<bound> thelist =gridding.get(gridStandPoint); 
		if (!thelist.contains(newbound))
			thelist.add(newbound);
		//*/
		
		//2.���
		/*
		do {
			myVector gridStandPoint =GetGridPoint(NowPoint); 
			if (!gridding.containsKey(gridStandPoint))
				gridding.put(gridStandPoint, new Vector<bound>());
			gridding.get(gridStandPoint).add(newbound);
			NowPoint.plusto(newbound.dircetor.mul(griddingsize));
			length+=griddingsize;
		} while (length<=newbound.getLength());
		//*/
	}
	
	public void setGriddingSzie(float size)
	{
		griddingsize = size;
		freshMapGridding();
	}
	
	public void freshMapGridding(){
		gridding.clear();
		for (bound temp : vailableBound) {
			addBound(temp);
		}
	}
	/*
	//��ȡ���������������������
	private myVector GetGridPoint(myVector point) {
		myVector res = new myVector();
		res.x = ((int)(point.x/griddingsize))*griddingsize;
		res.y = ((int)(point.y/griddingsize))*griddingsize;
		return res;
	}
	//*/
	
	public void removeTool(gameTool atool) {
		// TODO Auto-generated method stub
		vailableTool.remove(atool);
		for (bound aBound : atool.bounds) {
			vailableBound.remove(aBound);
		}
	}
}
