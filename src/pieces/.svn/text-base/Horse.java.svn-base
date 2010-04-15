package pieces;

import java.util.LinkedList;
import xadrezonator.*;

public class Horse extends Piece {

    //Construtores
    public Horse() {
        super();
    }

    public Horse(Player owner, int color, Position position, String id) {
        super(owner,color,position,id);
        setName("Cavalo");
    }

     public int getPieceType(){
        return PiecesConstants.HORSE_TYPE;
    }

    @Override
    public LinkedList<Position> possibleDestinationPositions() {
        LinkedList<Position> positions = new LinkedList<Position>();
        positions.add(getPosition()); //Adiciona a posição corrente ao head da List

        if (getPosition().getX() > 1) {
            if (getPosition().getY() < 7)
                positions.add(new Position(getPosition().getX() - 2, getPosition().getY() + 1));

            if (getPosition().getY() > 0)
                positions.add(new Position(getPosition().getX() - 2, getPosition().getY() - 1));
        }
        
        if (getPosition().getX() > 0) {
            if (getPosition().getY() < 6)
                positions.add(new Position(getPosition().getX() - 1, getPosition().getY() + 2));
            
            if (getPosition().getY() > 1)
                positions.add(new Position(getPosition().getX() - 1, getPosition().getY() - 2));
        }

        if (getPosition().getX() < 6) {
            if (getPosition().getY() < 7)
                positions.add(new Position(getPosition().getX() + 2, getPosition().getY() + 1));

            if (getPosition().getY() > 0)
                positions.add(new Position(getPosition().getX() + 2, getPosition().getY() - 1));
        }
        
        if (getPosition().getX() < 7) {
            if (getPosition().getY() < 6)
                positions.add(new Position(getPosition().getX() + 1, getPosition().getY() + 2));

            if (getPosition().getY() > 1)
                positions.add(new Position(getPosition().getX() + 1, getPosition().getY() - 2));
        }

        return positions;
    }

    @Override
    public LinkedList<Position> wayTo(Position position){
        return new LinkedList<Position>();
    }

    @Override
    public Piece copy() {
        Horse horse = new Horse();
        horse.setId(this.getId());
        horse.setColor(this.getColor());
        horse.setName(this.getName());
        horse.setOwner(this.getOwner());
        horse.setPosition(new Position(getPosition().getX(), getPosition().getY()));
        if (hasMoved()) horse.pieceMoved();
        return horse;
    }
}
