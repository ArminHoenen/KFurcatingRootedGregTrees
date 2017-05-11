# Number of compositions of n with k parts, each between a and b
# 01.05.2017
# Steffen Eger
# Please cite: Armin Hoenen, Steffen Eger, Ralf Gehrke. (2017) How many stemmata with root degree k?. Proceedings of the 15th Meeting on the Mathematics of Language (MoL). Association for Computational Linguistics (ACL).


class Colex:
    def __init__(self,n,k,a,b):
        self.k = k
        self.n = n
        self.b = b
        self.a = a
        self.cc = [a for j in xrange(k)]
        self.X = []
        self.doPrint = False
    def genColex(self): 
        self.generateMin()
        if self.doPrint:
            print self.n,self.k
        self._genColex(self.n,self.k)
    def generateMin(self):
        self.min = []
        for i in xrange(self.n+1):
            self.min.append(self.find(i))
    def find(self,n):
        for s in xrange(1,self.k+1):
            if s*self.b>=n: return s
    def _genColex(self,n,r,indent=0):
        if n==0: 
            self.X.append( self.cc[:] )
        else:
            if (self.cc[r-1]-self.a)==self.b: r = r-1
            l = self.min[n]
            for i in xrange(l,r+1):
                if i==l: e=n-(l-1)*self.b
                else: e=1
                self.cc[i-1] = self.cc[i-1]+e;
                if self.doPrint:
                    print " "*indent,n-e,i
                self._genColex(n-e,i,indent)
                self.cc[i-1] = self.cc[i-1]-e;


