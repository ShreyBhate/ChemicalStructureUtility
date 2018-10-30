cd /home/MEDCHEM2/INCHI_VALIDATION/chemaxon/code;
path=`pwd`
rm -rf ./../Output/*
rm -rf *.class
/usr/local/jdk1.6.0_20/bin/javac -Djava.ext.dirs=./../lib/ MolConvertTest.java
/usr/local/jdk1.6.0_20/bin/java -Xms1024m -Xmx1024m -Djava.ext.dirs=./../lib/ MolConvertTest $1 $2
rm -rf *.class
echo "Inchi validation done!!"
echo $path
cd $path;
