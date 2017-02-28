// module full_adder_tb();
    /**************************************************************************/
    /**************************** HILIHASE UTIL FUNCTIONS *********************/
    /**************************************************************************/
    /*
        This section must be placed in a separate file.
    */
    
    /**
     * convert: 
     *   Convert the classical signal values into integer. Maybe there is a
     *   built-in function for this.
     */
    function byte  convert ( logic a ) ;
    begin
        if (a === 1'b1)
            return 1;
        if (a === 1'b0)
            return 0;
        if (a === 1'bx)
            return 2;
        if (a === 1'bz)
            return 3;
            
            end
    endfunction

    /**
     *  hilihase_init:
     *  initialise the Java framework
     *  argc must be 2
     *  argv is the path to the java class. (/home/ebenera/hilihase/target/classes)
     */
    import "DPI-C" function int  hilihase_init ( int argc, string argv );
    
    /**
     *  hilihase_step:
     *  At the beginnig of each time-slot simulator must call this function
     *  to inform the framework about the step.
     *  The return value can be:
     *      0: Nothing -- Go on
     *      1: Exit the simulator
     *      Negative: error
     */
    import  "DPI-C" context function int  hilihase_step ( int curr_time );

    /**
     * hilihase_close
     * Closes the Java framework at the end of the simulation.
     */
    import "DPI-C" function int  hilihase_close ( ) ;
    
    /**
     * hilihase_echo1:
     *    Just for test. It does not uses Java. It just test the DPI aka.
     * the connection between the systemverilog simulator and the C native funtions.
     * It just exhoes the given parameter.
     */
    import "DPI-C" function int  hilihase_echo1 ( int a);
    
    /**
     * hilihase_echo2:
     *  Same as hilihase_echo1, just it uses and tests Java framework too.
     *    (You must initialise the Framework before calling this.)
     */
    import "DPI-C" function int  hilihase_echo2 ( int a);
    
    /**
     *   You must register all signals at the beginnig of the simulation with they init value.
     */
    import "DPI-C" function int  hilihase_register (int id, string name, byte init_val);
    
    /**
     * hilihase_read: Simulator must call this funtion on each signal each change 
     *   to inform the JAva framework about the state of signals
     *   Use registered id to identify the signals.
     */
    import "DPI-C" context function int  hilihase_read (int id, byte a, int sim_time);
    
    
    import "DPI-C" function int  hilihase_start_tc(string str);
    
    /**
     *   hilihase_drive: 
     * This queries the signal with the gives signal if it changed in the current timeslot.
     */
    // import "DPI-C" task hilihase_drive (int id, byte val);
  
    /**
     *   hilihase_drive: 
     * This queries the signal with the gives signal if it changed in the current timeslot.
     */
    // export "DPI-C" function hilihase_drive2;
    
    /*function void hilihase_drive2(int id, byte data);
    begin
        $display("FSDFSDFSDFSDFSDFSDFSDFSDFSFSDF$$$$$$$$$$$$$$$$$$$$$$$");
         case (id)
            1: top_x = data;
            2: top_y = data;
            default : $display("[%t ] ERROR in hilihase_drive2", $realtime ()); 
         endcase
    end
    endfunction
    */
  
    task automatic hilihase_read_wrap(int id,  ref  logic signal_to_listen );
    begin    
        forever @(signal_to_listen) begin
           int a =  hilihase_read (id,  convert(signal_to_listen ), $realtime);
           assert(a == 0);
           //$display("[%t ] b2: %d", $realtime (), a); 
        end
    end
    endtask