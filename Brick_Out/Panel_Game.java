package Brick_Out;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Panel_Game extends JPanel implements Runnable{
	
	static final int DIAMETER=15;  			//���� ����
	static final int XBALL=400;				//ball x,y�� �ʱ� ��ġ
	static final int YBALL=300;
	static final int XDIR=0;
	static final int YDIR=1;
	
	static int BAR_WIDTH=80;				//���� ���� , ����
	static final int BAR_HEIGHT=15;
	
	static final int BRICK_WIDTH=50;		//���� ���� , ����
	static final int BRICK_HEIGHT=25;
	
	static final int PERCENT=30;
	static final int ITEM_DIAMETER=20;
	
	static final int ROUND=5;				//RoundRect�� ����
	
	int stage;		//�������� ��ȣ
	int life;		//���
	int score;		//����
	String strscore;
	
	int offset;		//���� offset temp����
	int count;		//10ms���� count���ֱ� ���� temp����
	int keycode;	//Ű���� �Է�
	int overcount;	//���ӿ������ȭ�� ���� ī��Ʈ
	int imagenum;	//�ִϸ��̼� �̹��� ��ȣ
	
	int explosioncnt;
	
	int OBJECT_ALPHA;
	int BACKGROUND_ALPHA;
	
	int mouseX,mouseY;	//���콺 x,y��ǥ
	
	int backgroundnum;	//1: ���� ���� �̹��� 2:���� ���� �̹���
	Boolean isloop;			 //���ӷ��� true:���� ��� ���� false:���� ����
	Boolean isimage;		 //explosionimage true:�׸� false:�ȱ׸�
	
	Frame_Game game;
	
	Thread thread;
	
	Color color;
	Graphics2D g2;
	
	Image background;		//��� �̹���
	Image heart;			//����� �̹���
	Image image;			//���� �̹���
	AlphaComposite alphaComposite;
	
	ArrayList<Ball> ballList;	
	ArrayList<Item> itemList;
	ArrayList<Explosion_Animation> explosionList;
	Explosion_Animation exp;
	
	Brick brick; 	//ArrayList<Brick>�� ���� �����ϱ� ���� �ӽú���
	Ball ball;		//ArrayList<Ball>�� ���� �����ϱ� ���� �ӽú���	
	Item item;		//ArrayList<Item>�� ���� �����ϱ� ���� �ӽú���	
	User_Bar bar;
	Brick_Setstage setlevel;		
	
	ImageIcon playbuttonbasic;
	ImageIcon playbuttonentered;
	ImageIcon pausebuttonbasic;
	ImageIcon pausebuttonentered;
	ImageIcon powerbuttonbasic;
	ImageIcon powerbuttonentered;

	JButton playbutton;
	JButton pausebutton;
	JButton powerbutton;
	
	public Panel_Game(Frame_Game game) {
		this.game=game;
		
		isloop=true;
		isimage=false;
		
		OBJECT_ALPHA=255;
		BACKGROUND_ALPHA=50;
		
		strscore=new String();
		
		score=0;
		life=3;
		count=0;
		overcount=0;
		explosioncnt=0;
		imagenum=0;
		
		setBorder(BorderFactory.createLineBorder(color.black,2));
		
		ballList=new ArrayList<Ball>();
		ballList.add(new Ball(this,XBALL,YBALL,XDIR,YDIR,true));
		//ballList.add(new Ball(this,10,10,0,0,true,true));
		itemList=new ArrayList<Item>();			
		itemList.add(new Item(this,0,0,true));		
		
		//���� �ִϸ��̼��� �׸� ����Ʈ �����޾Ƽ�
		explosionList=new ArrayList<Explosion_Animation>();
		
		thread=new Thread(this);
		
		bar=new User_Bar(this);
		setlevel=new Brick_Setstage(this);
		item=new Item(this,0,0,false);
		
		background=new ImageIcon(getClass().getResource("../image/background_basic.jpg")).getImage();
		heart=new ImageIcon(getClass().getResource("../image/heart.png")).getImage();
		
		playbuttonbasic=new ImageIcon(getClass().getResource("../image/playbutton.png"));
		playbuttonentered=new ImageIcon(getClass().getResource("../image/playbuttonentered.png"));
		pausebuttonbasic=new ImageIcon(getClass().getResource("../image/pausebutton.png"));
		pausebuttonentered=new ImageIcon(getClass().getResource("../image/pausebuttonentered.png"));
		powerbuttonbasic=new ImageIcon(getClass().getResource("../image/powerbutton.png"));
		powerbuttonentered=new ImageIcon(getClass().getResource("../image/powerbuttonentered.png"));
		
		playbutton=new JButton(playbuttonbasic);
		pausebutton=new JButton(pausebuttonbasic);
		powerbutton=new JButton(powerbuttonbasic);
	
		playbutton.setBounds(660,10,30,30);
		playbutton.setBorderPainted(false); 		// ��ư�׵θ�
		playbutton.setContentAreaFilled(false);  // ��ư�������ǥ��
		playbutton.setFocusPainted(false); 	// ��ư��Ŀ��ǥ��
		playbutton.setFocusable(false); 		//��ư�� ���� ������ X
		playbutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				thread.resume();
				Main.intromusic.resume();
				OBJECT_ALPHA=255;
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				playbutton.setIcon(playbuttonentered);
			}
			public void mouseExited(MouseEvent e) {
				playbutton.setIcon(playbuttonbasic);
			}
			
		});
		add(playbutton);
		
		pausebutton.setBounds(710,10,30,30);
		pausebutton.setBorderPainted(false); 		// ��ư�׵θ�
		pausebutton.setContentAreaFilled(false);  // ��ư�������ǥ��
		pausebutton.setFocusPainted(false); 	// ��ư��Ŀ��ǥ��
		pausebutton.setFocusable(false); 		//��ư�� ���� ������ X
		pausebutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				thread.suspend();
				Main.intromusic.suspend();
				OBJECT_ALPHA=150;
				for(int i=0;i<10;i++) repaint();		//�Ͻ������ϸ� ȭ�� ������� �������
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				pausebutton.setIcon(pausebuttonentered);
			}
			public void mouseExited(MouseEvent e) {
				pausebutton.setIcon(pausebuttonbasic);
			}
		});
		add(pausebutton);
		
		powerbutton.setBounds(760,10,30,30);
		powerbutton.setBorderPainted(false); 		// ��ư�׵θ�
		powerbutton.setContentAreaFilled(false);  // ��ư�������ǥ��
		powerbutton.setFocusPainted(false); 	// ��ư��Ŀ��ǥ��
		powerbutton.setFocusable(false); 		//��ư�� ���� ������ X
		powerbutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				game.dispose();
				thread.interrupt();
				isloop=false;
				Frame_Intro intro=new Frame_Intro();
				intro.thread.start();
			}
			public void mouseEntered(MouseEvent e) {
				Music buttonmusic=new Music("buttonmusic",false);
				buttonmusic.start();
				powerbutton.setIcon(powerbuttonentered);
			}
			public void mouseExited(MouseEvent e) {
				powerbutton.setIcon(powerbuttonbasic);
			}
		});
		add(powerbutton);
		
	
		//this.setBackground(new Color(0,0,0,0));
		this.setFocusable(true);	
		this.setLayout(null);
		//this.setOpaque(false);		//�ܻ� �ȳ���
		this.addKeyListener(new KeyAdapter() {
	
			public void keyPressed(KeyEvent e) {
				keycode=e.getKeyCode();
				
				if(keycode==KeyEvent.VK_LEFT) {
					bar.x_direction=-7;
				}
				if(keycode==KeyEvent.VK_RIGHT) {
					bar.x_direction=7;
				}
				}//keypressed
			
			public void keyReleased(KeyEvent e) {		//Ű ���� ����			//flush?? Ű ���� ó��
					bar.x_direction=0;
				}
		});//addkeylistener
		this.addMouseListener(new MouseAdapter() { // �޴����� �̺�Ʈ
			@Override
			public void mousePressed(MouseEvent e) { // ���콺�� �޴��ٸ� ��������
				mouseX = e.getX(); // ���� ���콺�� x,y��ǥ�� ������
				mouseY = e.getY();
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if((e.getY()>=0&&e.getY()<=50)) {
					//e.getXOnsScreen() -> ��ü ȭ��ũ�⿡�� ���� ������ x,y��ġ
					game.setLocation(e.getXOnScreen()-mouseX, e.getYOnScreen()-mouseY);
				}
				else {
					bar.x=e.getX()-(BAR_WIDTH/2);
				}
			}
		});
		
	}//public Brick_Crush
	
	//������ ���� ����
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			setlevel.selectlevel();
			
			while(isloop) {
			ballListEvent();						//�� ���� + �浹Ȯ��	
			itemListEvent();						//�����۰� ����� �� �浹 Ȯ��
			bar.barEvent();							//bar x,y��ǥ �����ϴ� �κ�
			repaint();
			Thread.sleep(9);			//100���� 1�� ������ ����Ʈ��,�������ӿ� �ð� ������ �δ� ���� 
			}//while
			
		//���� ���� ����
		//���� ���� ���� -> copy,fire�������� �����߿��� gameoverȭ�� ������ ����
		score+=life*2000;				//���� Life*2000�� �߰� ȹ��
	
		OBJECT_ALPHA=150;
		
		for(int i=0;i<100;i++) {		//���ο� ���
			ballListEvent();
			itemListEvent();
			bar.barEvent();
			repaint();
			Thread.sleep(50);
			overcount++;
			//��1.5�ʵ� ��� ����
			if(overcount==35) {
				game.setSize(800,500);
				game.setLocationRelativeTo(null);
				if(backgroundnum==1) {
					background=new ImageIcon(getClass().getResource("../image/background_gameclear.jpg")).getImage();
					Music clearmusic=new Music("��̵�ȯȣ��",false);
					clearmusic.start();
				}
				else if(backgroundnum==2) 
					background=new ImageIcon(getClass().getResource("../image/background_gameover.jpg")).getImage();
				
			}
			if(OBJECT_ALPHA>0) OBJECT_ALPHA-=5;
		}//for
		
		game.dispose();					//���� ȭ�� ����
		
		new Frame_Rank(score,true);		//���ο� ��ŷâ ���
		
		Thread.sleep(1500);
		Main.intromusic.resume();
		
		//interrupt()?? ������,���� ���� �������!?
		//thread.interrupt();				//�긦 Frame_Rank()���� ���� �����Ű�� image load����;;
		
		}catch(Exception e) {}		
		}
	
	//�гο� �׸�(��,��,��)�� �׷��ִ� �޼ҵ�
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);

		g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); 	//��Ƽ�������
	  	g2.setStroke(new BasicStroke(2));															//�׵θ�
		  	
	  	//if(count<50) {
	  		alphaComposite(65);		//0~255���� ���� 	
	  		g2.drawImage(background, 0,0,null);
	  	//	count++;
	  	//}
	  	/*
	  	if(count>=50) {
	  		alphaComposite(65);
	  		g2.drawImage(background,0,0,null);
	  		count=0;
	  	}
	  	*/
	  	
	  	/*
		g2.setColor(effect);								//�ܻ� ȿ�� ����	//���� ���� ��ġ�� ��ĥ���� ������ ����� �޹���� ����̰ų� �������϶��� ����..
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());				//�̹����� �ְ����� �ܻ������� �ȵ��� �Ф�
		*/
	  	
	  	alphaComposite(OBJECT_ALPHA);	 //��渻�� �ٸ� �̹������� ������ �����ʴ´�.	
	  	setlevel.draw_setlevel(g2);      //�������� �� �ʱⰪ
	  	bar.draw_bar(g2);		 		 //�� �׸���
	  	drawBallList(g2); 	 	 		 //�� �׸���	  isball==true�϶�
	  	drawItemList(g2);		 		 //������ �׸���   isitem==true�϶�
	  	drawLife(g2);			 		 //������ �׸���
	  	drawScore(g2);			 		 //���� �׸���
	  	
	  	drawExplosionImage(g2);
		
		//repaint(); 		//�����忡�� repaint���� ����ٰ��ϸ� ���α׷� ������ �������� �ܻ��� ��� �������� X ���� �����
	}
	
	//���İ�(����)����
	public void alphaComposite(float afterimage) {		
		alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)afterimage/255); 	
	  	g2.setComposite(alphaComposite);
	}
	
	//ArrayList<Ball> ������ ��,�г� �ε����� �ñ輳�� && �̰� ��� Ȯ������� �ϴ� �͵�	//Panel_Game thread�� �־��� �κ�
	public void ballListEvent() {
		
		if(ballList.size()!=0) {		//ȭ�鿡 ���� ������
		for(int e=0;e<ballList.size();e++) {
			ball=ballList.get(e);
			ball.ballEvent();		
			}
		}//for i
	}
	//ArrayList<Ball>�� �� �׸��� �޼���
	public void drawBallList(Graphics2D g) {	
		
		if(ballList.size()!=0) {		//ȭ�鿡 ���� ������
		for(int d=0;d<ballList.size();d++) {
			ball=ballList.get(d);
			ball.drawBall(g);		
			}
		}//for i
	}
	//�����۰� ����� �ٰ� �ε����� ���� �̺�Ʈ ó��
	public void itemListEvent() {
		
		if(itemList.size()!=0) {		//ȭ�鿡 �������� ������
		for(int j=0;j<itemList.size();j++) {
			item=itemList.get(j);
			item.itemEvent();		
			}
		}//for i
	}
	//ArrayList<Item> �׸��� �޼���
	public void drawItemList(Graphics2D g) {
		
		if(itemList.size()!=0) {		//ȭ�鿡 �������� ������
		for(int j=0;j<itemList.size();j++) {
			item=itemList.get(j);
			item.drawItem(g);		
			}
		}//for i
	}
	
	public void drawExplosionImage(Graphics2D g2) {
	
		for(int i=0;i<explosionList.size();i++) {
			
			exp=explosionList.get(i);
			
			if(exp.isimage==true) {
				//explode����
				if(exp.state==1) {
				
				int set=3;
				//��� ���� 9*5�и������� ���� �׸��� �׸���~ 9*5�и������ ������ ���� �׸����� �̵�
				if(exp.explosioncnt/set==0)		  exp.imagenum=1; 
				else if(exp.explosioncnt/set==1)  exp.imagenum=2;
				else if(exp.explosioncnt/set==2)  exp.imagenum=3;
				else if(exp.explosioncnt/set==3)  exp.imagenum=4;
				else if(exp.explosioncnt/set==4)  exp.imagenum=5;
				else if(exp.explosioncnt/set==5)  exp.imagenum=6;
				else if(exp.explosioncnt/set==6)  exp.imagenum=7;
				else if(exp.explosioncnt/set==7)  exp.imagenum=8;
				else if(exp.explosioncnt/set==8)  exp.imagenum=9;
				else if(exp.explosioncnt/set==9)  exp.imagenum=10;
				else if(exp.explosioncnt/set==10) exp.imagenum=11;
				else if(exp.explosioncnt/set==11) exp.imagenum=12;
				else if(exp.explosioncnt/set==12) exp.imagenum=13;
				else if(exp.explosioncnt/set==13) exp.imagenum=14;
				else if(exp.explosioncnt/set==14) exp.imagenum=15;
				else if(exp.explosioncnt/set==15) exp.imagenum=16;
				else if(exp.explosioncnt/set==16) exp.imagenum=17;
				else if(exp.explosioncnt/set==17) exp.imagenum=18;
				else if(exp.explosioncnt/set==18) exp.imagenum=19;
				else if(exp.explosioncnt/set==19) exp.imagenum=20;
				else if(exp.explosioncnt/set==20) exp.imagenum=21;
				else if(exp.explosioncnt/set==21) exp.imagenum=22;
				else if(exp.explosioncnt/set==22) exp.imagenum=23;

				//���� explosioncnt/set�� 15���� ���� �� �� 9�и�������*5*15 �ð� ������ �� ���� �׸��� �׸���.
				if(exp.explosioncnt/set<24) {
					//imageX,imageY�� ���� �μ����� �� �� ���� x,y ��ǥ�� imageX,imageY�� �־��� 
					//paint()�ϱ� ���� ���� �����ϹǷ� ����X
					image=new ImageIcon(getClass().getResource("../image_explosion/explode"+(exp.imagenum)+".png")).getImage();
					g2.drawImage(image,exp.imageX-1,exp.imageY-13,null);
					exp.explosioncnt++;
				}
				//25�̻��̶�� �ʱ�ȭ
				else {
					exp.explosioncnt=0;
					exp.imagenum=0;
					exp.isimage=false;
				}
				}//state==1
				
				//explode_night����
				else if(exp.state==2) {
				
					int set=2;
					//��� ���� 9*5�и������� ���� �׸��� �׸���~ 9*5�и������ ������ ���� �׸����� �̵�
					if(exp.explosioncnt/set==0)		  exp.imagenum=1; 
					else if(exp.explosioncnt/set==1)  exp.imagenum=2;
					else if(exp.explosioncnt/set==2)  exp.imagenum=3;
					else if(exp.explosioncnt/set==3)  exp.imagenum=4;
					else if(exp.explosioncnt/set==4)  exp.imagenum=5;
					else if(exp.explosioncnt/set==5)  exp.imagenum=6;
					else if(exp.explosioncnt/set==6)  exp.imagenum=7;
					else if(exp.explosioncnt/set==7)  exp.imagenum=8;
					else if(exp.explosioncnt/set==8)  exp.imagenum=9;
					else if(exp.explosioncnt/set==9)  exp.imagenum=10;
					else if(exp.explosioncnt/set==10) exp.imagenum=11;
					else if(exp.explosioncnt/set==11) exp.imagenum=12;
					else if(exp.explosioncnt/set==12) exp.imagenum=13;
					else if(exp.explosioncnt/set==13) exp.imagenum=14;
					else if(exp.explosioncnt/set==14) exp.imagenum=15;
					else if(exp.explosioncnt/set==15)  exp.imagenum=16;
					else if(exp.explosioncnt/set==16)  exp.imagenum=17;
					else if(exp.explosioncnt/set==17)  exp.imagenum=18;
					else if(exp.explosioncnt/set==18)  exp.imagenum=19;
					else if(exp.explosioncnt/set==19)  exp.imagenum=20;
					else if(exp.explosioncnt/set==20) exp.imagenum=21;
					else if(exp.explosioncnt/set==21) exp.imagenum=22;
					else if(exp.explosioncnt/set==22) exp.imagenum=23;
					else if(exp.explosioncnt/set==23) exp.imagenum=24;
					else if(exp.explosioncnt/set==24)  exp.imagenum=25;
					else if(exp.explosioncnt/set==25)  exp.imagenum=26;
					else if(exp.explosioncnt/set==26)  exp.imagenum=27;
					else if(exp.explosioncnt/set==27)  exp.imagenum=28;
					else if(exp.explosioncnt/set==28)  exp.imagenum=29;
			
					//���� explosioncnt/set�� 15���� ���� �� �� 9�и�������*5*15 �ð� ������ �� ���� �׸��� �׸���.
					if(exp.explosioncnt/set<29) {
						//imageX,imageY�� ���� �μ����� �� �� ���� x,y ��ǥ�� imageX,imageY�� �־��� 
						//paint()�ϱ� ���� ���� �����ϹǷ� ����X
						image=new ImageIcon(getClass().getResource("../image_explosion_night/explode_night"+(exp.imagenum)+".png")).getImage();
						g2.drawImage(image,exp.imageX-26,exp.imageY-25,null);
						exp.explosioncnt++;
					}
					//25�̻��̶�� �ʱ�ȭ
					else {
						exp.explosioncnt=0;
						exp.imagenum=0;
						exp.isimage=false;
					}
				}//state==2
				
				//�ٽ�..... �̹��� ���� �Ҽ�������.. �� �ε巴�� ���� or �׵θ� �� �� �̻ڰ�^^
				else if(exp.state==3) {
					
					int set=5;
					int offset=1;
					
					//��� ���� 9*5�и������� ���� �׸��� �׸���~ 9*5�и������ ������ ���� �׸����� �̵�
					if(exp.explosioncnt/set==0)		 { exp.imageX--; exp.imageY--;  exp.width+=2; exp.height+=2;} 
					else if(exp.explosioncnt/set==1) { exp.imageX--; exp.imageY--;  exp.width+=2; exp.height+=2;}
					else if(exp.explosioncnt/set==2) { exp.imageX--; exp.imageY--; exp.width+=2; exp.height+=2;}
					//else if(exp.explosioncnt/set==3) { exp.imageX--; exp.imageY--;  exp.width+=2; exp.height+=2;}
					
					if(exp.explosioncnt/set<3) {
						g2.setColor(new Color(0,0,0,255));
						g2.drawRoundRect(exp.imageX, exp.imageY, exp.width, exp.height, 10, 10);
						exp.explosioncnt++;
					}
					else {
						exp.width=BRICK_WIDTH;
						exp.height=BRICK_HEIGHT;
						exp.isimage=false;
					}
				}//state==3
				
			}//isimage==true
		}//for
	}
	public int bar_iscollide() {			//�� �浹 Ȯ�� 1->�������� �ñ�� 2->�״�� ���⸸ �ٲ㼭 �� 3->���������� �ñ��
											//*���� �Ҹ��� �ּ� �浹�˻��ϴٰ� true�����̸� ���ñ�� -> false�� ��ȯ ���Ŀ� 2�� �ڿ� �ٽ� true������ ��ȯ�ؼ� �˻�
		
		if(bar.x_direction>0) {
			if((ball.x+DIAMETER+ball.x_direction>=bar.x+bar.x_direction)
					&&(ball.x+ball.x_direction<=bar.x+BAR_WIDTH+bar.x_direction)				
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+BAR_HEIGHT)) {
			explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
			score+=50;		//�ٿ� �ε����� �� 500��
			return 6;
			}
		}
		else if(bar.x_direction<0) {
			if((ball.x+DIAMETER+ball.x_direction>=bar.x+bar.x_direction)
					&&(ball.x+ball.x_direction<=bar.x+BAR_WIDTH+bar.x_direction)			
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+BAR_HEIGHT)) {
			explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
			score+=50;		//�ٿ� �ε����� �� 500��
			return 7;
			}
		}
		else if(bar.x_direction==0) {
			if((ball.x+DIAMETER+ball.x_direction>=bar.x+bar.x_direction)
					&&(ball.x+ball.x_direction<bar.x+(BAR_WIDTH-60)+bar.x_direction)				//������ �����ö��� �浹 Ȯ�� (0<=bar<=20) ����
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+2)) {										//��_�� <-�̷� ����
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//�ٿ� �ε����� �� 500��
				return 1;																	
			}
			else if((ball.x+ball.x_direction>=bar.x+(BAR_WIDTH-60)+bar.x_direction)
					&&(ball.x+DIAMETER+ball.x_direction<=bar.x+(BAR_WIDTH-20)+bar.x_direction)		//(20<bar<60) ����
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+2)) {
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//�ٿ� �ε����� �� 500��
				return 2;
			}
			else if((ball.x+DIAMETER+ball.x_direction>bar.x+(BAR_WIDTH-20)+bar.x_direction)
					&&(ball.x+ball.x_direction<=bar.x+BAR_WIDTH+bar.x_direction)					//(60<=bar<=80) ����
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+2)) {
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//�ٿ� �ε����� �� 500��
				return 3;
			}
			else if((ball.x+DIAMETER+ball.x_direction>=bar.x+bar.x_direction-4)
					&&(ball.x+DIAMETER+ball.x_direction<=bar.x+bar.x_direction+4)						//���� ���� ��(���� �ӵ���)
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+ball.y_direction<=bar.y+BAR_HEIGHT+2)) {	
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//�ٿ� �ε����� �� 500��
				return 4;
			}	
			else if((ball.x+ball.x_direction>=bar.x+BAR_WIDTH+bar.x_direction-4)
					&&(ball.x+ball.x_direction<=bar.x+BAR_WIDTH+bar.x_direction+4)					//���� �����ʸ�(���� �ӵ���)
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+ball.y_direction<=bar.y+BAR_HEIGHT+2)) {
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//�ٿ� �ε����� �� 500��
				return 5;
			}
		}//if(x_direction==0)
		return 0;		//��� �ش� �ȵ� ��
	}
	// �� �浹 Ȯ�� ���� �ӵ��ǻ��¸� Ball��ü�������� �������� �гο��� �ٷ� ����
	public int brick_iscollide() {														//* ���ϰ� brick���� ���� �� ������ ��
																						//* if ���ٸ��� return ������� or ��ȯ���� brick�ΰͿ��� ����
			for(int i=0;i<setlevel.bricklist.size();i++) {		
				brick=setlevel.bricklist.get(i);
				offset=setlevel.OFFSET/2;
			if(brick.isbrick==true) {		
				
				if((ball.x+DIAMETER+ball.x_direction>=brick.x-offset+4)				//���� ���� ����
					&&(ball.x+ball.x_direction<=brick.x+BRICK_WIDTH+offset-4)
					&&(ball.y+DIAMETER+ball.y_direction>=brick.y-offset)
					&&(ball.y+DIAMETER+ball.y_direction<=brick.y+offset+4)) {		
					
						if(ball.isfire==true) 		{
							brick.hp=brick.hp-3;
							score+=2000;		//���� �ε����� 2000
						}
						else if(ball.isfire==false) {
							brick.hp--;			//�� ü�� �Ҹ�
							score+=500;			//���� �ε����� 500
							if(brick.hp>0)explosionList.add(new Explosion_Animation(brick.x,brick.y,3,true));
						}
				
						if(brick.hp<=0) { 	
							brick.isbrick=false;				//�׸��� ����
							setlevel.bricklist.remove(i);				
							setlevel.isallcrush();				//�������� �˻�
							if(isloop==true)
								if(item.setRandomPercent(PERCENT)==true) 
									itemList.add(new Item(this, brick.x, brick.y, true));
							explosionList.add(new Explosion_Animation(brick.x,brick.y,1,true));
							
							score+=1000;		//���� ���� 500
						}
						return 1;
				}
				else if((ball.x+DIAMETER+ball.x_direction>=brick.x-offset+4)			//���� �Ʒ� ����
					&&(ball.x+ball.x_direction<=brick.x+BRICK_WIDTH+offset-4)
					&&(ball.y+ball.y_direction>=brick.y+BRICK_HEIGHT-offset-4)
					&&(ball.y+ball.y_direction<=brick.y+BRICK_HEIGHT+offset)) {
					
						if(ball.isfire==true) 		{
							brick.hp=brick.hp-3;
							score+=2000;		//���� �ε����� 2000
						}
						else if(ball.isfire==false) {
							brick.hp--;			//�� ü�� �Ҹ�
							score+=500;			//���� �ε����� 500
							if(brick.hp>0)explosionList.add(new Explosion_Animation(brick.x,brick.y,3,true));
						}
					
						if(brick.hp<=0) { 	
							brick.isbrick=false;				//�׸��� ����
							setlevel.bricklist.remove(i);				
							setlevel.isallcrush();				//�������� �˻�
							if(isloop==true)
								if(item.setRandomPercent(PERCENT)==true) 
									itemList.add(new Item(this, brick.x, brick.y, true));
							
							explosionList.add(new Explosion_Animation(brick.x,brick.y,1,true));
							
							score+=1000;		//���� ���� 500
						}	
						return 2;
				}
				else if((ball.x+DIAMETER+ball.x_direction>=brick.x-offset)			//���� ���� ����
					&&(ball.x+DIAMETER+ball.x_direction<=brick.x+offset+4)
					&&(ball.y+DIAMETER+ball.y_direction>=brick.y-offset+4)
					&&(ball.y+ball.y_direction<=brick.y+BRICK_HEIGHT+offset-4)) {	
					
						if(ball.isfire==true) 		{
							brick.hp=brick.hp-3;
							score+=2000;		//���� �ε����� 2000
						}
						else if(ball.isfire==false) {
							brick.hp--;			//�� ü�� �Ҹ�
							score+=500;			//���� �ε����� 500
							if(brick.hp>0)explosionList.add(new Explosion_Animation(brick.x,brick.y,3,true));
						}
					
						if(brick.hp<=0) { 	
							brick.isbrick=false;				//�׸��� ����
							setlevel.bricklist.remove(i);				
							setlevel.isallcrush();				//�������� �˻�
							if(isloop==true)
								if(item.setRandomPercent(PERCENT)==true) 
									itemList.add(new Item(this, brick.x, brick.y, true));
							
							explosionList.add(new Explosion_Animation(brick.x,brick.y,1,true));
							
							score+=1000;		//���� ���� 500
						}
						return 3;
				}
				else if((ball.x+ball.x_direction>=brick.x+BRICK_WIDTH-offset-4)		//���� ������ ����
					&&(ball.x+ball.x_direction<=brick.x+BRICK_WIDTH+offset)
					&&(ball.y+DIAMETER+ball.y_direction>=brick.y-offset+4)
					&&(ball.y+ball.y_direction<=brick.y+BRICK_HEIGHT+offset-4)) {
					
						if(ball.isfire==true) 		{
							brick.hp=brick.hp-3;
							score+=2000;		//���� �ε����� 2000
						}
						else if(ball.isfire==false) {
							brick.hp--;			//�� ü�� �Ҹ�
							score+=500;			//���� �ε����� 500
							if(brick.hp>0)explosionList.add(new Explosion_Animation(brick.x,brick.y,3,true));
						}
						
						if(brick.hp<=0) { 	
							brick.isbrick=false;				//�׸��� ����
							setlevel.bricklist.remove(i);				
							setlevel.isallcrush();				//�������� �˻�
							if(isloop==true)
								if(item.setRandomPercent(PERCENT)==true) 
									itemList.add(new Item(this, brick.x, brick.y, true));
							
							explosionList.add(new Explosion_Animation(brick.x,brick.y,1,true));
							
							score+=1000;		//���� ���� 500
						}
						return 4;
				}
			}//isbrick==true
		}//for
		return 0;		//�浹 ���� ��
	}//brick_iscross
	
	//������ �浹
	public int item_iscollide() {
		for(int i=0;i<itemList.size();i++) {
			item=itemList.get(i);
			if(item.isitem==true) {		//�������� ȭ�鿡 �׷����� ���� �˻���
				if(item.y>bar.y-50) {	//�ٿ� �Ÿ��� ��������� ���� �˻�!
					if((item.x+ITEM_DIAMETER>=bar.x+bar.x_direction)
						&&(item.x<=bar.x+BAR_WIDTH+bar.x_direction)
						&&(item.y+ITEM_DIAMETER+item.y_direction>=bar.y)
						&&(item.y+item.y_direction<=bar.y+BAR_HEIGHT)) {
						
							item.isitem=false;					//�������� �Ծ��� �� false
							itemList.remove(i);					//����Ʈ���� �����
							score+=2000;		//������ ������ 2000
							
							if(item.itemRandom==1)		return 1;
							else if(item.itemRandom==2)	return 2;
							else if(item.itemRandom==3)	return 3;
							else if(item.itemRandom==4)	return 4;
				}
				}//item.y>bar.y-50
			}//isitem==true
		}//for
		return 0;		//�ƹ��ϵ� X
	}//item_iscollide
	public void drawLife(Graphics2D g) {
		for(int i=0;i<life;i++) g.drawImage(heart,20+(i*15),550,null);
	}
	public void drawScore(Graphics2D g) {
		strscore=Integer.toString(score);
		g.setFont(new Font("DIALOG",Font.ITALIC|Font.BOLD,25));
		g.setColor(color.orange);
		g.drawString("Score  "+strscore, 20, 35);
	}
}
