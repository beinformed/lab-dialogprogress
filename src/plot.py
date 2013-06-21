import pylab as plt
import csv
import itertools
import sys

if(len(sys.argv) < 2):
    print("Syntax:\n\
python plot.py <results> [out]\n\
<results> location of the file containing the predictor's results.\n\
[out]: name of file to save the image to. If not provided, the graph is shown on the screen.")
    exit()

location = sys.argv[1]
out = sys.argv[2] if len(sys.argv) >= 3 else ''

print('Generating graph...')

graphs = {}
graphlabels = {}
with open(location, 'rt') as csvfile:
    reader = csv.reader(csvfile)
    next(reader, None)
    maingroup = lambda x: x[0:3]
    subgroup = lambda x: x[3]
    data = sorted(reader, key=maingroup)
    for (u,xl,yl), l in itertools.groupby(data, key=maingroup):
        gdata = sorted(list(l), key=subgroup)
        graphs[u] = []
        graphlabels[u] = (xl, yl)
        for p, d in itertools.groupby(gdata, key=subgroup):
            graphs[u] += [(p, [x[4:] for x in d],),]
            
fig = plt.figure(figsize=(10,10))
linedata = []
linenames = []
start = 221
plots = []
for unit in graphs:
    lines = graphs[unit]
    ax = fig.add_subplot(start)
    plt.title(unit)
    plt.xlabel(graphlabels[unit][0])
    plt.ylabel(graphlabels[unit][1])
    start += 1
    for (line, data) in lines:
        ndata = [(float(x), float(y)) for [x,y] in data]
        X, Y = zip(*sorted(ndata, key=lambda x : x[0]))
        ax.plot(X, Y, label=line)

plt.legend(bbox_to_anchor=(0, 0, 1, 1), bbox_transform=plt.gcf().transFigure)

if out == '':
    plt.show()
else:
    plt.savefig(out, bbox_inches=0)
    print('Graph saved to ' + out)
