package Brick_Out;
import java.awt.Color;
import java.awt.Graphics2D;

public class User_Bar {
	 static final int BAR_X=370;		//초기 bar x,y
	 static final int BAR_Y=530;
	 
	 private Panel_Game panel;
	 Color color;
	 
	 int x_direction;					//속도 좌우로만 움직이기에 x속도만 가져온 것
	 int x,y;
	
	public User_Bar(Panel_Game brick_crush) {
		this.panel=brick_crush;
		x_direction=0;
		x=BAR_X;
		y=BAR_Y;
	}
	
	public void draw_bar(Graphics2D g) {			//사각형 그려주는 메소드
		//g.clearRect(x-5, BAR_Y-5,  bc.BAR_WIDTH+10, bc.BAR_HEIGHT+10);
		g.setColor(color.cyan);
		g.fillRoundRect(x, y, panel.BAR_WIDTH, panel.BAR_HEIGHT, 10, 10);
		g.setColor(color.black);
		g.drawRoundRect(x, y, panel.BAR_WIDTH, panel.BAR_HEIGHT, 10, 10);
	}
	
	//Panel_Game thread에 넣어줄 부분
	public void barEvent() {		//우선 키보드로 입력받고 좌우로만 움직인다.		
		if((x>=0)&&(x<=panel.getWidth()-panel.BAR_WIDTH)) {//해당 범위 에서만 움직임
			x=x+x_direction;
		}
		if(x>-10&&x<0) {							//해당범위에서 벗어나면 움직이지 않으므로 벗어나면 위치를 해당범위 안으로 옮겨줌
			x=0;
		}
		if((x>panel.getWidth()-panel.BAR_WIDTH)&&(x<panel.getWidth()-panel.BAR_WIDTH+10)){
			x=(panel.getWidth()-panel.BAR_WIDTH-1);
		}
	}
}
