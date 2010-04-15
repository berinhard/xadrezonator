package pieces;

import java.util.LinkedList;
import xadrezonator.*;

public class Bishop extends Piece {

    public Bishop() {
        super();
    }

    public Bishop(Player owner, int color, Position position, String id) {
        super(owner,color,position,id);
        setName("Bispo");
    }

     public int getPieceType(){
        return PiecesConstants.BISHOP_TYPE;
    }

    @Override
    public LinkedList<Position> possibleDestinationPositions() {
        LinkedList<Position> positions = new LinkedList<Position>();
        positions.add(getPosition());//Adiciona a posição corrente ao head da List

        int x0 = getPosition().getX();
        int y0 = getPosition().getY();
        int counter;

        //Loop que cuida das posições à Nordeste
        counter = 0;
        while (true) {
            counter++;
            if ((x0+counter <= 7) && (y0+counter <= 7)) {
                positions.add(new Position(x0+counter, y0+counter));
            } else {
                break;
            }
        }

        //Loop que cuida das posições à Sudoeste
        counter = 0;
        while (true) {
            counter++;
            if ((x0-counter >= 0) && (y0-counter >= 0)) {
                positions.add(new Position(x0-counter,y0-counter));
            } else {
                break;
            }
        }

        //Loop que cuida das posições à Sudeste
        counter = 0;
        while (true) {
            counter++;
            if ((x0+counter <= 7) && (y0-counter >= 0)) {
                positions.add(new Position(x0+counter,y0-counter));
            } else {
                break;
            }
        }

        //Loop que cuida das posições à Noroeste
        counter = 0;
        while (true) {
            counter++;
            if ((x0-counter >= 0)&&(y0+counter <= 7)) {
                positions.add(new Position(x0-counter,y0+counter));
            } else {
                break;
            }
        }

        return positions;
    }

    @Override
    public LinkedList<Position> wayTo(Position position) {

        //Conjunto a ser retornado
        LinkedList<Position> positions = new LinkedList<Position>();

        //Primeiro, verifica-se se a posição pode ser atingida pela peça
        if (!position.isInList(possibleDestinationPositions())) {
            //Se não puder, retorna um conjunto vazio
            return positions;
        }

        int x0 = getPosition().getX();
        int y0 = getPosition().getY();

        int yMove = position.getY() - y0;
        int xMove = position.getX() - x0;

        if ((xMove > 0) && (yMove > 0)) {
            for (int i = 1; i < xMove; i++) {
                positions.add(new Position(x0+i,y0+i));
            }
        } else {
            if ((xMove > 0) && (yMove < 0)) {
                for (int i = 1; i < xMove; i++) {
                    positions.add(new Position(x0+i,y0-i));
                }
            } else {
                if ((xMove < 0) && (yMove > 0)) {
                    for (int i = 1; i < yMove; i++) {
                        positions.add(new Position(x0-i,y0+i));
                    }
                } else {
                    if ((xMove < 0) && (yMove < 0)) {
                        for (int i = 1; i < Math.abs(xMove); i++) {
                            positions.add(new Position(x0-i,y0-i));
                        }
                    }
                }
            }
        }
        return positions;
    }

    @Override
    public Piece copy() {
        Bishop bishop = new Bishop();
        bishop.setId(this.getId());
        bishop.setColor(this.getColor());
        bishop.setName(this.getName());
        bishop.setOwner(this.getOwner());
        bishop.setPosition(new Position(getPosition().getX(), getPosition().getY()));
        if (hasMoved()) bishop.hasMoved();
        return bishop;
    }


}
