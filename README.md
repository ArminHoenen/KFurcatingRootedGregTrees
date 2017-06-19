# KFurcatingRootedGregTrees
This project provides code for computing the numbers of rooted Greg trees with root degree k, as described in the paper mentioned herebelow.
# Usage
<b>Java</b>
- Create a new Class named "KFurcatingRootedGregTrees" and paste the Java Code there, add package declaration if necessary.<br/>
- From here you can use the code in any programming environment such as eclipse, intellJIdea or netBeans etc. If you change  
     the maxlim variable in line 34 you can set your individual maximum m until which you want a result. 

<b>OR</b>
Use the runnable jar "kfurc.jar" by commadnline call: "java -jar kfurc.jar <i>m</i>"<br/>
where <i>m</i> must be a positive Integer which is the maximum <i>m</i> for which you want a result.<br/><br/>
The output will be 2 tables, the first will give per line (m=1 .. maximum m specified by user) the exact numbers of trees
for each possible k (k<=m). The second gives per line (m=1 .. maximum m specified by user) the proportions of unifurcations,
bifurcations, multifurcations and of multifurcations divided by bifurcations (see Hering (1967)).<br/><br/>
<b>Python</b>
- Copy all three classes into the same location, make sure all required packages are installed (e.g. scipy).
- Call on commandline: "python greg_root_degree.py <i>m</i>"<br/>
  Each line (k=1 .. m) of the output will give the exact number of k-furcating rooted Greg trees at m labeled nodes and their proportion among all trees with m survivors.
# Please cite
A. Hoenen, S. Eger and R. Gehrke. How many stemmata with root degree k? Proceedings of MOL 2017.
