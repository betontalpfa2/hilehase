package hu.beton.hilihase.util;

public class Util {
	public static void assertUtil(boolean exp){
		if(exp){
			return;
		}
		throw new AssertionError("ERROR");
	}
}
