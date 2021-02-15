public class Calculation {

    private final double[][] matrixA;
    private final double[] matrixB;
    private final double[][] matrixC;
    private final double[] matrixD;
    private final double[] matrixX;
    private final double[] matrixPreviousX;
    private final double[] matrixObservationalError;

    private final int size;
    private double accuracy;

    public Calculation (int size){
        this.size = size;
        this.matrixA = new double[size][size];
        this.matrixB = new double[size];
        this.matrixC = new double[size][size];
        this.matrixD = new double[size];
        this.matrixX = new double[size];
        this.matrixPreviousX = new double[size];
        this.matrixObservationalError = new double[size];
    }

    public int getSize() {
        return size;
    }

    public double getAccuracy(){
        return accuracy;
    }

    public double[] getMatrixX() {
        return matrixX;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public void setElementMatrixA(int i, int j, double element){
        this.matrixA[i][j] = element;
    }

    public void setElementMatrixB(int i, double element){
        this.matrixB[i] = element;
    }

    /**
     * Выводит СЛАУ в консоль
     */
    public void printMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrixA[i][j] + "*x" + (j + 1));
                if (j != size - 1) {
                    System.out.print(" + ");
                }
            }
            System.out.print(" = " + matrixB[i] + "\n");
        }
    }

    /**
     * Проверка диагонального преобладания
     *
     * @return true, если есть диагональное преобладание
     */
    public boolean isDiagonallyDominant() {
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int j = 0; j < size; j++) {
                sum += Math.abs(matrixA[i][j]);
                sum -= Math.abs(matrixA[i][i]);
                if (Math.abs(matrixA[i][i]) < sum)
                    return false;
            }
        }
        return true;
    }

    /**
     * Приводит матрицу к диагональному преобладанию
     */
    public void makeDiagonallyDominant() {
        for (int j = 0; j < size; j++) {
            double max = Double.MIN_VALUE;
            int maxi = -1;
            for (int i = 0; i < size; i++) {
                if (matrixA[i][j] > max) {
                    max = matrixA[i][j];
                    maxi = i;
                }
            }
            if (j != 0 && max > matrixA[maxi][j - 1]) {
                changeRawInMatrix(j, maxi);
            } else if (j == 0) {
                changeRawInMatrix(j, maxi);
            }
        }
    }

    /**
     * Меняет местами 2 строки из матрицы
     *
     * @param i1 индекс 1 строки
     * @param i2 индекс 2 строки
     */
    private void changeRawInMatrix(int i1, int i2) {
        for (int j = 0; j < size; j++) {
            double currentElement = this.matrixA[i1][j];
            this.matrixA[i1][j] = this.matrixA[i2][j];
            this.matrixA[i2][j] = currentElement;
        }
        double element = this.matrixB[i1];
        this.matrixB[i1] = this.matrixB[i2];
        this.matrixB[i2] = element;
    }

    /**
     * Метод вычисляет значения матриц С и D из значений матриц A и B,
     * начальные значения Х равны элементам матрицы D
     */
    public void calculateMatrixPreviousXMatrixCMatrixD() {
        for (int i = 0; i < size; i++) {
            double currentDiagonalA = matrixA[i][i];
            for (int j = 0; j < size; j++) {
                double currentC;
                if (i != j) {
                    currentC = -matrixA[i][j] / currentDiagonalA;
                } else {
                    currentC = 0;
                }
                matrixC[i][j] = currentC;
            }
            matrixD[i] = matrixB[i] / currentDiagonalA;
            matrixPreviousX[i] = matrixD[i];
        }
    }

    /**
     * Вычисляет значения x^(k+1) из x^(k)
     *
     * @return максимальный критерий по абсолютным отклонениям
     */
    public double calculateOneIteration() {
        double maxObservationalError = -1.;
        for (int i = 0; i < size; i++) {
            double currentX = 0.;
            for (int j = 0; j < size; j++) {
                currentX += matrixC[i][j] * matrixPreviousX[j];
            }
            currentX += matrixD[i];
            matrixX[i] = currentX;
            matrixObservationalError[i] = Math.abs(matrixX[i] - matrixPreviousX[i]);
            if (maxObservationalError < matrixObservationalError[i]) {
                maxObservationalError = matrixObservationalError[i];
            }
        }
        return maxObservationalError;
    }

    public void changeMatrixPreviousX(){
        if (size >= 0) System.arraycopy(matrixX, 0, matrixPreviousX, 0, size);
    }

}