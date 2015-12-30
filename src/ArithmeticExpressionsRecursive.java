/*
 * Assignment #2, COMP 352
 * Coded by: Lenmor Larroza Dimanalata, ID# 27699727
 * 
 * Arithmetic Expressions Evaluator using Recursion
 * 
 * 
 * This is the Java implementation of the a calculator for arithmetic expressions
 * it reads the input file of math expressions and outputs results
 *
 * INPUT FILE : "expressions.txt"   - each line contains syntactically correct arithmetic expressions
 * OUTPUT FILE : "results.txt"   - the expression and corresponding result is printed here
 * 
 * it supports operations on integers and reals and the standard operator precedence is observed
 *  
 *  
 * ALGORITHM :  Recursive Descent Parsing
 * 
 * grammar rules are applied to the expressions, corresponding to the precedence of the operators
 * a separate method for each grammar rule (precedence rule) is applied,
 * namely Levels 1-8 (L1 - L8)
 * 
 * DESCENDing from priority L8 to L1
 * 
 * L8 -> == !=
 * L7 -> > < >= <=
 * L6 -> + and -
 * L5 -> * and /
 * L4 -> power
 * L3 -> the minus sign for negative numbers is handled in getting the "tokens"
 * and becomes a negative number, thus not used here
 * L2 -> factorial
 * L1 -> parenthesis
 *
 *  the expression is divided into chunks  of  <term> <op> <term> starting from the lowest precedence rule
 *  and then further divided by the next precedence rule, until they are all 
 *  broken down into terms where we can perform the operation
 *  
 *  parenthesis is counted as a term, that recursively repeats the process for every set of parenthesis met
 *  
 *  [term] <op> [term]
 *  
 *  
 *  i.e for an expression  2*1+(5-3) == 4^1+3!
 *  
 *  L8 divides them into [ 2*1+(5-3)]  <==> [ 4^1-3! ] , waits for lower levels of recursion to finish and execute   [ ] == [ ]
 *  L7 (no matching op)
 *  L6 divides first term into   [2*1] and  [(5-3)]   ; second term to [4^1] [3!]     , waits for lower levels of recursion to finish and execute [ ] + [ ]   and [ ] - [ ] 
 *  L5 divides  [2*1] into [2] <*> [1]  , waits for lower levels of recursion to finish and execute [] * []  
 *  L4 divides 4^1 into [4] <^> [1], waits for lower levels of recursion to finish and execute [] ^ []
 *  L2 divides 3! into [3] <!> [1], waits for lower levels of recursion to finish and execute [] ! []				// strategy here is make the factorial a binary operation by making the right term [1]
 *  
 *  L1 detects parenthesis at (5-3), removes it's parenthesis and passes this one back to the top 
 *  L8 (no matching op)
 *  L7 (no matching op)
 *  L6 divides 5-3 into [5] - [3]
 *  L6 passes these into lower levels, but now there's no other term to be processed
 *  so it performs the operation by calling doOp, which returns the results
 *  
 *  since this one is solved (8) the waiting recursive levels before will be executed in order,
 *  going up, or popped from the JVM stack by the order these calls are pushed before
 *  
 *  until L8 has all the information to perform [ ] == [ ]
 *  which will result to a false
 *  
 *  
 *  an ArrayList is used to hold all the tokens (numbers and operators), as it is passed to each level (method)
 *  as each level gets its [term] <op> [term], by popping the top of the arrayList
 *  
 */


//import all necessary IO and data libraries
import java.io.FileInputStream;			
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;


public class ArithmeticExpressionsRecursive {

	public static boolean fact;				// check if operations is a factorial

	public static void main(String[] args) throws IOException {

		Scanner inFile = null;					
		PrintWriter outFile = null;  
		int lineNum=0, totalElapsed = 0;

		double startTime = 0;				// 3 long variables used for measuring run time of each call
		double stopTime = 0;
		double elapsedTime = 0;

		inFile = new Scanner(new FileInputStream("expressions.txt"));		// read file
		outFile = new PrintWriter(new FileWriter("output.txt"));			// write to file

		//get each line in the file
		while (inFile.hasNextLine()) {
			
			lineNum++;

			startTime = System.currentTimeMillis();	//TIMING###### get starting time before processing

			String line = inFile.nextLine();	
			String[] result = toArr(line);			// call method to produce token array from the expression

			//System.out.println(Arrays.toString(result));

			ArrayList<String> allC = new ArrayList<String> ();			// arraylist that contains all tokens

			for (String x: result)										// put everything in the arraylist
				allC.add(x);

			String answer = L8(allC);					// CALL to start recursive parsing from lowest level L8, final result after is stored in answer

			stopTime = System.currentTimeMillis();	//TIMING######## get finishing time after processing
			elapsedTime = stopTime - startTime;			//TIMING ######## get time elapsed
			totalElapsed += elapsedTime;

			outFile.printf("%-50s -> %-25s  executed in %-5.0f ms   ", line, answer, elapsedTime);
			outFile.println();

		}   // end of current line
		
		// end of file

		outFile.println();
		outFile.println();
		outFile.println(lineNum + " expressions executed at a total of " + totalElapsed + " ms");
		
		inFile.close();
		outFile.close();
	

}

/**
 * Level 8, lowest precedence, == and !=
 * 
 * @param allC arraylist of tokens
 * @return answer
 */
public static String L8(ArrayList<String> allC) {

	String val = L7(allC);				// get left term
	//System.out.println("val " + val + "array size " + allC.size());
	if (allC.size() > 0) 			
	{
		//while (allC.size() > 0) 					// Base case-> array is empty, Recursive Call -> get [term] <op> [term] and descend
		while (allC.get(0).equals("==")  || allC.get(0).equals("!=") ) {
			String op = allC.remove(0);			//get operator
			String nextVal = L7(allC);			// get right term

			val = doOp(val, nextVal, op);
		}
	}
	return val;				// base case, return value if array is empty		
}


/**
 * Level 7, 2nd lowest precedence, >  >= < <=
 * 
 * @param allC arraylist of tokens
 * @return answer
 */
public static String L7(ArrayList<String> allC) {

	String val = L6(allC);				// get left term
	//System.out.println("val " + val + "array size " + allC.size());
	if (allC.size() > 0) 			// Base case-> array is empty, Recursive Call -> get [term] <op> [term] and descend
	{
		//while (allC.size() > 0) 
		while ( allC.get(0).equals(">")  || allC.get(0).equals(">=")
				|| allC.get(0).equals("<")  || allC.get(0).equals("<=")) {
			String op = allC.remove(0);			//get operator
			String nextVal = L6(allC);			// get right term

			val = doOp(val, nextVal, op);
		}
	}
	return val;				// base case, return value if array is empty		
}

/**
 * Level 6, 3rd lowest precedence, + and -
 * 
 * @param allC arraylist of tokens
 * @return answer
 */
public static String L6(ArrayList<String> allC) {

	String val = L5(allC);				// get left term
	//System.out.println("val " + val + "array size " + allC.size());
	if (allC.size() > 0) 			// Base case-> array is empty, Recursive Call -> get [term] <op> [term] and descend
	{
		//while (allC.size() > 0)
		while ( allC.get(0).equals("+")  || allC.get(0).equals("-") ) {
			String op = allC.remove(0);			//get operator
			String nextVal = L5(allC);			// get right term

			val = doOp(val, nextVal, op);
		}
	}
	return val;				// base case, return value if array is empty		
}

/**
 * Level 5, 4th lowest precedence, * and /
 * 
 * @param allC arraylist of tokens
 * @return answer
 */
public static String L5(ArrayList<String> allC) {

	String val = L4(allC);				// get left term
	//System.out.println("val " + val + "array size " + allC.size());
	if (allC.size() > 0) 			// Base case-> array is empty, Recursive Call -> get [term] <op> [term] and descend
	{
		//while (allC.size() > 0)
		while ( allC.get(0).equals("*")  || allC.get(0).equals("/") ) {
			String op = allC.remove(0);			//get operator
			String nextVal = L4(allC);			// get right term

			val = doOp(val, nextVal, op);
		}
	}
	return val;				// base case, return value if array is empty		
}

/**
 * Level 4, 5th lowest precedence, ^
 * 
 * @param allC arraylist of tokens
 * @return answer
 */
public static String L4(ArrayList<String> allC) {

	String val = L2(allC);				// get left term
	//System.out.println("val " + val + "array size " + allC.size());
	if (allC.size() > 0) 			// Base case-> array is empty, Recursive Call -> get [term] <op> [term] and descend
	{
		//while  (allC.size() > 0)
		while ( allC.get(0).equals("^") ) {
			String op = allC.remove(0);			//get operator
			String nextVal = L2(allC);			// get right term

			val = doOp(val, nextVal, op);
		}
	}
	return val;				// base case, return value if array is empty		
}


/**
 * Level 2, 6th lowest precedence, !
 * 
 * @param allC arraylist of tokens
 * @return answer
 */
public static String L2(ArrayList<String> allC) {

	String val = getAtoms(allC);				// get left term
	//System.out.println("val " + val + "array size " + allC.size());
	if (allC.size() > 0) 			// Base case-> array is empty, Recursive Call -> get [term] <op> [term] and descend
	{
		//while (allC.size() > 0) 
		while ( allC.get(0).equals("!") ) {
			String op = allC.remove(0);			//get operator
			String nextVal = getAtoms(allC);			// get right term

			val = doOp(val, nextVal, op);
		}
	}
	return val;				// base case, return value if array is empty		
}

/**
 * Level 1, highest precedence, here we get the Final Terms (ATOMS), if we get ( ), we 
 * send the expression inside the parenthesis back to the top 
 * or just NUMBERS
 * 
 * @param allC arraylist of tokens
 * @return answer
 */
public static String getAtoms(ArrayList<String> allC) {

	//if number or boolean, return this
	if (allC.get(0).matches("[-]*[0-9]+[.]*[0-9]*") || allC.get(0) == "true" || allC.get(0) == "false") 				
		return allC.remove(0);		//######## return the value, this will cause the pending operations in the previous levels to start doing operations #####

	else if (allC.get(0).equals("(")  )
	{
		//atom is an expression in parenthesis
		allC.remove(0);				//remove the (

		String val = L8(allC);			// ############## RECURSIVE CALL because we have a parenthesis, send the expression to the top
		//System.out.println(" inside factor " + val);
		allC.remove(0);  			//remove the )

		return val;					
	}
	else
	{	// if expression is invalid
		System.out.println("EXPRESSION ERROR");
		System.exit(0);
		return "";
	}
}

/**
 * 
 * performs operation, [term1] <operation> [term2]
 * 
 * 
 * @param term1
 * @param term2
 * @param ope
 * @return result of operation in String
 */
public static String doOp(String term1, String term2, String op) {

	String result="";

	double x = Double.parseDouble(term2);		// get term1
	double y = Double.parseDouble(term1);		// get term2

	//perform operation depending on operation symbol passed
	switch (op) {

	case "!":
		result = factorial(  (int) Math.floor(y)  ) + "";
		break;

	case "^":
		result = power(y, x) + "";
		break;

	case "*":
		result = (y * x) + "";
		break;

	case "/":
		result = (y / x) + "";
		break;

	case "+":
		result = (y + x) + "";
		break;

	case "-":
		result = (y - x) + "";
		break;

	case ">":
		result = (y > x) + "";
		break;

	case "<":
		result = (y < x) + "";
		break;

	case "<=":
		result = (y <= x) + "";
		break;

	case ">=":
		result = (y >= x) + "";
		break;

	case "!=":
		result = (y != x) + "";
		break;

	case "==":
		result = (y == x) + "";
		break;
	}

	//System.out.println("operation" + y + "" + op + "" + x);
	return result;
}

/**
 * solves for the factorial of passed integer
 * 
 * @param x integer
 * @return factorial of passed x
 */
public static int factorial(int x) {
	int result=1;

	while (x != 0 )
	{
		result *= x;
		x--;
	}
	return  result;
}

/**
 * 
 * solves for base raised to exp
 * 
 * @param base
 * @param exp
 * @return result in double
 */
public static double power(double base, double exp){

	double result = 1;

	while (exp != 0)
	{
		result =  result * base;
		exp--;
	}
	return result;
}

/**
 * obtains the tokens from the expression
 * 
 * 
 * @param line expression String
 * @return String array of tokens
 */
public static String[] toArr (String line) {

	line = line.trim();

	//add a # at the start and end to avoid index out of bounds
	line = "#" + line + "#";

	ArrayList<String> all = new ArrayList<String> ();

	char[] cArr = line.toCharArray();
	int i = 1;
	while (i <= cArr.length-2)
	{
		if (Character.isDigit(cArr[i]))				// if number
		{
			String temp= cArr[i] + "";
			int j=1;

			while (Character.isDigit(cArr[i+j] ) ) {
				temp += cArr[i+j];
				j++;
			}
			all.add(temp);
			i = i + j;
		}						// if negative number
		else if ((cArr[i] == '-') && (!((cArr[i-1] + "").matches("[0-9]"))) && (cArr[i-1] != ')')			// if negative number
				&& (Character.isDigit(cArr[i+1])) ) 
		{
			String temp= "-" ;
			int j=1;

			while (Character.isDigit(cArr[i+j] ) ) {
				temp += cArr[i+j];
				j++;
			}
			all.add(temp);
			i = i + j;
		}						// logical operator
		else if ((cArr[i] + "").matches("[><=!]") && cArr[i+1] == '=')			// if logical operator
		{
			all.add(cArr[i] + "=");
			i++;	// skip next
		}
		else if (cArr[i] == '=')		// skip equal sign
			i++;
		else if (cArr[i] == ' ')		// skip space
			i++;
		else
		{
			all.add(cArr[i] + "");		// anything else, add
			i++;
		}
	}

	//if minus sign before parenthesis " -( " at starting
	if (all.get(0).equals("-") && all.get(1).equals("("))
		all.add(0,"0");				// make previous element 0

	//force ! to be a binary operator
	for (int m=0; m< all.size(); m++)
	{
		if (all.get(m).equals("!"))			// if ! found
			all.add(m+1, "1");				// make next element 1
	}

	//add a # at the start and end to avoid index out of bounds
	all.add(0, "#");
	all.add(all.size(),"#");

	//join the decimals
	int k = 1;
	String temp2 = "";

	ArrayList<String> allNew = new ArrayList<String> ();

	while (k <= all.size() -2)									
	{
		if (all.get(k+1).equals("."))
		{
			if (all.get(k).matches("[-]*[0-9]+")) 		//if left side a number
			{
				//System.out.println("match on " + all.get(k-1));
				temp2 = all.get(k) + ".";
			}

			//get right side, of course this would be a number
			temp2 = temp2 + all.get(k+2);

			allNew.add(temp2);
			temp2 = "";
			k+=2;
		}
		else
			allNew.add(all.get(k));	

		k++;
	}


	allNew.add(0, "(");
	allNew.add(allNew.size(), ")");

	int ctr=0;					// put all arraylist contents to array
	String[] tempArr = new String[allNew.size()];
	for (String s : allNew)
	{
		tempArr[ctr] = s;
		ctr++;
	}



	return tempArr;
}



} // end class
