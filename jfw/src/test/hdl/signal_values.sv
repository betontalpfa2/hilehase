module signal_test();
    reg clk;
    reg a;
    reg b;
    
    initial begin
        clk = 1;
        a = 0;
    end
    
    always #5
        clk = ~clk;
        
    always @(posedge clk) begin
        $display("[%t ] before invert: %d", $time(), a); 
        // fprintf("a: %d, time: %t", a, realtime);
        a = ~a;
        $display("[%t ] after invert: %d", $time(), a); 
        // $display(a, realtime);
    end
    
       
    always @(posedge clk) begin
        b <= a;
        $display("[%t ] simple: %d", $time(), a);
        $display("[%t ] b: %d", $time(), b);
    end
    
    
    always @(posedge clk) begin
        // $display(a, realtime);
    end
    
    always @(posedge a) begin
        $display("[%t ] posedge a: %d", $time(), a);
    end

endmodule
