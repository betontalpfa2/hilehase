package hu.beton.hilihase.jfw;

public class Sample2 implements IBase
 {
	public static void hilihase_log(String msg) {

    	System.out.println("HILIHASE: " + msg);
	}
	
    public static int echo(int n) {
       return n;
    }

    public static boolean booleanMethod(boolean bool) {
        return !bool;
    }

    public static int hilihase_signal(byte a){
        if (a < 2){
        System.out.println("Signal is defined!");
        return 0;
        }
        
        System.out.println("Signal is undefined!");
        return 1;
    }

    public static int hilihase_step(int current_time){
        return -1;
    }
        
    public static int hilihase_register(int id, String name, byte initval){
    	Sample2.hilihase_log("Signal registered. Id: " + id + " Name: " + name + " init val: " + initval);
    	Base.getBase().register_signal(id, name, ValueE.ValueOf(initval));
        return 0;
    }
    

	public static int hilihase_read(int id, byte val){
    	Base.getBase().read_signal(id, ValueE.ValueOf(val));
        return -1;
    }
    
    public static byte hilihase_drive(int id){
        return -1;
    }
    
    
 }