package Brick_Out;
import java.awt.Color;
import java.awt.Graphics2D;

public class User_Bar {
	 static final int BAR_X=370;		//�ʱ� bar x,y
	 static final int BAR_Y=530;
	 
	 private Panel_Game panel;
	 Color color;
	 
	 int x_direction;					//�ӵ� �¿�θ� �����̱⿡ x�ӵ��� ������ ��
	 int x,y;
	
	public User_Bar(Panel_Game brick_crush) {
		this.panel=brick_crush;
		x_direction=0;
		x=BAR_X;
		y=BAR_Y;
	}
	
	public void draw_bar(Graphics2D g) {			//�簢�� �׷��ִ� �޼ҵ�
		//g.clearRect(x-5, BAR_Y-5,  bc.BAR_WIDTH+10, bc.BAR_HEIGHT+10);
		g.setColor(color.cyan);
		g.fillRoundRect(x, y, panel.BAR_WIDTH, panel.BAR_HEIGHT, 10, 10);
		g.setColor(color.black);
		g.drawRoundRect(x, y, panel.BAR_WIDTH, panel.BAR_HEIGHT, 10, 10);
	}
	
	//Panel_Game thread�� �־��� �κ�
	public void barEvent() {		//�켱 Ű����� �Է¹ް� �¿�θ� �����δ�.		
		if((x>=0)&&(x<=panel.getWidth()-panel.BAR_WIDTH)) {//�ش� ���� ������ ������
			x=x+x_direction;
		}
		if(x>-10&&x<0) {							//�ش�������� ����� �������� �����Ƿ� ����� ��ġ�� �ش���� ������ �Ű���
			x=0;
		}
		if((x>panel.getWidth()-panel.BAR_WIDTH)&&(x<panel.getWidth()-panel.BAR_WIDTH+10)){
			x=(panel.getWidth()-panel.BAR_WIDTH-1);
		}
	}
}
