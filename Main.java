public static int getRandomNumber(int limit) //Функция возвращает целые числа из диапазона 0...limit-1
{
    return (int) (Math.random() * limit);
}
void main() {
    Agent[][] Field = new Agent[20][20];
    for (int i = 0; i < 10; i++) {//создаем траву
        boolean fl = true;
        while (fl) {
            int a = getRandomNumber(19);
            int b = getRandomNumber(19);
            if (Field[a][b] == null) {
                Field[a][b] = new Grass(a, b);
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
                Field[a][b] = new Antelope(a, b);
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
                Field[a][b] = new Wolf(a, b);
                fl = false;
            }
        }
    }
    boolean flag = true;
    int move = 0;
    while (flag){
        move ++;
        int counterGrass = 0;
        int counterAntelope = 0;
        int counterWolf = 0;
        for (int i = 0; i < 20; i++){
            for (int j = 0; j < 20; j++){
                if (Field[i][j] != null){
                    Field[i][j].action(Field);
                    showField(Field);
                    System.out.println(move);
                }
                if (Field[i][j] instanceof Grass){
                    counterGrass ++;
                }
                if (Field[i][j] instanceof Antelope){
                    counterAntelope ++;
                }
                if (Field[i][j] instanceof Wolf){
                    counterWolf ++;
                }
            }
        }
        if (counterGrass == 0){
            flag = false;
            System.out.println("Трава исчезла");
        }
        if (counterAntelope == 0){
            flag = false;
            System.out.println("Антелопы вымерли");
        }
        if (counterWolf == 0){
            flag = false;
            System.out.println("Волки вымерли");
        }

    }
    //тут типа while и условия для существования мира

    //внутри проходим по полю и по порядку вызываем экшн у всех, после каждого хода отображаем новое поле и делаем паузу небольшую
}
abstract class Agent{
    public int energy;
    public int x;
    public int y;
    public Agent(int x, int y){
        energy = 5;
        this.x = x;
        this.y = y;
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
            int [] newcords = getcords(x, y, Field);
            if (newcords[0] != -1){
                energy = 5;
                Field[newcords[0]][newcords[1]] = new Grass(newcords[0], newcords[1]);
                Field[newcords[0]][newcords[1]].action(Field);
                showField(Field);
            }
            else{
                energy = 10;
            }
        }
    }

    public Grass(int a, int b) {
        super(a, b);
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

    public Antelope(int a, int b) {
        super(a, b);
    }
}
public class Wolf extends Agent {

    @Override
    public void action(Agent[][] Field) {
        movement(Field);
        splitting(Field);
    }

    @Override
    public void movement(Agent[][] Field) {
        int[] closestAntelope = findAntelope(Field);
        if (closestAntelope[0] != -100){
            //Нашел антилопу
            //если одно из двух направлений заблокировано, выбираем автоматически второе
            //если оба направлений заблокированы, идем вдоль одной из стен в обратном направлении рандомно, пока не найдем выход в другое направление
            //добавить флаг чтобы не забывал что пытается выбраться из угла

            //Если оба направления свободны
            //Сравниваем модуль разницы иксов и модуль разницы игриков, что больше в ту сторону и двигаемся.
            //если иксы то смотрим справа или слева и делаем 1 шаг туда, аналогично с игриками
            // если одинаково, то рандомное число 1 или 2, 1 иксы, 2 игреки
        }
        else{
            int [] newcords = getcords(x, y, Field);
            if (newcords[0] != -1) {

                Field[newcords[0]][newcords[1]] = Field[x][y];
                Field[x][y] = null;
                x = newcords[0];
                y = newcords[1];
            }
        }

    }
    public int[] findAntelope(Agent[][] Field){//поиск примитивный, если 2 антелопы на одинак расстоянии то идет к первой найденной
        int[] closest = new int[]{-100, -100};
        for (int i = -3; i < 2; i++){
            for (int j = -3; j<2; j++){
                if (Field[this.x + i][this.y + j] instanceof Antelope){
                    if (Math.abs(i)+Math.abs(j) < Math.abs(closest[0])+Math.abs(closest[1])){
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

    public Wolf(int a, int b) {
        super(a, b);
    }
}

int[] getcords(int x, int y, Agent[][] Field){ //1 вправо, 2 вниз, 3 влево, 4 вверх, если что-то занято то следующее, если 4 занято то возвращаем -1 -1
    int scenario = getRandomNumber(4)+1;
    for (int i = 0; i < 4; i++) {
        switch (scenario) {
            case 1:
                if (x!= 19){
                    if (Field[x+1][y] == null)//на 1 меньше тк координаты и место в списке не совпадают
                        return new int[]{x+1, y};
                }
            case 2:
                if (y!= 19){
                    if (Field[x][y+1] == null)//на 1 меньше тк координаты и место в списке не совпадают
                        return new int[]{x, y+1};
                }
            case 3:
                if (x!= 0){
                    if (Field[x-1][y] == null)//на 1 меньше тк координаты и место в списке не совпадают
                        return new int[]{x-1, y};
                }
            case 4:
                if (y!= 0){
                    if (Field[x][y-1] == null)//на 1 меньше тк координаты и место в списке не совпадают
                        return new int[]{x, y-1};
                }
        }
        scenario = scenario % 4 + 1;
    }
    return new int[]{-1, -1};
}

static void clearConsole(){
    try {
        new ProcessBuilder("cmd", "/c", "cls")
                .inheritIO()
                .start()
                .waitFor();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
void showField(Agent[][] Field){

    clearConsole();
    for (int i = 0; i < 20; i++){
        for (int j = 0; j < 20; j++){
            if (Field[i][j] instanceof Grass){
                System.out.print("G ");
            }
            else if (Field[i][j] instanceof Antelope){
                System.out.print("A ");
            }
            else if (Field[i][j] instanceof Wolf){
                System.out.print("W ");
            }
            else {
                System.out.print("_ ");
            }
        }
        System.out.println();
    }
    try {
        Thread.sleep(100);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
    }
}

