package GameShower;

import java.awt.GridLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JPanel;

import gameBody.twoPlayersManger;

public class TwoPlayerPlane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public twoPlayersManger  manger;
	public TwoPlayerPlane(ObjectInputStream gm_in,ObjectOutputStream gm_out){
		manger = new twoPlayersManger(gm_in,gm_out);
		setLayout(new GridLayout(1, 2));
		add(manger.another);
		add(manger.me);
	}
}
