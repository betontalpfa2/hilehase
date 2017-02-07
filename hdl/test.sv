module Bus(input In1, output Out1);
    int a;
    

  import "DPI-C" function int  hilihase_init ( int argc, string argv ) ;
  
  import "DPI-C" function int  hilihase_close ( ) ;
  import "DPI-C" function int  hilihase_echo1 ( int a);
  import "DPI-C" function int  hilihase_echo2 ( int a);
  
  // import "DPI-C" function int java(input int asrgc,
                                         // input string argv);
  // import "DPI-C" function int slave_write(input int address,
                                         // input int data);
  // export "DPI-C" function write;  // Note – not a function prototype

  // This SystemVerilog function could be called from C
  // function void write(int address, int data);
    // Call C function
    // a = slave_write(address, data); // Arguments passed by copy
    // $display(a)
  // endfunction
  
  initial begin
  
  a = hilihase_echo1(42); // Arguments passed by copy
    $display(a);
  // ...
  
  #5
 a= hilihase_init(2, "/home/ebenera/hilihase/jni");
    $display(a);
  a = hilihase_echo2 ( 84 );
    $display(a);
  a =hilihase_close ( ) ;
  // a = java(2, "/home/ebenera/hilihase/jni"); // Arguments passed by copy
    $display(a);
  
  #5
  $finish(0);
  
  end
endmodule

// #include "svdpi.h"
// extern void write(int, int);    // Imported from SystemVerilog
// void slave_write(const int I1, const int I2)
// {  
  // buff[I1] = I2;
  // ...
// }