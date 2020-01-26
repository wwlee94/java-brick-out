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
	JButton returnbutton;		 //��Ʈ�� ȭ������ ����
	
	JTextField textname;
	
	Color color;
	
	int mouseX,mouseY;
	
	String string;
	String strscore;//int -> string
	int score;		//������
	
	int count;
	
	boolean isresister;		//true:������ ��ϰ��� �׸��� false:��� �Ұ�
	
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
		this.setBackground(new Color(255,255,255,0));				//����� �������� ���� ���õ�
		this.setLayout(null);
		this.setVisible(true);
		
		JPanel borderpanel=new JPanel();
		borderpanel.setBorder(BorderFactory.createLineBorder(color.black,2));
		borderpanel.setBackground(new Color(0,0,0,0));
		borderpanel.setBounds(0,0,800,600);
		add(borderpanel);
		
		this.addMouseListener(new MouseAdapter() { // �޴����� �̺�Ʈ
			@Override
			public void mousePressed(MouseEvent e) { // ���콺�� �޴��ٸ� ��������
				mouseX = e.getX(); // ���� ���콺�� x,y��ǥ�� ������
				mouseY = e.getY();
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(e.getY()>=0&&e.getY()<=50) {
					//e.getXOnsScreen() -> ��ü ȭ��ũ�⿡�� ���� ������ x,y��ġ
					setLocation(e.getXOnScreen()-mouseX, e.getYOnScreen()-mouseY);
				}
			}
		});

		resisterbutton.setBounds(575,500,200,50);
		resisterbutton.setBorderPainted(false); 		// ��ư�׵θ�
		resisterbutton.setContentAreaFilled(false);  // ��ư�������ǥ��
		resisterbutton.setFocusPainted(false); 	// ��ư��Ŀ��ǥ��
		resisterbutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				if(isresister==true) {
				string=textname.getText();
				if(!string.equals("")) {	
				//if(string!="") {  -> �ȉ� ���� �ּ� �񱳶�
					
						connection.setData(string, score);
						connection.getData();
						System.out.println("������ ����:"+connection.isAdmin("admin", "admin")); //������ �� �Ǿ����� Ȯ���ϴ� ��
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
		returnbutton.setBorderPainted(false); 		// ��ư�׵θ�
		returnbutton.setContentAreaFilled(false);  // ��ư�������ǥ��
		returnbutton.setFocusPainted(false); 	// ��ư��Ŀ��ǥ��
		
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
		textname.setFont(new Font("���� ���",Font.BOLD,17));
		add(textname);
	}

	public void paint(Graphics g) {
		image=createImage(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
		graphics=image.getGraphics();
		drawScreen(graphics);
		g.drawImage(image, 0, 0, null);				//drawScreen �׸��� �ʱ�ȭ �����ִ� ����
	}
	public void drawScreen(Graphics g) {
		
		g.drawImage(background, 0, 0, null);
		paintComponents(g);
		g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); //��Ƽ�������
		
		g2.setColor(new Color(0,0,0,120));
	  	g2.fillRoundRect(75, 90, 650, 400, 10, 10);
	  	g2.setColor(color.orange);
		g2.setFont(new Font("���� ���",Font.ITALIC|Font.BOLD,40));
	  	g2.drawString("Ranking", 320, 60);
	  	
	  	g2.setColor(new Color(255,255,255));
	  	print(g2);
	  	g2.drawString("Name:",390,532);
	  	
		repaint();
	}
	public void print(Graphics2D g) {
			int x,y;
			g2.setFont(new Font("���� ���",Font.ITALIC|Font.BOLD,25));
			g.drawString("����", 170, 140);
			g.drawString("�̸�", 370, 140);
			g.drawString("����", 570, 140);
			g2.setFont(new Font("���� ���",Font.ITALIC|Font.BOLD,20));
		
				y=190;
				for(int i=0;i<connection.count;i++) {	
					if(i<8) {							//8�� ���� ��� ����
					x=170;
					g.drawString((i+1)+" ��  ", x, y);
					x+=200;
					g.drawString(connection.DBname[i], x, y);
					x+=200;
					g.drawString(""+connection.DBscore[i], x, y);
					y+=40;
					}//i<8
				}
	}
	
}
