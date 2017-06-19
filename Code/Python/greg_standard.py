import numpy as np

#
# An efficient implementation of the formula g(m,n) from the paper: C. Flight. (1990) How many stemmata?. Manuscripta 34(2), 122-128.
# See also: https://oeis.org/A005264


def greg2(n):
    m=0
    for k in xrange(n):
        m+= TT(n,k)
    return m


# g(m,n) from the Flight paper
def TT(n,k,retTable=False):
    M = k+n 
    # need arbitrary precision
    A = np.zeros((n+1,M+1),dtype=object)
    A[0,0]=1L
    A[1,0]=1L
    for i in xrange(n+1):
        for j in xrange(M+1):
            if (i,j) in [(0,0),(1,0)]: continue
            if j>i:
                continue
            if j>0 and i>0:
                f1 = A[i-1,j-1]
            else:
                f1 = 0
            if i>0:
                f2 = A[i-1,j]
            else:
                f2 = 0
            if i>0 and j<M:
                f3 = A[i-1,j+1]
            else:
                f3 = 0
            alpha = i+j-3
            beta = i+j-1
            gamma = i+j-2
            delta = j+1
            A[i,j] = (alpha+1)*f1+(beta+gamma+1)*f2+delta*f3
    if retTable:
        return A
    else:
        return A[n,k]

