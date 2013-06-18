Requirements
---------------
* Java 1.6 and up (for the predictors)
* Python 2.7 and up (for the graph)
  * Matplotlib for python
  * Numpy for python

How to run
---------------
* To generate dummy data, run:
java RandomDataGenerator <location>

where <location> is the name of the file to write the data to.

* To get the predictor's results, run:
java PredictorTester <in> <out>

where <in> is the location of the file containing the data, and <out>
is the location of the file to write the results to.

* To display a graph of the results, run:
python plot.py <results>

where <results> is the location of the file containing the predictor's results.

