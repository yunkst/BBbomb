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
	Socket ServerConnect ; 
	ServerSocket me_server ;
	
	//控件
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
		//建立监听线程
		Thread listen = new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						Socket connect =  me_server.accept();
						ObjectInputStream tStream= new ObjectInputStream(connect.getInputStream());
						gamerInfo goalPlayer = (gamerInfo)tStream.readObject();
						tStream.close();
						int res = JOptionPane.showConfirmDialog(null, "是否接受来自"+goalPlayer.toString()+"的邀请", "邀请", JOptionPane.YES_NO_OPTION);
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
		//安排事件
		BackHome.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				owner.showCard(gameForm.cN_start);
			}
		});
		invideButton.addActionListener(new ActionListener() {
			//对其他玩家进行邀请
			@Override
			public void actionPerformed(ActionEvent e) {
				//1.获取玩家信息
				gamerInfo goalGamer = gamerlist.elementAt(gamers.getSelectedIndex()) ;
				//2.尝试连接
				try {
					Socket link = new Socket(InetAddress.getByAddress(goalGamer.address) , goalGamer.Port);
					OutputStream tempstream = link.getOutputStream();
					new ObjectOutputStream(tempstream).writeObject(owner.me);
					tempstream.close();
					link.close();
				} catch (IOException e2) {
					info.setText("邀请失败");
				}
			}
		});
		
	}
	//与目标玩家建立游戏
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
			info.setText("找到服务器");
		}catch(UnknownHostException e){
			info.setText("服务器失踪");
			return;
		}
		//开始建立连接
		ObjectInputStream stream ;
		try {
			ServerConnect = new Socket(serverAddress, ServerInfo.port);
			ObjectOutputStream tobjectStream= new ObjectOutputStream(ServerConnect.getOutputStream());
			tobjectStream.writeObject(owner.me);
			stream = new ObjectInputStream(ServerConnect.getInputStream());	
		} catch (IOException e) {
			info.setText("服务器连接失败");
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
					info.setText("序列化失败第"+String.valueOf(failTimes)+"次");
				} catch (IOException e) {
					if (failTimes++>100)
						getGamerInfo.cancel();
					info.setText("获取数据失败第"+String.valueOf(failTimes)+"次");
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
		info = new JLabel("启动中。。。",JLabel.CENTER);//留给网络线程提示信息
		invideButton = new JButton("邀请");
		BackHome = new JButton("返回");
		gamers=new JList<>(gamerlist);
		gamers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addc(new JScrollPane(gamers), 2, 1,50, 100, 0, 1);
		addc(info, 2, 1, 50, 5, 0, 0);
		addc(invideButton, 1, 1, 50, 15, 0, 2);
		addc(BackHome, 1, 1, 50, 15, 1, 2);
	}
	
	public void addc(Component component,int gridwidth,int gridheight, int weightx,int weighty,int gridx,int gridy) {
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
