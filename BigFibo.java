/**
 * Copyright (c) 2018 Jayant Sinha. All rights reserved.
 *
 * Use of this source code is governed by the MIT License, as found in the LICENSE.txt file.
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigFibo {

    // Base matrix is basically matrix which is raised to the nth power:
    // [1 1]
    // [1 0]
    private static BigInteger baseMatrix[][] = new BigInteger[][]{
            {BigInteger.ONE, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ZERO}
    };

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java BigFibo N");
            System.exit(1);
            return;
        }
        int n = Integer.parseInt(args[0]);
        if(n<0) {
            System.err.println("N should be greater than 0");
        } else if (n == 0) {
            System.out.println(0);
        }
        long start = System.currentTimeMillis();
        BigInteger nthFibo = getNthFibonacci(n);
        long end = System.currentTimeMillis() - start;
        System.out.println(nthFibo);
        System.out.println("Time taken to generate = "+end+" ms");
    }

    /**
     *
     * Calculates Nth Fibonacci number using Matrix Exponentiation method.
     * [F(n)  ]   [1  1]^n [F(1)]
     * [F(n-1)] = [1  0]   [F(0)]
     *
     * Time complexity is O(K^3 log N)
     */
    private static BigInteger getNthFibonacci(int n) {
        List<BigInteger[][]> matrices = new ArrayList<>();
        matrices.add(baseMatrix);
        boolean[] binN = convertToBinary(n);
        int binArrayLen = binN.length;
        // Storing biN.length matrices raising to 2^x where x is the power of 2
        // The storage is in sequence to binN
        for (int i = 1; i < binArrayLen; i++) {
            matrices.add(
                    matrixMultiply(matrices.get(i - 1), matrices.get(i - 1))
            );
        }

        // Multiplying matrices at positions in equivalence to the binary representation
        // i.e.; if 13 is represented as 1011 in big-endian, then matrices at position
        // 0, 2 and 3 are multiplied to get the resultant matrix.
        BigInteger resultMatrix[][] = new BigInteger[2][2];
        for (int i = 0; i < binArrayLen; i++) {
            if (binN[i]) {
                if (resultMatrix[0][0] == null) {
                    resultMatrix = matrices.get(i);
                } else {
                    resultMatrix = matrixMultiply(resultMatrix, matrices.get(i));
                }
            }
        }

        // Since, [F(n)  ]   [1  1]^n [F(1)] and [F(1)]   [1]
        //        [F(n-1)] = [1  0]   [F(0)]     [F(0)] = [0]
        // the result is always the element at index (1,0)
        return resultMatrix[1][0];
    }

    // Multiplies two 2nd order matrices
    private static BigInteger[][] matrixMultiply(BigInteger a[][], BigInteger b[][]) {
        BigInteger mul[][] = new BigInteger[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                mul[i][j] = BigInteger.ZERO;
                for (int k = 0; k < 2; k++) {
                    mul[i][j] = mul[i][j].add(a[i][k].multiply(b[k][j]));
                }
            }
        }
        return mul;
    }

    // Converts integer to Big-endian binary format
    private static boolean[] convertToBinary(int n) {
        String binaryStr[] = Integer.toBinaryString(n).split("");
        boolean[] binary = new boolean[binaryStr.length];
        int j = 0;
        for (int i = binaryStr.length - 1; i >= 0; i--) {
            if (binaryStr[i].equals("1")) {
                binary[j++] = true;
            } else {
                binary[j++] = false;
            }
        }
        return binary;
    }
}

