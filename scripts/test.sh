#!/bin/bash

./make.sh
if [ $? != 0 ]
    then
    exit 1
fi


echo "Running test ..."
cd rundir 
vsim Bus -batch -sv_lib ../jfw/target/nar/jfw-1.0-SNAPSHOT-amd64-Linux-gpp-shared/lib/amd64-Linux-gpp/shared/libjfw-1.0-SNAPSHOT -64 -do "run -all"
cd ..