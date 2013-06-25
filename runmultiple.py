import subprocess
import sys
import os
from re import sub

if(len(sys.argv) < 4):
    print("Syntax:\n\
python plot.py <datadir> <config> <outdir>\n\
<data>: the directory containing the data to parse, train on and plot.\n\
<config>: a config file containing the predictors to use\n\
<out>: the directory for the output csv files (input to plot.py)")
    exit()

data_location = sys.argv[1]
config_location = sys.argv[2]
output_location = sys.argv[3]

files = [os.path.splitext(file)[0] for file in os.listdir(data_location) if file.endswith('.csv')]
train = [file for file in files if file.endswith('_train')]
test = [file for file in files if file.endswith('_test')]
pairs = list(zip(train, test))

print('> Running java to generate results...')
# Windows seperates cp with ; other OS's do it with :
classpath = 'bin;encog.jar' if os.name == 'nt' else 'bin:encog.jar'


for (train,test) in pairs:
    train_location = os.path.join(data_location, train + '.csv')
    test_location = os.path.join(data_location, test + '.csv')
    result_location = os.path.join(output_location, sub('_train$', '', train) + '.csv')
    print('> Generating ' + result_location + '...')
    subprocess.call(['java', '-Xmx1g', '-cp', classpath, 'com.beinformed.research.labs.dialogprogress.testing.PredictorTester',
                train_location, test_location, result_location, config_location])
