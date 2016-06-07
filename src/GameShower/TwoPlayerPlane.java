package GameShower;

import java.awt.GridLayout;
import java.net.Socket;

import javax.swing.JPanel;

import gameBody.twoPlayersManger;

public class TwoPlayerPlane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public twoPlayersManger  manger;
	public TwoPlayerPlane(Socket connect){
		manger = new twoPlayersManger(connect);
		setLayout(new GridLayout(2, 1));
		add(manger.another);
		add(manger.me);
	}
}
