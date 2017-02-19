// Verilog Test Fixture Template

  `timescale 1 ns / 1 ns

  module TEST_gate;
	reg r1, r2, q1, q2;
	reg clk, clk2, clk3;
	wire w1;

	

   initial begin
      r1 <= 1;
		clk <= 1;
		clk2 <= 0;
      #200
      $finish();
	end

	/*
	// UC_MultiEvent: multiple event on the same signal (Questa: OK, ISIM: OK)
   initial begin
		@(posedge clk)
		$display("posclk %t", $time());	// this will prints at 1000
		@(posedge clk)
		$display("posclk %t", $realtime);// this will prints at 2000
		@(posedge clk)
		$display("posclk %t", $realtime);// this will prints at 3000
   end
	*/
	
    /*
	// UC_display: $display() system-function displays the previous value. Except 0!!! (Questa: OK, ISIM: OK)
   initial begin
		//$monitor("monitor: clk: %d  at: %t", clk, $time());
		$display("display: clk: %d  at: %t", clk, $time()); 	// display: clk: 1  at:  0
		#5
		$display("display: clk: %d  at: %t", clk, $time());	// display: clk: 1  at:  5000
		#5
		$display("display: clk: %d  at: %t", clk, $time()); //display: clk: 0  at:     10000
		#5
		$display("display: clk: %d  at: %t", clk, $time()); //display: clk: 1  at:     15000
   end
	*/
	
	/*
	// UC_expression: expressions uses the previous value except at 0!!! (Questa: OK, ISIM: OK)
   initial begin
		//$monitor("monitor: clk: %d  at: %t", clk, $time());
		if(clk == 1) begin	//True
			$display("display: clk IS 1 at: %t", $time());	// print
		end else begin
			$display("display: clk IS NOT 1 at: %t", $time()); // NO
		end
		#5
		if(clk == 1) begin	// TRUE
			$display("display: clk IS 1 at: %t", $time());	// print
		end else begin
			$display("display: clk IS NOT 1 at: %t", $time());	// NO
		end
		#5
		if(clk == 1) begin	// FALSE
			$display("display: clk IS 1 at: %t", $time());	// NO
		end else begin
			$display("display: clk IS NOT 1 at: %t", $time());	//print
		end
		#5
		if(clk == 1) begin	// TRUE
			$display("display: clk IS 1 at: %t", $time());
		end else begin
			$display("display: clk IS NOT 1 at: %t", $time());
		end
		#5
		if(clk == 1) begin	//FALSE
			$display("display: clk IS 1 at: %t", $time());
		end else begin
			$display("display: clk IS NOT 1 at: %t", $time());
		end
   end
	*/
	
/*
	// UC_display_set_non_block: $display() system-function displays the previous value. Except 0!!! (Questa: OK, ISIM: OK)
	// The value of r1 is equals the clock all the time.
   initial begin
		//$monitor("monitor: clk: %d  at: %t", clk, $time());
		r1 <= 1;
		$display("display: clk: %d   r1: %d at: %t", clk,  r1, $time()); // display: clk: 1   r1: 1 at:  0
		#5
		r1 <= ~r1;
		$display("display: clk: %d   r1: %d at: %t", clk,  r1, $time()); // display: clk: 1   r1: 1 at:  5000
		#5
		r1 <= ~r1;
		$display("display: clk: %d   r1: %d at: %t", clk,  r1, $time()); // display: clk: 0   r1: 0 at:  10000
		#5
		r1 <= ~r1;
		$display("display: clk: %d   r1: %d at: %t", clk,  r1, $time()); // display: clk: 1   r1: 1 at:  15000
   end
*/
/*
	// UC_display_set_block: $display() system-function displays the new value!!! (Questa: OK, ISIM: OK)
	// The value of r1 is equals the clock all the time.
   initial begin
		//$monitor("monitor: clk: %d  at: %t", clk, $time());
		r1 = 1;
		$display("display: clk: %d   r1: %d at: %t", clk,  r1, $time()); // display: clk: 1   r1: 1 at:   0
		#5
		r1 = ~r1;
		$display("display: clk: %d   r1: %d at: %t", clk,  r1, $time()); // display: clk: 1   r1: 0 at:   5000
		#5
		r1 = ~r1;
		$display("display: clk: %d   r1: %d at: %t", clk,  r1, $time()); // display: clk: 0   r1: 1 at:   10000
		#5
		r1 = ~r1;
		$display("display: clk: %d   r1: %d at: %t", clk,  r1, $time()); // display: clk: 1   r1: 0 at:   15000
   end
*/
	
	/*
	// UC_two_event_consecutive: wait two event on different signals consecutive which are shots in the same time. (Questa: OK, ISIM: OK)
	// only one event can be shot in one timeslot.
	// Display shows the new value after event.
   initial begin
		@(posedge clk2)
		$display("posclk2 clk1: %d  , clk2: %d  %t", clk, clk2, $time());	// posclk2 clk1: 0  , clk2: 1   5000
		@(negedge clk)
		$display("negclk clk1: %d  , clk2: %d  %t", clk, clk2, $time());	// negclk clk1: 0  , clk2: 1   15000
		@(posedge clk)
		$display("posclk clk1: %d  , clk2: %d  %t", clk, clk2, $time()); // posclk clk1: 1  , clk2: 0   20000
		@(negedge clk2)
		$display("negclk2 clk1: %d  , clk2: %d  %t", clk, clk2, $time()); // negclk2 clk1: 1  , clk2: 0   30000
   end
	*/
	
	/*
	// UC_display_blocking_assigned value
	// display shows the previous value of assigned values. (Questa not equals ISIM) !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	assign w1 = r1;
	
   initial begin
		r1 = 1;
		$display("display: r1: %d   w1: %d at: %t", r1, w1, $time()); // display: r1: 1   w1: x at:   0     
		#5
		r1 = ~r1;
		$display("display: r1: %d   w1: %d at: %t", r1, w1, $time()); // display: r1: 0   w1: 1 at:   5000
		#5
		r1 = ~r1;
		$display("display: r1: %d   w1: %d at: %t", r1, w1, $time()); // display: r1: 1   w1: 0 at:   10000
		#5
		r1 = ~r1;
		$display("display: r1: %d   w1: %d at: %t", r1, w1, $time()); // display: r1: 0   w1: 1 at:   15000
   end
	*/
    
	
	// UC_two_event_consecutive: wait two event on different signals consecutive which are shots in the same time.
	// only one event can be shot in one timeslot.
	// Display shows the new value after event.  (Questa: OK, ISIM: OK)
   initial begin
		r2 <= 0;
		r1 <= 0;
		q1 <= 0;
		@(posedge clk2)
			r2 <= clk;		// 0, same like the following	
			q1 <=  clk2;
			if(clk == 0) begin	// TRUE HOW !!!!!       
				r1 <= 0;		// this
			end else begin
				r1 <= 1;
				end
		@(negedge clk)
			q1 <=  clk;
			if(clk == 0) begin	// TRUE         
				r1 <= 0;		// this
			end else begin
				r1 <= 1;
				end
			r2 <= clk;
		
		@(posedge clk2)
			r2 <= clk3;		// 0    
			q1 <=  clk2;    // 1
			if(clk == 0) begin	// TRUE HOW !!!!!       
				r1 <= 0;		// this
			end else begin
				r1 <= 1;
				end
		@(negedge clk)
			q1 <=  clk3;    // 0
			if(clk == 0) begin	// TRUE         
				r1 <= 0;		// this
			end else begin
				r1 <= 1;
				end
			r2 <= clk;
            
            
		@(posedge clk3)
			r2 <= clk3;		// 1    
			q1 <=  clk2;    // 1
			if(clk == 0) begin	// TRUE HOW !!!!!       
				r1 <= 0;		// this
			end else begin
				r1 <= 1;
				end
		@(posedge clk3)
			q1 <=  clk3;    // 1
			if(clk == 0) begin	// TRUE         
				r1 <= 0;		// this
			end else begin
				r1 <= 1;
				end
			r2 <= clk;
		end
        
        
		
	// ...cont...
   initial begin
		@(posedge clk2)
			q2 <= q1;		// 0: depends on that q1 was assigned as blocking or non blocking
			
		@(negedge clk)
			q2 <= q1;		// 1: depends on that q1 was assigned as blocking or non blocking
		
		end

    
    
	always #5 begin
		clk <= ~clk;
		clk2 <= ~clk2;
	end
    
	always @ clk2
		clk3 <= clk2;

  endmodule
