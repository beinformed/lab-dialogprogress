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
python src/plot.py <data> [out]

where <data> is the location of the datafile and [out] is the target location
for an image file containing the graph. If [out] is not specified, a window
is opened and the graph is shown that way.
