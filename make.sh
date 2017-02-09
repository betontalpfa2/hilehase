#!/bin/bash


if [  -z ${__SETUP_RUNNED__+x} ]
  then
  echo "please run the following command:"
  echo "source sourceme.sh"
  exit 1
fi

echo "Building connector.so ..."
cd clibs
gcc -fPIC connector.c -shared -o connector.so -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" -ljvm -L$JAVA_HOME/jre/lib/amd64/server
if [ $? != 0 ]
    then
    exit 1
fi
cd ..

echo "Compiling Sample2.java..."
cd jfw
mvn compile
# javac ./main/Sample2.java
if [ $? != 0 ]
    then
    exit 1
fi
cd ..

echo "Compiling test.sv ..."
cd rundir 
vlog ../hdl/full_adder.v
vlog ../hdl/test.sv
if [ $? != 0 ]
    then
    exit 1
fi
cd ..