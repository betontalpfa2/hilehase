// Verilog Test Fixture Template

  `timescale 1 ns / 1 ns

  module TEST_gate;
	reg r1, r2, r3, r4;
	reg clk, cclk;
	wire w1;

	

   initial begin
      r1 <= 1;
      r2 <= 0;
      clk <= 1;
      #200
      $finish();
    end
    
    always #5
        clk <= ~clk;
        
    always @(posedge clk) begin
        r1 <= r2;
        r2 <= r1;
        r3 <= cclk;
    end
            
    always @(posedge cclk) begin
        r4 <= clk;
    end
    
	always @ clk
		cclk <= clk;
        
        
  endmodule
