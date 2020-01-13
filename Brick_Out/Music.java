package Brick_Out;


import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Music extends Thread{		// ���α׷��ȿ� ���� ���α׷�

	Player player;
	String name;
	File file;
	FileInputStream fis;				//����Ʈ �������� ��ǲ��Ʈ��
	//BufferedInputStream bis;			//�޸� ���۸� �������� - ���� ��� �ӵ���� 
	
	int randomnum;
	String str;
	boolean isloop;						//true: ���� ��� ��� false:�ѹ��� ���
	
	public Music(String name,boolean isloop){
		this.isloop=isloop;
		this.name=name;
	}
	//�� �ݺ�..
	@Override
	public void run(){			//Thread
		try{
			do{
				file=new File(getClass().getResource("../music/" + name +".mp3").toURI());
				fis=new FileInputStream(file);  
				player =new Player(fis);
				player.play();
			}while(isloop);
		}catch(Exception e){
		System.out.println(e.getMessage());
	}
}
}