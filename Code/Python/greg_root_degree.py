#! /usr/bin/python
#
#
# Computing numbers of Greg trees with root degrees k and m labeled and n unlabeled nodes
# Steffen Eger
# 01.05.2017 
# Please cite: Armin Hoenen, Steffen Eger, Ralf Gehrke. (2017) How many stemmata with root degree k?. Proceedings of the 15th Meeting on the Mathematics of Language (MoL). Association for Computational Linguistics (ACL).


import sys,numpy as np,itertools
from scipy.special import binom,factorial
import operator
from colex import Colex
from decimal import Decimal
from greg_standard import TT,greg2


# SAMPLE RUN:
# python2 greg_root_degree.py 10
# lists all numbers of trees g_k(10) for k=1..10

def prod(factors):
    return reduce(operator.mul, factors, 1)

# the function g_k(m,n) from the paper
# C1, C2 are the summation indices
def g_k(m,n,k,C1,C2,table=None):
    my_sum = 0
    for s in C1:
	if sum(s)!=m-1: continue
        multi = int(int(factorial(m-1))*1.0/prod([factorial(x) for x in s]))
        q = 1
        M = 0
        l = [range(s_i) for s_i in s]
        for p in itertools.product(*l):
            if sum(p)!=n: continue
            q = 1
            for i in xrange(k):
                q *= table[s[i],p[i]]
            M += int(q)
        my_sum += multi*M
    my_sum2 = 0
    if k==1:
	return (my_sum*m+my_sum2)/int(factorial(k))
    for s in C2:
	if sum(s)!=m: continue
        multi = int(factorial(m)*1.0/prod([factorial(x) for x in s]))
        M = 0
        l = [range(s_i) for s_i in s]
        for p in itertools.product(*l):
            if sum(p)!=n-1: continue
            q = 1
            for i in xrange(k):
                q *= table[s[i],p[i]]
            M += int(q)
        my_sum2 += multi*M
    return (my_sum*m+my_sum2)/int(factorial(k))

# the function g_k(m) from the paper, evaluated at different k
def gk(m):

  deg={}
  ss = greg2(m)
  total=Decimal(1.0)
  # the matrix g(m,n) of Greg trees
  table = TT(m,m-1,retTable=True)

  for k in xrange(1,m+1):
    q=0
    u = m-1
    v = m
    a = 1
    b = u+1
    # compositions of n with k parts
    if u-k*a>=0:
      C1 = Colex(u-k*a,k,a,b-a)
      C1.genColex()
      x = C1.X
    else:
      x = []
    C2 = Colex(v-k*a,k,a,b-a)
    C2.genColex()
    for n in xrange(m):
      u = g_k(m,n,k,x,C2.X,table=table)
      q+=u
    print k,q,
    w = Decimal(q)*Decimal(1)/Decimal(ss)
    print w
    deg[k]=q
    total -= w


if __name__ == "__main__":

  n=int(sys.argv[1])
  print "k","Number_of_trees","Proportion"
  try:
    k=int(sys.argv[2])
  except IndexError:
    k=2
    gk(n)
