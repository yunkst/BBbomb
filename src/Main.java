

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import GameShower.gameForm;


public class Main {
	public static void main(String[] args)
	{
		/*
		try{
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		}catch(Exception e){
			System.out.println("…Ë÷√Õ‚π€ ß∞‹");
		}
		
		//*/
		//*
		gameForm form = new gameForm();
		
		form.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		/*
		
		appletShower shower  =  new appletShower();
		shower.setSize(500,700);
		//*/
	}
}
