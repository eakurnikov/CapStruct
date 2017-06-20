package com.private_void.utils;

import java.util.Arrays;
import java.util.Random;

public final class GeneratorTests {
    private static int n1 = 16;
    private static int n2 = 8;
    private static int n3 = 5;
    private static int n4 = 4;

    private static int s = 0;
    private static double N = 0;
    private static int Nmax = 600 * (int)Math.pow(2, 10);

    private static int[] v1 = new int[n1];
    private static int[][] v2 = new int[n2][n2];
    private static int[][][] v3 = new int[n3][n3][n3];
    private static int[][][][] v4 = new int[n4][n4][n4][n4];

    private static double[] X2 = new double[10];
    private static double[] D = new double[600];
    private static double Dm = 0;

    private static double[] e = new double[Nmax];
    private static double[] y = new double[600];
    private static double sum;
    private static double F;

    private static double[] p = {9 * 0.1, 9 * 0.01, 9 * 0.001, 9 * 0.0001, 0.0001};
    private static double[] n = new double[5];
    private static double nsum;

    private static Random rand;

    public static void main(String[] args) {
        runTests(2);
    }

    public static void runTests(int seed) {
        rand = new Random(seed);
        makeArray(e);
        test1(e);
        test2(e);
        test3(e);
        test4(e);
        test5(e);
        test6(e);
    }

    private static void test6(double[] e) {
        int k;
        for (s = 8; s <= 10; s++) {
            N = 600 * Math.pow(2, s);

            for (int i = 0; i < 10; i++) {
                X2[i] = 0;
            }

            for (int j = 0; j < N - 2; j++) {
                if (Math.floor(e[j] * 10) == Math.floor(e[j + 1] * 10)) {
                    n[0]++;
                    k = 1;

                    while (Math.floor(e[j + k] * 10) == Math.floor(e[j + k + 1] * 10) && j + k + 1 < N) {
                        if (k < 5) {
                            n[k - 1]--;
                            n[k]++;
                        }
                        k++;
                    }
                    j += k;
                }
            }
            nsum = n[0] + n[1] + n[2] + n[3] + n[4];

            for (int i = 0; i < 4; i++) {
                X2[i] += Math.pow(n[i] - nsum * p[i], 2) / (nsum * p[i]) + Math.pow(n[4] - nsum * p[4], 2) / (nsum * p[4]);
            }
        }
        F = max(X2);
        System.out.println("F6 = " + F);
        try {
            System.in.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void test6_old(double[] e) {
        for (s = 8; s <= 10; s++) {
            N = 600 * Math.pow(2, s);

            for (int i = 0; i < 10; i++) {
                X2[i] = 0;
            }

            for (int j = 0; j < N; j++) {
                if (Math.floor(e[j] * 10) == Math.floor(e[j] * 100)) {
                    n[0]++;

                    if (Math.floor(e[j] * 100) == Math.floor(e[j] * 1000)) {
                        n[0]--;
                        n[1]++;

                        if (Math.floor(e[j] * 1000) == Math.floor(e[j] * 10000)) {
                            n[1]--;
                            n[2]++;

                            if (Math.floor(e[j] * 10000) == Math.floor(e[j] * 100000)) {
                                n[2]--;
                                n[3]++;

                                if (Math.floor(e[j] * 100000) == Math.floor(e[j] * 1000000)) {
                                    n[3]--;
                                    n[4]++;
                                }
                            }
                        }
                    }
                }
            }
            nsum = n[0] + n[1] + n[2] + n[3] + n[4];

            for (int i = 0; i < 4; i++) {
                X2[i] += Math.pow(n[i] - nsum * p[i], 2) / (nsum * p[i]) + Math.pow(n[4] - nsum * p[4], 2) / (nsum * p[4]);
            }
        }
        F = max(X2);
        System.out.println("F6 = " + F);
        try {
            System.in.read();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void test5(double[] e) {
        for (s = 6; s <= 10; s++) {
            N = 150 * Math.pow(2, s);
            sum = 0;

            for (int m = 0; m < n4; m++) {
                for (int l = 0; l < n4; l++) {
                    for (int j = 0; j < n4; j++) {
                        for (int i = 0; i < n4; i++) {
                            v4[i][j][l][m] = 0;
                        }
                    }
                }
            }

            for (int k = 0; k < N; k++) {
                for (int m = 0; m < n4; m++) {
                    for (int l = 0; l < n4; l++) {
                        for (int j = 0; j < n4; j++) {
                            for (int i = 0; i < n4; i++) {
                                if ((e[k] >= m * 0.25) && (e[k] < (m + 1) * 0.25)) {
                                    if ((e[k + 1] >= l * 0.25) && (e[k + 1] < (l + 1) * 0.25)) {
                                        if ((e[k + 2] >= j * 0.25) && (e[k + 2] < (j + 1) * 0.25)) {
                                            if ((e[k + 3] >= i * 0.25) && (e[k + 3] < (i + 1) * 0.25)) {
                                                v4[i][j][l][m]++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int m = 0; m < n4; m++) {
                for (int l = 0; l < n4; l++) {
                    for (int j = 0; j < n4; j++) {
                        for (int i = 0; i < n4; i++) {
                            sum += Math.pow(v4[i][j][l][m] - N / 256, 2);
                        }
                    }
                }
            }

            X2[s - 6] = sum * 256 / N;
        }
        F = max(X2);
        System.out.println("F5 = " + F);
    }

    private static void test4(double[] e) {
        for (s = 4; s <= 10; s++) {
            N = 200 * Math.pow(2, s);
            sum = 0;

            for (int l = 0; l < n3; l++) {
                for (int j = 0; j < n3; j++) {
                    for (int i = 0; i < n3; i++) {
                        v3[i][j][l] = 0;
                    }
                }
            }

            for (int k = 0; k < N; k++) {
                for (int l = 0; l < n3; l++) {
                    for (int j = 0; j < n3; j++) {
                        for (int i = 0; i < n3; i++) {
                            if ((e[k] >= l * 0.2) && (e[k] < (l + 1) * 0.2)) {
                                if ((e[k + 1] >= j * 0.2) && (e[k + 1] < (j + 1) * 0.2)) {
                                    if ((e[k + 2] >= i * 0.2) && (e[k + 2] < (i + 1) * 0.2)) {
                                        v3[i][j][l]++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (int l = 0; l < n3; l++) {
                for (int j = 0; j < n3; j++) {
                    for (int i = 0; i < n3; i++) {
                        sum += Math.pow(v3[i][j][l] - N / 125, 2);
                    }
                }
            }

            X2[s - 4] = sum * 125 / N;
        }
        F = max(X2);
        System.out.println("F4 = " + F);
    }

    private static void test3(double[] e) {
        for (s = 2; s <= 10; s++) {
            N = 300 * Math.pow(2, s);
            sum = 0;

            for (int j = 0; j < n2; j++) {
                for (int i = 0; i < n2; i++) {
                    v2[i][j] = 0;
                }
            }

            for (int k = 0; k < N; k++) {
                for (int j = 0; j < n2; j++) {
                    for (int i = 0; i < n2; i++) {
                        if ((e[k] >= j * 0.125) && (e[k] < (j + 1) * 0.125)) {
                            if ((e[k + 1] >= i * 0.125) && (e[k + 1] < (i + 1) * 0.125)) {
                                v2[i][j]++;
                            }
                        }
                    }
                }
            }

            for (int j = 0; j < n2; j++) {
                for (int i = 0; i < n2; i++) {
                    sum += Math.pow(v2[i][j] - N / 64, 2);
                }
            }

            X2[s - 2] = sum * 64 / N;
        }
        F = max(X2);
        System.out.println("F3 = " + F);
    }

    private static void test2(double[] e) {
        for (s = 1; s <= 10; s++) {
            N = 600 * Math.pow(2, s);
            sum = 0;

            for (int i = 0; i < n1; i++) {
                v1[i] = 0;
            }

            for (int j = 0; j < N; j++) {
                for (int i = 0; i < n1; i++) {
                    if ((e[j] >= i * 0.0625) && (e[j] < (i + 1) * 0.0625)) {
                        v1[i]++;
                    }
                }
            }

            for (int i = 0; i < n1; i++) {
                sum += Math.pow(v1[i] - N / 16, 2);
            }

            X2[s - 1] = sum * 16 / N;
        }
        F = max(X2);
        System.out.println("F2 = " + F);
    }

    private static void test1(double[] e) {
        N = 600;
        for (int i = 0; i < 600; i++) {
            y[i] = e[i];
        }
        Arrays.sort(y);
        for (int i = 0; i < 600; i++) {
            if (Math.abs(y[i] - i / N) > Math.abs(y[i] - (i + 1) / N)) {
                D[i] = Math.abs(y[i] - i / N);
            }
            else {
                D[i] = Math.abs(y[i] - (i + 1) / N);
            }
        }
        Dm = max(D);
        hip(Dm, N);
    }

    private static void hip(double Dm, double N) {
        double kappa = Dm * Math.sqrt(N);
        System.out.println("Kappa = " + kappa);
    }

    private static void makeArray(double[] e) {
        for (int i = 0; i < Nmax; i++) {
            e[i] = rand.nextDouble();
        }
    }

    private static double max(double[] a) {
        double max = a[0];
        for (int i = 0; i < a.length; i++) {
            if (a[i] > max)
                max = a[i];
        }
        return max;
    }
}