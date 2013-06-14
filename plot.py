import pylab as plt
import csv
import itertools
import subprocess
import sys

if(len(sys.argv) < 2):
    print("Syntax:\n\
python plot.py <results>\n\
<results>: The predictor's output to plot")
    exit()

location = sys.argv[1]

graphs = {}
with open(location, 'rt') as csvfile:
    reader = csv.reader(csvfile)
    next(reader, None)
    maingroup = lambda x: x[1]
    subgroup = lambda x: x[0]
    data = sorted(reader, key=maingroup)
    for u, l in itertools.groupby(data, key=maingroup):
        gdata = sorted(list(l), key=subgroup)
        graphs[u] = []
        for p, d in itertools.groupby(gdata, key=subgroup):
            graphs[u] += [(p, [x[2:] for x in d],),]
            
fig = plt.figure(figsize=(12,5))
linedata = []
linenames = []
start = 121
for unit in graphs:
    lines = graphs[unit]
    ax = fig.add_subplot(start)
    plt.title(unit)
    plt.xlabel('Trace Length')
    plt.ylabel('Error')
    start += 1
    for (line, data) in lines:
        ndata = [(int(x), float(y)) for [x,y] in data]
        X, Y = zip(*sorted(ndata, key=lambda x : x[0]))
        ax.plot(X, Y, label=line)
    plt.savefig('test.pdf', bbox_inches=0)
plt.legend()
plt.show()
plt.savefig('accuracy.pdf', bbox_inches=0)
