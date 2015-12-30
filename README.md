# ArithmeticExpressionParser


  Arithmetic Expressions Evaluator using Recursion
  Coded by: Lenmor Larroza Dimanalata, ID# 27699727
  
  This is the Java implementation of the a calculator for arithmetic expressions
  it reads the input file of math expressions and outputs results
 
  - INPUT FILE : "expressions.txt"   - each line contains syntactically correct arithmetic expressions
  - OUTPUT FILE : "results.txt"   - the expression and corresponding result is printed here

 it supports operations on integers and reals and the standard operator precedence is observed
  
 ### ALGORITHM :  Recursive Descent Parsing
  
 * grammar rules are applied to the expressions, corresponding to the precedence of the operators
 * a separate method for each grammar rule (precedence rule) is applied,
 * namely Levels 1-8 (L1 - L8)
 * DESCENDing from priority L8 to L1
  
 * L8 -> == !=
 * L7 -> > < >= <=
 * L6 -> + and -
 * L5 -> * and /
 * L4 -> power
 * L3 -> the minus sign for negative numbers is handled in getting the "tokens"
 * and becomes a negative number, thus not used here
 * L2 -> factorial
 * L1 -> parenthesis
 *  the expression is divided into chunks  of  <term> <op> <term> starting from the lowest precedence rule
 *  and then further divided by the next precedence rule, until they are all 
 *  broken down into terms where we can perform the operation
 *  parenthesis is counted as a term, that recursively repeats the process for every set of parenthesis met
```java
 *  [term] <op> [term]
 #####  i.e for an expression  2*1+(5-3) == 4^1+3!
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
 *  since this one is solved (8) the waiting recursive levels before will be executed in order,
 *  going up, or popped from the JVM stack by the order these calls are pushed before
 *  until L8 has all the information to perform [ ] == [ ]
 *  which will result to a false
 *  an ArrayList is used to hold all the tokens (numbers and operators), as it is passed to each level (method)
 *  as each level gets its [term] <op> [term], by popping the top of the arrayList
```


### Input

```java
(4/(2+1)*(5+7))
34.5*(23+1.5)/2
9^2<8+5*2!
3!-(2-(5*6/4))>((15+8)/2)
2^5-3!  == 8!*(12.5+100/4)
0.1^5-((4+67/20) + 3.14*3 )
( 45/((3+2) ^ 2) - 5! * (2.5 - (10+7) ))
100*2+(100-1)*3+(100-2)*4
-5+2/-7*4!  <= (1-84)/7
2*1+(5-3) != 4^1+3!
9^2>=8+5*2!
(2+3)*((-4+7)+2^5)
-(5+(9^2)-1)+(10-(2^2))
(((9/2)^3)*((4^7)-54))/4
(92-(7^3))/((6^2)-(51*2))
(6*(62^2))-((6/5)+(-6*4/(7^4)))
((8^3)^2)/((27+3^2)-51)
((7+3)^2)-(6-(94^3))+(3/(4^2))
((35+7)^2)/(20+(13*(5^2)))
(7+(6^(2*3))/((5^3)-(5-12)))
(8^2)+(7/(2^4))-(-9-(4^4))
(((9-4)^2)-((8-2)^5))/((8-4)^(2+6))
```

### Output
```java
(4/(2+1)*(5+7))                                    -> 16.0                       executed in 7     ms   
34.5*(23+1.5)/2                                    -> 422.625                    executed in 4     ms   
9^2<8+5*2!                                         -> false                      executed in 2     ms   
3!-(2-(5*6/4))>((15+8)/2)                          -> false                      executed in 3     ms   
2^5-3!  == 8!*(12.5+100/4)                         -> false                      executed in 2     ms   
0.1^5-((4+67/20) + 3.14*3 )                        -> -16.76999                  executed in 2     ms   
( 45/((3+2) ^ 2) - 5! * (2.5 - (10+7) ))           -> 1741.8                     executed in 6     ms   
100*2+(100-1)*3+(100-2)*4                          -> 889.0                      executed in 2     ms   
-5+2/-7*4!  <= (1-84)/7                            -> true                       executed in 2     ms   
2*1+(5-3) != 4^1+3!                                -> true                       executed in 1     ms   
9^2>=8+5*2!                                        -> true                       executed in 0     ms   
(2+3)*((-4+7)+2^5)                                 -> 175.0                      executed in 2     ms   
-(5+(9^2)-1)+(10-(2^2))                            -> -79.0                      executed in 2     ms   
(((9/2)^3)*((4^7)-54))/4                           -> 372017.8125                executed in 4     ms   
(92-(7^3))/((6^2)-(51*2))                          -> 3.803030303030303          executed in 2     ms   
(6*(62^2))-((6/5)+(-6*4/(7^4)))                    -> 23062.80999583507          executed in 2     ms   
((8^3)^2)/((27+3^2)-51)                            -> -17476.266666666666        executed in 3     ms   
((7+3)^2)-(6-(94^3))+(3/(4^2))                     -> 830678.1875                executed in 3     ms   
((35+7)^2)/(20+(13*(5^2)))                         -> 5.113043478260869          executed in 3     ms   
(7+(6^(2*3))/((5^3)-(5-12)))                       -> 360.45454545454544         executed in 2     ms   
(8^2)+(7/(2^4))-(-9-(4^4))                         -> 329.4375                   executed in 3     ms   
(((9-4)^2)-((8-2)^5))/((8-4)^(2+6))                -> -0.1182708740234375        executed in 2     ms   


22 expressions executed at a total of 59 ms

```
