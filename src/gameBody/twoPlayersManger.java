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
import transferInfo.Transfer_Action;
//������ҵ���Ϸ�����߼�
public class twoPlayersManger implements gameManger{
	//TODO: ��һ����ʱ������ʱ����_action���ݣ��������ܵ���
	public GameTable me = new GameTable();
	public GameTable another= new GameTable();
	ObjectInputStream gm_in;
	ObjectOutputStream gm_out;
	public int nextBrickCount=-1;//��һ�����ӵ�ש����Ŀ
	public twoPlayersManger(ObjectInputStream gm_in,ObjectOutputStream gm_out) {
		this.gm_in= gm_in;
		this.gm_out = gm_out;
		
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
				//��������
				try {
					gm_out.writeObject(me._aAction.getPack());
					gm_out.flush();
					another._aAction.freshByPack((Transfer_Action)gm_in.readObject()); 
					another.paint(another.getGraphics());
					
					if (another._aAction.isOver()&&me._aAction.isOver()){
						wait(1000);
						if (another._aAction.isFail()&&!me._aAction.isFail())
							JOptionPane.showMessageDialog(null, "ʤ��","���",JOptionPane.OK_OPTION);
						else if (!another._aAction.isFail()&&me._aAction.isFail())
							JOptionPane.showMessageDialog(null, "ʧ��","���",JOptionPane.OK_OPTION);
						else if (another._aAction.isFail()&&me._aAction.isFail()){
							JOptionPane.showMessageDialog(null, "ƽ��","���",JOptionPane.OK_OPTION);
						}
						nextBrickCount = (int)(another._aAction.hittedBallCount*1.5);
						newTurn();
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "����");
					System.exit(0);
					timer.cancel();
				}
			}
		}, 10, 10);
	}
	private void newTurn() {
		me._aAction.nextBrickCount=nextBrickCount;
		me._aAction.hittedBallCount = 0;
		if(!me._aAction.TurnGo())
			JOptionPane.showMessageDialog(null, "ʧ��");
		else
			me.started = false;
	}
	@Override
	public void OneTurnOver() {
		
	//��Ϸ�����󣬷���һ���������ݣ��ܵ��Է�������֮�󣬸���
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
