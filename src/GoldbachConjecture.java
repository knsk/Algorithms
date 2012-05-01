import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class GoldbachConjecture {
	/*
	 * Verify Goldbach's Conjecture for 4 <= i <= 2^20 where i % 2 = 0 
	 */
	public static void main(String args[]) {
		int a = -1;
		int b = -1;
		int num = 0;
		boolean flag = false;
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter("result.txt"));
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		for (int i = 4; i <= Math.pow(2,20); i += 2) {
			a = getLowerPrimeOfSum(i);
			if (a == -1) {
				flag = true;
				num = i;
				break;
			}
			b = i - a;
			try {
			    bufferedWriter.write(a + " + " + b + " = " + i + System.getProperty("line.separator"));
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		try {
			bufferedWriter.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		if (flag) {
			System.out.println("There don't exist two primes that sum up to " + num);
		}
	}
	
	/*
	   Given an integer k (1<=k<=2,000,000,000), find two prime numbers that sum up to it and return the lower number. 
	   If there are multiple solutions, always return the lowest prime. If there are no solutions, return -1.
	   Examples:
	   k=12 gives 5 (5 + 7 = 12)
	   k=68 gives 7 (7 + 61 = 68)
	   k=77 gives -1
	 */
	
	public static int getLowerPrimeOfSum(int k) {
		if (k < 4) { // Smallest prime number is 2
			return -1;
		}
		
		int biggestPrime = 2;
		int num = k;
		while (num > 2 && !isPrime(num)) {
			num--;
		}
		biggestPrime = num;
		// System.out.println("biggestPrime: " + biggestPrime);
		
		for (int i = 2; i <= biggestPrime; i++) {
			int counterpart = k - i;
			if (isPrime(i) && isPrime(counterpart)) {
				return i;
			}
		}
		
		return -1;
	}
	
	/*
	 *  Primality test of number n can be done by checking if any number between 2 and square root of n divides n.
	 *  sqrt(2000000000) < 44722
	 *  For the speedup of primality test, prepare the first 55 primes and then check with every odd number from 56th prime: 259 to number/44722
	 */
	private static boolean isPrime(int number) {
		int maxtest = number / 44722;
		int primes55[] = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71, 73,79,83,89,97,101,103,107,109,113,127,
						  131,137,139,149,151,157,163,167,173,179,181,191,193,197,199,211,223,227,229,233,239,241,251,257};
		 
		for (int i = 0; i < 55; i++) { 
			if (number % primes55[i] == 0) { 
				if (number == primes55[i]) { 
					return true;
		        } else { 
		            return false;
		        } 
			} 
		} 
		 
		for (int i = 259; i < maxtest; i += 2) 
			if (number % i == 0) {
				return false;
			}
		
		return true;
	}
}
