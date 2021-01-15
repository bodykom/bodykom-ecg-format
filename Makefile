parse: clean
	javac ParseMultiChannelEcg.java
	java ParseMultiChannelEcg 000052.zip > bodykom.dat
	gnuplot bodykom.gnuplot

clean:
	rm -f ParseMultiChannelEcg.class bodykom-*\.png bodykom.dat

