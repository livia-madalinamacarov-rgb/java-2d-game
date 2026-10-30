package PaooGame;

/*
 * Clasa EnemyFactory este responsabilă pentru crearea inamicilor.
 * Ea centralizează instanțierea inamicilor în funcție de un tip primit ca text.
 */
public class EnemyFactory {
    /*
     * Creează și returnează un inamic în funcție de tipul primit.
     * Momentan, pentru tipul dog se creează un obiect Dog; pentru tipuri necunoscute se returnează null.
     */
    public static Entity createEnemy(Handler handler,String type, int x, int y) {
        if (type.equals("dog")) {
            return new Dog(handler ,x, y);
        }

        return null;
    }
}
