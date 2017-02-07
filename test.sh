#!/bin/bash

./make.sh
if [ $? != 0 ]
    then
    exit 1
fi


echo "Running test ..."
cd rundir 
vsim Bus -batch -sv_lib /home/ebenera/hilihase/clibs/connector -64 -do "run -all"
cd ..