/** 
 * @Author : Mridul Chopra
 * Concordia Student Id : 40164868
 * Date Created : 27-Feb-2021
 */
package totp.registeration;

import java.math.BigInteger;
import java.util.List;

/**
 *  Contains all the utilities related to modular arithmetic in the BigInteger Format.
 *  Also supports the primitive data types by extending the older version.
 */
public class ModularArithmeticV2{

	/**
	 *  Return the remainder when a is divided by b or 
	 *  return a mod b
	 *  @param a 
	 *  @param b
	 */
	public static BigInteger mod(BigInteger a , BigInteger b) 
	{
		return a.compareTo(BigInteger.ZERO) == 1
						? a.remainder(b) 
						: b.subtract(a.multiply(new BigInteger("-1")).remainder(b));
	}
	
	
	/**
	 *  Return the  a^powA mod b. Uses square and multiply technique
	 *  to calculate the result.
	 *  
	 *  @param a 
	 *  @param b
	 *  @param powA i.e power of a 
	 */
	public static BigInteger mod(BigInteger a, BigInteger b, BigInteger powA) 
	{	
		// in case the power is negative we need to get inverse
		if( powA.compareTo(BigInteger.ZERO) == -1 )
		{
			powA = powA.multiply(new BigInteger("-1"));
			a = inverse(a,b);
		}
		
		
//		converting the power to binary representation
		String pow = powA.toString(2);
		
//		Initialize the final result and current modulus to be used in loop
		BigInteger result = BigInteger.ONE;
		BigInteger currentMod = mod(a,b);
		
//		Iterate the pow string from reverse
		for( int i = pow.length()-1; i>=0; i--) 
		{
			
//			get the boolean value present at that place
			int bool = Integer.parseInt(pow.charAt(i)+"");
			BigInteger bigBool = bool == 0 ? BigInteger.ZERO : BigInteger.ONE;
			
			// multiply with result if boolean value is 1
			if ( bool == 1 )
				result = result.multiply( currentMod.multiply(bigBool));
//				result *= bool*currentMod;
			
			
//		update the current mod by squaring and getting new value
			currentMod = mod( currentMod.multiply(currentMod), b);
//			currentMod = mod(currentMod*currentMod,b);
			
		}
		
		return mod(result,b);
		
	}
 
	/**
	 *  Calculates the moudulus inverse of a wrt n i.e.
	 *  
	 *  a^-1 mod n
	 *  
	 *  
	 *  @param a 
	 *  @param n
	 */
	public static BigInteger inverse(BigInteger a, BigInteger n) 
	{
		a = mod(a,n); // in case a > n or a < 0
		
		// inverse only exists if gcd is 1 
		if( !gcd(a,n).equals(BigInteger.ONE) )
			throw new IllegalArgumentException("Inverse does not exist for a = "+a+ " and n = "+n);
		
		// initialize variables which we will update in loops
		BigInteger x = n, y = a, p = BigInteger.ZERO, q =BigInteger.ONE;
		BigInteger rem = x.remainder(y), que = x.divide(y);
		
		while(rem.compareTo(BigInteger.ZERO) == 1) 
		{
			 x = y;
			 y = rem;
			 
			 BigInteger prevQ = q;
			 q = p.subtract(que.multiply(q));
			 p = prevQ;
			 
			 rem = x.remainder(y);
			 que = x.divide(y);
		}
		
		return mod(q,n); // return the inverse mod is done in case we get negative number
	}
	
	
	/**
	 *  Calcluate the Greatest Common divisor of a and b.
	 *  calculates gcd according to euclidian algorithm
	 */
	public static BigInteger gcd(BigInteger a, BigInteger b) {
		
		if( a.equals(BigInteger.ZERO))
			return b;
		
		if( b.equals(BigInteger.ZERO))
			return a;
		
		return a.compareTo(b) ==1  ? gcd(b, a.remainder(b) ) : gcd(a , b.remainder(a) );
		
	}
	
	/**
	 *  Solves the given given list of modular equations 
	 *  using Chinese Remainder Theorm (CRT)
	 *  
	 *  @param eqns equations to be computed
	 *  @param mod the modulus for the resultant equation
	 *  @return result using crt
	 */
	public static BigInteger crt( List< List<BigInteger> > eqns, BigInteger mod) 
	{
		BigInteger result = BigInteger.ZERO;
		
		for( List<BigInteger> eqn : eqns)
		{
			
			BigInteger tempMod = eqn.get(1); // mod for the current part
			BigInteger p1 = eqn.get(0); // part 1 contains the multiplications
			BigInteger p2 = BigInteger.ONE; // part 2 contains multiplication of inverses
			
			// calculating p1 and p2
			for( List<BigInteger> eq : eqns)
			{
				if(eq == eqn)
					continue;
				p1 = p1.multiply(eq.get(1));
				p2 = p2.multiply( inverse(eq.get(1), tempMod) );
			}
			
			// adding to the final result
			result = result.add( mod(p1.multiply(p2), mod) );
		}
		
		return mod(result, mod); // mod with mod of resultant equation
	}
}
