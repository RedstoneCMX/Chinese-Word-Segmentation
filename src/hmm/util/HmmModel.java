package hmm.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class HmmModel {
	private final int BASE_NUM = 3;
    private final int SNUM = 4;//״̬??
    private final int WORD_NUM = 5216; //????
    private int [] P;  //???Ը???
    private int [][] A; //״̬ת??
    private int [][] B; //????????
	public HmmModel() {
		P = new int[SNUM];
		A = new int[SNUM][SNUM];
		B = new int[SNUM][WORD_NUM];
	}
	public int[] getP() {
		return P;
	}
	public void setP(int[] p) {
		P = p;
	}
	public int[][] getA() {
		return A;
	}
	public void setA(int[][] a) {
		A = a;
	}
	public int[][] getB() {
		return B;
	}
	public void setB(int[][] b) {
		B = b;
	}
	private int getState(char ch) {
		switch (ch) {
		case 'B':
			 return 0;
		case 'M':
			 return 1;
		case 'E':
			 return 2;
		case 'S':
			 return 3;
		default:
			return -1;
		}
	}
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public void genModel(String bmesFile,String dict)
	{
		for (int i = 0; i < SNUM; i++) {
			    P[i] = 0;
			for (int j = 0; j < SNUM; j++) {
				A[i][j] = 0;
			}
		}
		for (int i = 0; i < SNUM; i++) {
		    for (int j = 0; j < WORD_NUM; j++) {
			    B[i][j] = 0;
		     }
	    }
		Map<String, Integer> map = new HashMap();
		map.put("????", 0);
		map.put("????????", 1);
		map.put("????", 2);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(bmesFile),"UTF-8"));
			String line = null;
			int countWord = 0;
			try {
				int word_index = BASE_NUM;
				while ((line = reader.readLine()) != null){
					line = line.replaceAll(" ", "");
					int preStateIndex = -1;
					while(line.indexOf('/') != -1){
						String state = line.substring(line.indexOf('/')+1,line.indexOf('/')+2);
						String word = line.substring(0,line.indexOf('/'));
						int stIndex = getState(state.charAt(0));
						if(stIndex != -1){
							P[stIndex]++;
							if(preStateIndex != -1) A[preStateIndex][stIndex]++;
							preStateIndex = stIndex;
							if(map.get(word) != null) B[stIndex][map.get(word)]++;
							else{
								map.put(word, word_index);
								B[stIndex][word_index]++;
								word_index++;
							}
							countWord++;
						}else{
							System.out.println("error!!");
						}
						line = line.substring(line.indexOf('/')+2,line.length());
					}
				}
				//System.out.println(word_index);
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
		GenDictFile gen = new GenDictFile();
		gen.genDict(dict, map);
	}
}
