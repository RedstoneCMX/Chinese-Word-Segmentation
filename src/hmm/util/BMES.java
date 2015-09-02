package hmm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class BMES {
	  final String ChineseSym = "“”：；？《》。，！———－、）（【】［］●’‘,/?\\{}[]()~⋯℃α→』『∥";
	  //中文标点
	  private boolean isChSym(String str){
		  if(ChineseSym.contains(str)) return true;
		  else return false;
	  }
	  //判断英文符号
	public boolean isSymbol(byte ch){
		  if((ch >= 33 && ch <= 47) ||(ch >= 58 && ch <= 64) ||(ch >= 91 && ch <= 96) ||(ch >= 123 && ch <= 126) ) return true;
		  return false;
	  }
	 //判断字母或者数字
	private boolean isNumOrAlpha(byte ch) {
		  if((ch >= '1' && ch <= '9') ||(ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) return true;
		  else return false;
	}
	 //去掉英文字符
	public byte[] trimNumAndAlpha(byte[] str){
		 int cnt1 = 0;
		 for (int i = 0; i < str.length; i++) {
			if(isNumOrAlpha(str[i]) || isSymbol(str[i])){
				str[i] = ' ';
				cnt1++;
			}
		}
		byte[] reByte = new byte[str.length - cnt1];
		int cnt = 0;
		for (int i = 0; i < str.length; i++) {
			if(str[i] == ' '){
				continue;
			}else{
				reByte[cnt++] = str[i];
			}
		}
		return reByte;
	  }
	private String trimStr(String str) {
		String reStr = "";
		for (int i = 0; i < str.length(); i++) {
			String tmp = str.substring(i,i+1);
			if(!isChSym(tmp)) reStr += tmp;
		}
		return reStr;
	}
	//标记BMES
	public String makeBMES(String str) {
	   String strBMES = "";
	   int index = 0; 
	   if(str.length() == 0)return "";
	   //byte[] strByte = str.getBytes("UTF-8");
	   //if(strByte.length < 3) return "";
	   while(true){
	      if((str.length() == 1) || (str.length()==2&&str.contains("“")) ){
	    	  strBMES += str + "/S";
			  return strBMES; 
	      }else{
	    	 if(str.substring(0, 1).equals("“")){
	    		strBMES += str.substring(0,2) + "/B";
	    		index += 2;
	    	 }else{
	    		 strBMES += str.substring(0,1) + "/B";
	    		 index += 1;
	    	 }
	    	 boolean flag = false;
	  		while(true){
	  			if((str.substring(index, index+1).equals("“"))&&(index == str.length()-2)){
	  				flag = true;
	  				break;
	  			}
	  			if(index == str.length()-1) break;
	  			if(str.substring(index,index+1).equals("“")){
		    		strBMES += str.substring(index,index+2) + "/M";
		    		index += 2;
	  			}else{
		    		strBMES += str.substring(index,index+1) + "/M";
		    		index += 1;
	  			}
	  		}
	  		if(flag) strBMES += str.substring(index,index+2) + "/E";
	  		else strBMES += str.substring(index,index+1) + "/E";
	  		return strBMES;
	      }
	   }
	}
	public void run(String in ,String out) {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		File outFile = new File(out);
		if(!outFile.exists()){
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out),"UTF-8"));
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(in),"UTF-8"));
			String line = null;
			try {
				TrimStr tr = new TrimStr();
				while ((line = reader.readLine()) != null){
					line = trimStr(line); //去掉标点符号
					String[] word = line.split(" ");
					for (int i = 0; i < word.length; i++) {
						//byte[] byte1;
						//byte1 = word[i].getBytes("UTF-8");
						//byte1 = trimNumAndAlpha(byte1);	
						word[i] = tr.trim_str(word[i]);
					//	System.out.println(makeBMES(word[i]));
						if(!makeBMES(word[i]).isEmpty()){
							writer.write(makeBMES(word[i]));
						}
					}
					writer.write("\n");
				}
			 reader.close();
			 writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			System.out.println("文件不存在!!!");
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
