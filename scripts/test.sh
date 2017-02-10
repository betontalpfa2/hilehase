#!/bin/bash

if [  -z ${__SETUP_RUNNED__+x} ]
  then
  echo "please run the following command:"
  echo "source sourceme.sh"
  exit 1
fi

echo "Running test ..."
cd target 
vsim Bus -batch -sv_lib nar/jfw-1.0-SNAPSHOT-amd64-Linux-gpp-shared/lib/amd64-Linux-gpp/shared/libjfw-1.0-SNAPSHOT -64 -do "run -all"
cd ..