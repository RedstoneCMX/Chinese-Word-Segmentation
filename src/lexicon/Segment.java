package lexicon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
public class Segment {
	
	private TreeMap<String, Boolean> Words;
	private TreeMap<String, Boolean> Num;
	private TreeMap<String, Boolean> Name;
	//分隔符
	//private String separator = "/";
	private String separator = "  ";
	//处理中文标点符号
	final String ChineseSym = "“”：；？《》。，！———－、）（【】［］●’‘,/?\\{}[]()~⋯℃α→』『∥";
	//处理数字以及全角数字
	final String numsym = "0123456789０１２３４５６７８９％%.";
	
	//构造函数，对类中的元素初始化，并读入词库
	public Segment() {
		Words=new TreeMap<String, Boolean>();
		Name=new TreeMap<String, Boolean>();
		Num=new TreeMap<String, Boolean>();
		String newword = null;
		try {
			//读入词典
			FileReader worddata = new FileReader("Dictionary.txt");
			BufferedReader in = new BufferedReader(worddata);
		//	int i=0;
			while ((newword=in.readLine())!=null){
	//			i++;
				Words.put(newword.intern(),true);
			}
			in.close();
//			System.out.println();
//			System.out.println("Loading Dictionary.txt OK");
//			System.out.println("load words number is " + i);
		} catch (IOException e) {
			System.out.println("Loading Dictionary.txt failuer");
			e.printStackTrace();
		}
		try {//读入姓词典
			FileReader worddata = new FileReader("Surname.txt");
			BufferedReader in = new BufferedReader(worddata);
		//	int i = 0;
			while ((newword = in.readLine()) != null) {
				//++i;
				Name.put(newword.intern(), true);
			}
			in.close();
//			System.out.println();
//			System.out.println("Loading Surname.txt OK");
//			System.out.println("load Surname number is " + i);
		} catch (IOException e) {
			System.out.println("Loading Surname.txt failuer");
			e.printStackTrace();
		}
		try {//读入数字词典
			FileReader worddata = new FileReader("Number.txt");
			BufferedReader in = new BufferedReader(worddata);
		//	int i = 0;
			while ((newword = in.readLine()) != null) {
			//	++i;
				Num.put(newword.intern(), true);
			}
			in.close();
//			System.out.println();
//			System.out.println("Loading Number.txt OK");
//			System.out.println("load Number number is " + i);
		} catch (IOException e) {
			System.out.println("Loading Number.txt failuer");
			e.printStackTrace();
		}

	}

	//正向匹配算法
	public String forwardSegment(String cline){
		
		StringBuffer outline = new StringBuffer();
		int i, clength;
		char currentchar;
		boolean nameFlag = false,numFlag = false;
		
		clength = cline.length();

		for (i=0;i<clength;i++){
			currentchar=cline.charAt(i);
			if (Character.UnicodeBlock.of(currentchar)==Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS){
			// 是中文汉字
				if (i+7<cline.length()&& (Words.containsKey(cline.substring(i,i+8).intern()))){
					outline=outline.append(cline.substring(i,i+8));
					outline=outline.append(separator);
					i=i+7;
				} 
				else if(i+6<cline.length()&&(Words.containsKey(cline.substring(i,i+7).intern()))){
					outline=outline.append(cline.substring(i,i+7));
					outline=outline.append(separator);
					i=i+6;
				} 
				else if(i+5<cline.length()&&(Words.containsKey(cline.substring(i,i+6).intern()))){
					outline=outline.append(cline.substring(i,i+6));
					outline=outline.append(separator);
					i=i+5;
				} 
				else if(i+4<cline.length()&&(Words.containsKey(cline.substring(i,i+5).intern()))){
					outline=outline.append(cline.substring(i,i+5));
					outline=outline.append(separator);
					i=i+4;
				} 
				else if(i+3<cline.length()&&(Words.containsKey(cline.substring(i,i+4).intern()))){
					outline=outline.append(cline.substring(i,i+4));
					outline=outline.append(separator);
					i=i+3;
				} 
				else if(i+2<cline.length()&&(Words.containsKey(cline.substring(i,i+3).intern()))){
					outline=outline.append(cline.substring(i,i+3));
					outline=outline.append(separator);
					i=i+2;
				} 
				else if(i+1<cline.length()&&(Words.containsKey(cline.substring(i,i+2).intern()))){
					outline=outline.append(cline.substring(i,i+2));
					outline=outline.append(separator);
					i=i+1;
				} 
				else{
					
					
					//简单处理中文数字以及姓名
					nameFlag=false;numFlag=false;
					outline=outline.append(cline.substring(i,i+1));
					if(Name.containsKey(cline.substring(i,i+1))){
						nameFlag=true;
					}
					else if(Num.containsKey(cline.substring(i,i+1))){
						numFlag=true;
					}
					
					if (numFlag==true||nameFlag==true){
						continue;
					}
					outline=outline.append(separator);
				}
			} 
			else { 
				
				
				// 不是中文汉字，可能是数字、引文其他的//记得处理？？？？？？？？？？？？？？？	
				//这里处理中文标点符号
				if(ChineseSym.contains(cline.substring(i,i+1))){
					outline.append(cline.substring(i,i+1));
					outline=outline.append(separator);
					
				}
				//判断数字
				else if(numsym.contains(cline.substring(i,i+1))){
					String temp_str=cline.substring(i,i+1);
					String temp_conn="";
					while(numsym.contains(temp_str)&&i<clength){
						temp_conn += temp_str;
						i++;
						if(i<=clength-1)
							temp_str = cline.substring(i,i+1);
					}
					i--;
					outline.append(temp_conn);
					outline=outline.append(separator);
				}
				//判断英文
				else if(isAlpha(currentchar)){
					char temp_cha=currentchar;
					String temp_conn="";
					while(isAlpha(temp_cha)&&i<clength){
						temp_conn += temp_cha;
						i++;
						if(i<=clength-1)
							temp_cha = cline.charAt(i);
					}
					i--;
					outline.append(temp_conn);
					outline=outline.append(separator);
				}
				
				else{
					outline.append(currentchar);
					outline=outline.append(separator);
				}
			}
		}
		return outline.toString();
	}

	//最大逆向匹配
	public String backwardSegment(String cline){
		String currentword = new String();
		String outline = new String();
		int i, clength;
		char currentchar;
		

		clength = cline.length();

		for (i=clength-1;i>=0;i--){
			currentchar=cline.charAt(i);
			if (Character.UnicodeBlock.of(currentchar)==Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
				// 是中文汉字
				if (currentword.length()==0){
					if(i<clength-1&&(Character.isWhitespace(cline.charAt(i+1))==false)){
						// 若前一个不是空白，则两词之间要加分词符
						outline=separator+outline;
					}
					currentword=""+currentchar+currentword;

				} 
				else{
					if(Words.containsKey(new String(""+currentchar+currentword.toString()).intern())&&
					((Words.get(new String(""+currentchar+ currentword.toString()).intern()).equals(true)))){
						// word is in
						currentword=""+currentchar+currentword;
					} 
					else if(i-1>=0&&(Words.containsKey(new String(cline.substring(i-1,i)+currentchar
					+currentword.toString()).intern()))){
						currentword=""+currentchar+currentword;
					} 
					else if(i-2>=0&&(Words.containsKey(new String(cline.substring(i-2,i)+currentchar
					+currentword.toString()).intern()))){
					currentword=""+currentchar+currentword;
					} 
					else if(i-3>=0&&(Words.containsKey(new String(cline.substring(i-3,i)+currentchar
					+currentword.toString()).intern()))){
						currentword=""+currentchar+currentword;
					} 
					else if(i-4>=0&&(Words.containsKey(new String(cline.substring(i-4,i)+currentchar
					+currentword.toString()).intern()))){
						currentword=""+currentchar+currentword;
					} 
					else if(i-5>=0&&(Words.containsKey(new String(cline.substring(i-5,i)+currentchar
					+currentword.toString()).intern()))) {
						currentword=""+currentchar+currentword;
					} 
					else if(i-6>=0&&(Words.containsKey(new String(cline.substring(i-6,i)+currentchar
					+currentword.toString()).intern()))){
						currentword=""+currentchar+currentword;
					} 
					else {
						// 开始一个新词
						outline=currentword+outline;
						if(Character.isWhitespace(currentchar)==false){
							outline=separator+outline;
						}
						currentword="";
						currentword=""+currentchar;
					}
				}
			} 
			else{ 
				// 不是中文汉字，可能是数字、引文其他的
				
			/*	if(i-1>=0){
					if(ChineseSym.contains(cline.substring(i-1,i))){
						outline=currentword+outline;
						if(Character.isWhitespace(currentchar)==false){
							outline=separator+outline;
						}
						currentword="";
						if(Character.isWhitespace(currentchar)==false){
							outline=" "+currentchar+outline;
						}
					}
				}*/

				if(currentword.length()>0){
					outline=currentword+outline;
					if(Character.isWhitespace(currentchar)==false){
						outline=separator+outline;
					}
					currentword="";
					
				}
				String temp_chstr=""+currentchar;
				if(ChineseSym.contains(temp_chstr))
					outline=separator+currentchar+outline;
				else 
					outline=""+currentchar+outline;
				
				
			}
		}
		outline=currentword+outline;
		return outline.toString();
	}
	
	private boolean isAlpha(char ch){
		if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')|| (ch >= 'Ａ'&& ch <= 'Ｚ') || (ch >= 'ａ' && ch <= 'ｚ')) return true;
		return false;
	}
	
}
