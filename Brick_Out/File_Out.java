package Brick_Out;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//�ؽ�Ʈ ���� ����
public class File_Out {
	//������ ����η� ��ã��.. //�ذ�� �Ʒ�ó�� �ٵ� �� src������ �ƴ϶� bin���Ͽ�??
	//C:\Users\micke\Desktop\Java_termproject_Brickcrush_v1.1.0\bin\Brick_Out\..\rankscore.txt
	//(getPath()."rankname.txt") ��� ��� ����� ����!?? �� ����?
	//C:\Users\micke\Desktop\Java_termproject_Brickcrush_v1.1.0\bin\Brick_Out\rankscore.txt
	
	File rankname=new File("src/text/rankname.txt");
	File rankscore=new File("src/text/rankscore.txt");
	
	String name;
	String score;
	
	boolean isreset;		//true: �ʱ�ȭ false:�� �̾
	//�̸��� ������ �����ڿ��� �޾ƿ���
	public File_Out(String p_name,String p_score,boolean p_isreset) {
		this.name=p_name;
		this.score=p_score;
		this.isreset=p_isreset;
		
		BufferedWriter bw1,bw2;
		
		try {
			if(isreset==false) {

			bw1=new BufferedWriter(new FileWriter(rankname,true));
			bw1.write(name);	
			bw1.newLine();		//������������
			bw1.close();
			
			bw2=new BufferedWriter(new FileWriter(rankscore,true));
			bw2.write(score);
			bw2.newLine();
			bw2.close();
			}
			else if(isreset==true) {	//�ʱ�ȭ ��Ű������ ���� (���� �Ⱦ��� ���� �ʱ�ȭ)
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
