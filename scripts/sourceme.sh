#!/bin/bash

# Check if __SETUP_RUNNED__ is exist:
if [  -z ${__SETUP_RUNNED__+x} ]
  then
    # not exist...
    module add jdk/latest
    export LD_LIBRARY_PATH=$JAVA_HOME/jre/lib/amd64/server:$LD_LIBRARY_PATH
    export __SETUP_RUNNED__=1
    module add maven
    
    QUESTA_HOME=`which vsim`
    QUESTA_HOME="$(dirname "$QUESTA_HOME")"
    QUESTA_HOME="$(dirname "$QUESTA_HOME")"
    QUESTA_HOME="$(dirname "$QUESTA_HOME")"
    export QUESTA_HOME
else
    echo "Nothing to setup. To run again setup please unset __SETUP_RUNNED__ env var.";
fi

