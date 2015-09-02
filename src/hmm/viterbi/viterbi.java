package hmm.viterbi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import hmm.util.TrimStr;

public class viterbi {
	 final String ChineseSym = "“”：；？《》。，！———－、）（【】［］●’‘,/?\\{}[]()~⋯℃α→『』∥";
	 private  String MatrixA;
	 private  String MatrixB;
	 private  String MatrixP;
	 private  String dict;
	 //中文标点
	 private boolean isChSym(String str){
		  if(ChineseSym.contains(str)) return true;
		  else return false;
	  }
	private String trimStr(String str) {
			String reStr = "";
			for (int i = 0; i < str.length(); i++) {
				String tmp = str.substring(i,i+1);
				if(!isChSym(tmp)) reStr += tmp;
			}
			return reStr;
		}
	public String getMatrixA() {
		return MatrixA;
	}
	public void setMatrixA(String matrixA) {
		MatrixA = matrixA;
	}
	public String getMatrixB() {
		return MatrixB;
	}
	public void setMatrixB(String matrixB) {
		MatrixB = matrixB;
	}
	public String getMatrixP() {
		return MatrixP;
	}
	public void setMatrixP(String matrixP) {
		MatrixP = matrixP;
	}
	public String getDict() {
		return dict;
	}
	public void setDict(String dict) {
		this.dict = dict;
	}
	final int SNUM = 4;//状态数
	final int WORD_NUM = 5216; //字数
	public String run(String sentence) {
		int[] len = new int[100];
		String sentence1 = trimStr(sentence);
		TrimStr ts = new TrimStr();
		sentence1 = ts.trim_str(sentence1,len);
	    double[] P = new double[SNUM];
	    double[][] A = new double[SNUM][SNUM];
	    double[][] B = new double[SNUM][WORD_NUM];
	    BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(MatrixP),"UTF-8"));
			String line = null;
			try {
				while ((line = reader.readLine()) != null){
					String[] num = line.split(" ");
					for (int i = 0; i < num.length; i++) {
						P[i] = Double.parseDouble(num[i]);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				reader.close();
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
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(MatrixA),"UTF-8"));
			String line = null;
			try {
				int lineNo = 0;
				while ((line = reader.readLine()) != null){
					String[] num = line.split(" ");
					for (int i = 0; i < num.length; i++) {
						A[lineNo][i] = Double.parseDouble(num[i]);
					}
					lineNo++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				reader.close();
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
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(MatrixB),"UTF-8"));
			String line = null;
			try {
				int lineNo = 0;
				while ((line = reader.readLine()) != null){
					String[] num = line.split(" ");
					for (int i = 0; i < num.length; i++) {
						B[lineNo][i] = Double.parseDouble(num[i]);
					}
					lineNo++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				reader.close();
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
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(dict),"UTF-8"));
			String line = null;
			try {
				while ((line = reader.readLine()) != null){
					String[] num = line.split(" ");
					map.put(num[0], Integer.parseInt(num[1]));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				reader.close();
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
		String[] arrAtr = sentence1.split("“");
		int row = sentence1.length() - arrAtr.length + 1;
		double[][] delta = new double[row][SNUM];
		int[][] path = new int[row][SNUM];
		int word_index = -1;
		int se_index = 0;
		String strVal = "";
		if(sentence1.substring(0,1).equals("“")){
			strVal = sentence1.substring(0,2);
			se_index = 2;
		}else{
			strVal = sentence1.substring(0,1);
			se_index = 1;
		}
		if(map.get(strVal) != null){
			word_index = map.get(strVal);
			
		}
		//初始概率
		for(int i = 0;i < SNUM;i++){
			if(word_index != -1){
				delta[0][i] = P[i] * B[i][word_index];
				path[0][i] = -1;
			}
		}
		//递归计算
		double value = 0;
		for (int t = 1; t < row; t++) {
			if(sentence1.substring(se_index,se_index+1).equals("“")){
				strVal = sentence1.substring(se_index,se_index+2);
				se_index += 2;
			}else{
				strVal = sentence1.substring(se_index,se_index+1);
				se_index += 1;
			}
			if(map.get(strVal) != null){
			   word_index = map.get(strVal);
				
			}else{
				word_index = -1;
			}
			for (int i = 0; i < SNUM; i++) {
				double maxVal = -1;
				int maxIndex = -1;
				for (int j = 0; j < SNUM; j++) {
					value = delta[t-1][j]*A[j][i];
					if(value > maxVal){
						maxVal = value;
						maxIndex = j;
					}
				}
				if(word_index != -1){
					  delta[t][i] = maxVal * B[i][word_index];
				      path[t][i] = maxIndex;
				}
			}
		}
		//终止
		double max = -1;
		int maxIndex = -1;
		for (int i = 0; i < SNUM; i++) {
			if(delta[row-1][i] > max &&(i == 2 || i == 3)){
				max = delta[row-1][i];
				maxIndex = i;
			}
			//System.out.println(delta[row-1][i] + "");
		}
		//回溯
		Stack<Integer> stack = new Stack<Integer>(); 
		stack.push(maxIndex);
		for (int t = row-1; t > 0; t--) {
			maxIndex = path[t][maxIndex];
			stack.push(maxIndex);
		} 
		int count = 0;
		/*while (true) {
			if(len[count] == -1) break;
			System.out.println(len[count] + ",");
			count++;
		}*/
		String segSentence = "";
		int SIndex = 0;
		int mIndex = 0;
	   while(!stack.isEmpty()){
		  
		  int state = stack.pop();
	//	  System.out.println(state+" ");
		  if(state == 2 || state == 3){
			  if(sentence1.substring(SIndex,SIndex+1).equals("“")){
				  if(SIndex + 3 != segSentence.length()){}
				  segSentence += sentence.substring(mIndex,mIndex+len[count])+"  ";
				  mIndex+=len[count++];
				  SIndex+=2;
				  }
			  else {segSentence += sentence1.substring(SIndex,SIndex+1)+"  "; SIndex++;mIndex++;}
		  }else{
			  if(sentence1.substring(SIndex,SIndex+1).equals("“")){   
				  segSentence += sentence.substring(mIndex,mIndex+len[count]);
				  SIndex+=2;
				  mIndex+=len[count++];
				  }
			  else {
				  segSentence += sentence1.substring(SIndex,SIndex+1); 
				  SIndex++;
				  mIndex++;
				}
		  }
	   }
	 //  System.out.println(segSentence);
	   return segSentence;
	}
}
