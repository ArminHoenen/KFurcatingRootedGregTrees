package mainpack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * This code computes numbers of rooted Greg trees Flight (1990), which are trees with labeled and unlabeled nodes where
 * the latter must have an outdegree of at least 2. These trees have been used as mathematical models for stemmata
 * that is manuscript genealogies. For details, see Hoenen et al. (2017).
 * In the code,, since we are consistently dealing with rooted Greg trees, whenever the word tree is mentioned in a comment
 * this relates to exactly those types of trees.
 *  
 * License:
 * Please cite: 
 * Armin Hoenen, Steffen Eger, Ralf Gehrke. (2017) How many stemmata with root degree k?. Proceedings of the 15th Meeting on the Mathematics of Language (MoL). Association for Computational Linguistics (ACL).
 *
 */

public class KFurcatingRootedGregTrees {

	public static ArrayList<BigInteger[]> rowsGr; //container for numbers of (m,n)-trees; extensible from Flight (1990, Table II)
	public static ArrayList<ArrayList<int[]>> gregmn; 
	public static ArrayList<ArrayList<Integer>> allSets;
	public static int actK;
	public static PrintWriter pwrite;
	public static BigInteger perm;
	
    public static void main (String[] args)throws Exception{
		perm=BigInteger.valueOf(0);
		int maxlim=16;//until this value of m, numbers are computed
    	
    	 rowsGr=new ArrayList<BigInteger[]>();

         /*
          * Now, Table II from Flight (1990) is hard-coded for three reasons. 
          * a) later, for computation of k-furcating trees, numbers will be based on different (m,n) trees
          * b) the computation/formula of numbers of (m,n) trees is defined recursively by Flight (1990) and so we need initial values
          * c) Accessing hard-coded values is quicker than on-the-fly-computation, since Flight(1990) gives numbers until m=5, we used these initially;
          *    for further speedup, the hard-coded proportion can be increased altering this code, if doing so, for instance from the numbers in our paper,
          *    please also change the index of the first for-loop, which now starts in 6 to whatever value of m is the first for which no hard-code is available.
          */ 
          
    	 BigInteger[] zero={}; //in this way indices correspond to values for m
         BigInteger[] one={BigInteger.valueOf(1)};
         BigInteger[] two={BigInteger.valueOf(2),BigInteger.valueOf(1)};
         BigInteger[] three={BigInteger.valueOf(9),BigInteger.valueOf(10),BigInteger.valueOf(3)};
         BigInteger[] four={BigInteger.valueOf(64),BigInteger.valueOf(113),BigInteger.valueOf(70),BigInteger.valueOf(15)};
         BigInteger[] five={BigInteger.valueOf(625),BigInteger.valueOf(1526),BigInteger.valueOf(1450),BigInteger.valueOf(630),BigInteger.valueOf(105)};

        rowsGr.add(zero);
        rowsGr.add(one);
        rowsGr.add(two);
        rowsGr.add(three);
        rowsGr.add(four);
        rowsGr.add(five);

       //Looping over the values of m from 6 (=end off hard-coded section) until your chosen maximum in order to fill the table of numbers of (m,n) trees
       for(int m=6;m<maxlim;m++){
           //for each m, there are m possible n (0..m-1), which is why act, the actual row of the table has m values coded into an array for quicker and more effective access
    	   BigInteger[] act=new BigInteger[m]; 
           for (int n=0;n<m;n++){

               /*
                * What follows is the implementation of the recursive formula of Flight (1990) for the computation of rooted (m,n) trees.
                * In Flight's paper for clarity of argument: [α + 1]  g*(m-1,n-1) + [β + γ + 1] g*(m-1,n) + δ g*(m-1,n+1) 
                *                                            with α = m + n - 3, β = m + n - 1, γ = m + n - 2 and δ = n + 1
                *                    filling in α,β,γ and δ: [m + n - 3 + 1] g*(m-1,n-1) +
                *                                            [m + n - 1 + m + n - 2 + 1] g*(m-1,n) +
                *                                            [n + 1] g*(m-1,n+1)
                *                             simplified to: [m + n - 2] g*(m-1,n-1) +
                *                                            [2m + 2n - 2] g*(m-1,n) +
                *                                            [n + 1] g*(m-1,n+1)
                */ 
                 
        	   BigInteger gmnminus1=BigInteger.valueOf(0); //g*(m-1,n-1) .. read: m and n minus 1;
        	   BigInteger gmminus1n=BigInteger.valueOf(0); //g*(m-1,n)
        	   BigInteger gmminus1nplus1=BigInteger.valueOf(0); //g*(m-1,n+1)

               //get values for recursion from previous table rows if applicable (n in range of non-zero values), else value remains 0
               if (n-1>=0){
                   gmnminus1=rowsGr.get(m-1)[n-1];     
               }
               if (n<=m-2){
                   gmminus1n=rowsGr.get(m-1)[n];
               }
               if (n<=m-3){
                   gmminus1nplus1=rowsGr.get(m-1)[n+1];
               }

               //basic values of m and n
               BigInteger mbi=BigInteger.valueOf(m);
               BigInteger nbi=BigInteger.valueOf(n);
               
               //[m + n - 2] g*(m-1,n-1)
               BigInteger r1=mbi.add(nbi).subtract(BigInteger.valueOf(2)).multiply(gmnminus1);
               
               //[2m + 2n - 2] g*(m-1,n)
               BigInteger r2=BigInteger.valueOf(2).multiply(mbi).add(BigInteger.valueOf(2).multiply(nbi)).subtract(BigInteger.valueOf(2)).multiply(gmminus1n);
               
               //[n + 1] g*(m-1,n+1)
               BigInteger r3=nbi.add(BigInteger.valueOf(1)).multiply(gmminus1nplus1);

               //summing up
               BigInteger res= r1.add(r2).add(r3);

               //assigning value to actual table cell for (m,n)
               act[n]=res;

           }//for n

           /*
           BigInteger sum=BigInteger.valueOf(0);
           for (BigInteger a:act){
        	   sum=sum.add(a);
           }
           System.out.println(sum);
           */
           
         rowsGr.add(act);
       }//for m
       
       BigInteger previousSum=BigInteger.valueOf(1);
       //System.out.println("Number of rooted Greg trees.");
       
       //supercontainer with an ArrayList<BigInteger> for each m, which has numbers of root 1..k furcating trees
       ArrayList<ArrayList<BigInteger>> allkFurcs=new ArrayList<ArrayList<BigInteger>>();
       
       for (int a=2;a<rowsGr.size();a++){

    	   System.out.print("m="+a+" ");
    	   //container for k-furcations
    	   ArrayList<BigInteger> allFurcs= new ArrayList<BigInteger>();
    	   
    	   //as basis of inner function, get all g(m,n)
    	   BigInteger[] act=rowsGr.get(a);
           BigInteger sum=BigInteger.valueOf(0);
           for (int b=0;b<act.length;b++){
               sum=sum.add(act[b]);
           }
           //speed up computation using m * g(m-1) for root unifurcating trees, which can only be labeled
           BigInteger m=BigInteger.valueOf(a);
           BigInteger runif=m.multiply(previousSum);
           allFurcs.add(runif);

           //for k=2 to k=m-2; k is furc and m is a
           for (int furc=2;furc<a-1;furc++){
        	   
        	   //distinguish root labeled and root unlabeled
        	   //nodes to be distributed among subtrees:
        	   int mForRootLabelled=a-1;
        	   int mForRootUnlabelled=a;
        	   
        	   
        	   //Here, get all distinct trees with furc subtrees and m labeled nodes, 2 cases, note, g(m) is computed not g(m,n)
        	   //TODO speed-up by saving root unlabeled values and reusing them for root labeled of next m, where possible (only for cases, where n is suitable)
        	   //              any root labeled tree for k with n > 0 has the same number of distinct label permuted subtrees as an (m-1,n+1) root unlabeled tree; n + 1 < m
        	   
        	   BigInteger rootLabelled=getAllTrees(mForRootLabelled, furc);
        	   BigInteger rootUnLabelled=getAllTrees(mForRootUnlabelled, furc);
        	   
        	   BigInteger actFurc=rootLabelled.multiply(m);
        	   allFurcs.add(actFurc.add(rootUnLabelled));
        	   
        	  
           }//for furcation degree k
           
           //speedup; for k=m-1, if a >2 formula coincides with pentagonal numbers  3n^2-n/2 
           if (a>2){
           BigInteger aminone=BigInteger.valueOf(a);
           aminone=aminone.multiply(aminone);
           aminone=aminone.multiply(BigInteger.valueOf(3));
           aminone=aminone.subtract(BigInteger.valueOf(a));
           aminone=aminone.divide(BigInteger.valueOf(2));
           allFurcs.add(aminone);
           }
           //speed-up, adding the 1 single k=m tree
           allFurcs.add(BigInteger.valueOf(1));
           //BigDecimal bigDecX = new BigDecimal(runif);
           //BigDecimal bigDecY = new BigDecimal(sum);
           // to divide:
           //BigDecimal prop= bigDecX.divide(bigDecY,MathContext.DECIMAL128);
           //System.out.println("$"+a+"$ & $"+rbif+"$ & $"+allrlt+"$ & $"+prop2.setScale(3,RoundingMode.HALF_UP)+"$ \\\\ \\hline");
           //System.out.println(a+"&"+runif+"&"+prop.setScale(3,RoundingMode.HALF_UP)+"\\\\ \\hline");  //sum
           
           //output
           for (BigInteger ac:allFurcs){
        	   System.out.print(ac+" ");
           }
           System.out.print("\n");
           
           //add to list of all 
           allkFurcs.add(allFurcs);
           //for computation of unifurcating trees in next higher m
           previousSum=sum;
       }
       
       
       //printing out percentages of root unifurcating, bifurcating and multifurcating trees
       System.out.println("relative frequencies:");
       for (int a=1;a<allkFurcs.size();a++){
    	   System.out.print("m= "+(a+2)+" "); //all furcs start at m=3, m=1 and m=2 trivial
    	   
    	   BigInteger sum=BigInteger.valueOf(0);
    	   BigInteger unif=allkFurcs.get(a).get(0);
    	   BigInteger bif=allkFurcs.get(a).get(1);
    	   
    	   BigInteger multif=BigInteger.valueOf(0);
    	   for (int b=2;b<allkFurcs.get(a).size();b++){
    		   multif=multif.add(allkFurcs.get(a).get(b));
    	   }
    	   sum=sum.add(multif).add(unif).add(bif);
    	   
    	   BigDecimal bigDecSum = new BigDecimal(sum);
    	   
    	   BigDecimal bigDecUnif = new BigDecimal(unif);
           BigDecimal bigDecBif = new BigDecimal(bif);
           BigDecimal bigDecMultif = new BigDecimal(multif);
           
           // to divide:
           BigDecimal propUnif= bigDecUnif.divide(bigDecSum,MathContext.DECIMAL128);
           BigDecimal propBif= bigDecBif.divide(bigDecSum,MathContext.DECIMAL128);
           BigDecimal propMultif= bigDecMultif.divide(bigDecSum,MathContext.DECIMAL128);
           BigDecimal hering= bigDecMultif.divide(bigDecBif,MathContext.DECIMAL128);
           
           System.out.print(propUnif.setScale(3,RoundingMode.HALF_UP)+" "+propBif.setScale(3,RoundingMode.HALF_UP)+" "+propMultif.setScale(3,RoundingMode.HALF_UP)+" "+hering.setScale(3,RoundingMode.HALF_UP)+"\n");  
       }
       
    }//main
    
    
    
    public static BigInteger getAllTrees(int m, int furc)throws Exception{

    	if (furc==m)return BigInteger.valueOf(1);

    	//create all non empty k-tuples
    	//each subset = 1, least
    	//then distribute rest ... 
    	
    	actK=furc;
    	allSets=new ArrayList<ArrayList<Integer>>();
    	BigInteger result=BigInteger.valueOf(0);
    	
    	int maxSubsetSize=m-(furc-1);
    	if (maxSubsetSize<=0)return BigInteger.valueOf(0);
    	
    	int[] numbers = new int [maxSubsetSize];
    	
    	for (int i=0;i<maxSubsetSize;i++){
    		numbers[i]=(i+1);
    	}
        int target = m;
        sum_up(numbers,target);
        
        BigInteger localSum=BigInteger.valueOf(0);
        for (ArrayList<Integer>act:allSets){
        	//for each such set for each member > 1, there can be n=1..x-1 internodes
        	
        	ArrayList<ArrayList<Integer>> internodesPerMember= new ArrayList<ArrayList<Integer>>();
        	for (int mem:act){
        		ArrayList<Integer> internodes=new ArrayList<Integer>();
        		int memminus1=mem -1;
        		for (int j=0;j<=memminus1;j++){
        			internodes.add(j);
        		}
        		internodesPerMember.add(internodes);
        	}
        	
           	//now act has the partition
        	//internodesPerMember has for each partition member the possible internodes
        	gregmn= new ArrayList<ArrayList<int[]>>();
        	addToGregMN(act,internodesPerMember,new ArrayList<int[]>(),0,0);//
        	
        	BigInteger permuts=BigInteger.valueOf(1);
        	int choosebase=m;
        	for (int y=0;y<gregmn.get(0).size();y++){	    
        		permuts=permuts.multiply(binom(choosebase,gregmn.get(0).get(y)[0]));
        		choosebase-=gregmn.get(0).get(y)[0];
        	}
        	perm=permuts;
        	
        	for (int x=0;x<gregmn.size();x++){
        		BigInteger localProd=BigInteger.valueOf(1);
        		ArrayList<int[]> actGregmn=gregmn.get(x);
        		//int choosebase=m;
        		for (int y=0;y<actGregmn.size();y++){
        			//if(actGregmn.get(y).length!=2)System.out.println("Heureka");
        			BigInteger gmn=rowsGr.get(actGregmn.get(y)[0])[actGregmn.get(y)[1]];
        			localProd=localProd.multiply(gmn);  
        			
        		}
        		localSum=localSum.add(localProd);	
        	}
        	
        	
        	//multiply 3 members as n choose k (then subtract k from n) * m,n greg tree from table
        	
        	
        	//divide by k
        	//localSum=localSum.divide(BigInteger.valueOf(furc));
        	localSum=localSum.multiply(perm);
        	//localSum=localSum.divide(BigInteger.valueOf(furc));
        	result=result.add(localSum);
        	localSum=BigInteger.valueOf(0);
        	//System.out.println("res"+result);
        	//System.out.println("sum("+Arrays.toString(act.toArray())+")="+target);
        }
        
        //divide by k!
        result=result.divide(fac(furc));
    	return result;
    }
    
    

    static void sum_up_recursive(int[]numbers, int target, ArrayList<Integer> partial) {

 	    int s = 0;
        for (int x: partial) s += x;
        if (s == target&&partial.size()==actK){
          //   System.out.println("sum("+Arrays.toString(partial.toArray())+")="+target);
            //TODO instead make Set<ArrayList<Integer>>  and add only of set doesnt have the permut
        	allSets.add(partial);
            
            }
        if (s >= target)
             return;
        for(int i=0;i<numbers.length;i++) {
              ArrayList<Integer> remaining = new ArrayList<Integer>();
              int n = numbers[i];
              
              ArrayList<Integer> partial_rec = new ArrayList<Integer>(partial);
              partial_rec.add(n);
              sum_up_recursive(numbers,target,partial_rec);
        }
     }
     static void sum_up(int[] numbers, int target) {
         sum_up_recursive(numbers,target,new ArrayList<Integer>());
     }
    
     public static void addToGregMN(ArrayList<Integer> act,ArrayList<ArrayList<Integer>> internodesPerMember, ArrayList<int[]> set,
    	 int setIndex, int interIndex)throws Exception{
    	 
    	 //System.out.println(setIndex);
    	 
    	 int m=act.get(setIndex);
    	 int n=internodesPerMember.get(setIndex).get(interIndex);
    	 int[] actmn=new int[]{m,n};
    	 if (interIndex<internodesPerMember.get(setIndex).size()-1){
    		 ArrayList<int[]> setclone=(ArrayList<int[]>)set.clone();
			 addToGregMN(act,internodesPerMember,setclone,setIndex,(interIndex+1));// 
    	 }
    	 set.add(actmn);
    	 //System.out.println(set.size()+" "+setIndex);
    	 if (set.size()<actK){
    		 setIndex+=1;
    		 ArrayList<int[]> setclone=(ArrayList<int[]>)set.clone();
    		 addToGregMN(act,internodesPerMember,setclone,setIndex,0);
    	 }
    	 if (set.size()==actK){
    		 
    		/* 
    		 System.out.println(actK);
    		 for (int x=0;x<set.size();x++){
    			 System.out.print(actK+" "+Arrays.toString(set.get(x)));
    		 }
    		 System.out.println();
    		 */
    		 gregmn.add(set);
    	 }
    	
    	 
    	 
    	 
     }//addToGregmn
     
     public static BigInteger binom(int n, int k)throws Exception{
    
    	 BigInteger nfac=fac(n);
    	 BigInteger kfac=fac(k);
    	 BigInteger nminkfac=fac((n-k));
    	 
    	 BigInteger denom=kfac.multiply(nminkfac);
    	 BigInteger result=nfac.divide(denom);
    	 
    	 
    	 
    	 return result;
     }
     
     public static BigInteger fac(int n)throws Exception{
    	 
    	 if (n==0||n==1)return BigInteger.valueOf(1);
    	 BigInteger nin=BigInteger.valueOf(n);
    	 for (int a=nin.intValue()-1;a>1;a--){
    		 nin=nin.multiply(BigInteger.valueOf(a));  		 
    	 }
    	 
    	return nin; 
     }
     
     
}//class
 
