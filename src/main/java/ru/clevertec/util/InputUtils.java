package ru.clevertec.util;

import java.util.Scanner;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InputUtils {
    private static final Scanner SCANNER;

   static {
       SCANNER = new Scanner(System.in);
   }

    public static int readIntFromConsole(String message, int bound) {
        int number;
        do {
            System.out.println(message + ":");
            while (!SCANNER.hasNextDouble()) {
                System.out.println("Введенное не является числом.");
                SCANNER.next();
            }
            number = SCANNER.nextInt();
        } while (isNotInBounds(number, bound));
        return number;
    }

    public static String readStringFromConsole(String message) {
        System.out.println(message);
        return SCANNER.nextLine();
    }

    private static boolean isNotInBounds(int number, int bound) {
        if (number < 1 || number > bound) {
            System.out.println("Введенное не является положительным числом.");
        }
        return number < 1 || number > bound;
    }

    public static void waitEnterKeyPressed() {
        System.out.println("*** Нажмите клавишу ENTER для продолжения ***");
        //TODO
        SCANNER.nextLine(); //next
    }

}
