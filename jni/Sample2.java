public class Sample2
 {
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
        
        // if (a < 2)
        System.out.println("Signal is undefined!");
        return 1;
    }

    public static int hilihase_step(int current_time){
        return -1;
    }
        
    public static int hilihase_register(int id, char[] name, byte initval){
        return -1;
    }
    
    public static int hilihase_read(int id, byte val){
        return -1;
    }
    
    public static byte hilihase_drive(int id){
        return -1;
    }
    
    
 }