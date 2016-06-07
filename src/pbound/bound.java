package pbound;

import java.awt.Color;
import java.io.Serializable;

import model.ball;
import model.myVector;
import usefulFunction.CalFunction;

public abstract class bound implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public myVector[] endpoint = new myVector[2];
	double A,B,C;//直线方程的参数
	public myVector dircetor ;
	double length;
	public Color color= Color.white;
	public double getLength(){
		return length;
	}
	//重新计算边界的方程
	public void recal()
	{
		double[] equator = CalFunction.getlineEqiation(endpoint[0], endpoint[1]);
		A = equator[0];
		B = equator[1];
		C = equator [2];
	}
	
	//边界移动
	public void move(myVector d){
		endpoint[0].plusto(d);
		endpoint[1].plusto(d);
		recal();
	}
	public bound(myVector p1,myVector p2){
		dircetor = p2.diff(p1).unitization();
		endpoint[0] = p1;
		endpoint[1] = p2;
		length = p1.diff(p2).getlength();//计算长度
		recal();
	}
	//计算点到边界的距离
	public double getDistance(myVector Point){
		Double distance = Math.abs((A*Point.x+B*Point.y+C)/Math.sqrt(A*A+B*B));	
		double e1_dis =endpoint[0].pow_Distance(Point); 
		double e2_dis =endpoint[1].pow_Distance(Point); 
		//2.观察是否有有效撞击
		double inlinemod = Math.sqrt(e1_dis-distance*distance)+Math.sqrt(e2_dis-distance*distance);
		if(inlinemod<=length)//常规撞击
			return distance;
		else
		{
			e1_dis = Math.sqrt(e1_dis);
			e2_dis = Math.sqrt(e2_dis);
			return Math.min(e1_dis, e2_dis);
		}
	}
	public boolean isAvailableHit(ball checkedBall){
		return getDistance(checkedBall.location)<=checkedBall.size;
	}
	
	public boolean makeBallReflect(ball checkedBall){
		myVector ballLocation  = checkedBall.location;
		myVector planeVerDirecror = null ;//撞击边界的垂直方向向量
		
		//更准确的算法：
		//1.计算点到直线以及两个点距离
		Double distance = Math.abs((A*ballLocation.x+B*ballLocation.y+C)/Math.sqrt(A*A+B*B));	
		if (distance>checkedBall.size/2)//必然无法撞击
			return false;
		double e1_dis =endpoint[0].pow_Distance(ballLocation); 
		double e2_dis =endpoint[1].pow_Distance(ballLocation); 
		//2.观察是否有有效撞击
		double inlinemod = Math.sqrt(e1_dis-distance*distance)+Math.sqrt(e2_dis-distance*distance);
		if(inlinemod<=length)//常规撞击
			planeVerDirecror = dircetor.getVertical();
		else{
			myVector effortPoint =null;//撞击点
			e1_dis = Math.sqrt(e1_dis);
			e2_dis = Math.sqrt(e2_dis);
			if (e1_dis<=checkedBall.size/2)
				effortPoint = endpoint[0];
			else if (e2_dis<=checkedBall.size/2)
				effortPoint = endpoint[1];
			else 
				return false;
			planeVerDirecror=effortPoint.diff(ballLocation);			
		}
		//防止锁死计算
		double numb= A*checkedBall.location.x +B*checkedBall.location.y+C;
		myVector temp = endpoint[0].plus(checkedBall.direction);
		double num2 = A*temp.x+B*temp.y+C;
		if (num2*numb>0)
			return false;
		//计算垂直方向上的投影,的一个反向量
		myVector projection =planeVerDirecror.unitization().getProjection(checkedBall.direction).mul(2);
		checkedBall.direction.plusto(projection).unitizationTo();
		return true;
	}
	/*
	//检查该坐标是否标准的打击在边界上（在endpoint坐标系构成的十字空间外外认为是非标准撞击（需要证明））
	private boolean isOut(myVector location){
		myVector p1,p2;
		boolean NotNormal = true;
		if (endpoint[0].x>endpoint[1].x){
			p1 = endpoint[1];
			p2 = endpoint[0];
		}
		else{
			p1 = endpoint[0];
			p2 = endpoint[1];
		}
		if (location.x>p1.x &&location.x<p2.x)
			NotNormal = false;
		if (endpoint[0].y>endpoint[1].y){
			p1 = endpoint[1];
			p2 = endpoint[0];
		}
		else{
			p1 = endpoint[0];
			p2 = endpoint[1];
		}
		if (location.y>p1.y &&location.y<p2.y)
			NotNormal = false;
		return NotNormal;
	}
	//*/
	//球在运动到相应地点，调用周围边界的方法,成功计算返回1+，2表示要删除当前球
	public abstract int calculate(ball checkedBall);
}
