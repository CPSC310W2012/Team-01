#Given two lists, this script prints out elements only in
#list A, only in list B, and in both A and B.
A = [1,2,3,4,5,6,7,8]
B = [2,4,6,8,10,12,14,16]
inB = dict(zip(B,B))
inBoth = []
inA = []

for key in A:
	if inB.get(key, None):
		inBoth.append(key)
		del inB[key]
	else:
		inA.append(key)

print "A: %s" % A
print "B: %s" % B
print "In A only: %s" % inA
print "In B only: %s" % inB.keys()
print "In both: %s" % inBoth
