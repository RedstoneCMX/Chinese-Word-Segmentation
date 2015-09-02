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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GenDictFile {
@SuppressWarnings("unused")
private final int BASE_NUM = 3;
@SuppressWarnings("rawtypes")
public void genDict(String dictFile,Map map) {
	   File outFile = new File(dictFile);
		if(!outFile.exists()){
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dictFile),"UTF-8"));
			 Set   set  =   map.entrySet();   
		     Iterator iterator = set.iterator();   
		      while(iterator.hasNext()) {   
		          Map.Entry  mapentry = (Map.Entry)iterator.next();   
		          try {
					writer.write(mapentry.getKey()+" "+String.valueOf(mapentry.getValue()) +"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
		      } 
		      try {
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
@SuppressWarnings("unused")
public void genDict(String in,String dict){
		Map<String, Integer> map = new HashMap<String, Integer>();
		BufferedReader reader = null;
		int lineno = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(dict),"UTF-8"));
			String line = null;
			try {
				while ((line = reader.readLine()) != null){
					String[] num = line.split(" ");
					map.put(num[0], Integer.parseInt(num[1]));
					lineno++;
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
}
}

