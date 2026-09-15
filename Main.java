import java.util.*;

public static int getRandomNumber(int limit) //Функция возвращает целые числа из диапазона 1...limit
{
    return (int) (Math.random() * limit)+1;
}
public static void main() {
    Agent[][] Field = new Agent[100][100];
    for (int i = 0; i < 10; i++) {//создаем траву

    }
    for (int i = 0; i < 10; i++) {//создаем антилоп

    }
    for (int i = 0; i < 10; i++) {//создаем волков

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
public class Grass extends Agent{

    @Override
    public void action(Agent[][] Field){//тут агент оценивает ситуацию, принимает решение, получает или теряет энергию, вызывает сплит или мувмент если надо, после сплита добавляет нового агента на поле

    }
    @Override
    public void movement() {
    //Трава не двигается, конкретно тут ничего не происходит
    }
    @Override
    public Agent splitting(){// анализируется поле вокруг, рандомно выбирается клетка и происходит деление, возвращается новый тип, которого экшн должен добавить на поле
        if (energy >= 11){
            energy = 6;
            return new Grass(a, b);
        }
    }

    public Grass(int a, int b){
        super(a, b);
    }

}