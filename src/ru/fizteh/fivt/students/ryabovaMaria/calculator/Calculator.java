package ru.fizteh.fivt.students.ryabovaMaria.calculator;

import java.math.BigInteger;

public class Calculator {
    static String calcExpr = new String();

    static BigInteger numValue;

    static int curPosition;

    static lexem curLex;

    enum lexem {
        NUM,
        PLUS,
        MINUS,
        MUL,
        DIV,
        OPEN,
        CLOSE,
        END
    }

    public static void concatIntoExpr(String[] args) {
        int numOfArgs = args.length;
        StringBuilder tempString = new StringBuilder();
        for (int i = 0; i < numOfArgs; ++i) {
            tempString.append(args[i]);
            tempString.append(" ");
        }
        calcExpr = tempString.toString();
    }

    public static void getLexem() throws Exception {
        if (curPosition >= calcExpr.length()) {
            curLex = lexem.END;
            return;
        }
        char c = calcExpr.charAt(curPosition);
        while (curPosition < calcExpr.length()
                && Character.isWhitespace(c)) {
            ++curPosition;
            if (curPosition >= calcExpr.length()) {
                break;
            }
            c = calcExpr.charAt(curPosition);
        }
        if (curPosition >= calcExpr.length()) {
            curLex = lexem.END;
            return;
        }
        if (Character.isDigit(c) || Character.isLetter(c)) {
            curLex = lexem.NUM;
        } else {
            switch(c) {
                case '+':
                    curLex = lexem.PLUS;
                    break;
                case '-':
                    curLex = lexem.MINUS;
                    break;
                case '*':
                    curLex = lexem.MUL;
                    break;
                case '/':
                    curLex = lexem.DIV;
                    break;
                case '(':
                    curLex = lexem.OPEN;
                    break;
                case ')':
                    curLex = lexem.CLOSE;
                    break;
                default:
                    throw new Exception("Bad symbol");
            }
            ++curPosition;
            return;
        }
        StringBuilder tempString = new StringBuilder();
        while (curPosition < calcExpr.length()) {
            c = calcExpr.charAt(curPosition);
            if (Character.isLetterOrDigit(c)) {
                tempString.append(c);
            } else {
                break;
            }
            ++curPosition;
        }
        numValue = new BigInteger(tempString.toString(), 19);
    }

    public static BigInteger parseMultiplier() throws Exception {
        switch (curLex){
            case NUM:
                BigInteger answer = numValue;
                getLexem();
                return answer;
            case OPEN:
                getLexem();
                answer = parseExpr();
                if (curLex == lexem.CLOSE) {
                    getLexem();
                } else {
                    throw new Exception("Bad bracket balance");
                }
                return answer;
            case CLOSE:
                throw new Exception("Early close bracket");
            case END:
                throw new Exception("Early end");
            case MUL:
            case DIV:
            case PLUS:
            case MINUS:
                throw new Exception("Extra sign");
        }
        return BigInteger.ZERO;
    }

    public static BigInteger parseSummand() throws Exception {
        BigInteger ans = parseMultiplier();
        while (curLex == lexem.MUL || curLex == lexem.DIV) {
            lexem curSign = curLex;
            getLexem();
            if (curSign == lexem.MUL) {
                ans = ans.multiply(parseMultiplier());
            }
            if (curSign == lexem.DIV) {
                BigInteger nextValue = parseMultiplier();
                if (nextValue == BigInteger.ZERO) {
                    throw new Exception("Division by zero");
                }
                ans = ans.divide(nextValue);
            }
        }
        return ans;
    }

    public static BigInteger parseExpr() throws Exception {
        BigInteger ans = parseSummand();
        while (curLex == lexem.PLUS || curLex == lexem.MINUS) {
            lexem curSign = curLex;
            getLexem();
            if (curSign == lexem.PLUS) {
                ans = ans.add(parseSummand());
            }
            if (curSign == lexem.MINUS) {
                ans = ans.subtract(parseSummand());
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        concatIntoExpr(args);
        curPosition = 0;
        try {
            getLexem();
        } catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        try {
            BigInteger answer = parseExpr();
            if (curLex != lexem.END) {
                if (curLex == lexem.CLOSE) {
                    System.err.println("Bad balance");
                }
                else {
                    System.err.println("Bad lexem");
                }
                System.exit(1);
            } else {
                System.out.println(answer.toString(19));
            }
        } catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
