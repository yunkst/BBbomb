package gameBody;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import GameShower.GameTable;

//单人游戏界面
public class soloGame extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GameTable me_table = new OneGamer().table;
	public soloGame(){
		setLayout(new BorderLayout());
		add(me_table,BorderLayout.CENTER);
	}
	
}
