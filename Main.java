public class Main {
    public static void main(String[] args) {
        new Main().main();
    }
    public void main() {
        Agent[][] Field = new Agent[20][20];
        for (int i = 0; i < 10; i++) {//создаем траву
            boolean fl = true;
            while (fl) {
                int a = getRandomNumber(19);
                int b = getRandomNumber(19);
                if (Field[a][b] == null) {
                    Field[a][b] = new Grass(a, b, 0);
                    fl = false;
                }
            }
        }
        for (int i = 0; i < 10; i++) {//создаем антилоп
            boolean fl = true;
            while (fl) {
                int a = getRandomNumber(19);
                int b = getRandomNumber(19);
                if (Field[a][b] == null) {
                    Field[a][b] = new Antelope(a, b, 0);
                    fl = false;
                }
            }
        }
        for (int i = 0; i < 10; i++) {//создаем волков
            boolean fl = true;
            while (fl) {
                int a = getRandomNumber(19);
                int b = getRandomNumber(19);
                if (Field[a][b] == null) {
                    Field[a][b] = new Wolf(a, b, 0);
                    fl = false;
                }
            }
        }
        boolean flag = true;
        int move = 0;
        while (flag) {
            move++;
            int counterGrass = 0;
            int counterAntelope = 0;
            int counterWolf = 0;
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    if (Field[i][j] instanceof Grass) {
                        counterGrass++;
                    }
                    if (Field[i][j] instanceof Antelope) {
                        counterAntelope++;
                    }
                    if (Field[i][j] instanceof Wolf) {
                        counterWolf++;
                    }
                    if (Field[i][j] != null && Field[i][j].move < move) {
                        Field[i][j].move++;
                        Field[i][j].action(Field);
                        System.out.println(i);
                        System.out.println(j);
                        showField(Field);
                    }
                }
            }
            if (counterGrass == 0) {
                flag = false;
                System.out.println("Трава исчезла");
            }
            if (counterAntelope == 0) {
                flag = false;
                System.out.println("Антелопы вымерли");
            }
            if (counterWolf == 0) {
                flag = false;
                System.out.println("Волки вымерли");
            }

        }
        //тут типа while и условия для существования мира

        //внутри проходим по полю и по порядку вызываем экшн у всех, после каждого хода отображаем новое поле и делаем паузу небольшую
    }

    abstract class Agent {
        public int move;
        public int energy;
        public int x;
        public int y;

        public Agent(int x, int y, int move) {
            energy = 5;
            this.x = x;
            this.y = y;
            this.move = move;
        }

        public abstract void action(Agent[][] Field);

        public abstract void movement(Agent[][] Field);

        public abstract void splitting(Agent[][] Field);

    }

    public class Grass extends Agent {

        @Override
        public void action(Agent[][] Field) {
            energy++;
            splitting(Field);
        }

        @Override
        public void movement(Agent[][] Field) {
            //Трава не двигается, конкретно тут ничего не происходит
        }

        @Override
        public void splitting(Agent[][] Field) {// анализируется поле вокруг, рандомно выбирается клетка и происходит деление, возвращается новый тип, которого экшн должен добавить на поле
            // тут определяется клетка спавна типули
            if (energy == 11) {
                int[] newcords = getcords(x, y, Field);
                if (newcords[0] != -1) {
                    energy = 5;
                    Field[newcords[0]][newcords[1]] = new Grass(newcords[0], newcords[1], move);
                    Field[newcords[0]][newcords[1]].action(Field);
                    showField(Field);
                } else {
                    energy = 10;
                }
            }
        }

        public Grass(int a, int b, int move) {
            super(a, b, move);
        }
    }

    public class Antelope extends Agent {

        @Override
        public void action(Agent[][] Field) {//тут агент оценивает ситуацию, принимает решение, получает или теряет энергию, вызывает сплит или мувмент если надо
            //В случае антелопы сначала происходит мувмент с оценкой ситуации и тд, затем проверка на сплит
            //Если сплит происходит, вызывается экшн у нового типули после его занесения в поле
            if (energy >= 11) {
                energy = 6;
                //то добавить на поле splitting и вызвать его экшн
            }
        }

        @Override
        public void movement(Agent[][] Field) {
            //У антелопы главная задача убежать от хищника, если его нет то трава
            //если нет травы то рандомное направление, главное чтобы не шла туда сюда,
            //Надо выбрать направление и пусть оно будет задано пока не найдет край поля или цель
            //Если во время ходов нашлась еда, то пробуем сплит
            //если во время погони от хищника по пути есть еда, выбираем путь с едой
            //после спавна типули вызывается его экшн чтобы он сходил
            showField(Field);

        }

        @Override
        public void splitting(Agent[][] Field) {// анализируется поле вокруг, рандомно выбирается клетка и происходит деление, возвращается новый тип, которого экшн должен добавить на поле
            // тут определяется клетка спавна типули
            showField(Field);


        }

        public Antelope(int a, int b, int move) {
            super(a, b, move);
        }
    }

    public class Wolf extends Agent {

        @Override
        public void action(Agent[][] Field) {
            movement(Field);
            splitting(Field);//Сделать сплит подобный траве, сплит происходит после 3х ходов, если энергии перебор она пропадает
        }

        @Override
        //надо сделать что если сожрал антилопу то плюс ее энергия, если просто ход то минус энергия
        //также надо сделать чтобы волк делал 3 хода, тратил на это 1 энергию
        //если энергия 0 то сдох
        public void movement(Agent[][] Field) {//надо сделать что если сожрал антилопу то плюс ее энергия, если просто ход то минус энергия
            energy--;
            for (int i = 0; i < 3; i++) {
                int[] closestAntelope = findAntelope(Field);
                if (closestAntelope[0] != -100) { //антелопа найдена
                    if ((Math.abs(x - closestAntelope[0])) > (Math.abs(y - closestAntelope[1]))) {
                        energy = energy + movex(Field, closestAntelope, true);
                    } else {
                        energy = energy + movey(Field, closestAntelope, true);
                    }
                } else {
                    int[] newcords = getcords(x, y, Field);
                    if (newcords[0] != -1) {
                        Field[newcords[0]][newcords[1]] = Field[x][y];
                        Field[x][y] = null;
                        x = newcords[0];
                        y = newcords[1];
                    }
                }
            }
        }

        public int movex(Agent[][] Field, int[] closestAntelope, boolean flag) {
            int en = 0;
            if (x > closestAntelope[0]) {
                if (Field[x - 1][y] == null || Field[x - 1][y] instanceof Antelope) {
                    if (Field[x - 1][y] instanceof Antelope) {
                        en = Field[x - 1][y].energy;
                    }
                    Field[x - 1][y] = Field[x][y];
                    Field[x][y] = null;
                    x = x - 1;

                } else if (flag) {
                    en = movey(Field, closestAntelope, false);
                }
            } else {
                if (Field[x + 1][y] == null || Field[x + 1][y] instanceof Antelope) {
                    if (Field[x + 1][y] instanceof Antelope) {
                        en = Field[x + 1][y].energy;
                    }
                    Field[x + 1][y] = Field[x][y];
                    Field[x][y] = null;
                    x = x + 1;
                } else if (flag) {
                    en = movey(Field, closestAntelope, false);
                }

            }
            return en;
        }

        public int movey(Agent[][] Field, int[] closestAntelope, boolean flag) {
            int en = 0;
            if (y > closestAntelope[1]) {
                if (Field[x][y - 1] == null || Field[x][y - 1] instanceof Antelope) {
                    if (Field[x][y - 1] instanceof Antelope) {
                        en = Field[x][y - 1].energy;
                    }
                    Field[x][y - 1] = Field[x][y];
                    Field[x][y] = null;
                    y = y - 1;
                } else if (flag) {
                    en = movex(Field, closestAntelope, false);
                }

            } else {
                if (Field[x][y + 1] == null || Field[x][y + 1] instanceof Antelope) {
                    if (Field[x][y + 1] instanceof Antelope) {
                        en = Field[x][y + 1].energy;
                    }
                    Field[x][y + 1] = Field[x][y];
                    Field[x][y] = null;
                    y = y + 1;
                } else if (flag) {
                    en = movex(Field, closestAntelope, false);
                }

            }
            return en;
        }

        public int[] findAntelope(Agent[][] Field) {//поиск примитивный, если 2 антелопы на одинак расстоянии то идет к первой найденной
            int[] closest = new int[]{-100, -100};
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    if (this.x + i < 0 || this.x + i >= 20) continue;
                    if (this.y + j < 0 || this.y + j >= 20) continue;

                    if (Field[this.x + i][this.y + j] instanceof Antelope) {
                        if (Math.abs(i) + Math.abs(j) < Math.abs(x - closest[0]) + Math.abs(y - closest[1])) {
                            closest[0] = this.x + i;
                            closest[1] = this.y + j;
                        }
                    }
                }
            }
            return closest;
        }

        @Override
        public void splitting(Agent[][] Field) {// анализируется поле вокруг, рандомно выбирается клетка и происходит деление, возвращается новый тип, которого экшн должен добавить на поле
            // тут определяется клетка спавна типули
            //после спавна типули вызывается его экшн чтобы он сходил


        }

        public Wolf(int a, int b, int move) {
            super(a, b, move);
        }
    }


    int[] getcords(int x, int y, Agent[][] Field) { //1 вправо, 2 вниз, 3 влево, 4 вверх, если что-то занято то следующее, если 4 занято то возвращаем -1 -1
        int scenario = getRandomNumber(4) + 1;
        for (int i = 0; i < 4; i++) {
            switch (scenario) {
                case 1:
                    if (x != 19) {
                        if (Field[x + 1][y] == null)//на 1 меньше тк координаты и место в списке не совпадают
                            return new int[]{x + 1, y};
                    }
                case 2:
                    if (y != 19) {
                        if (Field[x][y + 1] == null)//на 1 меньше тк координаты и место в списке не совпадают
                            return new int[]{x, y + 1};
                    }
                case 3:
                    if (x != 0) {
                        if (Field[x - 1][y] == null)//на 1 меньше тк координаты и место в списке не совпадают
                            return new int[]{x - 1, y};
                    }
                case 4:
                    if (y != 0) {
                        if (Field[x][y - 1] == null)//на 1 меньше тк координаты и место в списке не совпадают
                            return new int[]{x, y - 1};
                    }
            }
            scenario = scenario % 4 + 1;
        }
        return new int[]{-1, -1};
    }

    static void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls")
                    .inheritIO()
                    .start()
                    .waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showField(Agent[][] Field) {

        clearConsole();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (Field[i][j] instanceof Grass) {
                    System.out.print("G ");
                } else if (Field[i][j] instanceof Antelope) {
                    System.out.print("A ");
                } else if (Field[i][j] instanceof Wolf) {
                    System.out.print("W ");
                } else {
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }

    public static int getRandomNumber(int limit) //Функция возвращает целые числа из диапазона 0...limit-1
    {
        return (int) (Math.random() * limit);
    }
}