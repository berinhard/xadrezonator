/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IA;


import java.util.LinkedList;
import java.util.ListIterator;
import xadrezonator.Position;


public class move {
 private Position posini;
 private Position posfin;

    public move(Position posini,Position posfin) {
        this.posini = posini;
        this.posfin = posfin;
    }

    public Position getposini(){
        return this.posini;
    }

    public Position getposfin(){
        return this.posfin;
    }



    public int getX(Position pos) {
        return pos.getX();
    }

    public int getY(Position pos) {
        return pos.getY();
    }


    public boolean isInList (LinkedList<Position> list, Position pos){
        ListIterator<Position> listIterator = list.listIterator();
        while (listIterator.hasNext()){
            Position comparePosition = listIterator.next();
            if (pos.getX() == comparePosition.getX()
                       && pos.getY() == comparePosition.getY())
                return true;
        }
        return false;
    }
}


