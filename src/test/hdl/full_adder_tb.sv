

module full_adder_tb();
    parameter tcName = "Maximal";
    parameter Mode = 1;
    int a;
    reg b;
    int b2;
    
    
    logic top_x;
    logic top_y;
    logic top_cin;
     
    logic out;
    logic carryout;
    
    logic [5:0] drivelist;
    
    assign top_x =  drivelist [1];
    assign top_y = drivelist [2];
    
`include "/home/ebenera/hilihase/misc/hilehase_util.sv"
    /**
     *   hilihase_drive: 
     * This queries the signal with the gives signal if it changed in the current timeslot.
     */
    export "DPI-C" function hilihase_drive2;
    
    function void hilihase_drive2(int id, byte data);
    begin
        $display("FSDFSDFSDFSDFSDFSDFSDFSDFSFSDF$$$$$$$$$$$$$$$$$$$$$$$");
         case (id)
            1: drivelist[1] = data;
            2: drivelist[2] = data;
            default : $display("[%t ] ERROR in hilihase_drive2", $realtime ()); 
         endcase
    end
    endfunction
  
  
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
        $monitor("monitor a:%h b:%h @ %0t", out, carryout, $time);
        $monitor("monitor a:%h b:%h @ %0t", top_x, top_y, $time);
        drivelist[1] = 0;
        drivelist[2] = 0;
        top_cin = 0;
    
        $display("BABABABA LILALALALALAL");
        a= hilihase_init(2, "./classes");
        assert(a==0);
        $display(a);
        if (a<0)$finish(a);
        
        // a= hilihase_register_time(0, "top_x", top_x);
        a= hilihase_register(1, "top_x", top_x);
        assert(a==0)else begin  $error("hilihase_register: top_x");$finish(a); end
        a= hilihase_register(2, "top_y", top_y);
        assert(a==0)else begin  $error("hilihase_register: top_y"); $finish(a);end
        a= hilihase_register(3, "top_cin", top_cin);
        assert(a==0)else begin $error("hilihase_register: top_cin");$finish(a);  end
        a= hilihase_register(4, "out", out);
        assert(a==0)else begin  $error("hilihase_register: out");$finish(a); end
        a= hilihase_register(5, "carryout", carryout);
        assert(a==0)else begin  $error("hilihase_register: carryout");$finish(a); end
        a= hilihase_start_tc(tcName);
        assert(a==0)else begin  $error("hilihase_start_tc: minimal");$finish(a); end
        
        $display("BABABABA FTFTFTFTFT");
        $fflush() ;
        fork
            hilihase_read_wrap(1,  top_x );
            hilihase_read_wrap(2,  top_y );
            hilihase_read_wrap(3,  top_cin );
            hilihase_read_wrap(4,  out );
            hilihase_read_wrap(5,  carryout );
            
            
        join_none;
        
        
        #100
        a =hilihase_close ( ) ;
        $finish(0);
    end 
    
    
    always #1 begin
        $display($realtime());
        a = hilihase_step (  $realtime() );
    end
    
endmodule
