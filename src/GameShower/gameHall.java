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
//游戏大厅
public class gameHall extends JPanel{
	//1.开始连接服务器
	//2.每隔一段时间，获取服务器中注册玩家的信息
	//3.发出邀请为直接将请求发送给对方的IP,互为服务端与客户端
	//4.建立连接，开始游戏
	/**
	 * 
	 */
	//常量
	public static final int Invited_Deny = 54;
	public static final int Invited_Achieve = 20;
	
	//
	gameForm owner ;
	Vector<gamerInfo> gamerlist = new Vector<>();
	InetAddress serverAddress;
	Timer getGamerInfo= new Timer();
	gamerInfo goalPlayer = null;
	//ServerSocket me_server ;
	//表单
	Socket Server_list_socket ;
	ObjectInputStream list_in;
	
	//邀请
	Socket Server_request_socket;
	Socket Server_Brequest_socket;
	ObjectInputStream request_in;
	ObjectOutputStream request_out;
	ObjectInputStream request_Bin;
	ObjectOutputStream request_Bout;
	
	//游戏
	Socket Game_Socket;
	ObjectInputStream gm_in;
	ObjectOutputStream gm_out;
	
	//控件
	JLabel info ;
	JButton invideButton ;
	JButton BackHome ;
	JList<gamerInfo> gamers;
	private static final long serialVersionUID = 1L;
	public gameHall(gameForm form){
		this.owner = form;
		setLayout();
		
		//安排事件
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
	
	//设置初始化
	public void setRequest() throws UnknownHostException, IOException{
		//准备流
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
		
		//初始化
		request_out.writeObject(owner.me);
		
		//设置按钮事件
		invideButton.addActionListener(new ActionListener() {
			//对其他玩家进行邀请
			@Override
			public void actionPerformed(ActionEvent e) {
				//1.获取玩家信息
				goalPlayer = gamerlist.elementAt(gamers.getSelectedIndex()) ;
				//2.尝试连接
				try {
					request_out.writeObject(goalPlayer);
					request_out.flush();
					gamerInfo quest= (gamerInfo)request_Bin.readObject();
					if (quest.equals(goalPlayer)){
						if (quest.Port==0)
							JOptionPane.showConfirmDialog(null, "邀请被拒绝","邀请",JOptionPane.YES_OPTION);
						else
							getIntoPlay();
						goalPlayer=null;
					}
				} catch (Exception e2) {
					e2.printStackTrace();
					info.setText("发送请求失败");
				}
			}
		});
		//接受邀请
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while(true){
					gamerInfo quest;
					//TODO 修罗场问题
					try {
						quest = (gamerInfo)request_in.readObject();
						
							int res = JOptionPane.showConfirmDialog(null, "是否接受来自"+quest.toString()+"的邀请", "邀请", JOptionPane.YES_NO_OPTION);
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
	
	//与目标玩家建立游戏
	private void getIntoPlay()//gamerInfo goal){
	{
		info.setText("进入游戏");
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
			info.setText("找到服务器");
		}catch(UnknownHostException e){
			info.setText("服务器失踪");
			return;
		}
		//开始建立连接
		try {
			Server_list_socket = new Socket(serverAddress, ServerInfo.Listport);
			OutputStream out=Server_list_socket.getOutputStream();
			InputStream tInputStream =Server_list_socket.getInputStream();
			ObjectOutputStream tobjectStream= new ObjectOutputStream(out);
			tobjectStream.writeObject(owner.me);
			list_in =new ObjectInputStream(tInputStream) ;
			info.setText("成功连接到服务器");
		} catch (IOException e) {
			e.printStackTrace();
			info.setText("服务器连接失败");
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
							info.setText("当前有"+String.valueOf(gamerlist.size())+"个玩家在线");
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
		info = new JLabel("启动中。。。",JLabel.CENTER);//留给网络线程提示信息
		invideButton = new JButton("邀请");
		BackHome = new JButton("返回");
		gamers=new JList<>(gamerlist);
		gamers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JButton fresh = new JButton("刷新");
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
		//此方法用来添加控件到容器中
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth=gridwidth;		//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
		gbc.gridheight=gridheight;		//该方法是设置组件垂直所占用的格子数
		gbc.weightx=weightx;				//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		gbc.weighty=weighty;				//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		gbc.gridx=gridx;
		gbc.gridy=gridy;
		gbc.fill=GridBagConstraints.BOTH;
		add(component,gbc);
	}
}
