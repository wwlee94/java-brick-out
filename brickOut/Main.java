package Brick_Out;

import java.util.Random;

public class Main {
	public static final int SCREEN_WIDTH=800;
	public static final int SCREEN_HEIGHT=600;
	public static Music intromusic;
	
	public static void main(String args[]) {
		
		Random random=new Random();
		int rannum;
		rannum=random.nextInt(2)+1;
		intromusic=new Music("intromusic"+rannum,true);
		intromusic.start();
	}
}
