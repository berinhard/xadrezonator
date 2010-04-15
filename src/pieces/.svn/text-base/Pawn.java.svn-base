package pieces;

import java.util.LinkedList;
import xadrezonator.*;

public class Pawn extends Piece {

    private boolean bonusMove; //Booleano que informa se o peão andou duas casas no primeiro movimento

    //Construtor
    public Pawn() {
        super();
    }

    public Pawn(Player owner, int color, Position position, String id) {
        super(owner, color, position, id);
        setName("Peão");

    }

    public void setBonusMove(boolean bonusMove) {
        this.bonusMove = bonusMove;
    }

    public boolean didBonusMove() {
        return bonusMove;
    }

     public int getPieceType(){
        return PiecesConstants.PAWN_TYPE;
    }

    @Override
    public LinkedList<Position> possibleDestinationPositions() {
        LinkedList<Position> positions = new LinkedList<Position>();
        positions.add(getPosition());

        int x0 = getPosition().getX();
        int y0 = getPosition().getY();

        if (isWhite()) {
            if (y0 + 1 <= 7) {
                positions.add(new Position(x0, y0 + 1));

                if (x0 - 1 >= 0) {
                    positions.add(new Position(x0 - 1, y0 + 1));
                }

                if (x0 + 1 <= 7) {
                    positions.add(new Position(x0 + 1, y0 + 1));
                }
            }

            if (!hasMoved()) {
                positions.add(new Position(x0, y0 + 2));
            }
        } else if (isBlack()) {
            if (y0 - 1 >= 0) {
                positions.add(new Position(x0, y0 - 1));

                if (x0 - 1 >= 0) {
                    positions.add(new Position(x0 - 1, y0 - 1));
                }

                if (x0 + 1 <= 7) {
                    positions.add(new Position(x0 + 1, y0 - 1));
                }
            }

            if (!hasMoved()) {
                positions.add(new Position(x0, y0 - 2));
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
        int xMove = position.getX() - x0;
        int yMove = position.getY() - y0;

        if (hasMoved()) {
            return positions;
        }
        if (xMove == 0) {
            if (yMove > 1) {
                positions.add(new Position(x0, y0 + 1));
            } else {
                if (yMove < 1) {
                    positions.add(new Position(x0, y0 - 1));
                }
            }
        }
        return positions;
    }

    @Override
    public Piece copy() {
        Pawn pawn = new Pawn();
        pawn.setId(this.getId());
        pawn.setColor(this.getColor());
        pawn.setName(this.getName());
        pawn.setOwner(this.getOwner());
        pawn.setPosition(new Position(getPosition().getX(), getPosition().getY()));
        pawn.setBonusMove(this.didBonusMove());
        if (hasMoved()) pawn.pieceMoved();
        return pawn;
    }
}
