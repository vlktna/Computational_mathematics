import Exceptions.MyException;
import java.io.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Communication {

    private final Scanner scanner;
    private Calculation calculation;

    public Communication() {
        this.scanner = new Scanner(System.in);
    }

    public Calculation getCalculation() {
        return this.calculation;
    }

    /**
     * Считывает СЛАУ из файла
     *
     * @throws MyException
     */
    private void readMatrixFromFile() throws MyException {
        System.out.println("Укажите полный путь к файлу: ");
        String fileName = scanner.next();
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName)));
            readMatrix(sc, false);
            sc.close();
        } catch (FileNotFoundException e) {
            throw new MyException("Файл не найден", e);
        }
    }

    /**
     * Заполняет матрицу A и B данными с клавиатуры/файла
     *
     * @param scanner - сканер для клавиатуры/файла
     * @throws MyException
     */
    public void readMatrix(Scanner scanner, boolean isConsole) throws MyException {
        readSize(scanner, isConsole);
        readAccuracy(scanner, isConsole);
        if (isConsole) {
            System.out.println("Напишите коэффициенты а при х и коэффициенты b\n" +
                    "Например, уравнение '5*x1 + 3*x2 = 6' запишите в виде '5 3 6'");
        }
        int size = calculation.getSize();
        try {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size + 1; j++) {
                    if (j != size) {
                        calculation.setElementMatrixA(i, j, scanner.nextDouble());
                    } else {
                        calculation.setElementMatrixB(i, scanner.nextDouble());
                    }
                }
            }
        } catch (NullPointerException e) {
            throw new MyException("Файл пуст", e);
        } catch (NumberFormatException | NoSuchElementException e) {
            throw new MyException("Файл составлен неверно", e);
        }
    }

    /**
     * Считывает размер СЛАУ с клавиатуры/файла
     *
     * @param scanner - сканер для клавиатуры/файла
     * @throws MyException
     */
    private void readSize(Scanner scanner, boolean isConsole) throws MyException {
        try {
            if (isConsole) {
                System.out.println("Напишите размер матрицы:");
            }
            int size = Math.abs(scanner.nextInt());
            if (size <= 0) throw new MyException("Размер матрицы должен быть числом больше 0", null);
            System.out.println("\nРазмер матрицы = " + size);
            this.calculation = new Calculation(size);
            this.calculation.setAccuracy(size);
        } catch (InputMismatchException e) {
            throw new MyException("Размер матрицы должен быть целым числом больше 0", e);
        } catch (NoSuchElementException e) {
            throw new MyException("Файл пуст", e);
        }
    }

    /**
     * Считывает точность вычислений с клавиатуры/файла
     *
     * @param scanner - сканер для клавиатуры/файла
     * @throws MyException
     */
    private void readAccuracy(Scanner scanner, boolean isConsole) throws MyException {
        try {
            if (isConsole) {
                System.out.println("Напишите точность (пр. 0,01):");
            }
            double accuracy = scanner.nextDouble();
            if (accuracy < 0) throw new MyException("Точность должна быть числом больше 0 и меньше 1", null);
            if (accuracy > 1) throw new MyException("Точность должна быть числом больше 0 и меньше 1", null);
            System.out.println("Точность = " + accuracy);
            this.calculation.setAccuracy(accuracy);
        } catch (InputMismatchException e) {
            throw new MyException("Точность должна быть числом больше 0 и меньше 1", e);
        }
    }

    /**
     * Заполняет матрицу A и B рандомными данными
     *
     * @param scanner - сканер для клавиатуры/файла
     * @throws MyException
     */
    public void generateRandomMatrix(Scanner scanner) throws MyException {
        readSize(scanner, true);
        readAccuracy(scanner, true);
        int size = calculation.getSize();
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int j = 0; j < size; j++) {
                double newElement = roundToTwo(Math.random());
                sum += newElement;
                calculation.setElementMatrixA(i, j, newElement);
            }
            calculation.setElementMatrixA(i, i, roundToTwo(Math.random() + sum));
            calculation.setElementMatrixB(i, roundToTwo(Math.random()));
        }
    }

    /**
     * Округляет число до 2 знаков после запятой
     *
     * @param n - исходное число
     * @return - округленное число
     */
    private double roundToTwo(double n) {
        return (double) Math.round(n * 1000) / 100;
    }

    public void start() throws MyException {
        System.out.println("1 - считать матрицу из файла\n" +
                "2 - ввод матрицы с клавиатуры\n" +
                "3 - случайная генерация матрицы\n\n" +
                "Напишите номер команды: ");
        try {
            int answer = scanner.nextInt();
            switch (answer) {
                case 1:
                    readMatrixFromFile();
                    break;
                case 2:
                    readMatrix(this.scanner, true);
                    break;
                case 3:
                    generateRandomMatrix(scanner);
                    break;
                default:
                    System.out.println("Команда не найдена");
            }
            scanner.close();
        } catch (InputMismatchException e) {
            throw new MyException("Нужно написать номер требуемой команды", e);
        }
    }
}
