#!/bin/bash


if [[ "$OSTYPE" == "linux-gnu" ]]; then
        # ...
    # continue
    echo $OSTYPE
elif [[ "$OSTYPE" == "linux" ]]; then
        # ...
    # continue
    echo $OSTYPE
elif [[ "$OSTYPE" == "darwin"* ]]; then
        # Mac OSX
    exit 1
elif [[ "$OSTYPE" == "cygwin" ]]; then
        # POSIX compatibility layer and Linux environment emulation for Windows
    exit 2
elif [[ "$OSTYPE" == "msys" ]]; then
        # Lightweight shell and GNU utilities compiled for Windows (part of MinGW)
        # we just test JAVA on Windows.
    exit 0
elif [[ "$OSTYPE" == "win32" ]]; then
        # I'm not sure this can happen.
    exit 4
elif [[ "$OSTYPE" == "freebsd"* ]]; then
        # ...
    exit 5
else
        # Unknown.
    exit 6
fi

if [  -z ${__SETUP_RUNNED__+x} ]
  then
  echo "please run the following command:"
  echo "source sourceme.sh"
  exit 1
fi

vlog -dpiheader ./src/main/c/dpiheader.h src/test/hdl/test.sv