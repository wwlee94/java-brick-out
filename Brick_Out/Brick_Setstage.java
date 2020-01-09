package Brick_Out;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Brick_Setstage {

	static final int OFFSET=10;			//블럭 사이 간격 오른쪽 과 아래
	static final int FIRST_X=70;		//블럭을 그릴 첫번째 x좌표 , y좌표
	static final int FIRST_Y=50;
	
	Panel_Game panel;
	Brick brick;				//temp역할
	
	int brick_x,brick_y;
	
	ArrayList<Brick> bricklist;			//Brick의 ArrayList

	//생성자 함수
	public Brick_Setstage(Panel_Game brick_crush){
		this.panel=brick_crush;
		
		bricklist=new ArrayList<Brick>();
	
	}
	//stage변수에 따라 스테이지 변경	//Panel_Game thread시작전 에 넣어줄 부분
public void selectlevel() {
		
		if(panel.stage==1) {
			bricklist.clear();
			for(int i=0;i<11;i++)							//생성자함수에서 초기화 해준 x,y좌표를 블럭마다마다 가지고 있다.
				for(int j=0;j<4;j++) {
					brick_x=FIRST_X+i*rowset();
					brick_y=FIRST_Y+j*colset();
					if(i==0||i==10||j==0||j==3)
						bricklist.add(new Brick(panel,brick_x,brick_y,3));		//(맨왼쪽 세로줄||맨오른쪽 세로줄||맨윗줄||맨아래줄)
					else if(i==1||i==3||i==5||i==7) bricklist.add(new Brick(panel,brick_x,brick_y,1));
					else bricklist.add(new Brick(panel,brick_x,brick_y,2));
				}
		}//panel.stage==1
		else if(panel.stage==2) {
			bricklist.clear();
			for(int i=0;i<11;i++)
				for(int j=0;j<4;j++) {
					brick_x=FIRST_X+i*rowset();
					brick_y=FIRST_Y+j*colset();
					if(j==0)
						bricklist.add(new Brick(panel,brick_x,brick_y,3));
					else if(j==1)
						bricklist.add(new Brick(panel,brick_x,brick_y,2));
					else if(j==3||j==2)
						bricklist.add(new Brick(panel,brick_x,brick_y,1));
				}
		}
		else if(panel.stage==3) {
			bricklist.clear();
			for(int i=0;i<2;i++)
				for(int j=0;j<2;j++) {
					brick_x=FIRST_X+i*rowset();
					brick_y=FIRST_Y+j*colset();
					bricklist.add(new Brick(panel,brick_x,brick_y,1));
				}
		}
	}
	
	//블럭깨기의 해당 스테이지 블럭을 그려주는 메소드
	public void draw_setlevel(Graphics2D g) {
		if(panel.stage==1||panel.stage==2||panel.stage==3) {
			for(int i=0;i<bricklist.size();i++) {
				brick=bricklist.get(i);
				if(brick.isbrick==true) 
					if(brick.hp==1) brick.draw_greenbrick(g);			//hp=1: 초록, hp=2: 노랑, hp=3: 검정
					else if(brick.hp==2) brick.draw_yellowbrick(g);			
					else if(brick.hp==3) brick.draw_blackbrick(g);
			}//for
		}//stage
	}
	
	
	//벽돌 가로 정렬 -> 블럭 정의 편하게 하기 위함
	public int rowset() {					
		return panel.BRICK_WIDTH+OFFSET;
	}
	
	//벽돌 세로 정렬 -> 블럭 정의 편하게 하기 위함
	public int colset() {							
		return panel.BRICK_HEIGHT+OFFSET;
	}
	
	//isallcrush: 벽돌 깼을 때 화면위의 모든 블럭이 부서지면 *게임 종료* 연산
	public void isallcrush() {		
			if(bricklist.size()==0) {
				panel.isloop=false;		//gamepanel에서 life=0||size()==0이면 게임 종료	
				
				Music endmusic=new Music("gameovermusic",false);
				endmusic.start();
				Main.intromusic.isloop=false;
				Main.intromusic.suspend();
				//Main.intromusic.player.close();
				//Main.intromusic.interrupt();
				
				panel.backgroundnum=1;
			}
	}
}
