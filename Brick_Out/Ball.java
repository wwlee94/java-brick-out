package Brick_Out;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;


public class Ball 
{
	 Panel_Game panel;
	 Color color;
	
	 Random random;
	 
	 int x_direction,y_direction;     //공의 속도
	 int x,y;					 	  //공의 위치 좌표
	 
	 int randomnum;
	 
	 int bar_state;   //바 충돌 ->공 움직임변화주기 위한 변수
	 int brick_state; //블럭 충돌 -> 공 움직임변화주기 위한 변수
	 int item_state;  //아이템 충돌 -> 공 움직임 변화주기 위한 변수
	
	 Boolean isball;		//isball true:화면에 공 그려줌 false:화면에 공 삭제
	 Boolean isfire;		//isfire true:fireball상태 false:평소 상태
	 Boolean isfireupside;	//isfire볼 상태로 패널 상단에 맞았을 때 true:맞았을 때 false:아닐 때
	 Boolean iscopy;		//iscopy true:copyball상태 false:평소 상태
	 
	 
	//볼이 어딘가 부딪칠 때 판단하는 건 패널에서가 아니라 볼 객체에서!?
	public Ball(Panel_Game brick_Out,int x,int y,int xdir,int ydir,Boolean isball){
		this.panel=brick_Out;
		this.x=x;
		this.y=y;
		this.x_direction=xdir;
		this.y_direction=ydir;
		this.isball=isball;
		
		random=new Random();
		
		isfireupside=false;
		isfire=false;
		iscopy=false;
	}
	
    public void drawBall(Graphics2D g) {		//isball:true 일 때만 공을 그림
    	
    	if(isball==true) {
    		if(isfire==true) {
    			g.setColor(color.red);
    			g.fillOval(x, y, panel.DIAMETER, panel.DIAMETER);		//x,y좌표 마다마다 계속 그려주는 역할
    			g.setColor(color.yellow);
    			g.drawOval(x, y, panel.DIAMETER, panel.DIAMETER);
    		}
    		else if(iscopy==true) {
    			g.setColor(color.black);
    			g.fillOval(x, y, panel.DIAMETER, panel.DIAMETER);		//x,y좌표 마다마다 계속 그려주는 역할
    			g.setColor(color.orange);	//or g.setColor(color.white);
    			g.drawOval(x, y, panel.DIAMETER, panel.DIAMETER);
    		}
    		else {
    			g.setColor(color.white);
    			g.fillOval(x, y, panel.DIAMETER, panel.DIAMETER);		//x,y좌표 마다마다 계속 그려주는 역할
    			g.setColor(color.black);
    			g.drawOval(x, y, panel.DIAMETER, panel.DIAMETER);
    		}
    	}
    }
    //ball 이벤트 연산들 모아둔 것
    public void ballEvent() {					//isball:true 일 때만 공 연산함
    	
    	if(isball==true) {
    		bar_move_event(); 		//bar의 x 방향  ball의 x,y방향 변경
    		brick_move_event();		//ball의 x,y방향만 변경, 화면에서 충돌시 삭제
    		panel_move_event();		//ball의 x,y방향만 변경
    		item_move_event();		//공과 복사아이템 충돌 판별
    		move();   	            //실제  x,y좌표 이동하는 부분
    	}
    }
    //공의 움직임 
    public void move() {
    	x=x+x_direction;	//x방향은 x_direction크기 만큼 계속 증가하거나 줄어든다
		y=y+y_direction;	//y방향은 y_direction크기 만큼 계속 증가하거나 줄어든다
    }
	//바와 공의 충돌 이벤트처리
	public void bar_move_event() {
		Music barmusic=new Music("barmusic",false);
		bar_state=panel.bar_iscollide();	
		switch(bar_state) {
		case 0:
			break;
		case 1:
			barmusic.start();
			x_direction=-1;			//X->0~20범위 //-1,-4
			y_direction=-3;	
			break;
		case 2:						//X->20~60범위//3
			barmusic.start();
			y_direction=-2;			
			break;			
		case 3:						//X->60~80범위//1,-4
			barmusic.start();
			x_direction=1;			
			y_direction=-3;		
			break;
		case 4:						//맨왼쪽부분 Y->0~15범위//-4,1
			barmusic.start();
			x_direction=-4;
			y_direction=-2;
			panel.bar.x_direction=-panel.bar.x_direction;
			break;
		case 5:						//맨오른쪽부분 Y->0~15범위//4,-1
			barmusic.start();
			x_direction=4;
			y_direction=-2;
			panel.bar.x_direction=-panel.bar.x_direction;
			break;
		case 6: //x_direction>0
			barmusic.start();
			x_direction=4;
			y_direction=-3;
			break;
		case 7:	//x_direction<0
			barmusic.start();
			x_direction=-4;
			y_direction=-3;
			break;
		}
	}//bar_collision()
	
	//패널과 공의 충돌 이벤트처리
	public void panel_move_event() {
		Music panelmusic=new Music("crushmusic",false);
		//좌,우 패널 팅김
		if(x+x_direction<0||x+x_direction>panel.getWidth()-panel.DIAMETER) {
			panelmusic.start();
			panel.explosionList.add(new Explosion_Animation(x,y,2,true));
			x_direction=-x_direction;  //  ||<-- 팅 --> or   <-- 팅 -->||
		}
		//상단 팅
		else if(y+y_direction<0) {		
			panelmusic.start();
			panel.explosionList.add(new Explosion_Animation(x,y,2,true));
			y_direction=-y_direction;
			isfireupside=true;
		}	
		//하단 팅 하면 목숨 -- 0개되면 *게임 오버*
		else if(y+y_direction>panel.getHeight()) {
			//화면에 공이 하나 있을 때
			if(panel.ballList.size()==1) {			
				panel.life--;				//목숨--		
				//목숨이 0개 됬을 때
				if(panel.life==0) {			
					
					panel.isloop=false; 	 //gamepanel에서 life=0||brickList.size()==0이면 게임 종료
					
					Music gameovermusic=new Music("gameovermusic",false);
					gameovermusic.start();
					Main.intromusic.isloop=false;
					Main.intromusic.suspend();
					//Main.intromusic.player.close();
					//Main.intromusic.interrupt();
					
					panel.backgroundnum=2;
					
				}
				x=panel.XBALL;
				y=panel.YBALL;
				randomnum=random.nextInt(2);
				if(randomnum==0) {
					x_direction=1;
					y_direction=1;
				}
				else if(randomnum==1) {
					x_direction=-1;
					y_direction=1;
				}
			}//size()==1
			//화면에 공이 여러개 있을 때(copy item을 먹었을 상황이겠쥬?)
			else if(panel.ballList.size()!=1){			
				 isball=false;
				 for(int p=0;p<panel.ballList.size();p++)
					 if(panel.ballList.get(p).isball==false) {
						 panel.ballList.remove(p);
					 }
			}//size()!=1
		}
	}//ball_range
	
	//블럭과 공의 충돌 이벤트처리
	public void brick_move_event() {
		Music brickmusic=new Music("crushmusic",false);
		brick_state=panel.brick_iscollide();
			switch(brick_state) {
			case 0:
				break;
			case 1:		//상단
				brickmusic.start();
				if(isfire==false)	y_direction=-2;		//isfire상태가 아닐때 튕기고
				else if(isfire==true&&isfireupside==true) {	//isfire상태에서 패널 윗부분 맞으면 공이 블럭을 뚫지 못함
					y_direction=-2;						//그 전까진 공이 벽돌을 뚫는다!!
				}
				break;																	
			case 2:		//하단
				brickmusic.start();
				if(isfire==false)	y_direction=2;
				else if(isfire==true&&isfireupside==true) {	//isfire상태에서 패널 윗부분 맞으면 공이 블럭을 뚫지 못함
					y_direction=2;						//그 전까진 공이 벽돌을 뚫는다!!
				}
				break;
			case 3:		//맨왼쪽 벽돌
				brickmusic.start();
				if(isfire==false)	x_direction=-2;
				else if(isfire==true&&isfireupside==true) {	//isfire상태에서 패널 윗부분 맞으면 공이 블럭을 뚫지 못함
					x_direction=-2;						//그 전까진 공이 벽돌을 뚫는다!!
				}
				break;
			case 4:		//맨오른쪽 벽돌
				brickmusic.start();
				if(isfire==false)	x_direction=2;
				else if(isfire==true&&isfireupside==true) {	//isfire상태에서 패널 윗부분 맞으면 공이 블럭을 뚫지 못함
					x_direction=2;						//그 전까진 공이 벽돌을 뚫는다!!
				}
				break;
			}
		}
	
	//바와 아이템 충돌 이벤트처리
	//아이템과 공이 충돌 한 순간
	public void item_move_event() {			//1:Copyball 2:fireball 3:barexpand 4:extralife
		Music ballitemmusic=new Music("ballitemmusic",false);
		Music longbarmusic=new Music("longbarmusic",false);
		Music extralifemusic=new Music("extralifemusic",false);
		item_state=panel.item_iscollide();
		switch(item_state) {
		case 0:
			break;
		case 1:			
			ballitemmusic.start();
			//isfire상태가 아닐때 copy 배경화면설정
				new Timer().schedule(new TimerTask() {
					int count=0;
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(count<500) {
							panel.background=new ImageIcon(getClass().getResource("../image/background_night.jpg")).getImage();
							if(panel.ballList.get(0).isfire==false) {
								for(int i=0;i<panel.ballList.size();i++) panel.ballList.get(i).iscopy=true;
								}
							count++;
						}
						else if(count>=500) {
							panel.background=new ImageIcon(getClass().getResource("../image/background_basic.jpg")).getImage();
							for(int i=0;i<panel.ballList.size();i++) panel.ballList.get(i).iscopy=false;
							this.cancel(); //10초 지나면 타이머 종료
						}
						//isfire상태이면 타이머 종료 -> isfire가 우선순위 더 높게 
						//isfire아이템을 먹고 -> iscopy아이템을 먹은 상태!!
						if(panel.ballList.get(0).isfire==true) {
							panel.background=new ImageIcon(getClass().getResource("../image/background_sunset.jpg")).getImage();
							for(int i=0;i<panel.ballList.size();i++) panel.ballList.get(i).iscopy=false;
							this.cancel();
						}
						if(panel.isloop==false) this.cancel();	//게임 종료 상태일때는 타이머 종료-> 아이템 적용시에도 뒷 배경 변경가능
					}//run()
				}, 10, 10);
		
			
			if((x_direction>=0&&y_direction<=0)							//진행방향이 왼쪽상단,오른쪽하단일 때
					||((x_direction<=0&&y_direction>0))) {				//오른쪽상단,왼쪽하단으로 튕기게
				panel.ballList.add(new Ball(panel, x, y, 2, 1,true));
				panel.ballList.add(new Ball(panel, x, y,-2,-1,true));
			}
			else if((x_direction>=0&&y_direction>=0)						//진행방향이 오른쪽상단,왼쪽하단일 때
					||(x_direction<=0&&y_direction<=0)) {					//왼쪽 상단,오른쪽 하단으로 튕기게
				panel.ballList.add(new Ball(panel, x, y,-2, 1,true));
				panel.ballList.add(new Ball(panel, x, y, 2,-1,true));
			}
			
			System.out.println("Copyball");
			break;
			
		case 2:
			ballitemmusic.start();
			
			for(int i=0;i<panel.ballList.size();i++) {
				panel.ballList.get(i).isfireupside=false;
			}
			
			new Timer().schedule(new TimerTask() {
				int count=0;
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(count<500) {		//10초
						panel.background=new ImageIcon(getClass().getResource("../image/background_sunset.jpg")).getImage();
						for(int i=0;i<panel.ballList.size();i++) {
							panel.ballList.get(i).isfire=true;
						//	panel.ballList.get(i).isfireupside=false; 얘는 같이하면  X
						}
						count++;
					}
					else if(count>=500) {
						panel.background=new ImageIcon(getClass().getResource("../image/background_basic.jpg")).getImage();
						for(int i=0;i<panel.ballList.size();i++) panel.ballList.get(i).isfire=false;
						this.cancel();	//10초 지나면 타이머 종료
					}
					if(panel.isloop==false) this.cancel();		//게임 종료 상태일때는 타이머 종료 -> 아이템 적용시에도 뒷 배경 변경가능
				}
			}, 10,10);
			
			System.out.println("fireball");
			break;
			
		case 3:
			longbarmusic.start();
			new Timer().schedule(new TimerTask() {
				int barcount=0;
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(barcount<100) {							//5초
						if(panel.BAR_WIDTH<=160) {
							panel.BAR_WIDTH=panel.BAR_WIDTH+2;
							panel.bar.x=panel.bar.x-1;
						}
						barcount++;
					}
					else if(barcount>=100&&barcount<140) {		//
						if(panel.BAR_WIDTH>=80) {
							panel.BAR_WIDTH=panel.BAR_WIDTH-2;
							panel.bar.x=panel.bar.x+1;
						}
						barcount++;
					}
					else if(barcount>=140){
						this.cancel();		//타이머 종료
					}
					if(panel.isloop==false) {
						panel.BAR_WIDTH=80;
						this.cancel();		//게임 종료 상태일때는 타이머 종료 -> 아이템 적용시에도 뒷 배경 변경가능
					}
				}
			}, 10,50);
			System.out.println("barexpand");
			break;
			
		case 4:
			extralifemusic.start();
			panel.life++;
			System.out.println("extralife");
			break;
	}
	}
}
