Create a GITHUB repo
The c shared lib is only for connector. An interface between the SV and JAVA. Initialize and destroy JAVA and call the proper funtion.

Fundamentals:
    Signals and the time variable are equals (both implements an interface called ISignal)
    ISignal:
        set()
        processWaiton()
        WaitOn()
        
Simulation process structure:
    SV:                                              JAVA:
    +-> always @signal                                  Signal:
    |       read(signal)                                    
    |           +---------------------------------------->  set(){
    |                                                           processWaiton()  ---+
    .                                                       }                       |
    .                                                                               |
    .   ...                                                 processWaiton() {    <--+
    |                                                           if(...)
    |                                                               drive()
    +-  drive()  <----------------------------------------------------+
                                                            }
