package model;
import java.io.Serializable;
import java.lang.Math;
public class myVector implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double x;
	public double y;
	public myVector(){};
	public myVector(double x,double y){
		this.x = x;
		this.y = y;
	}
	public myVector(myVector v){
		this.x = v.x;
		this.y = v.y;
	}
	//��λ��
	public myVector unitization()
	{
		return mul(1/getlength());
	}
	public myVector unitizationTo()
	{
		return multo(1/getlength());
	}
	
	@Override
	public boolean equals(Object other){
		if (!(other instanceof myVector))
			return false;
		myVector another = (myVector) other;
		if (x==another.x)
			if (y == another.y)
				return true;
		return false;
		
	}
	@Override
	public int hashCode(){
		return (int)(gameMap.width*y+x);
	}
	
	//���������֮��ľ���
	public double distance(myVector another){
		return Math.sqrt(pow_Distance(another));
	}
	//�����ƽ��
	public double pow_Distance(myVector another){
		double x1 = x - another.x ,y1 = y-another.y;
		return x1*x1+y1*y1;
	}
	public myVector plus (myVector another){
		myVector res = new myVector();
		res.x = x +  another.x;
		res.y = y +  another.y;
		return res;
	}
	//�ڵ�ǰ�����ϼ���һ����
	public myVector plusto(myVector another){
		x  +=  another.x;
		y  += another.y;
		return this;
	}

	public myVector diff(myVector another){
		myVector res = new myVector();
		res.x = x -  another.x;
		res.y = y -  another.y;
		return res;
	}
	public double mul(myVector another){
		return x*another.x+y*another.y;
	}
	
	//��ȡ��������������ǰ�����ϵ�ͶӰ
	public myVector getProjection(myVector another){
		double m = this.mul(another);
		double m1=m/Math.sqrt(x*x+y*y);
		
		return this.mul(-m1);
	}
	
	public double getlength()
	{
		//TODO: �����и��Ż������Խ����ֵ���棬����Ҫ��֤x,yֵû�з����任
		return Math.sqrt(x*x+y*y);
	}
	
	public myVector mul(double sclar) {
		myVector res = new myVector();
		res.x = sclar *x;
		res.y = sclar *y;
		return res;
	}
	public myVector multo(double sclar) {
		x *= sclar ;
		y *= sclar ;
		return this;
	}
	public myVector getVertical(){
		myVector res = new myVector();
		if (x==0){
			if (y == 0)
				return null;//�޷�����0����
			res.y=0;
			res.x=1;
		}
		else if (y==0){
			res.x=0;
			res.y=1;
		}
		else {
			if (Math.abs(x ) >Math.abs(y)){
				res.x=1;
				res.y = -x/y; 
			}else{
				res.y= 1;
				res.y =-y/x; 
			}
		}
		return res;
	}
}
