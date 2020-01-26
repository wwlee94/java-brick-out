package Brick_Out;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Frame_Rank extends JFrame{

	Image image;
	Image background;
	Graphics graphics;
	Graphics2D g2;
	
	DBConnection connection;

	ArrayList<String> nameList;
	ArrayList<Integer> scoreList;
	
	ImageIcon resisterbuttonbasic=new ImageIcon(getClass().getResource("../image/resisterbutton.png"));
	ImageIcon resisterbuttonentered=new ImageIcon(getClass().getResource("../image/resisterbuttonentered.png"));
	ImageIcon returnbuttonbasic=new ImageIcon(getClass().getResource("../image/returnbutton2.png"));
	ImageIcon returnbuttonentered=new ImageIcon(getClass().getResource("../image/returnbutton2entered.png"));
	
	JButton resisterbutton;
	JButton returnbutton;		 //인트로 화면으로 복귀
	
	JTextField textname;
	
	Color color;
	
	int mouseX,mouseY;
	
	String string;
	String strscore;//int -> string
	int score;		//생성자
	
	int count;
	
	boolean isresister;		//true:점수판 등록가능 그리고 false:등록 불가
	
	public Frame_Rank(int p_score,boolean p_isresister) {
		this.score=p_score;
		this.isresister=p_isresister;
		
		connection=new DBConnection();
		connection.getData();
		
		background=new ImageIcon(getClass().getResource("../image/background_basic.jpg")).getImage();
		resisterbutton=new JButton(resisterbuttonbasic);
		returnbutton=new JButton(returnbuttonbasic);
		
		textname=new JTextField();
	
		this.setTitle("Rank Screen");
		this.setSize(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setBackground(new Color(255,255,255,0));				//배경이 투명으로 변함 투시됨
		this.setLayout(null);
		this.setVisible(true);
		
		JPanel borderpanel=new JPanel();
		borderpanel.setBorder(BorderFactory.createLineBorder(color.black,2));
		borderpanel.setBackground(new Color(0,0,0,0));
		borderpanel.setBounds(0,0,800,600);
		add(borderpanel);
		
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

		resisterbutton.setBounds(575,500,200,50);
		resisterbutton.setBorderPainted(false); 		// 버튼테두리
		resisterbutton.setContentAreaFilled(false);  // 버튼영역배경표시
		resisterbutton.setFocusPainted(false); 	// 버튼포커스표시
		resisterbutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				if(isresister==true) {
				string=textname.getText();
				if(!string.equals("")) {	
				//if(string!="") {  -> 안됌 ㅎㅎ 주소 비교라서
					
						connection.setData(string, score);
						connection.getData();
						System.out.println("관리자 여부:"+connection.isAdmin("admin", "admin")); //연동이 잘 되었는지 확인하는 것
						isresister=false;
					}//if(!string!="")
				}
				else if(isresister==false) {
					resisterbutton.setEnabled(false);
				}
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				resisterbutton.setIcon(resisterbuttonentered);
			}
			public void mouseExited(MouseEvent e) {
				resisterbutton.setIcon(resisterbuttonbasic);
			}
		});
		add(resisterbutton);
		
		returnbutton.setBounds(25, 500, 200, 50);
		returnbutton.setBorderPainted(false); 		// 버튼테두리
		returnbutton.setContentAreaFilled(false);  // 버튼영역배경표시
		returnbutton.setFocusPainted(false); 	// 버튼포커스표시
		
		returnbutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {

				setVisible(false);
				dispose();
				Frame_Intro introrank=new Frame_Intro();
				introrank.thread.start();
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
		
		textname.setBounds(480,510, 100, 30);
		textname.setBackground(new Color(0,0,0,120));
		textname.setBorder(null);
		textname.setForeground(new Color(255,255,255));
		textname.setFont(new Font("맑은 고딕",Font.BOLD,17));
		add(textname);
	}

	public void paint(Graphics g) {
		image=createImage(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		graphics=image.getGraphics();
		drawScreen(graphics);
		g.drawImage(image, 0, 0, null);				//drawScreen 그리고 초기화 시켜주는 역할
	}
	public void drawScreen(Graphics g) {
		
		g.drawImage(background, 0, 0, null);
		paintComponents(g);
		g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); //안티엘리어싱
		
		g2.setColor(new Color(0,0,0,120));
	  	g2.fillRoundRect(75, 90, 650, 400, 10, 10);
	  	g2.setColor(color.orange);
		g2.setFont(new Font("맑은 고딕",Font.ITALIC|Font.BOLD,40));
	  	g2.drawString("Ranking", 320, 60);
	  	
	  	g2.setColor(new Color(255,255,255));
	  	print(g2);
	  	g2.drawString("Name:",390,532);
	  	
		repaint();
	}
	public void print(Graphics2D g) {
			int x,y;
			g2.setFont(new Font("맑은 고딕",Font.ITALIC|Font.BOLD,25));
			g.drawString("순위", 170, 140);
			g.drawString("이름", 370, 140);
			g.drawString("점수", 570, 140);
			g2.setFont(new Font("맑은 고딕",Font.ITALIC|Font.BOLD,20));
		
				y=190;
				for(int i=0;i<connection.count;i++) {	
					if(i<8) {							//8위 까지 출력 가능
					x=170;
					g.drawString((i+1)+" 위  ", x, y);
					x+=200;
					g.drawString(connection.DBname[i], x, y);
					x+=200;
					g.drawString(""+connection.DBscore[i], x, y);
					y+=40;
					}//i<8
				}
	}
	
}
