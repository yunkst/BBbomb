package GameShower;
//玩家数据页

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamerInfoPage extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField Name = new JTextField();
	gameForm owner;
	public GamerInfoPage(gameForm owner){
		this.owner = owner;
		setLayout(new GridBagLayout());
		
		addc(new JLabel("昵称:",JLabel.CENTER), 1, 1, 20, 10, 0, 0,GridBagConstraints.EAST);
		addc(Name, 1, 1, 50, 10, 1, 0,GridBagConstraints.HORIZONTAL);
		JButton sureButton = new JButton("确定");
		addc(sureButton, 3, 1, 70, 10, 0, 1,GridBagConstraints.BOTH);
		
		sureButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				owner.me.Name = Name.getText();
				owner.showCard(gameForm.cN_gameHall);
				owner.connectServer();
			}
		});
	}
	private void addc(Component component,int gridwidth,int gridheight, int weightx,int weighty,int gridx,int gridy,int fill) {
		//此方法用来添加控件到容器中
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridwidth=gridwidth;		//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
		gbc.gridheight=gridheight;		//该方法是设置组件垂直所占用的格子数
		gbc.weightx=weightx;				//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		gbc.weighty=weighty;				//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
		gbc.gridx=gridx;
		gbc.gridy=gridy;
		gbc.fill=fill;
		add(component,gbc);
	}
}
