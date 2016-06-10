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



//��Ϸ����
public class gameForm extends JFrame {
	public gamerInfo me=new gamerInfo();
	public ServerSocket me_server ;
	public static final String cN_start = "start";
	public static final String cN_solo = "solo";
	public static final String cN_internet = "internet";
	public static final String cN_gameHall = "gameHall";
	public static final String cN_gamerInfo = "gamerInfo";
	public static final String cN_TwoPlayer = "TwoPlayer";
	//ѡ����ʾ��Ƭ
	public void showCard(String CardName){
		((CardLayout)getContentPane().getLayout()).show(getContentPane(), CardName);
	}
	
	/**
	 * 
	 */
	public void addc(Container jp1,Component component, GridBagConstraints gbc,int gridwidth,int gridheight, int weightx,int weighty,int gridx,int gridy) {
		//�˷���������ӿؼ���������
		gbc.gridwidth=gridwidth;		//�÷������������ˮƽ��ռ�õĸ����������Ϊ0����˵��������Ǹ��е����һ��
		gbc.gridheight=gridheight;		//�÷��������������ֱ��ռ�õĸ�����
		gbc.weightx=weightx;				//�÷����������ˮƽ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
		gbc.weighty=weighty;				//�÷������������ֱ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
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
	
	//��������������
	public void connectServer(){
		try {
			((gameHall)(getContentPane().getComponent(2))).connectServer();
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	//�رշ���������
	public void CloseConnect(){
		
	}
}
