package pieces;

import java.util.LinkedList;
import xadrezonator.*;

public class Rook extends Piece {

    public Rook() {
        super();
    }

    public Rook(Player owner, int color, Position position, String id) {
        super(owner,color,position, id);
        setName("Torre");
    }

    public int getPieceType(){
        return PiecesConstants.ROOK_TYPE;
    }

    @Override
    public LinkedList<Position> possibleDestinationPositions() {
        LinkedList<Position> positions = new LinkedList<Position>();
        positions.add(getPosition());//Adiciona a posição corrente ao head da List

        int x0 = getPosition().getX();
        int y0 = getPosition().getY();
        int counter;

        //Loop que cuida dos movimentos para o Norte
        counter = 0;
        while (true) {
            counter++;
            if (y0+counter <= 7) {
                positions.add(new Position(x0,y0+counter));
            } else {
                break;
            }
        }

        //Loop que cuida dos movimentos para o Leste
        counter = 0;
        while (true) {
            counter++;
            if (x0+counter <= 7) {
                positions.add(new Position(x0+counter,y0));
            } else {
                break;
            }
        }

        //Loop que cuida dos movimentos pro Sul
        counter = 0;
        while (true) {
            counter++;
            if (y0-counter >= 0) {
                positions.add(new Position(x0,y0-counter));
            } else {
                break;
            }
        }

        //Loop que cuida dos movimentos pro Oeste
        counter = 0;
        while (true) {
            counter++;
            if (x0-counter >= 0) {
                positions.add(new Position(x0-counter,y0));
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


        if (yMove > 0) {
            for (int i = 1; i < yMove; i++) {
                positions.add(new Position(x0,y0+i));
            }
        } else {
            if (yMove < 0) {
                for (int i = 1; i < Math.abs(yMove); i++) {
                    positions.add(new Position(x0,y0-i));
                }
            } else {
                if (xMove > 0) {
                    for (int i = 1; i < xMove; i++) {
                        positions.add(new Position(x0+i,y0));
                    }
                } else {
                    if (xMove < 0) {
                        for (int i = 1; i < Math.abs(xMove); i++) {
                            positions.add(new Position(x0-i,y0));
                        }
                    }
                }
            }
        }
        return positions;
    }

    @Override
    public Piece copy() {
        Rook rook = new Rook();
        rook.setId(this.getId());
        rook.setColor(this.getColor());
        rook.setName(this.getName());
        rook.setOwner(this.getOwner());
        rook.setPosition(new Position(getPosition().getX(), getPosition().getY()));
        if (hasMoved()) rook.pieceMoved();
        return rook;
    }



}
