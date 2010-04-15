package xadrezonator;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Classe que representa uma posição
 */

public class Position {
    private int x;
    private int y;
    
    public Position(){}

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Método que verifica se esta posição está em um conjunto de posições
     * @param list Lista de posições
     * @return booleano que informa se a posição está na lista
     */
    public boolean isInList (LinkedList<Position> list){
        ListIterator<Position> listIterator = list.listIterator();

        while (listIterator.hasNext()){
            Position comparePosition = listIterator.next();
            if (this.getX() == comparePosition.getX()
                       && this.getY() == comparePosition.getY())
                return true;
        }
        return false;
    }
}
