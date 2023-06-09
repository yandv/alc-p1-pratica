package br.com.yandv.method;

import br.com.yandv.method.utils.Debugger;
import br.com.yandv.method.utils.Resolver;

import java.util.AbstractMap;
import java.util.Map;

public class Resolution {

    public static void main(String[] args) {
        int n = 3, min = 1, max = 5;

        double[][] A = createMatrix(1, 2, 2, 3);
        double[] b = createVector(1, 2, 3);

        n = A.length;

        inverseOfMatrixByLU(A);
    }

    /**
     * Método de Gauss
     * <p>
     * O sistema A é do tipo Ax = b
     * Sendo A uma matriz quadrada de ordem n, x um vetor de n incógnitas e b um vetor de n resultados
     * <p>
     * Para solucionar o sistema, é necessário transformar a matriz A em uma matriz triangular superior
     * Para isso é necessário zerar os elementos abaixo da diagonal principal, sem alterar o resultado do sistema
     *
     * @param A Matriz
     * @param b Vetor de resultados
     */

    public static double[] gauss(double[][] A, String[] x, double[] b) {
        int n = A.length;

        for (int i = 0; i < n; i++) {
            gaussElimination(A, x, b, n, i);
        }

        return Resolver.solveUpper(A, b);
    }

    /**
     * Método de Gauss - Com pivoteamento parcial
     * <p>
     *
     * @param A Matriz
     * @param b Vetor de resultados
     */

    public static double[] gaussWithParcialPivot(double[][] A, String[] x, double[] b) {
        int n = A.length;

        for (int i = 0; i < n; i++) {
            int pivoIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (Math.abs(A[j][i]) > Math.abs(A[pivoIndex][i])) { // PELO AMOR DE DEUS KKKKKKKKKK
                    pivoIndex = j;
                }
            }

            if (pivoIndex != i) {
                for (int j = 1; j < n; j++) {
                    double temp = A[pivoIndex][j];
                    A[pivoIndex][j] = A[i][j];
                    A[i][j] = temp;
                }

                double temp = b[pivoIndex];
                b[pivoIndex] = b[i];
                b[i] = temp;

                String temp2 = x[pivoIndex];
                x[pivoIndex] = x[i];
                x[i] = temp2;
            }

            gaussElimination(A, x, b, n, i);
        }

        return Resolver.solveUpper(A, b);
    }

    /**
     * Método de Gauss - Com pivoteamento total
     * <p>
     *
     * @param A Matriz
     * @param b Vetor de resultados
     */

    public static double[] gaussWithCompletePivot(double[][] A, String[] x, double[] b) {
        int n = A.length;

        for (int i = 0; i < n; i++) {
            completePivot(A, x, b, n, i);
            gaussElimination(A, x, b, n, i);
        }

        return Resolver.solveUpper(A, b);
    }

    /**
     * Eliminação de Gauss, propriamente dita, sem pivoteamento
     */

    private static void gaussElimination(double[][] A, String[] x, double[] b, int n, int i) {
        for (int j = i + 1; j < n; j++) {
            double m = A[j][i] / A[i][i];

            A[j][i] = 0;

            for (int k = i + 1; k < n; k++) {
                A[j][k] += -(m * A[i][k]);
            }

            b[j] += -(m * b[i]);
        }
        Debugger.debug(A, x, b);
    }

    /**
     * Pivotamento total de uma matriz A e um vetor de resultados b
     *
     * @param A Matriz
     * @param b Vetor de resultados
     * @param n Tamanho da matriz
     * @param i Iteração atual
     */

    private static void completePivot(double[][] A, String[] x, double[] b, int n, int i) {
        int collumn = i;
        int row = i; // row or line ?????

        for (int j = i + 1; j < n; j++) {
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(A[j][k]) > Math.abs(A[collumn][row])) {
                    collumn = j;
                    row = k;
                }
            }
        }

        if (row != i) {
            for (int j = 0; j < n; j++) {
                double temp = A[j][row];
                A[j][row] = A[j][i];
                A[j][i] = temp;
            }
        }

        if (collumn != i) {
            for (int j = 0; j < n; j++) {
                double temp = A[collumn][j];
                A[collumn][j] = A[i][j];
                A[i][j] = temp;
            }

            double temp = b[collumn];
            b[collumn] = b[i];
            b[i] = temp;

            if (x != null) {
                String temp2 = x[collumn];
                x[collumn] = x[i];
                x[i] = temp2;
            }
        }
    }

    /**
     * @param A
     */

    public static Map.Entry<double[][], double[][]> cholesky(double[][] A) {
        int n = A.length;

        double[][] G = new double[n][n];

        for (int k = 0; k < n; k++) {
            double m = 0;

            for (int i = 0; i < k; i++) {
                m += G[k][i] * G[k][i];
            }

            m = A[k][k] - m;

            if (m <= 0) {
                break;
            }

            G[k][k] = Math.sqrt(m);

            for (int i = k + 1; i < n; i++) {
                double sum = 0;

                for (int j = 0; j < k; j++) {
                    sum += G[i][j] * G[k][j];
                }

                G[i][k] = (A[i][k] - sum) / G[k][k];
            }
        }

        double[][] L = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                L[i][j] = G[i][j];
            }
        }

        double[][] U = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                U[i][j] = G[j][i];
            }
        }

        System.out.println("A é simétrica definida positiva");
        return new AbstractMap.SimpleEntry<>(L, U);
    }

    public static double[] solveLUDecompose(double[][] A, String[] x, double[] b, boolean completePivot) {
        int n = A.length;

        if (completePivot) {
            LUdecomposeWithCompletePivot(A, b);
        } else {
            LUdecompose(A);
        }

        Debugger.debug(A);

        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        for (int i = 0; i < n; i++) {
            L[i][i] = 1;

            for (int j = 0; j < n; j++) {
                if (i > j) {
                    L[i][j] = A[i][j];
                } else {
                    U[i][j] = A[i][j];
                }
            }
        }

        double[] y = Resolver.solveLower(L, b);

        double[] solve = Resolver.solveUpper(U, y);
        Debugger.debug(solve, x);
        return solve;
    }

    /**
     * Decomposição LU de uma matriz A
     * Os elementos relevantes de L e U sejam armazenados na
     * própria matriz A, isto é, aij = uij, ∀i ≤ j e aij = lij ∀i > j
     *
     * @param A Matriz A
     * @return Matriz L e U
     */

    public static double[][] LUdecompose(double[][] A) {
        int n = A.length;

        for (int k = 0; k < 2; k++) {
            double pivo = A[k][k];

            for (int i = k + 1; i < n; i++) {
                double m = A[i][k] / pivo;

                for (int j = k; j < n; j++) {
                    A[i][j] -= m * A[k][j];
                }

                A[i][k] = m;
            }
        }

        Debugger.debug(A);
        return A;
    }

    /**
     * Decomposição LU de uma matriz A
     * Os elementos relevantes de L e U sejam armazenados na
     * própria matriz A, isto é, aij = uij, ∀i ≤ j e aij = lij ∀i > j
     *
     * SE A É INVERSÍVEL E ADMITE DECOMPOSIÇÃO LU (A = LU)
     *
     * @param A Matriz A
     * @return Matriz L e U
     */

    public static double[][] inverseOfMatrixByLU(double[][] A) {
        int n = A.length;

        LUdecompose(A);

        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        // tirando os items importantes de A e colocando em L e U

        for (int i = 0; i < n; i++) {
            L[i][i] = 1;

            for (int j = 0; j < n; j++) {
                if (i > j) {
                    L[i][j] = A[i][j];
                } else {
                    U[i][j] = A[i][j];
                }
            }
        }

        double[][] inverse = new double[n][n];
        double[][] identity = createIdentity(n);

        for (int k = 0; k < n; k++) {
            double[] y = Resolver.solveLower(L, identity[k]);
            double[] x = Resolver.solveUpper(U, y);

            inverse[k] = x;
        }

        Debugger.debug(inverse);
        return inverse;
    }

    private static double[][] createIdentity(int n) {
        double[][] identity = new double[n][n];

        for (int i = 0; i < n; i++) {
            identity[i][i] = 1;
        }

        return identity;
    }

    /**
     * Decomposição LU de uma matriz A com pivotação completa
     * Os elementos relevantes de L e U sejam armazenados na
     * própria matriz A, isto é, aij = uij, ∀i ≤ j e aij = lij ∀i > j
     *
     * @param A Matriz A com pivotação completa
     * @param b Vetor b
     * @return Matriz L e U
     */

    public static double[][] LUdecomposeWithCompletePivot(double[][] A, double[] b) {
        int n = A.length;

        for (int k = 0; k < 2; k++) {
            completePivot(A, null, b, n, k);
            double pivo = A[k][k];

            for (int i = k + 1; i < n; i++) {
                double m = A[i][k] / pivo;

                for (int j = k; j < n; j++) {
                    A[i][j] -= m * A[k][j];
                }

                A[i][k] = m;
            }
        }


        Debugger.debug(A);
        return A;
        //return new AbstractMap.SimpleEntry<>(L, U);
    }

    public static double[][] createMatrix(double... args) {
        int n = (int) Math.sqrt(args.length);
        double[][] A = new double[n][n];

        int card = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = args[card];
                card++;
            }
        }

        return A;
    }

    public static double[] createVector(double... args) {
        return args;
    }

    public static double[][] createRandomMatrix(int n, double min, double max) {
        double[][] A = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = Math.max((int) (Math.random() * (max - min) + min), 1);
            }
        }

        return A;
    }

    public static double[] createRandomVector(int n, double min, double max) {
        double[] vector = new double[n];

        for (int i = 0; i < n; i++) {
            vector[i] = Math.max((int) (Math.random() * (max - min) + min), 1);
        }

        return vector;
    }
}
