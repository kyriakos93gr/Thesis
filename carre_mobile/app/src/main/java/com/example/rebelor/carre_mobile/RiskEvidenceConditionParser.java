package com.example.rebelor.carre_mobile;

import android.util.Log;

import static android.content.ContentValues.TAG;

public class RiskEvidenceConditionParser {
    //http://codereview.stackexchange.com/questions/38348/boolean-expression-parser
    private static final String[] operators = {"!=", "=", ">=", "<=", ">", "<", "OR", "AND"};

    private static boolean parseAndEvaluateExpression(String ex) {
        for (char c : ex.toCharArray()) {
            if (!Character.isSpaceChar(c))
                return parseWithStrings(ex);
        }
        System.err.println("ERROR: Expression cannot be empty!");
        return false;
    }

    @SafeVarargs
    static <T> boolean evaluate(String or, T... rep) {
        String[] temp = new String[rep.length];
        for (int i = 0; i < rep.length; i++)
            temp[i] = "" + rep[i];
        return evaluate(or, temp);
    }

    static boolean evaluate(String or, String... vars) {
        if ((vars.length % 2 == 1 || vars.length < 2) && vars.length != 0) {
            System.err.println("ERROR: Invalid arguments!");
            return false;
        }

        for (int i = 0; i < vars.length; i += 2) {
//            or = or.replace("" + vars[i] + "", "" + vars[i+1]);
            or = or.replaceAll( "\\b"+ vars[i] + "\\b" , "" +vars[i+1]);
        }
        return parseAndEvaluateExpression(or);
    }

    private static boolean parseWithStrings(String s) {
        int[] op = determineOperatorPrecedenceAndLocation(s);
        int start = op[0];
        String left = s.substring(0, start).trim();
        String right = s.substring(op[1]).trim();
        String oper = s.substring(start, op[1]).trim();
        int logType = logicalOperatorType(oper);
//
//        System.out.println("PARSE: Left: \"" + left + "\" Right: \"" + right + "\" Operator: \"" + oper + "\"");
//
        if (logType == 0) // encounters OR- recurse
            return parseWithStrings(left) || parseWithStrings(right);
        else if (logType == 1) // encounters AND- recurse
            return parseWithStrings(left) && parseWithStrings(right);
        String leftSansParen = removeParens(left);
        String rightSansParen = removeParens(right);
        if (isInt(leftSansParen) && isInt(rightSansParen))
            return evaluate(Double.parseDouble(leftSansParen), oper, Double.parseDouble(rightSansParen));
        else
            return evaluate(leftSansParen, oper, rightSansParen); // assume they are strings
    }

    private static int[] determineOperatorPrecedenceAndLocation(String s) {
        s = s.trim();
        int minParens = Integer.MAX_VALUE;
        int[] currentMin = null;
        for (int sampSize = 1; sampSize <= 3; sampSize++) {
            for (int locInStr = 0; locInStr < (s.length() + 1) - sampSize; locInStr++) {
                int endIndex = locInStr + sampSize;
                String sub;
                if ((endIndex < s.length()) && s.charAt(endIndex) == '=')
                    sub = s.substring(locInStr, ++endIndex).trim();
                else
                    sub = s.substring(locInStr, endIndex).trim();
                if (isOperator(sub)) {
                    // Idea here is to weight logical operators so that they will still be selected over other operators
                    // when no parens are present
                    int parens = (logicalOperatorType(sub) > -1) ? parens(s, locInStr) - 1 : parens(s, locInStr);
                    if (parens <= minParens) {
                        minParens = parens;
                        currentMin = new int[]{locInStr, endIndex, parens};
                    }
                }
            }
        }
        return currentMin;
    }

    private static int logicalOperatorType(String op) {
        if (op.trim().equals("OR")) {
            return 0;
        } else if (op.trim().equals("AND")) {
            return 1;
        } else {
            return -1;
        }
    }

    private static int parens(String s, int loc) {
        int parens = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' && i < loc)
                parens++;
            if (s.charAt(i) == ')' && i >= loc)
                parens++;
        }
        return parens;
    }

//    private static String removeParens(String s)
//    {
//            s = s.trim();
//            String keep = "";
//            for (char c : s.toCharArray())
//            {
//                    if (!(c == '(') && !(c == ')'))
//                            keep += c;
//            }
//            return keep.trim();
//    }

    private static String removeParens(String s) {
        s = s.trim();
        StringBuilder keep = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (!(c == '(') && !(c == ')'))
                keep.append(c);
        }
        return keep.toString().trim();
    }

    private static boolean isOperator(String op) {
        op = op.trim();
        for (String s : operators) {
            if (s.equals(op))
                return true;
        }
        return false;
    }

    private static boolean isInt(String s) {
        for (char c : s.toCharArray())
            if (!Character.isDigit(c) && c != '.')
                return false;
        return true;
    }

    private static boolean evaluate(double left, String op, double right) {
        if (op.equals("=")) {
            return left == right;
        } else if (op.equals(">")) {
            return left > right;
        } else if (op.equals("<")) {
            return left < right;
        } else if (op.equals("<=")) {
            return left <= right;
        } else if (op.equals(">=")) {
            return left >= right;
        } else if (op.equals("!=")) {
            return left != right;
        } else {
            System.err.println("ERROR 1: Operator type not recognized.");
            return false;
        }
    }

    private static boolean evaluate(String left, String op, String right) {
        if (op.equals("=")) {
            return left.replace("'", "").equals(right.replace("'", ""));
        } else if (op.equals("!=")) {
            return !left.replace("'", "").equals(right.replace("'", ""));
        } else {
//            System.err.println("ERROR 2: Operator type not recognized.");
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        //String s = "( OB_16 < 30 AND OB_16 >= 25 ) AND OB_63 = 'male'";
        //String[] val = new String[]{"OB_16", "26", "OB_63", "male"};
        String s = "( OB_18 = 'stage1' OR OB_18 = 'stage2' OR OB_18 = 'stage3' OR ( OB_30 <= 90 AND OB_30 >= 30 ) ) AND OB_65 = 'yes'";
        String[] val = new String[]{"OB_18", "stage5", "OB_30", "30", "OB_65", "yes"};

        //String[] val = new String[]{};

//        System.out.println(RiskEvidenceConditionParser.evaluate(s, val));
    }


}
