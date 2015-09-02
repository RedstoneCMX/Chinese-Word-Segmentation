package hmm.util;

//三类特殊数据
public class TrimStr {
	private boolean isNum(char ch){
		if((ch >= '0' && ch <= '9')||(ch >= '０' && ch <= '９')) return true;
		return false;
	}
	private boolean isAlpha(char ch){
		if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')|| (ch >= 'Ａ'&& ch <= 'Ｚ') || (ch >= 'ａ' && ch <= 'ｚ')) return true;
		return false;
	}
	private boolean isSymbol(char ch){
		 int num = '０' - '0';
		  if((ch >= 33 && ch <= 47) ||(ch >= 58 && ch <= 64) ||(ch >= 91 && ch <= 96) ||(ch >= 123 && ch <= 126)||((ch >= 33+num && ch <= 47+num) ||(ch >= 58+num && ch <= 64+num) ||(ch >= 91+num && ch <= 96+num) ||(ch >= 123+num && ch <= 126+num)) ) return true;
		  return false;
	}
	private boolean isAllNum(String str){
		for (int i = 0; i < str.length(); i++) {
			if(!isNum(str.charAt(i))) return false;
		}
		return true;
	}
	private boolean isAllAlpha(String str){
		for (int i = 0; i < str.length(); i++) {
			if(!isAlpha(str.charAt(i))) return false;
		}
		return true;
	}
  public String trim_str(String str) {
		int moveIndex = 0;
		int curIndex = 0;
		String reString = "";
		boolean flag = false;
		while(moveIndex < str.length()){
			flag = false;
		  	while (isAlpha(str.charAt(moveIndex))||isNum(str.charAt(moveIndex))||isSymbol(str.charAt(moveIndex))) {
		  		moveIndex++;	
		  		flag = true;
				if(moveIndex >= str.length()) {
					  //moveIndex--;
					  break;
					}
			}
		  	if(flag){
		  		String tmp = str.substring(curIndex,moveIndex);
			  //	System.out.println(tmp);
			  	if(isAllNum(tmp)){
			  		reString += "“，";//全数字
			  		//len[cnt++] = tmp.length();
			  	}else if(isAllAlpha(tmp)){//全字母
			  		reString += "“。";
			  	}else{
			  		reString += "“！";//混合
			  	}
		  	}else{
		  		reString += str.substring(moveIndex,(moveIndex+1) > str.length()?str.length(): (moveIndex+1));
			  	moveIndex++;
		  	}
		  	curIndex=moveIndex;
	   }
		//System.out.println(reString);
		return reString;
	}
  public String trim_str(String str,int len[]) {
		int moveIndex = 0;
		int curIndex = 0;
		String reString = "";
		boolean flag = false;
		int cnt = 0;
		while(moveIndex < str.length()){
			flag = false;
		  	while (isAlpha(str.charAt(moveIndex))||isNum(str.charAt(moveIndex))||isSymbol(str.charAt(moveIndex))) {
		  		moveIndex++;	
		  		flag = true;
				if(moveIndex >= str.length()) {
					  //moveIndex--;
					  break;
					}
			}
		  	if(flag){
		  		String tmp = str.substring(curIndex,moveIndex);
			//  	System.out.println(tmp);
			  	if(isAllNum(tmp)){
			  		reString += "“，";//全数字
			  		len[cnt++] = tmp.length();
			  	}else if(isAllAlpha(tmp)){//全字母
			  		reString += "“。";
			  		len[cnt++] = tmp.length();
			  	}else{
			  		reString += "“！";//混合
			  		len[cnt++] = tmp.length();
			  	}
		  	}else{
		  		reString += str.substring(moveIndex,(moveIndex+1) > str.length()?str.length(): (moveIndex+1));
			  	moveIndex++;
		  	}
		  	curIndex=moveIndex;
	   }
		len[cnt] = -1;
		//System.out.println(reString);
		return reString;
	}
}
