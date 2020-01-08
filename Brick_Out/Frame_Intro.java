package Brick_Out;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame_Intro extends JFrame implements Runnable{
	
	Image image;
	Image background;
	Image cloud1;
	Image cloud2;
	Graphics graphics;
	
	ImageIcon startbuttonbasic=new ImageIcon(getClass().getResource("../image/startbutton.png"));
	ImageIcon startbuttonentered=new ImageIcon(getClass().getResource("../image/startbuttonentered.png"));
	ImageIcon exitbuttonbasic=new ImageIcon(getClass().getResource("../image/exitbutton.png"));
	ImageIcon exitbuttonentered=new ImageIcon(getClass().getResource("../image/exitbuttonentered.png"));
	ImageIcon rankbuttonbasic=new ImageIcon(getClass().getResource("../image/rankbutton.png"));
	ImageIcon rankbuttonentered=new ImageIcon(getClass().getResource("../image/rankbuttonentered.png"));
	
	Color color;
	
	//Frame_Intro
	JButton startbutton;
	JButton exitbutton;
	JButton rankbutton;
	
	Thread thread;

	int mouseX,mouseY;
	
	Random random;
	int rannum1,rannum2;

	int x1dir,x2dir;
	int cldX1,cldX2;
	int cldY1,cldY2;
	
	Boolean isloop;
	
	public void paint(Graphics g) {
		image=createImage(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		graphics=image.getGraphics();
		drawScreen(graphics);
		g.drawImage(image, 0, 0, null);
	}
	public void drawScreen(Graphics g) {
		g.drawImage(background, 0, 0, null);
		drawCloud(g);
		paintComponents(g);					//버튼이나 라벨등을 그려주는 역할 페인트 재정의 했으면 얘 해줘야 그려짐 배경 그리고 컴포넌트
		//repaint();
	}
	public Frame_Intro() {
		
		thread=new Thread(this);
		random=new Random();
		
		rannum1=random.nextInt(5)+1;
		rannum2=random.nextInt(5)+1;
		
		cloud1=new ImageIcon(getClass().getResource("../image/cloud"+rannum1+".png")).getImage();
		cldX1=0;
		do {
		cldY1=random.nextInt(300);
		}while(cldY1>50&&cldY2<200);
	
		x1dir=random.nextInt(2)+1;
		x1dir=x1dir;			//x1dir은 양수로 그대로
		
		cloud2=new ImageIcon(getClass().getResource("../image/cloud"+rannum2+".png")).getImage();
		cldX2=600;
		do {
		cldY2=random.nextInt(300);
		}while(cldY2>50&&cldY2<200);
		
		x2dir=random.nextInt(2)+1;
		x2dir=-x2dir;			//x2dir은 음수로 왼쪽으로 이동시킨다.
		
		isloop=true;
	
		background=new ImageIcon(getClass().getResource("../image/background_intro.jpg")).getImage();
		startbutton=new JButton(startbuttonbasic);
		exitbutton=new JButton(exitbuttonbasic);
		rankbutton=new JButton(rankbuttonbasic);
		
		this.setTitle("Intro Screen");
		this.setSize(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setBackground(new Color(255,255,255,0));					//배경이 투명으로 변함 투시됨
		this.setLayout(null);
		this.setVisible(true);
		
		JPanel panel=new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(color.black,2));
		panel.setBackground(new Color(0,0,0,0));
		panel.setBounds(0,0,800,600);
		add(panel);
		
		startbutton.setBounds(300, 405, 200, 50);
		startbutton.setBorderPainted(false); 		// 버튼테두리
		startbutton.setContentAreaFilled(false);  // 버튼영역배경표시
		startbutton.setFocusPainted(false); 	// 버튼포커스표시
		
		startbutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				
				isloop=false;
				//thread.interrupt();
				setVisible(false);
				dispose();
				new Frame_Stage();
				
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				startbutton.setIcon(startbuttonentered);
				
			}
			public void mouseExited(MouseEvent e) {
				startbutton.setIcon(startbuttonbasic);
			}
		});
		add(startbutton);
		
		exitbutton.setBounds(300, 460, 200, 50);
		exitbutton.setBorderPainted(false); 		// 버튼테두리
		exitbutton.setContentAreaFilled(false);  // 버튼영역배경표시
		exitbutton.setFocusPainted(false); 	// 버튼포커스표시
		
		exitbutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				exitbutton.setIcon(exitbuttonentered);
				
			}
			public void mouseExited(MouseEvent e) {
				exitbutton.setIcon(exitbuttonbasic);
			}
		});
		add(exitbutton);
		
		rankbutton.setBounds(300, 515, 200, 50);
		rankbutton.setBorderPainted(false); 		// 버튼테두리
		rankbutton.setContentAreaFilled(false);  // 버튼영역배경표시
		rankbutton.setFocusPainted(false); 	// 버튼포커스표시
		
		rankbutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {

				isloop=false;
				//thread.interrupt();
				setVisible(false);				//this.setVisible -> 오류
				dispose();
				new Frame_Rank(0,false);
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				rankbutton.setIcon(rankbuttonentered);
			}
			public void mouseExited(MouseEvent e) {
				rankbutton.setIcon(rankbuttonbasic);
			}
		});
		add(rankbutton);
	
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(isloop) {	
			try {
				moveCloud();
				Thread.sleep(20);
				repaint();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void drawCloud(Graphics g) {
		g.drawImage(cloud1,cldX1,cldY1,this);
		g.drawImage(cloud2,cldX2,cldY2,this);
	}
	public void moveCloud() {
		
		if(cldX1+x1dir<0-200||cldX1+x1dir>800) {
			rannum1=random.nextInt(5)+1;
			cloud1=new ImageIcon(getClass().getResource("../image/cloud"+rannum1+".png")).getImage();
			
			do {								//Brick Out을 가리지 않는 범위 설정
				cldY1=random.nextInt(300);
				}while(cldY1>50&&cldY2<200);
			
			x1dir=-x1dir;
		}
		if(cldX2+x2dir<0-200||cldX2+x2dir>800) {
			rannum2=random.nextInt(5)+1;
			cloud2=new ImageIcon(getClass().getResource("../image/cloud"+rannum2+".png")).getImage();
			
			do {								//Brick Out을 가리지 않는 범위 설정
				cldY2=random.nextInt(300);
				}while(cldY2>50&&cldY2<200);
			
			x2dir=-x2dir;
		}
		cldX1=cldX1+x1dir;
		cldX2=cldX2+x2dir;
	}
	
}
