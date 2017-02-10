#!/bin/bash


if [  -z ${__SETUP_RUNNED__+x} ]
  then
  echo "please run the following command:"
  echo "source sourceme.sh"
  exit 1
fi

vlog -dpiheader ./src/main/c/dpiheader.h src/test/hdl/test.sv