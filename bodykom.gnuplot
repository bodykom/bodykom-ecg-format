set style data lines
set terminal png nocrop enhanced size 1000,300
set xtics 150
set grid
do for [s=0:46080:1024] {
    end = s + 1023
    outfile = sprintf("bodykom-%08d-%08d.png", s, end)
    set output outfile
    plot [s:end][-6000:15000] "bodykom.dat" using 1:2,"bodykom.dat" using 1:3
}
