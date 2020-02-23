package Brick_Out;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class File_In {
	
	File rankname=new File("src/text/rankname.txt");
	File rankscore=new File("src/text/rankscore.txt");
	
	String str[];			//파일의 모든 이름 정보
	String readname;		//이름파일읽어오기
	String readscore;		//점수파일 읽어오기
	int score[];				//파일의 모든 점수 정보
	int count;
	int rank;				//랭킹 순위
	String strtmp;			//stringtemp
	int tmp;				//inttemp
	int max;				//내림차순을 위한 변수
	
	public File_In() {
		
		BufferedReader br1,br2;
		
		str=new String[10];
		score=new int[10];
		count=0;
		
		try {
			
			br1=new BufferedReader(new FileReader(rankscore));
			br2=new BufferedReader(new FileReader(rankname));
			
			//why readline=br1.readLine();
			//while(readline!=null)-> 오류..?
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
					
					//숫자 순서가 바뀌었으면 문자 순서도 바뀌어야함
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
