package ru.clevertec.util;

public class DrawUI {

    public static void drawStartMenuQ() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*                     Добро пожаловать,                    *");
        System.out.println("*                                                          *");
        System.out.println("*              выберите один из пунктов меню               *");
        System.out.println("************************************************************");
        System.out.println("1. View account data");
        System.out.println("2. Показать информацию о счетах");
        System.out.println("3. Операции со счетами");
        System.out.println("4. Выйти");

    }

    public static void drawTransactionMenu() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*               Операции со счетом                         *");
        System.out.println("************************************************************");
        System.out.println("1. Пополнить счет");
        System.out.println("2. Снять со счета");
        System.out.println("3. Перевод на другой счет");
    }

    public static void drawReplenishOperationHeader() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*                    Пополнить счет                        *");
        System.out.println("************************************************************");
    }

    public static void drawWithdrawOperationHeader() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*                    Снять со счета                        *");
        System.out.println("************************************************************");
    }

    public static void drawTransferOperationHeader() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*               Перевод на другой счет                     *");
        System.out.println("************************************************************");
    }

    public static void drawEnterLogin() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*             Пожалуйста, введите логин:                   *");
        System.out.println("************************************************************");
    }

    public static void drawEnterPassword() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*             Пожалуйста, введите пароль:                  *");
        System.out.println("************************************************************");
    }

    public static void drawViewAllAccounts() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*                 View all accounts:                       *");
        System.out.println("************************************************************");
    }

    public static void drawLogo() {
        System.out.println();
        System.out.println("\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +

                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n");
    }

}
