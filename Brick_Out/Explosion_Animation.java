package Brick_Out;

public class Explosion_Animation {
	
	//1:explode 2:explode_night
	int imagenum;		//�̹��� ��ȣ
	int explosioncnt;	//9�и�������� +1 ī��Ʈ
	int imageX,imageY;	//�̹����� x,y��ǥ
	
	//3:brick collide
	int width;
	int height;

	int state;			//�̹����� ���� 1:explode 2:explode_night 3:brickcollide
	
	Boolean isimage;
	
	public Explosion_Animation(int imageX,int imageY,int state,Boolean isimage){
		this.imageX=imageX;
		this.imageY=imageY;
		this.state=state;
		this.isimage=isimage;
		
		imagenum=0;
		explosioncnt=0;
		
		width=Panel_Game.BRICK_WIDTH;
		height=Panel_Game.BRICK_HEIGHT;
	}

}
