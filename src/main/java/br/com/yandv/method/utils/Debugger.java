package br.com.yandv.method;

public class Debugger {

    public static void debug(double[] result) {
        debug(result, 'x');
    }

    public static void debug(double[] result, char variable) {
        for (int i = 0; i < result.length; i++) {
            System.out.printf("%c%d = %f\n", variable, i, result[i]);
        }
    }

    public static void debug(double[][] A, double[] b) {
        int n = A.length;

        System.out.print("     ");

        for (int i = 0; i < n; i++) {
            if (i >= 10) {
                System.out.printf("%d         ", i);
            } else {
                System.out.printf("%d          ", i);
            }
        }

        System.out.print("   b\n");

        for (int i = 0; i < n; i++) {
            printfMatrixElement(A, n, i);

            System.out.printf("   %f", b[i]);
            System.out.print("\n");
        }

        System.out.print("\n");
    }

    public static void debug(double[][]... matrices) {
        for (double[][] A : matrices) {
            int n = A.length;

            System.out.print("     ");

            for (int i = 0; i < n; i++) {
                if (i >= 10) {
                    System.out.printf("%d         ", i);
                } else {
                    System.out.printf("%d          ", i);
                }
            }

            System.out.print("   b\n");

            for (int i = 0; i < n; i++) {
                printfMatrixElement(A, n, i);
                System.out.print("\n");
            }

            System.out.print("\n");
        }
    }

    private static void printfMatrixElement(double[][] A, int n, int i) {
        if (i >= 10) {
            System.out.printf("%d   ", i);
        } else {
            System.out.printf("%d    ", i);
        }

        for (int j = 0; j < n; j++) {
            if (A[i][j] < 0) {
                System.out.printf("%.5f   ", A[i][j]);
            } else {
                System.out.printf("%f   ", A[i][j]);
            }
        }
    }
}
