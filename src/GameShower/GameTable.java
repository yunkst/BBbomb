package GameShower;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import gameBody.action;
import gameBody.gameManger;
import model.ball;
import model.brick;
import model.gameMap;
import model.myVector;
import pbound.bound;
//���������Ϸ��
public class GameTable extends JPanel{

	private static final long serialVersionUID = 1L;

	public gameMap _mGameMap ;
	public action _aAction=new action();
	private Graphics offScreenBuffer = null;
	private Image offScreenImage = null;
	public double widthmap=1,heightmap=1;//����ӳ��
	public gameManger manger;
	
	
	public GameTable()
	{
		synchronized (_aAction) {
		_aAction.iniTheMap();
		_mGameMap=_aAction._gamemap;
		}
		addComponentListener(new ComponentAdapter(){
			@Override public void componentResized(ComponentEvent e){
				int width = getWidth();
				int height = getHeight();
				offScreenImage = createImage(width,height);
				offScreenBuffer = offScreenImage.getGraphics();
				widthmap = (double)(width)/gameMap.width;
				heightmap =(double)(height)/gameMap.height;
			}});
	}
	
	public boolean started = false;
	public void startHitAction(){
		if (started)
			return;
		started = true;
		Timer timer1=new Timer();
		Timer timer2 = new Timer();
		
		timer1.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				synchronized (_aAction) {
					_aAction.timeGo();
					paint(getGraphics());
					if (_aAction._gamemap.vailableBall.size()+_aAction.restBallCount<=0){
						timer1.cancel();
						manger.OneTurnOver();
						paint(getGraphics());
					}	
				}
			}
		}, 0, 10);
		timer2.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				synchronized (_aAction) {
					_aAction.launch();
					if (_aAction.restBallCount<=0)
						timer2.cancel();
				}	
			}
				
		}, 0, 80);
	}
	public boolean nextTurn(int nextBrickCount){
		synchronized (_aAction) {
			_aAction.nextBrickCount=nextBrickCount;
			return _aAction.TurnGo();	
		}
	}
	
	public void paint(Graphics g){
		synchronized (_aAction) {
			if(offScreenBuffer==null)
				return;
			offScreenBuffer.setColor(Color.black);
			offScreenBuffer.fillRect(0, 0, getWidth(), getHeight());
			_mGameMap = _aAction._gamemap;
			if (_mGameMap!=null)
				drawTable(offScreenBuffer);
			g.drawImage(offScreenImage, 0, 0, this);	
		}
	}
	
	public void drawString(Graphics g, String str, int xPos, int yPos) {
        int strWidth = g.getFontMetrics().stringWidth(str);
        g.drawString(str, xPos - strWidth / 2, yPos);
}
	private void drawTable(Graphics gc)
	{
		gc.setFont(new Font("����",Font.BOLD, 20));
	
		//1.����ש��
		for (int i = 0;i<_mGameMap.vailableBricks.size();++i){
			brick abrick = _mGameMap.vailableBricks.get(i);
			gc.setColor(abrick.color);
			drawString(gc,String.valueOf(abrick.live) , getx(abrick.x), gety(abrick.y));
		}

		//2.���Ʊ߽�

		for (int i = 0;i<_mGameMap.vailableBound.size();++i){
			bound bound = _mGameMap.vailableBound.get(i);
			gc.setColor(bound.color);
			gc.drawLine(getx(bound.endpoint[0].x),gety(bound.endpoint[0].y),
					    getx(bound.endpoint[1].x),gety(bound.endpoint[1].y));;
		
		}
		//3.������
		for (int i = 0;i<_mGameMap.vailableBall.size();++i)
		{
			ball oneball = _mGameMap.vailableBall.get(i);
			drawBall(gc, oneball);
		}
		{
			ball aEjector; 
				//4 ����������ƣ�������
				aEjector =_aAction._sStartBound.ejector; 
			drawBall(gc,aEjector);
			//���Ƽ�ͷ
			myVector p1 = aEjector.location.plus(aEjector.direction.mul(aEjector.size/2));
			myVector p2 = aEjector.location.plus(aEjector.direction.mul(-aEjector.size/2));
			gc.setColor(Color.blue);
			gc.fillOval(getx(p2.x-aEjector.size/4), gety(p2.y-aEjector.size/4), getx(aEjector.size/2),gety(aEjector.size/2));
			gc.drawLine(getx(p1.x), gety(p1.y), getx(p2.x), gety(p2.y));
			
			//��������
			gc.setColor(Color.gray);
			//gc.fillOval(getx(aEjector.location.x),gety(aEjector.location.y), 10, 10);
				gc.drawString(String.valueOf(_aAction.restBallCount),getx(aEjector.location.x+aEjector.size/2),
																	gety(aEjector.location.y+aEjector.size/2));
				gc.setFont(new Font("����", Font.BOLD, 35));
				gc.drawString("�������"+String.valueOf(_aAction.hittedBallCount),0, getHeight());
		}
	}
	private void drawBall(Graphics gc,ball ball)
	{
		gc.setColor(ball.color);
		gc.fillOval(getx(ball.location.x - ball.size/2),
				gety(ball.location.y-ball.size/2),
				getx (ball.size),gety( ball.size));
	
	}
	//����ӳ����x����
	private int getx(double w){
		return (int)(w*widthmap);
	}
	//����ӳ����x����
		private int gety(double y){
			return (int)(y*heightmap);
		}
}
