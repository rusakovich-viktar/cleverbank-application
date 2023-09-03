package ru.clevertec.util;

import java.math.BigDecimal;
import java.util.Scanner;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public class InputUtils {
    private static final Scanner SCANNER;

    static {
        SCANNER = new Scanner(System.in);
    }

    public static int readIntFromConsole(String message, int bound) {
        int number;
        do {
            log.info(message + ":");
            while (!SCANNER.hasNextInt()) {
                log.info("Введенное значение не является числом.");
                SCANNER.next();
            }
            number = SCANNER.nextInt();
            SCANNER.nextLine();
        } while (isNotInBounds(number, bound));
        return number;
    }

    public static String readStringFromConsole() {

        return SCANNER.nextLine();
    }

    public static String readStringFromConsole(String message) {
        log.info(message);
        return SCANNER.nextLine();
    }

    private static boolean isNotInBounds(int number, int bound) {
        if (number < 1 || number > bound) {
            log.info("Введенное значение вышло за границы допустимых значений.");
        }
        return number < 1 || number > bound;
    }

    public static int readIntFromConsoleWithoutBounds() {
        while (true) {
            String userInput = InputUtils.readStringFromConsole();
            if ("exit".equalsIgnoreCase(userInput)) {
                log.info("Выход из цикла");
                return -1;
            }
            try {
                return Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                log.info("Введенное значение не является числом.");
            }
        }
    }

    public static BigDecimal readIBigDecimalFromConsoleWithoutBounds() {
        while (true) {
            String userInput = InputUtils.readStringFromConsole();
            try {
                BigDecimal bigDecimal = new BigDecimal(userInput);
                return bigDecimal;
            } catch (NumberFormatException e) {
                log.info("Введенное значение не является числом. Попробуйте снова.");
            }
        }
    }

    public static void waitEnterKeyPressed() {
        log.info("*** Нажмите клавишу ENTER для продолжения ***");
        SCANNER.nextLine();
    }

}
