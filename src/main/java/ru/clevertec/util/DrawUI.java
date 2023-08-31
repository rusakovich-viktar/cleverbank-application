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
        System.out.println("4. Завершить сеанс и выйти");

    }

    public static void drawTransactionMenu() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*               Операции со счетом                         *");
        System.out.println("************************************************************");
        System.out.println("1. Пополнить счет (наличные)");
        System.out.println("2. Снять со счета (наличные)");
        System.out.println("3. Перевод на другой счет");
    }

    public static void drawTransactionTargetMenu() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*               Выберите направление перевода              *");
        System.out.println("************************************************************");
        System.out.println("1. Себе");
        System.out.println("2. Другому клиенту");
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
        System.out.println("*                  Перевод на другой счет                  *");
        System.out.println("************************************************************");
    }

    public static void drawTransferSourceHeader() {
        System.out.println("************************************************************");
        System.out.println("*   Выберите счет, с которого вы хотите перевести деньги   *");
        System.out.println("************************************************************");
    }

    public static void drawEnterLoginOperationHeader() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*             Пожалуйста, введите логин:                   *");
        System.out.println("************************************************************");
    }

    public static void drawEnterPasswordOperationHeader() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*             Пожалуйста, введите пароль:                  *");
        System.out.println("************************************************************");
    }

    public static void drawViewAllAccountsOperationHeader() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*            Показать информацию о счетах:                 *");
        System.out.println("************************************************************");
    }

    public static void drawDetailsAccountInfoOperationHeader() {
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*   Выберите счет для отображения детальной информации:    *");
        System.out.println("************************************************************");
    }


    public static void drawLogo() {
        System.out.println();
        System.out.println("""
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⠟⢉⣀⣈⠙⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿ WELCOME     ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⡇⢰⣿⠛⠻⠷⠾⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿ TO           ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⡇⠸⣿⣤⣴⡶⢶⣶⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿ CLEVERBANK  ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣦⣈⠉⢉⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                """);
    }

}
