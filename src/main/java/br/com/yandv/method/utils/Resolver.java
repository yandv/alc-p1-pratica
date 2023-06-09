package br.com.yandv.method;

public class Resolver {

    // mxn * nxp = mxp

    public static double[][] multiply(double[][] A, double[][] B) {
        if (A[0].length != B.length) {
            throw new IllegalArgumentException(
                    "A matriz A deve ter o mesmo número de linhas que a matriz B tem de colunas");
        }

        int n = A.length, p = B[0].length;
        double[][] result = new double[A.length][B[0].length];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return result;
    }

    public static double[] multiply(double[][] A, double[] B) {
        if (A[0].length != B.length) {
            throw new IllegalArgumentException(
                    "A matriz A deve ter o mesmo número de linhas que a matriz B tem de colunas");
        }

        int n = A.length;
        double[] result = new double[A.length];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                result[i] += A[i][k] * B[k];
            }
        }

        return result;
    }

    /**
     * Backward Substitution para resolver sistemas triangulares superiores
     *
     * @param A Matriz triangular superior
     * @param b Vetor de resultados
     */

    public static double[] solveUpper(double[][] A, double[] b) {
        int n = A.length;
        double[] result = new double[b.length];

        result[n - 1] = b[n - 1] / A[n - 1][n - 1];

        for (int k = n - 1; k >= 0; k--) {
            double sum = 0;

            for (int i = k + 1; i < n; i++) {
                sum += A[k][i] * result[i];
            }

            result[k] = (b[k] - sum) / A[k][k];
        }

        return result;
    }

    /**
     * Forward Substitution para resolver sistemas triangulares inferiores
     *
     * @param A Matriz triangular inferior
     * @param b Vetor de resultados
     * @return Vetor de resultados
     */

    public static double[] solveLower(double[][] A, double[] b) {
        int n = A.length;
        double[] result = new double[b.length];

        result[0] = b[0] / A[0][0];

        for (int k = 1; k < n; k++) {
            double sum = 0;

            for (int i = 0; i < k; i++) {
                sum += A[k][i] * result[i];
            }

            result[k] = (b[k] - sum) / A[k][k];
        }

        return result;
    }
}
