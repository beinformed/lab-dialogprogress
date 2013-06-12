import pylab as plt
import csv
import itertools
import subprocess

graphs = {}
with open('result.csv', 'rb') as csvfile:
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
            
fig = plt.figure()
linedata = []
linenames = []
start = 121
for unit in graphs:
    lines = graphs[unit]
    ax = fig.add_subplot(start)
    plt.title(unit)
    plt.xlabel('Trace Length')
    plt.ylabel('Squared Error')
    start += 1
    for (line, data) in lines:
        ndata = [(int(x), float(y)) for [x,y] in data]
        X, Y = zip(*sorted(ndata, key=lambda x : x[0]))
        ax.plot(X, Y, label=line)
    plt.legend()
plt.show()
