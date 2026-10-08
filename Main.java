

import javafx.application.Application;


public class Main {

    public class parameters {
        public static int split_en_grass = 10;
        public static int after_split_en_grass = 12;
        public static int anti_split_en_grass = 38;
        public static int en_for_move_grass = 3;
        public static int split_en_Antelope = 54;
        public static int after_split_en_Antelope = 10 ;
        public static int anti_split_en_Antelope = 185;
        public static int en_for_move_Antelope = 1;
        public static int split_en_Wolf = 2905;
        public static int after_split_en_Wolf = 348;
        public static int anti_split_en_Wolf = 1;
        public static int en_for_move_Wolf = 10;
        public static int Field_size = 70;
    }
    public static void main(String[] args) {
        Application.launch(Window.class, args);
    }
    public void main() {
        Agent[][] Field = new Agent[parameters.Field_size][parameters.Field_size];
        for (int i = 0; i < 105; i++) {//создаем траву
            boolean fl = true;
            while (fl) {
                int a = getRandomNumber(parameters.Field_size);
                int b = getRandomNumber(parameters.Field_size);
                if (Field[a][b] == null) {
                    Field[a][b] = new Grass(a, b, 0);
                    fl = false;
                }
            }
        }
        for (int i = 0; i < 25; i++) {//создаем антилоп
            boolean fl = true;
            while (fl) {
                int a = getRandomNumber(parameters.Field_size);
                int b = getRandomNumber(parameters.Field_size);
                if (Field[a][b] == null) {
                    Field[a][b] = new Antelope(a, b, 0);
                    fl = false;
                }
            }
        }
        for (int i = 0; i <25; i++) {//создаем волков
            boolean fl = true;
            while (fl) {
                int a = getRandomNumber(parameters.Field_size);
                int b = getRandomNumber(parameters.Field_size);
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
            for (int i = 0; i < parameters.Field_size; i++) {
                for (int j = 0; j < parameters.Field_size; j++) {
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


                    }
                }
            }
            if (Window.restart) {
                return;
            }
            int end = 0;

            if (counterGrass == 0) {
                flag = false;
                end = 1;
            }
            if (counterAntelope == 0) {
                flag = false;
                end = 2;
            }
            if (counterWolf == 0) {
                flag = false;
                end = 3;
            }
            showField(Field, move, end);
        }
    }

    abstract class Agent {
        public int move;
        public int energy;
        public int x;
        public int y;

        public Agent(int x, int y, int move) {
            energy = parameters.after_split_en_grass;
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
            energy = energy + parameters.en_for_move_grass;
            splitting(Field);
        }

        @Override
        public void movement(Agent[][] Field) {
            //Трава не двигается, конкретно тут ничего не происходит
        }

        @Override
        public void splitting(Agent[][] Field) {// анализируется поле вокруг, рандомно выбирается клетка и происходит деление, возвращается новый тип, которого экшн должен добавить на поле
            // тут определяется клетка спавна типули
            if (energy >= parameters.split_en_grass) {
                int[] newcords = getcords(x, y, Field);
                if (newcords[0] != -1) {
                    energy = parameters.after_split_en_grass;
                    Field[newcords[0]][newcords[1]] = new Grass(newcords[0], newcords[1], move);
                    Field[newcords[0]][newcords[1]].action(Field);
                } else {
                    energy = parameters.anti_split_en_grass;
                }
            }
        }

        public Grass(int a, int b, int move) {
            super(a, b, move);
            energy = 13;
        }
    }

    public class Antelope extends Agent {
//когда антелопа убегает она пытается убежать за границу
        @Override
        public void action(Agent[][] Field) {
            energy = energy - parameters.en_for_move_Antelope;
            if (energy <= 0){
                Field[x][y] = null;
            }
            movement(Field);
            splitting(Field);
        }

        @Override
        public void movement(Agent[][] Field) {
                for (int i = 0; i < 2; i++) {
                    int[] closestGoal = findwheretogo(Field);
                    if (closestGoal[0] != -100) { //цель найдена
                        if ((Math.abs(x - closestGoal[0])) > (Math.abs(y - closestGoal[1]))) {
                            energy = energy + movex(Field, closestGoal, true);
                        } else {
                            energy = energy + movey(Field, closestGoal, true);
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
        @Override
        public void splitting(Agent[][] Field) {
                if (energy >= parameters.split_en_Antelope) {
                    int[] newcords = getcords(x, y, Field);
                    if (newcords[0] != -1) {
                        energy = parameters.after_split_en_Antelope;
                        Field[newcords[0]][newcords[1]] = new Antelope(newcords[0], newcords[1], move);
                        Field[newcords[0]][newcords[1]].action(Field);
                    } else {
                        energy = parameters.anti_split_en_Antelope;
                    }
                }
        }

        public Antelope(int a, int b, int move) {
            super(a, b, move);
            energy = parameters.after_split_en_Antelope;
        }
        public int[] findwheretogo(Agent[][] Field) {
            int[] closest = new int[]{-100, -100};
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    if (this.x + i < 0 || this.x + i >= parameters.Field_size) continue;
                    if (this.y + j < 0 || this.y + j >= parameters.Field_size) continue;

                    if (Field[this.x + i][this.y + j] instanceof Wolf) {
                        if (Math.abs(i) + Math.abs(j) < Math.abs(x - closest[0]) + Math.abs(y - closest[1])) {
                            closest[0] = this.x + i;
                            closest[1] = this.y + j;
                        }
                    }
                }
            }

            if (closest[0] ==-100){
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (this.x + i < 0 || this.x + i >= parameters.Field_size) continue;
                        if (this.y + j < 0 || this.y + j >= parameters.Field_size) continue;
                        if (Field[this.x + i][this.y + j] instanceof Grass) {
                            if (Math.abs(i) + Math.abs(j) < Math.abs(x - closest[0]) + Math.abs(y - closest[1])) {
                                closest[0] = this.x + i;
                                closest[1] = this.y + j;
                            }
                        }
                    }
                }
            }
            else{
                if ((closest[0]-(x-closest[0]))>=0 && (closest[0]-(x-closest[0]))<parameters.Field_size){
                    closest[0] = closest[0]-(x-closest[0]);
                }
                if ((closest[1]-(y-closest[1]))>=0 && (closest[1]-(y-closest[1]))<parameters.Field_size){
                    closest[1] = closest[1]-(y-closest[1]);
                }
            }
            return closest;
        }
        public int movex(Agent[][] Field, int[] closestGoal, boolean flag) {
            int en = 0;
            if (x > closestGoal[0]) {
                if (x >0){
                    if ((Field[x - 1][y] == null || Field[x - 1][y] instanceof Grass)&& (x>0)) {
                        if (Field[x - 1][y] instanceof Grass) {
                            en = Field[x - 1][y].energy;
                        }
                        Field[x - 1][y] = Field[x][y];
                        Field[x][y] = null;
                        x = x - 1;
                    }
                }
                else if (flag) {
                    en = movey(Field, closestGoal, false);
                }
            } else {
                if (x<parameters.Field_size-1){
                    if ((Field[x + 1][y] == null || Field[x + 1][y] instanceof Grass)) {
                        if (Field[x + 1][y] instanceof Grass) {
                            en = Field[x + 1][y].energy;
                        }
                        Field[x + 1][y] = Field[x][y];
                        Field[x][y] = null;
                        x = x + 1;
                    }
                } else if (flag) {
                    en = movey(Field, closestGoal, false);
                }

            }
            return en;
        }

        public int movey(Agent[][] Field, int[] closestGoal, boolean flag) {
            int en = 0;
            if (y > closestGoal[1]) {
                if (y > 0){
                    if ((Field[x][y - 1] == null || Field[x][y - 1] instanceof Grass)&& (y>0)) {
                        if (Field[x][y - 1] instanceof Grass) {
                            en = Field[x][y - 1].energy;
                        }
                        Field[x][y - 1] = Field[x][y];
                        Field[x][y] = null;
                        y = y - 1;
                    }
                }
                else if (flag) {
                    en = movex(Field, closestGoal, false);
                }

            } else {
                if (y<parameters.Field_size-1){
                    if (Field[x][y + 1] == null || Field[x][y + 1] instanceof Grass) {
                        if (Field[x][y + 1] instanceof Grass) {
                            en = Field[x][y + 1].energy;
                        }
                        Field[x][y + 1] = Field[x][y];
                        Field[x][y] = null;
                        y = y + 1;
                    }
                }
                else if (flag) {
                    en = movex(Field, closestGoal, false);
                }

            }
            return en;
        }
    }


    public class Wolf extends Agent {

        @Override
        public void action(Agent[][] Field) {
            energy = energy - parameters.en_for_move_Wolf;
            if (energy <= 0){
                Field[x][y] = null;
            }
            movement(Field);
            splitting(Field);//Сделать сплит подобный траве, сплит происходит после 3х ходов, если энергии перебор она пропадает
        }

        @Override
        public void movement(Agent[][] Field) {//надо сделать что если сожрал антилопу то плюс ее энергия, если просто ход то минус энергия
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
                if (x>0){
                    if (Field[x - 1][y] == null || Field[x - 1][y] instanceof Antelope) {
                        if (Field[x - 1][y] instanceof Antelope) {
                            en = Field[x - 1][y].energy;
                        }
                        Field[x - 1][y] = Field[x][y];
                        Field[x][y] = null;
                        x = x - 1;
                }

                } else if (flag) {
                    en = movey(Field, closestAntelope, false);
                }
            } else {
                if (x < parameters.Field_size-1){
                    if (Field[x + 1][y] == null || Field[x + 1][y] instanceof Antelope) {
                        if (Field[x + 1][y] instanceof Antelope) {
                            en = Field[x + 1][y].energy;
                        }
                        Field[x + 1][y] = Field[x][y];
                        Field[x][y] = null;
                        x = x + 1;
                    }
                }
                else if (flag) {
                    en = movey(Field, closestAntelope, false);
                }

            }
            return en;
        }

        public int movey(Agent[][] Field, int[] closestAntelope, boolean flag) {
            int en = 0;
            if (y > closestAntelope[1]) {
                if (y >0){
                    if (Field[x][y - 1] == null || Field[x][y - 1] instanceof Antelope) {
                        if (Field[x][y - 1] instanceof Antelope) {
                            en = Field[x][y - 1].energy;
                        }
                        Field[x][y - 1] = Field[x][y];
                        Field[x][y] = null;
                        y = y - 1;
                    }
                }
                else if (flag) {
                    en = movex(Field, closestAntelope, false);
                }

            } else {
                if (y <parameters.Field_size-1){
                    if (Field[x][y + 1] == null || Field[x][y + 1] instanceof Antelope) {
                        if (Field[x][y + 1] instanceof Antelope) {
                            en = Field[x][y + 1].energy;
                        }
                        Field[x][y + 1] = Field[x][y];
                        Field[x][y] = null;
                        y = y + 1;
                    }
                }
                else if (flag) {
                    en = movex(Field, closestAntelope, false);
                }

            }
            return en;
        }

        public int[] findAntelope(Agent[][] Field) {//поиск примитивный, если 2 антелопы на одинак расстоянии то идет к первой найденной
            int[] closest = new int[]{-100, -100};
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    if (this.x + i < 0 || this.x + i >= parameters.Field_size) continue;
                    if (this.y + j < 0 || this.y + j >= parameters.Field_size) continue;

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
        public void splitting(Agent[][] Field) {
            if (energy >= parameters.split_en_Wolf) {
                int[] newcords = getcords(x, y, Field);
                if (newcords[0] != -1) {
                    energy = parameters.after_split_en_Wolf;
                    Field[newcords[0]][newcords[1]] = new Wolf(newcords[0], newcords[1], move);
                    Field[newcords[0]][newcords[1]].action(Field);
                } else {
                    energy = parameters.anti_split_en_Wolf;
                }
            }


        }

        public Wolf(int a, int b, int move) {
            super(a, b, move);
            energy = parameters.after_split_en_Wolf;
        }
    }


    int[] getcords(int x, int y, Agent[][] Field) { //1 вправо, 2 вниз, 3 влево, 4 вверх, если что-то занято то следующее, если 4 занято то возвращаем -1 -1
        int scenario = getRandomNumber(4) + 1;
        for (int i = 0; i < 4; i++) {
            switch (scenario) {
                case 1:
                    if (x != parameters.Field_size-1) {
                        if (Field[x + 1][y] == null)//на 1 меньше тк координаты и место в списке не совпадают
                            return new int[]{x + 1, y};
                    }
                case 2:
                    if (y != parameters.Field_size-1) {
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

    void showField(Agent[][] Field, int move, int end) {
        Window app = Window.getInstance();

        // Если нажали Стоп — ждём
        while (!app.isRunning()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        String[][] symbols =
                new String[parameters.Field_size][parameters.Field_size];

        for (int i = 0; i < parameters.Field_size; i++) {
            for (int j = 0; j < parameters.Field_size; j++) {

                if (Field[i][j] instanceof Grass) {
                    symbols[i][j] = "\uD83C\uDF31";
                } else if (Field[i][j] instanceof Antelope) {
                    symbols[i][j] = "\uD83E\uDD8C";
                } else if (Field[i][j] instanceof Wolf) {
                    symbols[i][j] = "\uD83D\uDC3A";
                } else {
                    symbols[i][j] = " ";
                }
            }
        }

        app.updateField(symbols);
        app.updateMove(move);
        if (end != 0){
            app.end(end);
        }

        try {
            Thread.sleep(app.getSleepTime());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int getRandomNumber(int limit) //Функция возвращает целые числа из диапазона 0...limit-1
    {
        return (int) (Math.random() * limit);
    }
}