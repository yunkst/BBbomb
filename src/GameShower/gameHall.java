package GameShower;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import model.*;
//��Ϸ����
public class gameHall extends JPanel{
	//1.��ʼ���ӷ�����
	//2.ÿ��һ��ʱ�䣬��ȡ��������ע����ҵ���Ϣ
	//3.��������Ϊֱ�ӽ������͸��Է���IP,��Ϊ�������ͻ���
	//4.�������ӣ���ʼ��Ϸ
	/**
	 * 
	 */
	//����
	public static final int Invited_Deny = 54;
	public static final int Invited_Achieve = 20;
	
	//
	gameForm owner ;
	Vector<gamerInfo> gamerlist = new Vector<>();
	InetAddress serverAddress;
	Timer getGamerInfo= new Timer();
	Socket ServerConnect ; 
	ServerSocket me_server ;
	
	//�ؼ�
	JLabel info ;
	JButton invideButton ;
	JButton BackHome ;
	JList<gamerInfo> gamers;
	private static final long serialVersionUID = 1L;
	public gameHall(gameForm form){
		this.owner = form;
		setLayout();
		test();
		this.me_server = form.me_server;
		//���������߳�
		Thread listen = new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						Socket connect =  me_server.accept();
						ObjectInputStream tStream= new ObjectInputStream(connect.getInputStream());
						gamerInfo goalPlayer = (gamerInfo)tStream.readObject();
						tStream.close();
						int res = JOptionPane.showConfirmDialog(null, "�Ƿ��������"+goalPlayer.toString()+"������", "����", JOptionPane.YES_NO_OPTION);
						OutputStream ts= connect.getOutputStream();
						if (res == JOptionPane.OK_OPTION)
						{
							ts.write(Invited_Achieve);
							getIntoPlay(goalPlayer);
						}
						else
							ts.write(Invited_Deny);
						ts.flush();
						ts.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		listen.start();
		//�����¼�
		BackHome.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				owner.showCard(gameForm.cN_start);
			}
		});
		invideButton.addActionListener(new ActionListener() {
			//��������ҽ�������
			@Override
			public void actionPerformed(ActionEvent e) {
				//1.��ȡ�����Ϣ
				gamerInfo goalGamer = gamerlist.elementAt(gamers.getSelectedIndex()) ;
				//2.��������
				try {
					Socket link = new Socket(InetAddress.getByAddress(goalGamer.address) , goalGamer.Port);
					OutputStream tempstream = link.getOutputStream();
					new ObjectOutputStream(tempstream).writeObject(owner.me);
					tempstream.close();
					link.close();
				} catch (IOException e2) {
					info.setText("����ʧ��");
				}
			}
		});
		
	}
	//��Ŀ����ҽ�����Ϸ
	private void getIntoPlay(gamerInfo goal){
		try {
			Socket socket = new Socket(InetAddress.getByAddress(goal.address), goal.Port);
			owner.setSize(owner.getWidth()*2,owner.getHeight());
			owner.getContentPane().add(gameForm.cN_TwoPlayer, new TwoPlayerPlane(socket));
			owner.showCard(gameForm.cN_TwoPlayer);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void connectServer(){
		try{
			serverAddress = InetAddress.getByName(ServerInfo.hostName);	
			info.setText("�ҵ�������");
		}catch(UnknownHostException e){
			info.setText("������ʧ��");
			return;
		}
		//��ʼ��������
		ObjectInputStream stream ;
		try {
			ServerConnect = new Socket(serverAddress, ServerInfo.port);
			ObjectOutputStream tobjectStream= new ObjectOutputStream(ServerConnect.getOutputStream());
			tobjectStream.writeObject(owner.me);
			stream = new ObjectInputStream(ServerConnect.getInputStream());	
		} catch (IOException e) {
			info.setText("����������ʧ��");
			return;
		}
		getGamerInfo.scheduleAtFixedRate(new TimerTask() {
			int failTimes = 0;
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					if(stream.available()==0)
						return;
					gamerlist.clear();
					gamerlist.addAll((Vector<gamerInfo>)(stream.readObject())) ;
					failTimes=0;
				} catch (ClassNotFoundException e) {
					if (failTimes++>100)
						getGamerInfo.cancel();
					info.setText("���л�ʧ�ܵ�"+String.valueOf(failTimes)+"��");
				} catch (IOException e) {
					if (failTimes++>100)
						getGamerInfo.cancel();
					info.setText("��ȡ����ʧ�ܵ�"+String.valueOf(failTimes)+"��");
				}
			}
		}, 10, 100);
	}
	public void clooseConnect()
	{
		getGamerInfo.cancel();
		try {
			ServerConnect.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void test(){
		/*
		gamerlist.addElement(new gamerInfo());
		gamerlist.addElement(new gamerInfo());
		gamerlist.addElement(new gamerInfo());
		gamerlist.addElement(new gamerInfo());
		
		gamerlist.elementAt(0).address=new byte[]{127,0,0,1};
		gamerlist.elementAt(0).Name="me";
		gamerlist.elementAt(0).Port=12012;
		gamerlist.elementAt(1).address=new byte[]{127,0,0,2};
		gamerlist.elementAt(1).Name="me2";
		gamerlist.elementAt(1).Port=12012;
		//*/
	}
	private void setLayout(){
		setLayout(new GridBagLayout());
		info = new JLabel("�����С�����",JLabel.CENTER);//���������߳���ʾ��Ϣ
		invideButton = new JButton("����");
		BackHome = new JButton("����");
		gamers=new JList<>(gamerlist);
		gamers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addc(new JScrollPane(gamers), 2, 1,50, 100, 0, 1);
		addc(info, 2, 1, 50, 5, 0, 0);
		addc(invideButton, 1, 1, 50, 15, 0, 2);
		addc(BackHome, 1, 1, 50, 15, 1, 2);
	}
	
	public void addc(Component component,int gridwidth,int gridheight, int weightx,int weighty,int gridx,int gridy) {
		//�˷���������ӿؼ���������
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth=gridwidth;		//�÷������������ˮƽ��ռ�õĸ����������Ϊ0����˵��������Ǹ��е����һ��
		gbc.gridheight=gridheight;		//�÷��������������ֱ��ռ�õĸ�����
		gbc.weightx=weightx;				//�÷����������ˮƽ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
		gbc.weighty=weighty;				//�÷������������ֱ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
		gbc.gridx=gridx;
		gbc.gridy=gridy;
		gbc.fill=GridBagConstraints.BOTH;
		add(component,gbc);
	}
}
