package gameBody;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;

import GameShower.GameTable;
import model.myVector;

//单机游戏控制
public class OneGamer implements gameManger{
	int nextBrickCount=3;
	public GameTable table = new GameTable();
	public OneGamer() {
		table.manger=this;
		table.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				double x = e.getX()/table.widthmap;
				double y = e.getY()/table.heightmap;
				synchronized (table._aAction) {
				myVector ej_dir= table._aAction._sStartBound.ejector.location;
				myVector temp = new myVector(x,y).diff(ej_dir);
					if(temp.y>0)
						temp.multo(-1);
					table._aAction._sStartBound.ejector.direction = temp.unitization();
				}
				table.paint(table.getGraphics());
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		table.addMouseListener(new MouseListener() {
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
				table.startHitAction();
			}
		});
	}
	//游戏结束后的准备
	public void OneTurnOver()
	{
		nextBrickCount +=3;
		table._aAction.nextBrickCount = nextBrickCount;
		table._aAction.hittedBallCount=0;
		table.started = false;
		if(!table._aAction.TurnGo()){
			JOptionPane.showMessageDialog(null,"游戏结束","信息",JOptionPane.INFORMATION_MESSAGE);
			table._aAction.iniTheMap();
			nextBrickCount=6;
		}
		
	}
}
