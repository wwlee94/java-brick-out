package Brick_Out;

import java.awt.Color;

import javax.swing.JFrame;

public class Frame_Game extends JFrame{

	//Frame_Main frame=new Frame_Main();  //쓰면 잔상 X던데
	
	Panel_Game panel;
	
	public Frame_Game() {
		
		this.setTitle("Game Screen");
		this.setSize(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		//this.setOpacity(0.9f);
		this.setBackground(new Color(255,255,255,0));				//배경이 투명으로 변함 투시됨
		this.setLayout(null);
		this.setVisible(true);
	
		panel=new Panel_Game(this);
		panel.setBounds(0,0,800,600);
		add(panel);
		
	}//public Frame_Game
}
