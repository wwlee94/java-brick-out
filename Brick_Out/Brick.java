package Brick_Out;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Brick {

	Panel_Game panel;
	Color color;
	
	Random random;
	
	int x,y;
	int hp;				 //블럭의 체력
	
	Boolean isbrick;	 //블럭 상태  true:벽돌그리기 false:삭제 
	
	public Brick(Panel_Game brick_crush,int x,int y,int hp){
		this.panel=brick_crush;
		this.x=x;
		this.y=y;
		this.hp=hp;
		isbrick=true;
	}
	public void draw_greenbrick(Graphics2D g) {		//블럭 체력: 1 -> 초록
		g.setColor(color.green);
		g.fillRoundRect(x, y, panel.BRICK_WIDTH, panel.BRICK_HEIGHT, panel.ROUND, panel.ROUND);
		g.setColor(color.black);
		g.drawRoundRect(x, y, panel.BRICK_WIDTH, panel.BRICK_HEIGHT, panel.ROUND, panel.ROUND);
	}
	public void draw_yellowbrick(Graphics2D g){		//블럭 체력: 2 -> 노랑
		g.setColor(color.yellow);
		g.fillRoundRect(x, y, panel.BRICK_WIDTH, panel.BRICK_HEIGHT, panel.ROUND, panel.ROUND);
		g.setColor(color.black);
		g.drawRoundRect(x, y, panel.BRICK_WIDTH, panel.BRICK_HEIGHT, panel.ROUND, panel.ROUND);
	}	
	public void draw_blackbrick(Graphics2D g) {		//블럭 체력: 3 -> 검정
		g.setColor(color.black);
		g.fillRoundRect(x, y, panel.BRICK_WIDTH, panel.BRICK_HEIGHT, panel.ROUND, panel.ROUND);
		g.setColor(color.orange);
		g.drawRoundRect(x, y, panel.BRICK_WIDTH, panel.BRICK_HEIGHT, panel.ROUND, panel.ROUND);
	}
	// public void draw_yellowbrick();  //anything make brick you want to
}
