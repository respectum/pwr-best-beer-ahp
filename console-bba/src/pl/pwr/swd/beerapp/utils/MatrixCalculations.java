package pl.pwr.swd.beerapp.utils;

import Jama.Matrix;
import edu.umbc.cs.maple.utils.JamaUtils;

public class MatrixCalculations {

    public static double[] columnsSum(Matrix inputMatrix) {

        int columnsNumber = inputMatrix.getColumnDimension();

        double[] columnsTotal = new double[columnsNumber];

        for (int column = 0; column < columnsNumber; column++) {
            columnsTotal[column] = 0;
            for (int row = 0; row < columnsNumber; row++) {
                columnsTotal[column] += inputMatrix.get(row, column);
            }
        }
        return columnsTotal;
    }

    public static Matrix normalizeMatrix(Matrix inputMatrix) {

        Matrix normalizedMatrix = new Matrix(inputMatrix.getColumnDimension(), inputMatrix.getRowDimension());
        double[] columnsTotal = columnsSum(inputMatrix);

        int columnsNumber = inputMatrix.getColumnDimension();
        for (int column = 0; column < columnsNumber; column++) {
            for (int row = 0; row < columnsNumber; row++) {
                normalizedMatrix.set(row, column, inputMatrix.get(row, column) / columnsTotal[column]);
            }
        }
        return normalizedMatrix;
    }

    public static double[] rowsAvg(Matrix inputMatrix) {

        Matrix normalizedMatrix = normalizeMatrix(inputMatrix);

        int rowsNumber = normalizedMatrix.getRowDimension();
        double[] averageRows = new double[rowsNumber];

        for (int row = 0; row < rowsNumber; row++) {
            averageRows[row] = 0;
            averageRows[row] += JamaUtils.rowsum(normalizedMatrix, row) / rowsNumber;
        }
        return averageRows;
    }

    public static boolean checkCoherence(Matrix inputMatrix) {
        double[] columnsSum = columnsSum(inputMatrix);
        double[] rowsAvg = rowsAvg(inputMatrix);


        double consistencyIndex, consistencyRatio, lambdaMax = 0;
        boolean inputMatrixCoherence = false;
        int inputMatrixSize = inputMatrix.getColumnDimension();
        double[] randomIndex = {0, 0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57, 1.59};

        for (int length = 0; length < inputMatrixSize; length++) {
            lambdaMax += columnsSum[length] * columnsSum[length];
        }

        consistencyIndex = (lambdaMax - inputMatrixSize) / (inputMatrixSize - 1);
        consistencyRatio = consistencyIndex / randomIndex[inputMatrixSize - 1];

        if (consistencyRatio <= 0.1) {
            return inputMatrixCoherence = true;
        } else
            return inputMatrixCoherence = false;
    }

    public static Matrix rank(double[] avgCriteriaVector, double[]... decisionComparisonsVectors) {


        int matrixRows = decisionComparisonsVectors.length;
        int matrixColumns = decisionComparisonsVectors[0].length;

        Matrix auxiliaryMatrix = new Matrix(matrixRows, matrixColumns);

        Matrix avgDecisionVector = new Matrix(avgCriteriaVector, 1);

        for (int row = 0; row < matrixRows; row++) {
            for (int column = 0; column < matrixColumns; column++) {

                auxiliaryMatrix.set(row, column, decisionComparisonsVectors[row][column]);
            }
        }
        Matrix rankMatrix = avgDecisionVector.times(auxiliaryMatrix);
//        rankMatrix.getColumnPackedCopy();
        return rankMatrix;
    }

}