package hu.beton.hilihase.jfw;

import java.util.Vector;

public class Sample2 implements IBase
{

	private static java.lang.reflect.Field LIBRARIES = null;
	static {
		try {
			LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames");
			LIBRARIES.setAccessible(true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String[] getLoadedLibraries(final ClassLoader loader) throws IllegalArgumentException, IllegalAccessException {
		final Vector<String> libraries = (Vector<String>) LIBRARIES.get(loader);
		return libraries.toArray(new String[] {});
	}

	private static void handleUnhandled(Exception ex) {
		System.out.println("[ CRITICAL WARNING ] Unhandled exception!!!");
		ex.printStackTrace();
	}

	public static void hilihase_log(String msg) {

		System.out.println("HILIHASE: " + msg);
	}

	public static int echo(int n) {
		return n;
	}

	@Deprecated
	public static boolean booleanMethod(boolean bool) {
		return !bool;
	}

	@Deprecated
	public static int hilihase_signal(byte a){
		if (a < 2){
			System.out.println("Signal is defined!");
			return 0;
		}

		System.out.println("Signal is undefined!");
		return 1;
	}

	public static int hilihase_step(int current_time){
		try{
			Base.getBase().step(current_time);
		} catch (Exception ex){
			handleUnhandled(ex);
			return -1;
		}
		return 0;
	}

	public static int hilihase_register(int id, String name, byte initval){
		try{
			Sample2.hilihase_log("Signal registered. Id: " + id + " Name: " + name + " init val: " + initval);
			Base.getBase().register_signal(id, name, ValueE.ValueOf(initval));
		} catch (Exception ex){
			handleUnhandled(ex);
			return -1;
		}
		return 0;
	}

	public static int hilihase_init(int param){
		try{
			System.out.println("Initialize JAVA (myself)");
//			System.out.println(System.getProperty("java.library.path"));
//			System.loadLibrary("jfw-1.0-SNAPSHOT");
//			System.load("/home/ebenera/hilihase/target/nar/jfw-1.0-SNAPSHOT-amd64-Linux-gpp-shared/lib/amd64-Linux-gpp/shared/libjfw-1.0-SNAPSHOT.so");
//			System.out.println("[  OK  ]@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			Base.initBase();
			System.out.println("[  OK  ]$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		} catch (Exception ex){
			handleUnhandled(ex);
			return -1;
		}
		return 0;
	}

	public static int hilihase_close(int param){
		try{
			System.out.println("Destroy Base...");
			Base.destroyBase();
			System.out.println("Destroing Base finished");
		} catch (Exception ex){
			handleUnhandled(ex);
			return -1;
		}
		return 0;
	}


	public static int hilihase_read(int id, byte val){
		try{
			Base.getBase().read_signal(id, ValueE.ValueOf(val));
		} catch (Exception ex){
			handleUnhandled(ex);
			return -1;
		}
		return 0;
	}

	//    public static byte hilihase_drive(int id){
	//        return Base.getBase().get(id).drive();
	//    }

	public native static int hilihase_drve(int id, byte val);

	public static int hilihase_start_tc(String tcName){
		try{
			Base.getBase().startTC(tcName);
		} catch (Exception ex){
			handleUnhandled(ex);
			return -1;
		}
		return 0;
	}

}