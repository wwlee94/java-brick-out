package Brick_Out;


import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Music extends Thread{		// 프로그램안에 작은 프로그램

	Player player;
	String name;
	File file;
	FileInputStream fis;				//바이트 형식으로 인풋스트림
	//BufferedInputStream bis;			//메모리 버퍼를 생성시켜 - 성능 향상 속도향상 
	
	int randomnum;
	String str;
	boolean isloop;						//true: 파일 계속 재생 false:한번만 재생
	
	public Music(String name,boolean isloop){
		this.isloop=isloop;
		this.name=name;
	}
	//곡 반복..
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