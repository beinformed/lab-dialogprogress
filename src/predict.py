import subprocess
import sys

if(len(sys.argv) < 3):
    print("Syntax:\n\
python plot.py <data> <config> <out>\n\
<data>: the data to parse, train on and plot.\n\
<config>: a config file containing the predictors to use\n\
<out>: location of the output csv file (input to plot.py)")
    exit()

location = sys.argv[1]
result_location = sys.argv[3]
config_location = sys.argv[2]

print('Running java to generate results...')

subprocess.call(['java', '-Xmx1g', '-cp', 'bin:neuroph-core-2.7.jar', 'com.beinformed.research.labs.dialogprogress.testing.PredictorTester',
                location, result_location, config_location])
