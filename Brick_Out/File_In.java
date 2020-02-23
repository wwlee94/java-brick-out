package Brick_Out;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class File_In {
	
	File rankname=new File("src/text/rankname.txt");
	File rankscore=new File("src/text/rankscore.txt");
	
	String str[];			//������ ��� �̸� ����
	String readname;		//�̸������о����
	String readscore;		//�������� �о����
	int score[];				//������ ��� ���� ����
	int count;
	int rank;				//��ŷ ����
	String strtmp;			//stringtemp
	int tmp;				//inttemp
	int max;				//���������� ���� ����
	
	public File_In() {
		
		BufferedReader br1,br2;
		
		str=new String[10];
		score=new int[10];
		count=0;
		
		try {
			
			br1=new BufferedReader(new FileReader(rankscore));
			br2=new BufferedReader(new FileReader(rankname));
			
			//why readline=br1.readLine();
			//while(readline!=null)-> ����..?
			while((readscore=br1.readLine())!=null) {
				score[count]=Integer.parseInt(readscore);
				count++;
			}		
			
			count=0;
			while((readname=br2.readLine())!=null) {
				str[count]=readname;
				count++;
			}
			
			for(int i=0;i<count;i++) {
				max=i;
				for(int j=i;j<count;j++) {
					if(score[j]>score[i]) {
					max=j;
					
					tmp=score[i];
					score[i]=score[max];
					score[max]=tmp;
					
					//���� ������ �ٲ������ ���� ������ �ٲ�����
					strtmp=str[i];
					str[i]=str[max];
					str[max]=strtmp;
					}
				}
			}

			br1.close();
			br2.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
