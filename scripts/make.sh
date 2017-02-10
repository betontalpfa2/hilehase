#!/bin/bash


if [  -z ${__SETUP_RUNNED__+x} ]
  then
  echo "please run the following command:"
  echo "source sourceme.sh"
  exit 1
fi


# echo "Compiling Java and C with maven..."
# cd jfw
# mvn compile
javac ./main/Sample2.java
# if [ $? != 0 ]
    # then
    # exit 1
# fi
# cd ..

echo "Compiling test.sv ..."
cd target 
vlog ../src/test/hdl/full_adder.v
vlog ../src/test/hdl/test.sv
if [ $? != 0 ]
    then
    exit 1
fi