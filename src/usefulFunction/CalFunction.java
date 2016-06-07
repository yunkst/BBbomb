package usefulFunction;

import model.myVector;

public class CalFunction {

	//计算两点所在直线方程
	public static double[] getlineEqiation(myVector p1,myVector p2)
	{
		double[] res = new double[3];
		res[0] = p2.y-p1.y;
		res[1] = p1.x-p2.x;
		res[2] = p1.x*(p1.y - p2.y) - p1.y*(p1.x - p2.x);
		return res;
	}
}
