package Brick_Out;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

public class Item {							
	
	Panel_Game panel;
	
	Color color;
	
	Random random;
	
	Font f;
	
	int x,y;
	int y_direction;
	int itemRandom;			//1:Copyball 2:fireball 3:barexpand 4:extralife
	
	Boolean isitem;			//isitem true:������ �׸��� false:������ �ȱ׸���
	
	public Item(Panel_Game brick_out, int x, int y, Boolean isitem) {
		this.panel=brick_out;
		this.x=x;
		this.y=y;
		this.isitem=isitem;	//isitem true:������ �׸��� false:������ �ȱ׸���
		
		y_direction=1;
		
		random=new Random();	
		
		f=new Font("���ﳲ��ü B", Font.BOLD, 15);
		
		itemRandom=setRandomItem();
	}
	//per%Ȯ���� true�� ��ȯ�ϴ� �޼��� (1���� 100�� ������ �����ϰ� �� ���� per���� ������ true)
	public Boolean setRandomPercent(int per) {
		if((random.nextInt(100)+1)<=per) return true;
		
		return false;
	}
	//0~3 ������ ��ȯ �޼���-���� ���� ������ ���� 
	public int setRandomItem() {
		return random.nextInt(4)+1;
	}
	public void itemEvent() {
		if(isitem==true) {
			if(y+y_direction>panel.getHeight()) {
					isitem=false;
					for(int i=0;i<panel.itemList.size();i++) {
						if(panel.itemList.get(i).isitem==false) {
							panel.itemList.remove(i);
						}
					}
			}
			y=y+y_direction;
		}
	}
	//Ȯ���� ������ ���� �������� ȭ�鿡 �׷��ִ� �޼ҵ�		//isitem:true�� ���� �׸�
	public void drawItem(Graphics2D g) {
			if(isitem==true) {
				switch(itemRandom) {
				case 1: drawCopyBall(g);  break;
				case 2: drawFireBall(g);  break;
				case 3: drawLongBar(g);   break;
				case 4: drawExtraLife(g); break;
				}
			}//isitem==true
	}
	//���׶� ������  x,y ��ǥ
	public int setItemX() {
		return x+15;
	}
	public int setItemY() {
		return y+4;
	}
	public void drawCopyBall(Graphics2D g){
		g.setColor(color.magenta);
		g.fillOval(setItemX(), setItemY(), panel.ITEM_DIAMETER, panel.ITEM_DIAMETER);
		g.setColor(color.black);
		g.drawOval(setItemX(), setItemY(), panel.ITEM_DIAMETER, panel.ITEM_DIAMETER);
		g.setFont(f);
		g.drawString("C", setItemX()+5, setItemY()+15);
		}
	public void drawFireBall(Graphics2D g){
		g.setColor(color.RED);
		g.fillOval(setItemX(), setItemY(), panel.ITEM_DIAMETER, panel.ITEM_DIAMETER);
		g.setColor(color.black);
		g.drawOval(setItemX(), setItemY(), panel.ITEM_DIAMETER, panel.ITEM_DIAMETER);
		g.setFont(f);
		g.drawString("F", setItemX()+6, setItemY()+16);
	}
	public void drawLongBar(Graphics2D g){
		g.setColor(color.cyan);
		g.fillOval(setItemX(), setItemY(), panel.ITEM_DIAMETER, panel.ITEM_DIAMETER);
		g.setColor(color.black);
		g.drawOval(setItemX(), setItemY(), panel.ITEM_DIAMETER, panel.ITEM_DIAMETER);
		g.setFont(f);
		g.drawString("L", setItemX()+6, setItemY()+15);
	}
	public void drawExtraLife(Graphics2D g){
		g.setColor(color.YELLOW);
		g.fillOval(setItemX(), setItemY(), panel.ITEM_DIAMETER, panel.ITEM_DIAMETER);
		g.setColor(color.black);
		g.drawOval(setItemX(), setItemY(), panel.ITEM_DIAMETER, panel.ITEM_DIAMETER);
		g.setFont(f);
		g.setColor(color.red);
		g.drawString("��", setItemX()+4, setItemY()+16);
	}
}
