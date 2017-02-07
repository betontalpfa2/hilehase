#!/bin/bash

source ./setup.sh

echo "Building connector.so ..."
cd clibs
gcc -fPIC connector.c -shared -o connector.so -I"$JAVA_HOME/include" -I"$JAVA_HOME/include/linux" -ljvm -L$JAVA_HOME/jre/lib/amd64/server
cd ..

echo "Compiling Sample2.java..."
cd jni 
javac Sample2.java 
cd ..

echo "Compiling test.sv ..."
cd rundir 
vlog ../hdl/test.sv
cd ..