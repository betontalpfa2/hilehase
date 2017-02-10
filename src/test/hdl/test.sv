module Bus();
    int a;
    reg b;
    int b2;
    
    
    reg top_x;
    reg top_y;
    reg top_cin;
     
    logic out;
    logic carryout;
    
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
    import "DPI-C" function int  hilihase_step ( int curr_time );

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
    import "DPI-C" function int  hilihase_read (int id, byte a);
    
    /**
     *   hilihase_drive: 
     * This queries the signal with the gives signal if it changed in the current timeslot.
     */
    import "DPI-C" function byte  hilihase_drive (int id);
  
  
    task automatic hilihase_read_wrap(int id,  ref  logic signal_to_listen );
    begin    
        forever @(signal_to_listen) begin
           a=  hilihase_read (id,  convert(signal_to_listen ));
    // $display("[%t ] b2: %d", $realtime (), a); 
        end
    end
    endtask
    /**************************************************************************/
    /**************************** Instantiate top module **********************/
    /**************************************************************************/
     
    fulladder uut (
        .x(top_x),
        .y(top_y),
        .cin(top_cin),
        .A(out),
        .cout(carryout)
    );
    
    /**************************************************************************/
    /**************************** Connect HILIHASE ****************************/
    /**************************************************************************/
    
    initial begin
        a= hilihase_init(2, "/home/ebenera/hilihase/target/classes");
        assert(a==0);
        if (a<0)$finish(a);
        
        a= hilihase_register(1, "top_x", top_x);
        assert(a==0);
        a= hilihase_register(2, "top_y", top_y);
        assert(a==0);
        a= hilihase_register(3, "top_cin", top_cin);
        assert(a==0);
        a= hilihase_register(4, "out", out);
        assert(a==0);
        a= hilihase_register(5, "carryout", carryout);
        assert(a==0);
        
        fork
            hilihase_read_wrap(1,  top_x );
            hilihase_read_wrap(2,  top_y );
            hilihase_read_wrap(3,  top_cin );
            hilihase_read_wrap(4,  out );
            hilihase_read_wrap(5,  carryout );
        join;
        
    end 
    
    
    always #1 begin
        $display($realtime());
        a = hilihase_step (  $realtime() );
        a = hilihase_step (  $realtime() );
    end
    
    always @(top_x) begin
        a = hilihase_read (1,  convert(top_x ));
    // $display("[%t ] b2: %d", $realtime (), a); 
    end
    // initial
    // begin
    // input1 =0;
    // input2 =0;
    // carryin =0;
    // #20; input1 =1;
    // #20; input2 =1;
    // #20; input1 =0;
    // #20; carryin =1;
    // #20; input2=0;
    // #20; input1=1; 
    // #20; input2=1;
    // #40;
    // end
  
  always @(b) begin
  // a = hilihase_signal (  convert(b ));
    $display("[%t ] b2: %d", $realtime (), a); 
  end
  
  
  // always #1 begin
  // a = hilihase_time (  $realtime() );
      
  
  initial begin
  
  // a = hilihase_echo1(42); // Arguments passed by copy
    // $display(a);
 
  // #5
    // a= hilihase_init(2, "/home/ebenera/hilihase/jni");

        // $display(a);
        // $stop(a);
    
    $display(a);
    #1
    b = 1;
    #1
    b = 0;
    #1
    b = 1'bx;
    #1
    b = 1'bz;
    #1
  a = hilihase_echo2 ( 84 );
    $display(a);
    #10
  a =hilihase_close ( ) ;
    $display(a);
  
  // #5
  $finish(0);
  
  end
endmodule
