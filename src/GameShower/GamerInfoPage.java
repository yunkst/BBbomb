package GameShower;
//�������ҳ

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
		
		addc(new JLabel("�ǳ�:",JLabel.CENTER), 1, 1, 20, 10, 0, 0,GridBagConstraints.EAST);
		addc(Name, 1, 1, 50, 10, 1, 0,GridBagConstraints.HORIZONTAL);
		JButton sureButton = new JButton("ȷ��");
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
		//�˷���������ӿؼ���������
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridwidth=gridwidth;		//�÷������������ˮƽ��ռ�õĸ����������Ϊ0����˵��������Ǹ��е����һ��
		gbc.gridheight=gridheight;		//�÷��������������ֱ��ռ�õĸ�����
		gbc.weightx=weightx;				//�÷����������ˮƽ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
		gbc.weighty=weighty;				//�÷������������ֱ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
		gbc.gridx=gridx;
		gbc.gridy=gridy;
		gbc.fill=fill;
		add(component,gbc);
	}
}
