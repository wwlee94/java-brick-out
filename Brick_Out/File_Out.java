package Brick_Out;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//텍스트 파일 쓰기
public class File_Out {
	//파일은 상대경로로 못찾음.. //해결법 아래처럼 근데 왜 src파일이 아니라 bin파일에??
	//C:\Users\micke\Desktop\Java_termproject_Brickcrush_v1.1.0\bin\Brick_Out\..\rankscore.txt
	//(getPath()."rankname.txt") 얘는 어디에 생기는 파일!?? 뭔 차이?
	//C:\Users\micke\Desktop\Java_termproject_Brickcrush_v1.1.0\bin\Brick_Out\rankscore.txt
	
	File rankname=new File("src/text/rankname.txt");
	File rankscore=new File("src/text/rankscore.txt");
	
	String name;
	String score;
	
	boolean isreset;		//true: 초기화 false:쭉 이어씀
	//이름과 점수를 생성자에서 받아오자
	public File_Out(String p_name,String p_score,boolean p_isreset) {
		this.name=p_name;
		this.score=p_score;
		this.isreset=p_isreset;
		
		BufferedWriter bw1,bw2;
		
		try {
			if(isreset==false) {

			bw1=new BufferedWriter(new FileWriter(rankname,true));
			bw1.write(name);	
			bw1.newLine();		//다음라인으로
			bw1.close();
			
			bw2=new BufferedWriter(new FileWriter(rankscore,true));
			bw2.write(score);
			bw2.newLine();
			bw2.close();
			}
			else if(isreset==true) {	//초기화 시키기위한 조건 (아직 안쓰임 파일 초기화)
				bw1=new BufferedWriter(new FileWriter(rankname));
				bw1.close();
				bw2=new BufferedWriter(new FileWriter(rankscore));
				bw2.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
