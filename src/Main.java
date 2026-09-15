import java.util.*;

public static int getRandomNumber(int limit) //Функция возвращает целые числа из диапазона 1...limit
{
    return (int) (Math.random() * limit)+1;
}
void main() {
    Agent[][] Field = new Agent[100][100];
    for (int i = 0; i < 10; i++) {//создаем траву
        boolean fl = true;
        while (fl) {
            int a = getRandomNumber(100);
            int b = getRandomNumber(100);
            if (Field[a - 1][b - 1] == null) {
                Field[a - 1][b - 1] = new Grass(a, b);
                fl = false;
            }
        }
    }
    for (int i = 0; i < 10; i++) {//создаем антилоп
        boolean fl = true;
        while (fl) {
            int a = getRandomNumber(100);
            int b = getRandomNumber(100);
            if (Field[a - 1][b - 1] == null) {
                Field[a - 1][b - 1] = new Antelope(a, b);
                fl = false;
            }
        }
    }
    for (int i = 0; i < 10; i++) {//создаем волков
        boolean fl = true;
        while (fl) {
            int a = getRandomNumber(100);
            int b = getRandomNumber(100);
            if (Field[a - 1][b - 1] == null) {
                Field[a - 1][b - 1] = new Wolf(a, b);
                fl = false;
            }
        }
    }
    boolean flag = true;

    while (flag){
        int counterGrass = 0;
        int counterAntelope = 0;
        int counterWolf = 0;
        for (int i = 0; i < 100; i++){
            for (int j = 0; j < 10; i++){


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
    public abstract void movement();
    public abstract Agent splitting();

}
public class Grass extends Agent {

    @Override
    public void action(Agent[][] Field) {//тут агент оценивает ситуацию, принимает решение, получает или теряет энергию, вызывает сплит или мувмент если надо
        //В случае травы сначала плюсуется энергия солнца, потом проверяется ее количество и делается сплит
        //Если сплит происходит, вызывается экшн у нового типули после его занесения в поле.Когда происходит сплит надо делать ++ в каунтер
        if (energy >= 11) {
            energy = 6;
            return new Grass(a, b);
        }
    }

    @Override
    public void movement() {
        //Трава не двигается, конкретно тут ничего не происходит
    }

    @Override
    public Agent splitting() {// анализируется поле вокруг, рандомно выбирается клетка и происходит деление, возвращается новый тип, которого экшн должен добавить на поле
        // тут определяется клетка спавна типули
        return new Grass(a, b);

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
    public void movement() {
        //У антелопы главная задача убежать от хищника, если его нет то трава
        //если нет травы то рандомное направление, главное чтобы не шла туда сюда,
        //Надо выбрать направление и пусть оно будет задано пока не найдет край поля или цель
        //Если во время ходов нашлась еда, то пробуем сплит
        //если во время погони от хищника по пути есть еда, выбираем путь с едой
        //после спавна типули вызывается его экшн чтобы он сходил
    }

    @Override
    public Agent splitting() {// анализируется поле вокруг, рандомно выбирается клетка и происходит деление, возвращается новый тип, которого экшн должен добавить на поле
        // тут определяется клетка спавна типули

        return new Antelope(a, b);

    }

    public Antelope(int a, int b) {
        super(a, b);
    }
}
public class Wolf extends Agent {

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
    public void movement() {
        //У Волка главная задача найти антелопу
        //если нет антелопы то рандомное направление, главное чтобы не шел туда сюда,
        //Надо выбрать направление и пусть оно будет задано пока не найдет край поля или цель
    }

    @Override
    public Agent splitting() {// анализируется поле вокруг, рандомно выбирается клетка и происходит деление, возвращается новый тип, которого экшн должен добавить на поле
        // тут определяется клетка спавна типули
        //после спавна типули вызывается его экшн чтобы он сходил
        return new Wolf(a, b);

    }

    public Wolf(int a, int b) {
        super(a, b);
    }
}