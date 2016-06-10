package GameShower;

import java.awt.Button;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
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
	gamerInfo goalPlayer = null;
	//ServerSocket me_server ;
	//��
	Socket Server_list_socket ;
	ObjectInputStream list_in;
	
	//����
	Socket Server_request_socket;
	Socket Server_Brequest_socket;
	ObjectInputStream request_in;
	ObjectOutputStream request_out;
	ObjectInputStream request_Bin;
	ObjectOutputStream request_Bout;
	
	//��Ϸ
	Socket Game_Socket;
	ObjectInputStream gm_in;
	ObjectOutputStream gm_out;
	
	//�ؼ�
	JLabel info ;
	JButton invideButton ;
	JButton BackHome ;
	JList<gamerInfo> gamers;
	private static final long serialVersionUID = 1L;
	public gameHall(gameForm form){
		this.owner = form;
		setLayout();
		
		//�����¼�
		BackHome.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				owner.showCard(gameForm.cN_start);
				closeDoorConnect();
			}
		});
		
		
	}
	public void connectServer() throws UnknownHostException, IOException{
		list_connectServer();
		setRequest();
	}
	
	//���ó�ʼ��
	public void setRequest() throws UnknownHostException, IOException{
		//׼����
		Server_request_socket = new Socket(ServerInfo.hostName,ServerInfo.RequestPort);
		Server_Brequest_socket = new Socket(ServerInfo.hostName,ServerInfo.RequestPort);
		Game_Socket = new Socket(ServerInfo.hostName,ServerInfo.RequestPort);
		
		//Server_request_socket.setTrafficClass(0x10);
		OutputStream out =Server_request_socket.getOutputStream();
		InputStream in = Server_request_socket.getInputStream();
		OutputStream Bout =Server_Brequest_socket.getOutputStream();
		InputStream Bin = Server_Brequest_socket.getInputStream();
		OutputStream gmout =Game_Socket.getOutputStream();
		InputStream gmin = Game_Socket.getInputStream();
		
		request_out = new ObjectOutputStream(out);
		request_in = new ObjectInputStream(in);
		request_Bout = new ObjectOutputStream(Bout);
		request_Bin = new ObjectInputStream(Bin);
		gm_out = new ObjectOutputStream(gmout);
		gm_in  = new ObjectInputStream(gmin);
		
		//��ʼ��
		request_out.writeObject(owner.me);
		
		//���ð�ť�¼�
		invideButton.addActionListener(new ActionListener() {
			//��������ҽ�������
			@Override
			public void actionPerformed(ActionEvent e) {
				//1.��ȡ�����Ϣ
				goalPlayer = gamerlist.elementAt(gamers.getSelectedIndex()) ;
				//2.��������
				try {
					request_out.writeObject(goalPlayer);
					request_out.flush();
					gamerInfo quest= (gamerInfo)request_Bin.readObject();
					if (quest.equals(goalPlayer)){
						if (quest.Port==0)
							JOptionPane.showConfirmDialog(null, "���뱻�ܾ�","����",JOptionPane.YES_OPTION);
						else
							getIntoPlay();
						goalPlayer=null;
					}
				} catch (Exception e2) {
					e2.printStackTrace();
					info.setText("��������ʧ��");
				}
			}
		});
		//��������
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while(true){
					gamerInfo quest;
					//TODO ���޳�����
					try {
						quest = (gamerInfo)request_in.readObject();
						
							int res = JOptionPane.showConfirmDialog(null, "�Ƿ��������"+quest.toString()+"������", "����", JOptionPane.YES_NO_OPTION);
							if(res== JOptionPane.OK_OPTION){
								request_Bout.writeObject(owner.me);
								//request_out.writeObject(null);
								request_Bout.flush();
								getIntoPlay();
							}
							else{
								gamerInfo returnInfo = new gamerInfo();
								returnInfo.address = owner.me.address;
								returnInfo.Port = 0;
								request_Bout.writeObject(returnInfo);
								request_Bout.flush();
							}
								
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
	
	//��Ŀ����ҽ�����Ϸ
	private void getIntoPlay()//gamerInfo goal){
	{
		info.setText("������Ϸ");
		closeDoorConnect();
		owner.getContentPane().add(gameForm.cN_TwoPlayer, new TwoPlayerPlane(gm_in,gm_out));
		owner.showCard(gameForm.cN_TwoPlayer);
	
		/*
		try {
			Socket socket = new Socket(InetAddress.getByAddress(goal.address), goal.Port);
			owner.setSize(owner.getWidth()*2,owner.getHeight());
			owner.getContentPane().add(gameForm.cN_TwoPlayer, new TwoPlayerPlane(socket));
			owner.showCard(gameForm.cN_TwoPlayer);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}//*/
	}
	
	public void list_connectServer(){
		try{
			serverAddress = InetAddress.getByName(ServerInfo.hostName);	
			info.setText("�ҵ�������");
		}catch(UnknownHostException e){
			info.setText("������ʧ��");
			return;
		}
		//��ʼ��������
		try {
			Server_list_socket = new Socket(serverAddress, ServerInfo.Listport);
			OutputStream out=Server_list_socket.getOutputStream();
			InputStream tInputStream =Server_list_socket.getInputStream();
			ObjectOutputStream tobjectStream= new ObjectOutputStream(out);
			tobjectStream.writeObject(owner.me);
			list_in =new ObjectInputStream(tInputStream) ;
			info.setText("�ɹ����ӵ�������");
		} catch (IOException e) {
			e.printStackTrace();
			info.setText("����������ʧ��");
			return;
		}
		Thread fleshList = new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				while (true){
					try {
						Vector<gamerInfo> newList =(Vector<gamerInfo>)(list_in.readObject());
						if (!newList.equals(gamerlist)){
							gamerlist.clear();
							gamerlist.addAll(newList) ;
							gamers.updateUI();
							info.setText("��ǰ��"+String.valueOf(gamerlist.size())+"���������");
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		fleshList.start();
	}

	public void closeDoorConnect()
	{
		getGamerInfo.cancel();
		try {
			Server_list_socket.close();
			Server_Brequest_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setLayout(){
		setLayout(new GridBagLayout());
		info = new JLabel("�����С�����",JLabel.CENTER);//���������߳���ʾ��Ϣ
		invideButton = new JButton("����");
		BackHome = new JButton("����");
		gamers=new JList<>(gamerlist);
		gamers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JButton fresh = new JButton("ˢ��");
		fresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gamers.updateUI();
			}
		});
		addc(new JScrollPane(gamers), 3, 1,50, 100, 0, 1);
		addc(info, 3, 1, 50, 5, 0, 0);
		addc(invideButton, 1, 1, 50, 15, 0, 2);
		addc(BackHome, 1, 1, 50, 15, 1, 2);
		addc(fresh, 1, 1,50, 15, 2, 2);
	}
	
	private void addc(Component component,int gridwidth,int gridheight, int weightx,int weighty,int gridx,int gridy) {
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
