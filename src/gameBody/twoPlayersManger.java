package gameBody;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import GameShower.GameTable;
import model.myVector;
//两个玩家的游戏控制逻辑
public class twoPlayersManger implements gameManger{
	//TODO: 用一个定时器，定时发送_action数据，当发现受到的
	public GameTable me = new GameTable();
	public GameTable another= new GameTable();
	public Socket Connect;
	public int nextBrickCount=-1;//下一轮增加的砖块数目
	public twoPlayersManger(Socket connect) {
		try {
			connect.setTrafficClass(0x10);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.Connect = connect ;
		
		me.manger=this;
		me.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				double x = e.getX()/me.widthmap;
				double y = e.getY()/me.heightmap;
				synchronized (me._aAction) {
				myVector ej_dir= me._aAction._sStartBound.ejector.location;
				myVector temp = new myVector(x,y).diff(ej_dir);
					if(temp.y>0)
						temp.multo(-1);
					me._aAction._sStartBound.ejector.direction = temp.unitization();
				}
				me.paint(me.getGraphics());
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		me.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				me.startHitAction();
			}
		});
		startSendingInfo();
	}
	private void startSendingInfo() {
		Timer timer= new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				//接受数据
				try {
					InputStream in = Connect.getInputStream();
					if (in.available()>0){
						ObjectInputStream tInputStream = new ObjectInputStream(in);
						another._aAction=(action)tInputStream.readObject();
						another.paint(another.getGraphics());
						if (another._aAction._gamemap.vailableBall.size()+another._aAction.restBallCount==0){
							if (another._aAction.hittedBallCount==0)
								JOptionPane.showMessageDialog(null, "胜利");
							nextBrickCount = (int)(another._aAction.hittedBallCount*1.5);
							OneTurnOver();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "掉线");
					System.exit(0);
					timer.cancel();
				}
				//发送数据
				try {
					OutputStream out = Connect.getOutputStream();
					ObjectOutputStream tOutputStream = new ObjectOutputStream(out);
					tOutputStream.writeObject(another._aAction);
					another.paint(another.getGraphics());
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "掉线");
					System.exit(0);
					timer.cancel();
				}
			}
		}, 10, 50);
	}
	@Override
	public void OneTurnOver() {
		if(nextBrickCount>0&&(me._aAction.restBallCount+me._aAction.restBallCount==0))
		{
			me._aAction.nextBrickCount=nextBrickCount;
			me._aAction.hittedBallCount = 0;
			if(!me._aAction.TurnGo())
				JOptionPane.showMessageDialog(null, "失败");
			else
				me.started = false;
		}
		nextBrickCount=-1;
		//游戏结束后，发送一个结束数据，受到对方的数据之后，更新
		/*try {
			OutputStream stream = Connect.getOutputStream();
			new DataOutputStream(stream).writeInt(me._aAction.hittedBallCount);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		//*/
	}

}
