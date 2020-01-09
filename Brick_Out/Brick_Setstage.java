package Brick_Out;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Brick_Setstage {

	static final int OFFSET=10;			//�� ���� ���� ������ �� �Ʒ�
	static final int FIRST_X=70;		//���� �׸� ù��° x��ǥ , y��ǥ
	static final int FIRST_Y=50;
	
	Panel_Game panel;
	Brick brick;				//temp����
	
	int brick_x,brick_y;
	
	ArrayList<Brick> bricklist;			//Brick�� ArrayList

	//������ �Լ�
	public Brick_Setstage(Panel_Game brick_crush){
		this.panel=brick_crush;
		
		bricklist=new ArrayList<Brick>();
	
	}
	//stage������ ���� �������� ����	//Panel_Game thread������ �� �־��� �κ�
public void selectlevel() {
		
		if(panel.stage==1) {
			bricklist.clear();
			for(int i=0;i<11;i++)							//�������Լ����� �ʱ�ȭ ���� x,y��ǥ�� �����ٸ��� ������ �ִ�.
				for(int j=0;j<4;j++) {
					brick_x=FIRST_X+i*rowset();
					brick_y=FIRST_Y+j*colset();
					if(i==0||i==10||j==0||j==3)
						bricklist.add(new Brick(panel,brick_x,brick_y,3));		//(�ǿ��� ������||�ǿ����� ������||������||�ǾƷ���)
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
	
	//�������� �ش� �������� ���� �׷��ִ� �޼ҵ�
	public void draw_setlevel(Graphics2D g) {
		if(panel.stage==1||panel.stage==2||panel.stage==3) {
			for(int i=0;i<bricklist.size();i++) {
				brick=bricklist.get(i);
				if(brick.isbrick==true) 
					if(brick.hp==1) brick.draw_greenbrick(g);			//hp=1: �ʷ�, hp=2: ���, hp=3: ����
					else if(brick.hp==2) brick.draw_yellowbrick(g);			
					else if(brick.hp==3) brick.draw_blackbrick(g);
			}//for
		}//stage
	}
	
	
	//���� ���� ���� -> �� ���� ���ϰ� �ϱ� ����
	public int rowset() {					
		return panel.BRICK_WIDTH+OFFSET;
	}
	
	//���� ���� ���� -> �� ���� ���ϰ� �ϱ� ����
	public int colset() {							
		return panel.BRICK_HEIGHT+OFFSET;
	}
	
	//isallcrush: ���� ���� �� ȭ������ ��� ���� �μ����� *���� ����* ����
	public void isallcrush() {		
			if(bricklist.size()==0) {
				panel.isloop=false;		//gamepanel���� life=0||size()==0�̸� ���� ����	
				
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
