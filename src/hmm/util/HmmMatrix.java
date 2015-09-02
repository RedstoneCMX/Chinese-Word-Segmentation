package hmm.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HmmMatrix {
	private final int SNUM = 4;
	private final int WORD_NUM = 5216; //字数
	private double[] MatrixP;
	private double[][] MatrixA ;
	private double[][] MatrixB ;
	private HmmModel hmm;
	public HmmMatrix() {
		MatrixP = new double[SNUM];
		MatrixA = new double[SNUM][SNUM];
		MatrixB = new double[SNUM][WORD_NUM];
		hmm = new HmmModel();
	}
	public double[] getMatrixP() {
		return MatrixP;
	}
	public void setMatrixP(double[] matrixP) {
		MatrixP = matrixP;
	}
	private void noneturing(final int[] count,double[] prob,int len){
	    double total=0.0;
	    for(int i=0;i<len;++i)
	        total+=count[i];
	    if(total==0.0){ 
	        for(int i=0;i<len;++i)
	            prob[i]=0.0;
	    }
	    else{
	        for(int i=0;i<len;++i)
	            prob[i]=count[i]/total;
	    }

	}  
	private void plusOneSmooth (final int[] count,double[] prob,int len) {
		double total = 0;
		for (int i = 0; i < len; i++) {
			total += count[i];
		}
		total += len;
		if(total == 0){
			for (int i = 0; i < len; i++) {
				prob[i] = 0.0;
			}
		}else{
			for (int i = 0; i < len; i++) {
				prob[i] = (count[i]+1)/total;
			}
		}
	}
	@SuppressWarnings("unused")
	public void goodturing(final int[] count,double[] prob,int len){
       Map<Integer, List<Integer>> count_map = new HashMap<Integer, List<Integer>>();//map可以自动按key排好序
	   int N = 0;            
	   for(int i = 0 ;i < len;++i){
	        int c = count[i];
	        N += c;
	        if(count_map.get(c) == null){
	            List<Integer> l = new ArrayList<Integer>();
	            l.add(i);
	            count_map.put(c, l);
	        }
	        else{
	        	count_map.get(c).add(i);
	        }
	    }	 
	    if(N==0){
	        for(int i = 0;i < len;++i)
	            prob[i]=0.0;
	        return;
	    }	
	    Iterator<Map.Entry<Integer, List<Integer>>> it = count_map.entrySet().iterator();   
	    while (it.hasNext()) {
	    	double pr;
	    	Map.Entry<Integer, List<Integer>> entry = it.next(); 
	        int r = entry.getKey();
	        int nr = entry.getValue().size();
	        if(it.hasNext()){
	        	Map.Entry<Integer, List<Integer>> entry1 = it.next();
	            int r_new = entry1.getKey();
	            if(r_new == r+1){
	                int nr_1 = entry1.getValue().size();
	                pr = (1.0+r)*nr_1/(N*nr);
	            } else{
	                pr=1.0*r/N;
	            }

	        }else{
	            pr=1.0*r/N;
	        }
	    }	   
	   /* List<Integer> l = (--iter)->second;

	        list<int>::const_iterator itr1=l.begin();

	        while(itr1!=l.end()){

	            int index=*itr1;

	            itr1++;

	            prob[index]=pr;

	        }

	        ++iter;

	    }

	     
*/
	    //概率归一化
	    double sum=0;
	    for(int i=0;i<len;++i)
	        sum+=prob[i];
	    for(int i=0;i<len;++i)
	        prob[i]/=sum;

	}
	public void buildMatrix(String in,String dict,String outp,String outa,String outb) {
		hmm.genModel(in,dict);
		//计算概率矩阵
		noneturing(hmm.getP(), MatrixP, SNUM);
		for (int i = 0; i < SNUM; i++) {
		   noneturing(hmm.getA()[i], MatrixA[i], SNUM);
		}
		for (int i = 0; i < SNUM; i++) {
			plusOneSmooth(hmm.getB()[i], MatrixB[i], WORD_NUM);
		}
		File outFile = new File(outp);
		if(!outFile.exists()){
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		outFile = new File(outa);
		if(!outFile.exists()){
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		outFile = new File(outb);
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
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outp),"UTF-8"));
			for (int i = 0; i < SNUM; i++) {
				try {
					writer.write(String.valueOf(MatrixP[i]));
					if(i == SNUM-1){
						writer.write("\n");
					}else{
						writer.write(" ");
					}
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
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outa),"UTF-8"));
			for (int i = 0; i < SNUM; i++) {
				for (int j = 0; j < SNUM; j++) {
					try {
						writer.write(String.valueOf(MatrixA[i][j]));
						if(j == SNUM-1){
							writer.write("\n");
						}else{
							writer.write(" ");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
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
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outb),"UTF-8"));
			for (int i = 0; i < SNUM; i++) {
				for (int j = 0; j < WORD_NUM; j++) {
					try {
						writer.write(String.valueOf(MatrixB[i][j]));
						if(j == WORD_NUM-1){
							writer.write("\n");
						}else{
							writer.write(" ");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
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
}
