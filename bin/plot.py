import pylab as plt
import csv
import itertools
import sys

if(len(sys.argv) < 2):
    print("Syntax:\n\
python plot.py <results>\n\
<results> location of the file containing the predictor's results.")
    exit()

location = sys.argv[1]

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
            
fig = plt.figure(figsize=(4,4))

for unit in graphs:
    lines = graphs[unit]
    plt.xlabel(graphlabels[unit][0])
    plt.ylabel(graphlabels[unit][1])
    for (line, data) in lines:
        ndata = [(float(x), float(y)) for [x,y] in data]
        X, Y = zip(*sorted(ndata, key=lambda x : x[0]))
        plt.plot(X, Y, label=line)
    plt.legend(bbox_to_anchor=(0, 0, 1, 1), bbox_transform=plt.gcf().transFigure)
    plt.savefig(location.replace('.csv', '') + '_' + unit.replace(' ', '_').lower() + '.png', bbox_inches=0)
    plt.close()
