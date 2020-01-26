package Brick_Out;

public class Explosion_Animation {
	
	//1:explode 2:explode_night
	int imagenum;		//이미지 번호
	int explosioncnt;	//9밀리세컨즈마다 +1 카운트
	int imageX,imageY;	//이미지의 x,y좌표
	
	//3:brick collide
	int width;
	int height;

	int state;			//이미지의 상태 1:explode 2:explode_night 3:brickcollide
	
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
