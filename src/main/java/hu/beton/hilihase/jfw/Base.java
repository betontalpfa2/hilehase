package hu.beton.hilihase.jfw;


public class Base {

//	private static Base base = null;
//	private List<Signal> signals;
	
	Base(){
//		System.out.println(System.getProperty("java.library.path"));
//		System.loadLibrary("jfw-1.0-SNAPSHOT");
//		signals = new ArrayList<Signal>();
//		signals.add(0, null);
//		base = this;
	}
	
//	public static Base getBase() {
//		if(null == base){
//			System.out.println("BASE is NULLL");
//			throw new NullPointerException("Base has not been initialized!");
//		}
//		return base;
//	}
	public synchronized void step(int currentTime) {
//		this.currenTime++;
		notifyAll();
		return;
	}

	public synchronized void startTC(String  tcName) {
		Thread tc = new Thread(new Runnable() {
			
//			@Override
			public void run() {
				try{
				} catch(Exception ex){
//					System.out.println("[ CRITICAL ERROR ] unhandled exception! at "  + getTime());
					ex.printStackTrace();
				}
			}
		});
		tc.start();
	}
	
	

//	public static void initBase() {
////		base = new Base();
//	}
	public static void destroyBase() {
//		base = null;
	}


}
