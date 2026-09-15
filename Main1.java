public class Main1 {

    public static void main(String[] args) throws Exception {

        int number = 0;

        while (number != 10) {

            // Очищаем консоль
            clearConsole();

            // Выводим 10 строк
            for (int i = 0; i < 10; i++) {
                System.out.println("Текст " + number + " строка " + i);
            }

            number++;

            // Ждём 1 секунду
            Thread.sleep(1000);
        }
    }


    static void clearConsole() throws Exception {

        new ProcessBuilder("cmd", "/c", "cls")
                .inheritIO()
                .start()
                .waitFor();
    }
}