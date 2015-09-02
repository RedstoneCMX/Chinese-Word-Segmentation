package hmm.hmmmain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import hmm.util.SplitByChineseSym;
import hmm.viterbi.viterbi;
import hmm.util.HmmMatrix;
import hmm.util.BMES;

public class hmmmain {
	
	//训练集训练
	public void  Trainset(String trainfile){
		BMES bmes = new BMES();
		HmmMatrix hmm = new HmmMatrix();
		bmes.run(trainfile,".\\bmes");
		hmm.buildMatrix(".\\bmes", ".\\mr.dict", "matp", "mata", "matb");
		return ;
	}
	//测试集测试
	public void Testset(String input, String output){
		SplitByChineseSym sb = new SplitByChineseSym();
		viterbi vb = new viterbi();
		vb.setDict(".\\mr.dict");
		vb.setMatrixA("mata");
		vb.setMatrixB("matb");
		vb.setMatrixP("matp");
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(input),"UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output),"UTF-8"));
			String line = null;
			try {
				while ((line = reader.readLine()) != null){
					String outLineString = "";
					line = line.replaceAll(" ", "");
					if(line.equals(""))continue;
					String[] SegLine = sb.splitStr(line);
					int cnt = 0;
					String symbol = sb.getSymbol();
					for (int i = 0; SegLine[i] != null; i++) {
						if(SegLine[i].isEmpty()) {outLineString += symbol.substring(cnt,cnt+1)+"  ";cnt++;continue;}
						outLineString += vb.run(SegLine[i]);
						if(cnt < symbol.length()) {outLineString += symbol.substring(cnt,cnt+1)+"  ";cnt++;}
					}
			//		System.out.println(outLineString);
				 writer.write(outLineString+"\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//直接输入进行测试
	public String Inputtest(String input){
		String output="";
		
		SplitByChineseSym sb = new SplitByChineseSym();
		viterbi vb = new viterbi();
		vb.setDict(".\\mr.dict");
		vb.setMatrixA("mata");
		vb.setMatrixB("matb");
		vb.setMatrixP("matp");
		String line = null;
		//token，没有指定分隔符默认情况下是空格换行等
		StringTokenizer tokens = new StringTokenizer(input);
		while(tokens.hasMoreElements()){
			line = tokens.nextToken();
			String outLineString = "";
			line = line.replaceAll(" ", "");
			if(line.equals(""))continue;
			String[] SegLine = sb.splitStr(line);
			int cnt = 0;
			String symbol = sb.getSymbol();
			for (int i = 0; SegLine[i] != null; i++) {
				if(SegLine[i].isEmpty()) {outLineString += symbol.substring(cnt,cnt+1)+"  ";cnt++;continue;}
				outLineString += vb.run(SegLine[i]);
				if(cnt < symbol.length()) {outLineString += symbol.substring(cnt,cnt+1)+"  ";cnt++;}
			}
			
			output = output + outLineString + "\n";
		}
		return output;
	}
}
