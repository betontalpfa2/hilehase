#!/bin/bash

source ./setup.sh

./make.sh

echo "Running test ..."
cd rundir 
vsim Bus -batch -sv_lib /home/ebenera/hilihase/clibs/connector -64 -do "run -all"
cd ..