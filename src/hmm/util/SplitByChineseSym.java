package hmm.util;

public class SplitByChineseSym {
	final String ChineseSym = "“”：；？《》。，！———－、）（【】［］●’‘,/?\\{}[]()~⋯℃α→』『∥";
	  //中文标点
	  private boolean isChSym(String str){
		  if(ChineseSym.contains(str)) return true;
		  else return false;
	  }
  private String symbol;
  public String getSymbol() {
	return symbol;
}
public void setSymbol(String symbol) {
	this.symbol = symbol;
}
public String[] splitStr(String str){
	 setSymbol("");
  	 int curIndex = 0;
  	 String[] strArray = new String[100];
  	 int cnt = 0;
  	 boolean flag = false;
      for (int i = 0; i < str.length(); i++) {
			if(isChSym(str.substring(i,i+1))){
				//out[cnt] ;
				symbol+=str.substring(i,i+1);
				strArray[cnt++] = str.substring(curIndex,i);	
				curIndex = i+1;
				flag = true;
			}else{
				flag = false;
			}
		}
      if(!flag)strArray[cnt] = str.substring(curIndex,str.length());
  	return strArray;
  }
}

