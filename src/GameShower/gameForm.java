package GameShower;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JFrame;

import gameBody.soloGame;
import model.gamerInfo;



//游戏界面
public class gameForm extends JFrame {
	public gamerInfo me=new gamerInfo();
	public ServerSocket me_server ;
	public static final String cN_start = "start";
	public static final String cN_solo = "solo";
	public static final String cN_internet = "internet";
	public static final String cN_gameHall = "gameHall";
	public static final String cN_gamerInfo = "gamerInfo";
	public static final String cN_TwoPlayer = "TwoPlayer";
	//选择显示卡片
	public void showCard(String CardName){
		((CardLayout)getContentPane().getLayout()).show(getContentPane(), CardName);
	}
	
	/**
	 * 
	 */
	public void addc(Container jp1,Component component, GridBagConstraints gbc,int gridwidth,int gridheight, int weightx,int weighty,int gridx,int gridy) {
		//此方法用来添加控件到容器中
		gbc.gridwidth=gridwidth;		//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
		gbc.gridheight=gridheight;		//该方法是设置组件垂直所占用的格子数
		gbc.weightx=weightx;				//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		gbc.weighty=weighty;				//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		gbc.gridx=gridx;
		gbc.gridy=gridy;
		gbc.fill=GridBagConstraints.BOTH;
		jp1.add(component,gbc);
	}
	private static final long serialVersionUID = 1L;
	public GameTable me_table ;
	public GameTable opp_table;
	
	public gameForm(){
		try {
			me_server = new ServerSocket(0);
			me.Port=me_server.getLocalPort();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Container contentPane =  getContentPane();
		contentPane.setLayout(new CardLayout());
		contentPane.add(cN_start, new StartShow(this));
		contentPane.add(cN_solo, new soloGame());
		contentPane.add(cN_gameHall,new gameHall(this));
		contentPane.add(cN_gamerInfo,new GamerInfoPage(this));
		setSize(570,700);
		setVisible(true);
	}
	
	//建立服务器连接
	public void connectServer(){
		try {
			((gameHall)(getContentPane().getComponent(2))).connectServer();
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	//关闭服务器连接
	public void CloseConnect(){
		
	}
}
