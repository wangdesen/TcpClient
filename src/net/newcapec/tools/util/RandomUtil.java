package net.newcapec.tools.util;

import java.util.Random;

public class RandomUtil {
	
	static Random random = new Random();
	
	public static long randomLong(){
		return random.nextLong();
	}
	
	public static void main(String[] args) {
		System.out.println(randomLong());
	}

	
	public static long KeyGeneratorGetNext(){
		return random.nextLong();
	}
}