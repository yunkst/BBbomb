package GameShower;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

//��ʼ����
public class StartShow extends JPanel{

	JButton Solo = new JButton("����");
	JButton InternetButton = new JButton("����");
	public gameForm owner ; 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public StartShow(gameForm owner) {
		this.owner=owner;
		Solo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				owner.showCard(gameForm.cN_solo);
			}
		});
		InternetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				owner.showCard(gameForm.cN_gamerInfo);
			}
		});

		setLayout(new GridLayout(2, 1));
		add(Solo);
		add(InternetButton);
	}
	
	
	
}
