= Parse Bodykom ECG data format and plot =

This is an example of how to parse and interpret the Bodykom ECG data format.
It contains a java class, a gnuplot script, and a Makefile to illustrate how to
use the example. The data file 000052.zip is an example of the Bodykom ECG
format.

== Known issues and assumptions ==

There are a some known issues, assumptions and recommendations when parsing
the data format.

- The exact directory structure and file names in the zip file will vary
depending on generation of recorder. Older recordings may not contain a
subdirectory.

- At the start of each recording signal distortion artifacts occurs. Therefore
the first minute of collected data should always be disregarded before ECG
analysis.

- Depending on how the recorder electrodes are positioned on the person using
the recorder the signals form one or both of the ECG channels may be inverted.
That is, the R-value of the QRS complex may be presented as a minimum value and
the Q/S values as maximum values. This can of course be corrected with a
inversion of the sample values.

- Temporary bluetooth signal loss between the recorder and the mobile device
will be illustrated as a square wave. These recording gaps must be excluded
from ECG analysis.
