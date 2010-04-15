package pieces;

import java.util.LinkedList;
import xadrezonator.*;

public class King extends Piece {

    public King() {
        super();
    }

    public King(Player owner, int color, Position position, String id) {
        super(owner, color, position, id);
        setName("Rei");
    }


    public int getPieceType(){
        return PiecesConstants.KING_TYPE;
    }

    @Override
    public LinkedList<Position> possibleDestinationPositions() {
        LinkedList<Position> positions = new LinkedList<Position>();
        positions.add(getPosition()); //Adiciona a posição corrente ao head da List

        if (getPosition().getX()-1 >= 0) {
            if (getPosition().getY()+1 <= 7)
                positions.add(new Position(getPosition().getX() - 1, getPosition().getY() + 1));

            positions.add(new Position(getPosition().getX() - 1, getPosition().getY()));

            if (getPosition().getY()-1 >= 0)
                positions.add(new Position(getPosition().getX() - 1, getPosition().getY() - 1));
        }

        if (getPosition().getY()+1 <= 7)
            positions.add(new Position(getPosition().getX(), getPosition().getY() + 1));
        
        if (getPosition().getY()-1 >= 0)
            positions.add(new Position(getPosition().getX(), getPosition().getY() - 1));

        if (getPosition().getX()+1 <= 7) {
            if (getPosition().getY()+1 <= 7)
                positions.add(new Position(getPosition().getX() + 1, getPosition().getY() + 1));

            positions.add(new Position(getPosition().getX() + 1, getPosition().getY()));

            if (getPosition().getY()-1 >= 0)
                positions.add(new Position(getPosition().getX() + 1, getPosition().getY() - 1));
        }

        return positions;
    }

    @Override
    public LinkedList<Position> wayTo(Position position) {
        return new LinkedList<Position>();
    }

    @Override
    public Piece copy() {
        King king = new King();
        king.setId(this.getId());
        king.setColor(this.getColor());
        king.setName(this.getName());
        king.setOwner(this.getOwner());
        king.setPosition(new Position(getPosition().getX(), getPosition().getY()));
        if (hasMoved()) king.pieceMoved();
        return king;
    }
}
