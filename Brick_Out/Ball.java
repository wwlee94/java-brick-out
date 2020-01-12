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
	 
	 int x_direction,y_direction;     //���� �ӵ�
	 int x,y;					 	  //���� ��ġ ��ǥ
	 
	 int randomnum;
	 
	 int bar_state;   //�� �浹 ->�� �����Ӻ�ȭ�ֱ� ���� ����
	 int brick_state; //�� �浹 -> �� �����Ӻ�ȭ�ֱ� ���� ����
	 int item_state;  //������ �浹 -> �� ������ ��ȭ�ֱ� ���� ����
	
	 Boolean isball;		//isball true:ȭ�鿡 �� �׷��� false:ȭ�鿡 �� ����
	 Boolean isfire;		//isfire true:fireball���� false:��� ����
	 Boolean isfireupside;	//isfire�� ���·� �г� ��ܿ� �¾��� �� true:�¾��� �� false:�ƴ� ��
	 Boolean iscopy;		//iscopy true:copyball���� false:��� ����
	 
	 
	//���� ��� �ε�ĥ �� �Ǵ��ϴ� �� �гο����� �ƴ϶� �� ��ü����!?
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
	
    public void drawBall(Graphics2D g) {		//isball:true �� ���� ���� �׸�
    	
    	if(isball==true) {
    		if(isfire==true) {
    			g.setColor(color.red);
    			g.fillOval(x, y, panel.DIAMETER, panel.DIAMETER);		//x,y��ǥ ���ٸ��� ��� �׷��ִ� ����
    			g.setColor(color.yellow);
    			g.drawOval(x, y, panel.DIAMETER, panel.DIAMETER);
    		}
    		else if(iscopy==true) {
    			g.setColor(color.black);
    			g.fillOval(x, y, panel.DIAMETER, panel.DIAMETER);		//x,y��ǥ ���ٸ��� ��� �׷��ִ� ����
    			g.setColor(color.orange);	//or g.setColor(color.white);
    			g.drawOval(x, y, panel.DIAMETER, panel.DIAMETER);
    		}
    		else {
    			g.setColor(color.white);
    			g.fillOval(x, y, panel.DIAMETER, panel.DIAMETER);		//x,y��ǥ ���ٸ��� ��� �׷��ִ� ����
    			g.setColor(color.black);
    			g.drawOval(x, y, panel.DIAMETER, panel.DIAMETER);
    		}
    	}
    }
    //ball �̺�Ʈ ����� ��Ƶ� ��
    public void ballEvent() {					//isball:true �� ���� �� ������
    	
    	if(isball==true) {
    		bar_move_event(); 		//bar�� x ����  ball�� x,y���� ����
    		brick_move_event();		//ball�� x,y���⸸ ����, ȭ�鿡�� �浹�� ����
    		panel_move_event();		//ball�� x,y���⸸ ����
    		item_move_event();		//���� ��������� �浹 �Ǻ�
    		move();   	            //����  x,y��ǥ �̵��ϴ� �κ�
    	}
    }
    //���� ������ 
    public void move() {
    	x=x+x_direction;	//x������ x_directionũ�� ��ŭ ��� �����ϰų� �پ���
		y=y+y_direction;	//y������ y_directionũ�� ��ŭ ��� �����ϰų� �پ���
    }
	//�ٿ� ���� �浹 �̺�Ʈó��
	public void bar_move_event() {
		Music barmusic=new Music("barmusic",false);
		bar_state=panel.bar_iscollide();	
		switch(bar_state) {
		case 0:
			break;
		case 1:
			barmusic.start();
			x_direction=-1;			//X->0~20���� //-1,-4
			y_direction=-3;	
			break;
		case 2:						//X->20~60����//3
			barmusic.start();
			y_direction=-2;			
			break;			
		case 3:						//X->60~80����//1,-4
			barmusic.start();
			x_direction=1;			
			y_direction=-3;		
			break;
		case 4:						//�ǿ��ʺκ� Y->0~15����//-4,1
			barmusic.start();
			x_direction=-4;
			y_direction=-2;
			panel.bar.x_direction=-panel.bar.x_direction;
			break;
		case 5:						//�ǿ����ʺκ� Y->0~15����//4,-1
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
	
	//�гΰ� ���� �浹 �̺�Ʈó��
	public void panel_move_event() {
		Music panelmusic=new Music("crushmusic",false);
		//��,�� �г� �ñ�
		if(x+x_direction<0||x+x_direction>panel.getWidth()-panel.DIAMETER) {
			panelmusic.start();
			panel.explosionList.add(new Explosion_Animation(x,y,2,true));
			x_direction=-x_direction;  //  ||<-- �� --> or   <-- �� -->||
		}
		//��� ��
		else if(y+y_direction<0) {		
			panelmusic.start();
			panel.explosionList.add(new Explosion_Animation(x,y,2,true));
			y_direction=-y_direction;
			isfireupside=true;
		}	
		//�ϴ� �� �ϸ� ��� -- 0���Ǹ� *���� ����*
		else if(y+y_direction>panel.getHeight()) {
			//ȭ�鿡 ���� �ϳ� ���� ��
			if(panel.ballList.size()==1) {			
				panel.life--;				//���--		
				//����� 0�� ���� ��
				if(panel.life==0) {			
					
					panel.isloop=false; 	 //gamepanel���� life=0||brickList.size()==0�̸� ���� ����
					
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
			//ȭ�鿡 ���� ������ ���� ��(copy item�� �Ծ��� ��Ȳ�̰���?)
			else if(panel.ballList.size()!=1){			
				 isball=false;
				 for(int p=0;p<panel.ballList.size();p++)
					 if(panel.ballList.get(p).isball==false) {
						 panel.ballList.remove(p);
					 }
			}//size()!=1
		}
	}//ball_range
	
	//���� ���� �浹 �̺�Ʈó��
	public void brick_move_event() {
		Music brickmusic=new Music("crushmusic",false);
		brick_state=panel.brick_iscollide();
			switch(brick_state) {
			case 0:
				break;
			case 1:		//���
				brickmusic.start();
				if(isfire==false)	y_direction=-2;		//isfire���°� �ƴҶ� ƨ���
				else if(isfire==true&&isfireupside==true) {	//isfire���¿��� �г� ���κ� ������ ���� ���� ���� ����
					y_direction=-2;						//�� ������ ���� ������ �մ´�!!
				}
				break;																	
			case 2:		//�ϴ�
				brickmusic.start();
				if(isfire==false)	y_direction=2;
				else if(isfire==true&&isfireupside==true) {	//isfire���¿��� �г� ���κ� ������ ���� ���� ���� ����
					y_direction=2;						//�� ������ ���� ������ �մ´�!!
				}
				break;
			case 3:		//�ǿ��� ����
				brickmusic.start();
				if(isfire==false)	x_direction=-2;
				else if(isfire==true&&isfireupside==true) {	//isfire���¿��� �г� ���κ� ������ ���� ���� ���� ����
					x_direction=-2;						//�� ������ ���� ������ �մ´�!!
				}
				break;
			case 4:		//�ǿ����� ����
				brickmusic.start();
				if(isfire==false)	x_direction=2;
				else if(isfire==true&&isfireupside==true) {	//isfire���¿��� �г� ���κ� ������ ���� ���� ���� ����
					x_direction=2;						//�� ������ ���� ������ �մ´�!!
				}
				break;
			}
		}
	
	//�ٿ� ������ �浹 �̺�Ʈó��
	//�����۰� ���� �浹 �� ����
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
			//isfire���°� �ƴҶ� copy ���ȭ�鼳��
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
							this.cancel(); //10�� ������ Ÿ�̸� ����
						}
						//isfire�����̸� Ÿ�̸� ���� -> isfire�� �켱���� �� ���� 
						//isfire�������� �԰� -> iscopy�������� ���� ����!!
						if(panel.ballList.get(0).isfire==true) {
							panel.background=new ImageIcon(getClass().getResource("../image/background_sunset.jpg")).getImage();
							for(int i=0;i<panel.ballList.size();i++) panel.ballList.get(i).iscopy=false;
							this.cancel();
						}
						if(panel.isloop==false) this.cancel();	//���� ���� �����϶��� Ÿ�̸� ����-> ������ ����ÿ��� �� ��� ���氡��
					}//run()
				}, 10, 10);
		
			
			if((x_direction>=0&&y_direction<=0)							//��������� ���ʻ��,�������ϴ��� ��
					||((x_direction<=0&&y_direction>0))) {				//�����ʻ��,�����ϴ����� ƨ���
				panel.ballList.add(new Ball(panel, x, y, 2, 1,true));
				panel.ballList.add(new Ball(panel, x, y,-2,-1,true));
			}
			else if((x_direction>=0&&y_direction>=0)						//��������� �����ʻ��,�����ϴ��� ��
					||(x_direction<=0&&y_direction<=0)) {					//���� ���,������ �ϴ����� ƨ���
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
					if(count<500) {		//10��
						panel.background=new ImageIcon(getClass().getResource("../image/background_sunset.jpg")).getImage();
						for(int i=0;i<panel.ballList.size();i++) {
							panel.ballList.get(i).isfire=true;
						//	panel.ballList.get(i).isfireupside=false; ��� �����ϸ�  X
						}
						count++;
					}
					else if(count>=500) {
						panel.background=new ImageIcon(getClass().getResource("../image/background_basic.jpg")).getImage();
						for(int i=0;i<panel.ballList.size();i++) panel.ballList.get(i).isfire=false;
						this.cancel();	//10�� ������ Ÿ�̸� ����
					}
					if(panel.isloop==false) this.cancel();		//���� ���� �����϶��� Ÿ�̸� ���� -> ������ ����ÿ��� �� ��� ���氡��
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
					if(barcount<100) {							//5��
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
						this.cancel();		//Ÿ�̸� ����
					}
					if(panel.isloop==false) {
						panel.BAR_WIDTH=80;
						this.cancel();		//���� ���� �����϶��� Ÿ�̸� ���� -> ������ ����ÿ��� �� ��� ���氡��
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
