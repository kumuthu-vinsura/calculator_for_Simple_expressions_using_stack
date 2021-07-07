//B A K Vinsura
//D/BCE/20/0016

package com.company;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your Expression : ");
        String inputExp = input.nextLine();
        Stack expStack = new Stack();
        Stack numStack = new Stack();

        boolean key = false;

        for (int index = 0; index < inputExp.length(); index++) {

            // select a character from inputExp string
            char fragment = inputExp.charAt(index);

            // Iterate through inputExp until an operator or number is found
            while (!isOperator(fragment) && !isNumber(fragment)) {
                ++index;
                if (index < inputExp.length()) fragment = inputExp.charAt(index);
                if (index == inputExp.length()) break;
            }

            // if end of the inputExp is reached, break loop
            if (index == inputExp.length()) break;

            // new StringBuilder for constructing the inputExp in suffix form
            StringBuilder suffixExpression = new StringBuilder("");

            if (fragment == '-' && key) {
                ++index;
                if (index < inputExp.length()) fragment = inputExp.charAt(index);
                suffixExpression.append('-');
            }

            while (isNumber(fragment) || fragment == '.') {
                key = false;
                suffixExpression.append(fragment);
                if (++index >= inputExp.length()) break;
                fragment = inputExp.charAt(index);
            }

            // BigDecimal class is used to handle large numbers
            if (!suffixExpression.toString().equals("")) {
                expStack.push(new BigDecimal(suffixExpression.toString()));
            }

            // Check if fragment is an operator
            if (fragment == '*' || fragment == '+' || fragment == '-') {
                if ((!numStack.empty())) {
                    if (((char) numStack.peek() == '*')) expStack.push(numStack.pop());
                }
            }

            if (fragment == '+' || fragment == '-') {
                if ((!numStack.empty()))
                    if (((char) numStack.peek() == '*') || ((char) numStack.peek() == '+') || ((char) numStack.peek() == '-')) {
                        expStack.push(numStack.pop());
                    }
            }

            if (isOperator(fragment)) {
                numStack.push(fragment);
                key = true;
            }

            // find bracket through the stack
            if (fragment == ')') {
                char cha = fragment;
                while (cha != '(') {
                    cha = (char) numStack.pop();
                    if (cha != '(' && cha != ')')
                        expStack.push(cha);
                }
            }
        }

        while (!numStack.empty()) {
            expStack.push(numStack.pop());
        }

        Stack opStack = new Stack<>();

        // New stack for operators
        while (!expStack.empty()) {
            opStack.push(expStack.pop());
        }

        // while suffix stack is not empty, perform arithmetic
        while (!opStack.empty()) {
            Object item = opStack.pop();
            if (item.getClass() == BigDecimal.class) {
                numStack.push((BigDecimal) item);
                continue;
            } else {

                // B,O,D,M,A,S
                // Since we only have (), *, - | below operations are placed in that order

                if ((char) item == '*' && (numStack.size() != 1)) numStack.push(((BigDecimal) numStack.pop()).multiply((BigDecimal) numStack.pop()));

                if ((char) item == '+' && (numStack.size() != 1)) numStack.push(((BigDecimal) numStack.pop()).add((BigDecimal) numStack.pop()));

                if ((char) item == '-') {
                    BigDecimal a = (BigDecimal) numStack.pop();
                    if (!numStack.empty() && numStack.peek().getClass() != char.class) numStack.push(((BigDecimal) numStack.pop()).subtract(a));
                    else numStack.push((a).multiply(new BigDecimal(-1)));
                }

            }
        }
        System.out.print(numStack.pop());
    }

    public static boolean isNumber(char fragment) {
        return fragment >= '0' && fragment <= '9';
    }

    public static boolean isOperator(char fragment) {
        return fragment == '+' || fragment == '-' || fragment == '*' || fragment == '(';
    }

}
