package ru.clevertec.util;

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
            while (!SCANNER.hasNextDouble()) {
                log.info("Введенное значение не является числом.");
                SCANNER.next();
            }
            number = SCANNER.nextInt();
        } while (isNotInBounds(number, bound));
        return number;
    }

//    public static String readStringFromConsole(String message) {
//        log.info(message);
//        return SCANNER.nextLine();
//    }

    private static boolean isNotInBounds(int number, int bound) {
        if (number < 1 || number > bound) {
            log.info("Введенное значение вышло за границы допустимых значений.");
        }
        return number < 1 || number > bound;
    }

//    public static void waitEnterKeyPressed() {
//        log.info("*** Нажмите клавишу ENTER для продолжения ***");
//        //TODO
//        SCANNER.nextLine(); //next
//    }

}
