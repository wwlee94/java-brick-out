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
	
	static final int DIAMETER=15;  			//볼의 지름
	static final int XBALL=400;				//ball x,y의 초기 위치
	static final int YBALL=300;
	static final int XDIR=0;
	static final int YDIR=1;
	
	static int BAR_WIDTH=80;				//바의 가로 , 세로
	static final int BAR_HEIGHT=15;
	
	static final int BRICK_WIDTH=50;		//블럭의 가로 , 세로
	static final int BRICK_HEIGHT=25;
	
	static final int PERCENT=30;
	static final int ITEM_DIAMETER=20;
	
	static final int ROUND=5;				//RoundRect의 굴곡
	
	int stage;		//스테이지 번호
	int life;		//목숨
	int score;		//점수
	String strscore;
	
	int offset;		//블럭의 offset temp변수
	int count;		//10ms마다 count해주기 위한 temp변수
	int keycode;	//키보드 입력
	int overcount;	//게임오버배경화면 띄우기 카운트
	int imagenum;	//애니메이션 이미지 번호
	
	int explosioncnt;
	
	int OBJECT_ALPHA;
	int BACKGROUND_ALPHA;
	
	int mouseX,mouseY;	//마우스 x,y좌표
	
	int backgroundnum;	//1: 게임 성공 이미지 2:게임 오버 이미지
	Boolean isloop;			 //게임루프 true:게임 계속 진행 false:게임 종료
	Boolean isimage;		 //explosionimage true:그림 false:안그림
	
	Frame_Game game;
	
	Thread thread;
	
	Color color;
	Graphics2D g2;
	
	Image background;		//배경 이미지
	Image heart;			//생명력 이미지
	Image image;			//폭발 이미지
	AlphaComposite alphaComposite;
	
	ArrayList<Ball> ballList;	
	ArrayList<Item> itemList;
	ArrayList<Explosion_Animation> explosionList;
	Explosion_Animation exp;
	
	Brick brick; 	//ArrayList<Brick>의 값를 저장하기 위한 임시변수
	Ball ball;		//ArrayList<Ball>의 값을 저장하기 위한 임시변수	
	Item item;		//ArrayList<Item>의 값을 저장하기 위한 임시변수	
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
		
		//폭발 애니메이션을 그릴 리스트 유무받아서
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
		playbutton.setBorderPainted(false); 		// 버튼테두리
		playbutton.setContentAreaFilled(false);  // 버튼영역배경표시
		playbutton.setFocusPainted(false); 	// 버튼포커스표시
		playbutton.setFocusable(false); 		//버튼에 초점 맞추지 X
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
		pausebutton.setBorderPainted(false); 		// 버튼테두리
		pausebutton.setContentAreaFilled(false);  // 버튼영역배경표시
		pausebutton.setFocusPainted(false); 	// 버튼포커스표시
		pausebutton.setFocusable(false); 		//버튼에 초점 맞추지 X
		pausebutton.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				thread.suspend();
				Main.intromusic.suspend();
				OBJECT_ALPHA=150;
				for(int i=0;i<10;i++) repaint();		//일시정지하면 화면 흐려지게 만드려고
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
		powerbutton.setBorderPainted(false); 		// 버튼테두리
		powerbutton.setContentAreaFilled(false);  // 버튼영역배경표시
		powerbutton.setFocusPainted(false); 	// 버튼포커스표시
		powerbutton.setFocusable(false); 		//버튼에 초점 맞추지 X
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
		//this.setOpaque(false);		//잔상 안남음
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
			
			public void keyReleased(KeyEvent e) {		//키 떼면 멈춤			//flush?? 키 버퍼 처리
					bar.x_direction=0;
				}
		});//addkeylistener
		this.addMouseListener(new MouseAdapter() { // 메뉴바의 이벤트
			@Override
			public void mousePressed(MouseEvent e) { // 마우스가 메뉴바를 눌렀을때
				mouseX = e.getX(); // 누른 마우스의 x,y좌표를 가져옴
				mouseY = e.getY();
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if((e.getY()>=0&&e.getY()<=50)) {
					//e.getXOnsScreen() -> 전체 화면크기에서 현재 프레임 x,y위치
					game.setLocation(e.getXOnScreen()-mouseX, e.getYOnScreen()-mouseY);
				}
				else {
					bar.x=e.getX()-(BAR_WIDTH/2);
				}
			}
		});
		
	}//public Brick_Crush
	
	//쓰레드 게임 루프
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			setlevel.selectlevel();
			
			while(isloop) {
			ballListEvent();						//공 연산 + 충돌확인	
			itemListEvent();						//아이템과 사용자 바 충돌 확인
			bar.barEvent();							//bar x,y좌표 변경하는 부분
			repaint();
			Thread.sleep(9);			//100분의 1초 단위로 페인트와,공움직임에 시간 간격을 두는 것임 
			}//while
			
		//게임 종료 상태
		//게임 종료 상태 -> copy,fire아이템이 적용중에도 gameover화면 설정을 위함
		score+=life*2000;				//현재 Life*2000점 추가 획득
	
		OBJECT_ALPHA=150;
		
		for(int i=0;i<100;i++) {		//슬로우 모션
			ballListEvent();
			itemListEvent();
			bar.barEvent();
			repaint();
			Thread.sleep(50);
			overcount++;
			//약1.5초뒤 배경 설정
			if(overcount==35) {
				game.setSize(800,500);
				game.setLocationRelativeTo(null);
				if(backgroundnum==1) {
					background=new ImageIcon(getClass().getResource("../image/background_gameclear.jpg")).getImage();
					Music clearmusic=new Music("어린이들환호성",false);
					clearmusic.start();
				}
				else if(backgroundnum==2) 
					background=new ImageIcon(getClass().getResource("../image/background_gameover.jpg")).getImage();
				
			}
			if(OBJECT_ALPHA>0) OBJECT_ALPHA-=5;
		}//for
		
		game.dispose();					//현재 화면 종료
		
		new Frame_Rank(score,true);		//새로운 랭킹창 띄움
		
		Thread.sleep(1500);
		Main.intromusic.resume();
		
		//interrupt()?? 쓰레드,음악 종료 해줘야함!?
		//thread.interrupt();				//얘를 Frame_Rank()보다 빨리 실행시키면 image load오류;;
		
		}catch(Exception e) {}		
		}
	
	//패널에 그림(공,바,블럭)을 그려주는 메소드
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);

		g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); 	//안티엘리어싱
	  	g2.setStroke(new BasicStroke(2));															//테두리
		  	
	  	//if(count<50) {
	  		alphaComposite(65);		//0~255투명도 설정 	
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
		g2.setColor(effect);								//잔상 효과 투명도	//투명도 색은 합치면 합칠수록 연해짐 얘들은 뒷배경이 흰색이거나 검은색일때만 적용..
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());				//이미지를 넣고나서는 잔상적용이 안됐음 ㅠㅠ
		*/
	  	
	  	alphaComposite(OBJECT_ALPHA);	 //배경말고 다른 이미지들은 투명도를 주지않는다.	
	  	setlevel.draw_setlevel(g2);      //스테이지 블럭 초기값
	  	bar.draw_bar(g2);		 		 //바 그리기
	  	drawBallList(g2); 	 	 		 //공 그리기	  isball==true일때
	  	drawItemList(g2);		 		 //아이템 그리기   isitem==true일때
	  	drawLife(g2);			 		 //라이프 그리기
	  	drawScore(g2);			 		 //점수 그리기
	  	
	  	drawExplosionImage(g2);
		
		//repaint(); 		//쓰레드에다 repaint말고 여기다가하면 프로그램 연산이 개빠르니 잔상이 길게 생기지도 X 슬맆 줘야함
	}
	
	//알파값(투명도)설정
	public void alphaComposite(float afterimage) {		
		alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)afterimage/255); 	
	  	g2.setComposite(alphaComposite);
	}
	
	//ArrayList<Ball> 공들이 블럭,패널 부딪힐때 팅김설정 && 이건 계속 확인해줘야 하는 것들	//Panel_Game thread에 넣어줄 부분
	public void ballListEvent() {
		
		if(ballList.size()!=0) {		//화면에 공이 있으면
		for(int e=0;e<ballList.size();e++) {
			ball=ballList.get(e);
			ball.ballEvent();		
			}
		}//for i
	}
	//ArrayList<Ball>의 볼 그리기 메서드
	public void drawBallList(Graphics2D g) {	
		
		if(ballList.size()!=0) {		//화면에 공이 있으면
		for(int d=0;d<ballList.size();d++) {
			ball=ballList.get(d);
			ball.drawBall(g);		
			}
		}//for i
	}
	//아이템과 사용자 바가 부딪혔을 때의 이벤트 처리
	public void itemListEvent() {
		
		if(itemList.size()!=0) {		//화면에 아이템이 있으면
		for(int j=0;j<itemList.size();j++) {
			item=itemList.get(j);
			item.itemEvent();		
			}
		}//for i
	}
	//ArrayList<Item> 그리기 메서드
	public void drawItemList(Graphics2D g) {
		
		if(itemList.size()!=0) {		//화면에 아이템이 있으면
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
				//explode상태
				if(exp.state==1) {
				
				int set=3;
				//모두 각각 9*5밀리세컨즈 동안 그림을 그린다~ 9*5밀리세컨즈가 지나면 다음 그림으로 이동
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

				//현재 explosioncnt/set이 15보다 작을 때 즉 9밀리세컨즈*5*15 시간 지나기 전 까진 그림을 그린다.
				if(exp.explosioncnt/set<24) {
					//imageX,imageY는 블럭이 부서졌을 때 그 블럭의 x,y 좌표를 imageX,imageY에 넣어줌 
					//paint()하기 전에 연산 먼저하므로 문제X
					image=new ImageIcon(getClass().getResource("../image_explosion/explode"+(exp.imagenum)+".png")).getImage();
					g2.drawImage(image,exp.imageX-1,exp.imageY-13,null);
					exp.explosioncnt++;
				}
				//25이상이라면 초기화
				else {
					exp.explosioncnt=0;
					exp.imagenum=0;
					exp.isimage=false;
				}
				}//state==1
				
				//explode_night상태
				else if(exp.state==2) {
				
					int set=2;
					//모두 각각 9*5밀리세컨즈 동안 그림을 그린다~ 9*5밀리세컨즈가 지나면 다음 그림으로 이동
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
			
					//현재 explosioncnt/set이 15보다 작을 때 즉 9밀리세컨즈*5*15 시간 지나기 전 까진 그림을 그린다.
					if(exp.explosioncnt/set<29) {
						//imageX,imageY는 블럭이 부서졌을 때 그 블럭의 x,y 좌표를 imageX,imageY에 넣어줌 
						//paint()하기 전에 연산 먼저하므로 문제X
						image=new ImageIcon(getClass().getResource("../image_explosion_night/explode_night"+(exp.imagenum)+".png")).getImage();
						g2.drawImage(image,exp.imageX-26,exp.imageY-25,null);
						exp.explosioncnt++;
					}
					//25이상이라면 초기화
					else {
						exp.explosioncnt=0;
						exp.imagenum=0;
						exp.isimage=false;
					}
				}//state==2
				
				//다시..... 이미지 간격 소수점으로.. 더 부드럽게 하자 or 테두리 좀 더 이쁘게^^
				else if(exp.state==3) {
					
					int set=5;
					int offset=1;
					
					//모두 각각 9*5밀리세컨즈 동안 그림을 그린다~ 9*5밀리세컨즈가 지나면 다음 그림으로 이동
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
	public int bar_iscollide() {			//바 충돌 확인 1->왼쪽으로 팅기게 2->그대로 방향만 바꿔서 팅 3->오른쪽으로 팅기게
											//*판정 불린값 둬서 충돌검사하다가 true상태이면 바팅기고 -> false로 변환 이후에 2초 뒤에 다시 true값으로 변환해서 검사
		
		if(bar.x_direction>0) {
			if((ball.x+DIAMETER+ball.x_direction>=bar.x+bar.x_direction)
					&&(ball.x+ball.x_direction<=bar.x+BAR_WIDTH+bar.x_direction)				
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+BAR_HEIGHT)) {
			explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
			score+=50;		//바에 부딪쳤을 때 500점
			return 6;
			}
		}
		else if(bar.x_direction<0) {
			if((ball.x+DIAMETER+ball.x_direction>=bar.x+bar.x_direction)
					&&(ball.x+ball.x_direction<=bar.x+BAR_WIDTH+bar.x_direction)			
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+BAR_HEIGHT)) {
			explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
			score+=50;		//바에 부딪쳤을 때 500점
			return 7;
			}
		}
		else if(bar.x_direction==0) {
			if((ball.x+DIAMETER+ball.x_direction>=bar.x+bar.x_direction)
					&&(ball.x+ball.x_direction<bar.x+(BAR_WIDTH-60)+bar.x_direction)				//위에서 내려올때의 충돌 확인 (0<=bar<=20) 범위
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+2)) {										//ㅁ_ㅁ <-이런 범위
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//바에 부딪쳤을 때 500점
				return 1;																	
			}
			else if((ball.x+ball.x_direction>=bar.x+(BAR_WIDTH-60)+bar.x_direction)
					&&(ball.x+DIAMETER+ball.x_direction<=bar.x+(BAR_WIDTH-20)+bar.x_direction)		//(20<bar<60) 범위
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+2)) {
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//바에 부딪쳤을 때 500점
				return 2;
			}
			else if((ball.x+DIAMETER+ball.x_direction>bar.x+(BAR_WIDTH-20)+bar.x_direction)
					&&(ball.x+ball.x_direction<=bar.x+BAR_WIDTH+bar.x_direction)					//(60<=bar<=80) 범위
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+DIAMETER+ball.y_direction<=bar.y+2)) {
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//바에 부딪쳤을 때 500점
				return 3;
			}
			else if((ball.x+DIAMETER+ball.x_direction>=bar.x+bar.x_direction-4)
					&&(ball.x+DIAMETER+ball.x_direction<=bar.x+bar.x_direction+4)						//바의 왼쪽 면(바의 속도도)
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+ball.y_direction<=bar.y+BAR_HEIGHT+2)) {	
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//바에 부딪쳤을 때 500점
				return 4;
			}	
			else if((ball.x+ball.x_direction>=bar.x+BAR_WIDTH+bar.x_direction-4)
					&&(ball.x+ball.x_direction<=bar.x+BAR_WIDTH+bar.x_direction+4)					//바의 오른쪽면(바의 속도도)
					&&(ball.y+DIAMETER+ball.y_direction>=bar.y-2)
					&&(ball.y+ball.y_direction<=bar.y+BAR_HEIGHT+2)) {
				explosionList.add(new Explosion_Animation(ball.x,ball.y,2,true));
				score+=50;		//바에 부딪쳤을 때 500점
				return 5;
			}
		}//if(x_direction==0)
		return 0;		//모두 해당 안될 때
	}
	// 블럭 충돌 확인 공의 속도의상태만 Ball객체에서변경 나머지는 패널에서 바로 변경
	public int brick_iscollide() {														//* 리턴값 brick으로 했을 때 오류난 듯
																						//* if 마다마다 return 안줬었음 or 반환값이 brick인것에서 오류
			for(int i=0;i<setlevel.bricklist.size();i++) {		
				brick=setlevel.bricklist.get(i);
				offset=setlevel.OFFSET/2;
			if(brick.isbrick==true) {		
				
				if((ball.x+DIAMETER+ball.x_direction>=brick.x-offset+4)				//블럭의 윗면 범위
					&&(ball.x+ball.x_direction<=brick.x+BRICK_WIDTH+offset-4)
					&&(ball.y+DIAMETER+ball.y_direction>=brick.y-offset)
					&&(ball.y+DIAMETER+ball.y_direction<=brick.y+offset+4)) {		
					
						if(ball.isfire==true) 		{
							brick.hp=brick.hp-3;
							score+=2000;		//벽돌 부딪히면 2000
						}
						else if(ball.isfire==false) {
							brick.hp--;			//블럭 체력 소모
							score+=500;			//벽돌 부딪히면 500
							if(brick.hp>0)explosionList.add(new Explosion_Animation(brick.x,brick.y,3,true));
						}
				
						if(brick.hp<=0) { 	
							brick.isbrick=false;				//그리기 삭제
							setlevel.bricklist.remove(i);				
							setlevel.isallcrush();				//종료인지 검사
							if(isloop==true)
								if(item.setRandomPercent(PERCENT)==true) 
									itemList.add(new Item(this, brick.x, brick.y, true));
							explosionList.add(new Explosion_Animation(brick.x,brick.y,1,true));
							
							score+=1000;		//벽돌 깨면 500
						}
						return 1;
				}
				else if((ball.x+DIAMETER+ball.x_direction>=brick.x-offset+4)			//블럭의 아래 범위
					&&(ball.x+ball.x_direction<=brick.x+BRICK_WIDTH+offset-4)
					&&(ball.y+ball.y_direction>=brick.y+BRICK_HEIGHT-offset-4)
					&&(ball.y+ball.y_direction<=brick.y+BRICK_HEIGHT+offset)) {
					
						if(ball.isfire==true) 		{
							brick.hp=brick.hp-3;
							score+=2000;		//벽돌 부딪히면 2000
						}
						else if(ball.isfire==false) {
							brick.hp--;			//블럭 체력 소모
							score+=500;			//벽돌 부딪히면 500
							if(brick.hp>0)explosionList.add(new Explosion_Animation(brick.x,brick.y,3,true));
						}
					
						if(brick.hp<=0) { 	
							brick.isbrick=false;				//그리기 삭제
							setlevel.bricklist.remove(i);				
							setlevel.isallcrush();				//종료인지 검사
							if(isloop==true)
								if(item.setRandomPercent(PERCENT)==true) 
									itemList.add(new Item(this, brick.x, brick.y, true));
							
							explosionList.add(new Explosion_Animation(brick.x,brick.y,1,true));
							
							score+=1000;		//벽돌 깨면 500
						}	
						return 2;
				}
				else if((ball.x+DIAMETER+ball.x_direction>=brick.x-offset)			//블럭의 왼쪽 범위
					&&(ball.x+DIAMETER+ball.x_direction<=brick.x+offset+4)
					&&(ball.y+DIAMETER+ball.y_direction>=brick.y-offset+4)
					&&(ball.y+ball.y_direction<=brick.y+BRICK_HEIGHT+offset-4)) {	
					
						if(ball.isfire==true) 		{
							brick.hp=brick.hp-3;
							score+=2000;		//벽돌 부딪히면 2000
						}
						else if(ball.isfire==false) {
							brick.hp--;			//블럭 체력 소모
							score+=500;			//벽돌 부딪히면 500
							if(brick.hp>0)explosionList.add(new Explosion_Animation(brick.x,brick.y,3,true));
						}
					
						if(brick.hp<=0) { 	
							brick.isbrick=false;				//그리기 삭제
							setlevel.bricklist.remove(i);				
							setlevel.isallcrush();				//종료인지 검사
							if(isloop==true)
								if(item.setRandomPercent(PERCENT)==true) 
									itemList.add(new Item(this, brick.x, brick.y, true));
							
							explosionList.add(new Explosion_Animation(brick.x,brick.y,1,true));
							
							score+=1000;		//벽돌 깨면 500
						}
						return 3;
				}
				else if((ball.x+ball.x_direction>=brick.x+BRICK_WIDTH-offset-4)		//블럭의 오른쪽 범위
					&&(ball.x+ball.x_direction<=brick.x+BRICK_WIDTH+offset)
					&&(ball.y+DIAMETER+ball.y_direction>=brick.y-offset+4)
					&&(ball.y+ball.y_direction<=brick.y+BRICK_HEIGHT+offset-4)) {
					
						if(ball.isfire==true) 		{
							brick.hp=brick.hp-3;
							score+=2000;		//벽돌 부딪히면 2000
						}
						else if(ball.isfire==false) {
							brick.hp--;			//블럭 체력 소모
							score+=500;			//벽돌 부딪히면 500
							if(brick.hp>0)explosionList.add(new Explosion_Animation(brick.x,brick.y,3,true));
						}
						
						if(brick.hp<=0) { 	
							brick.isbrick=false;				//그리기 삭제
							setlevel.bricklist.remove(i);				
							setlevel.isallcrush();				//종료인지 검사
							if(isloop==true)
								if(item.setRandomPercent(PERCENT)==true) 
									itemList.add(new Item(this, brick.x, brick.y, true));
							
							explosionList.add(new Explosion_Animation(brick.x,brick.y,1,true));
							
							score+=1000;		//벽돌 깨면 500
						}
						return 4;
				}
			}//isbrick==true
		}//for
		return 0;		//충돌 없을 때
	}//brick_iscross
	
	//아이템 충돌
	public int item_iscollide() {
		for(int i=0;i<itemList.size();i++) {
			item=itemList.get(i);
			if(item.isitem==true) {		//아이템이 화면에 그려졌을 때만 검사함
				if(item.y>bar.y-50) {	//바와 거리가 가까워졌을 때만 검사!
					if((item.x+ITEM_DIAMETER>=bar.x+bar.x_direction)
						&&(item.x<=bar.x+BAR_WIDTH+bar.x_direction)
						&&(item.y+ITEM_DIAMETER+item.y_direction>=bar.y)
						&&(item.y+item.y_direction<=bar.y+BAR_HEIGHT)) {
						
							item.isitem=false;					//아이템을 먹었을 때 false
							itemList.remove(i);					//리스트에서 비워줌
							score+=2000;		//아이템 먹으면 2000
							
							if(item.itemRandom==1)		return 1;
							else if(item.itemRandom==2)	return 2;
							else if(item.itemRandom==3)	return 3;
							else if(item.itemRandom==4)	return 4;
				}
				}//item.y>bar.y-50
			}//isitem==true
		}//for
		return 0;		//아무일도 X
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
