import Exceptions.MyException;

import java.util.Arrays;

public class Application {

    private final Communication communication;

    public Application(){
        this.communication = new Communication();
    }

    public void run(){
        try {
            communication.start();
            Calculation calculation = communication.getCalculation();
            calculation.makeDiagonallyDominant();
            if (calculation.isDiagonallyDominant()) {
                System.out.println("Матрица приведена к диагональному преобладанию");
                calculation.printMatrix();
                calculation.calculateMatrixPreviousXMatrixCMatrixD();
                int k = 0;
                while (calculation.calculateOneIteration() > calculation.getAccuracy()) {
                    k++;
                    calculation.changeMatrixPreviousX();
                }
                System.out.println("Найдены решения СЛАУ x = " + Arrays.toString(calculation.getMatrixX()) + " за k = " + k);
            } else {
                System.out.println("В матрице нет диагонального преобладания, данную систему нельзя решить " +
                        "методом простых итераций ");
            }
        }catch (MyException ignored){
        }
    }
}