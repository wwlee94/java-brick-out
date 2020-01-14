package Brick_Out;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame_Stage extends JFrame{

	Image image;
	Image background;
	Graphics graphics;
	
	int mouseX,mouseY;
	
	Color color;
	
	ImageIcon stagebutton1basic=new ImageIcon(getClass().getResource("../image/stagebutton1.png"));
	ImageIcon stagebutton1entered=new ImageIcon(getClass().getResource("../image/stagebutton1entered.png"));
	ImageIcon stagebutton2basic=new ImageIcon(getClass().getResource("../image/stagebutton2.png"));
	ImageIcon stagebutton2entered=new ImageIcon(getClass().getResource("../image/stagebutton2entered.png"));
	ImageIcon stagebutton3basic=new ImageIcon(getClass().getResource("../image/stagebutton3.png"));
	ImageIcon stagebutton3entered=new ImageIcon(getClass().getResource("../image/stagebutton3entered.png"));
	ImageIcon returnbuttonbasic=new ImageIcon(getClass().getResource("../image/returnbutton.png"));
	ImageIcon returnbuttonentered=new ImageIcon(getClass().getResource("../image/returnbuttonentered.png"));
	
	JButton stagebutton1;
	JButton stagebutton2;
	JButton stagebutton3;
	JButton returnbutton;		 //인트로 화면으로 복귀
	
	public void paint(Graphics g) {
		image=createImage(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		graphics=image.getGraphics();
		drawScreen(graphics);
		g.drawImage(image, 0, 0, null);
	}
	public void drawScreen(Graphics g) {
		g.drawImage(background, 0, 0, null);
		paintComponents(g);					//버튼이나 라벨등을 그려주는 역할 페인트 재정의 했으면 얘 해줘야 그려짐 배경 그리고 컴포넌트
		repaint();
	}
	public Frame_Stage() {
		
		background=new ImageIcon(getClass().getResource("../image/background_basic.jpg")).getImage();
		stagebutton1=new JButton(stagebutton1basic);
		stagebutton2=new JButton(stagebutton2basic);
		stagebutton3=new JButton(stagebutton3basic);
		returnbutton=new JButton(returnbuttonbasic);
		
		this.setTitle("Level Selection Screen");
		this.setSize(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setBackground(new Color(255,255,255,0));				//배경이 투명으로 변함 투시됨
		this.setLayout(null);
		this.setVisible(true);
		
		JPanel panel=new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(color.black,2));
		panel.setBackground(new Color(0,0,0,0));
		panel.setBounds(0,0,800,600);
		add(panel);
		
		this.addMouseListener(new MouseAdapter() { // 메뉴바의 이벤트
			@Override
			public void mousePressed(MouseEvent e) { // 마우스가 메뉴바를 눌렀을때
				mouseX = e.getX(); // 누른 마우스의 x,y좌표를 가져옴
				mouseY = e.getY();
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(e.getY()>=0&&e.getY()<=50) {
					//e.getXOnsScreen() -> 전체 화면크기에서 현재 프레임 x,y위치
					setLocation(e.getXOnScreen()-mouseX, e.getYOnScreen()-mouseY);
				}
			}
		});
		
		//stage 1
		stagebutton1.setBounds(100, 200, 200, 50);
		stagebutton1.setBorderPainted(false); 		// 버튼테두리
		stagebutton1.setContentAreaFilled(false);  // 버튼영역배경표시
		stagebutton1.setFocusPainted(false); 	// 버튼포커스표시
		
		stagebutton1.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {

				setVisible(false);
				dispose();
				Frame_Game game=new Frame_Game();
				game.panel.stage=1;
				game.panel.thread.start();
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				stagebutton1.setIcon(stagebutton1entered);
			}
			public void mouseExited(MouseEvent e) {
				stagebutton1.setIcon(stagebutton1basic);
			}
		});
		add(stagebutton1);
		
		//stage 2
		stagebutton2.setBounds(300, 200, 200, 50);
		stagebutton2.setBorderPainted(false); 		// 버튼테두리
		stagebutton2.setContentAreaFilled(false);  // 버튼영역배경표시
		stagebutton2.setFocusPainted(false); 	// 버튼포커스표시
		
		stagebutton2.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {

				setVisible(false);
				dispose();
				Frame_Game game=new Frame_Game();
				game.panel.stage=2;
				game.panel.thread.start();
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				stagebutton2.setIcon(stagebutton2entered);
			}
			public void mouseExited(MouseEvent e) {
				stagebutton2.setIcon(stagebutton2basic);
			}
		});
		add(stagebutton2);
		
		//stage 3
		stagebutton3.setBounds(500, 200, 200, 50);
		stagebutton3.setBorderPainted(false); 		// 버튼테두리
		stagebutton3.setContentAreaFilled(false);  // 버튼영역배경표시
		stagebutton3.setFocusPainted(false); 	// 버튼포커스표시
		
		stagebutton3.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {

				setVisible(false);
				dispose();
				Frame_Game game=new Frame_Game();
				game.panel.stage=3;
				game.panel.thread.start();
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				stagebutton3.setIcon(stagebutton3entered);
			}
			public void mouseExited(MouseEvent e) {
				stagebutton3.setIcon(stagebutton3basic);
			}
		});
		add(stagebutton3);
		
		returnbutton.setBounds(100, 450, 200, 50);
		returnbutton.setBorderPainted(false); 		// 버튼테두리
		returnbutton.setContentAreaFilled(false);  // 버튼영역배경표시
		returnbutton.setFocusPainted(false); 	// 버튼포커스표시
		
		returnbutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {

				setVisible(false);
				dispose();
				Frame_Intro introstage=new Frame_Intro();
				introstage.thread.start();
				
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				returnbutton.setIcon(returnbuttonentered);
			}
			public void mouseExited(MouseEvent e) {
				returnbutton.setIcon(returnbuttonbasic);
			}
		});
		add(returnbutton);
		

	}//public Frame_Level
}
