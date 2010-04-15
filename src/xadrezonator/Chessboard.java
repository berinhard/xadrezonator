package xadrezonator;

import java.util.LinkedList;
import java.util.ListIterator;
import pieces.*;

public class Chessboard {

    private final int size = 8;
    private Piece[][] board;
    private Action lastAction; //Última jogada

    /**
     * Construtor que inicializa um tabuleiro vazio
     * Será usado pelo método copy que retorna uma
     * cópia do tabuleiro
     */
    public Chessboard() {
        this.board = new Piece[getSize()][getSize()];
    }

    /**
     * Construtor que dados dois jogadores inicializa o tabuleiro com todas as peças
     * nos locais corretos.
     * @param human Jogador
     * @param computer Computador
     */
    public Chessboard(Player human, Player computer) {
        this.board = new Piece[getSize()][getSize()];

        String id;

        //Definindo as torres
        //==============================================================================
        for (int i = 0; i <= 7; i += 7) {
            id = "wr" + i;
            Position position = new Position(i, 0);
            setPieceAtBoard(position, new Rook(human, Piece.WHITE, position, id));
        }
        for (int i = 0; i <= 7; i += 7) {
            id = "br" + i;
            Position position = new Position(i, 7);
            setPieceAtBoard(position, new Rook(computer, Piece.BLACK, position, id));
        }
        //==============================================================================

        //Definindo os cavalos
        //==============================================================================
        for (int i = 1; i <= 6; i += 5) {
            id = "wh" + i;
            Position position = new Position(i, 0);
            setPieceAtBoard(position, new Horse(human, Piece.WHITE, position, id));
        }
        for (int i = 1; i <= 6; i += 5) {
            id = "bh" + i;
            Position position = new Position(i, 7);
            setPieceAtBoard(position, new Horse(computer, Piece.BLACK, position, id));
        }
        //==============================================================================

        //Definindo os bispos
        //==============================================================================
        for (int i = 2; i <= 5; i += 3) {
            id = "wb" + i;
            Position position = new Position(i, 0);
            setPieceAtBoard(position, new Bishop(human, Piece.WHITE, position, id));
        }
        for (int i = 2; i <= 5; i += 3) {
            id = "bb" + i;
            Position position = new Position(i, 7);
            setPieceAtBoard(position, new Bishop(computer, Piece.BLACK, position, id));
        }
        //==============================================================================


        //Definindo as rainhas
        //==============================================================================
        {
            Position position = new Position(3, 0);
            id = "wq";
            setPieceAtBoard(position, new Queen(human, Piece.WHITE, position, id));
        }
        {
            Position position = new Position(3, 7);
            id = "bq";
            setPieceAtBoard(position, new Queen(computer, Piece.BLACK, position, id));
        }
        //==============================================================================

        //Definindo os reis
        //==============================================================================
        {
            Position position = new Position(4, 0);
            id = "wk";
            setPieceAtBoard(position, new King(human, Piece.WHITE, position, id));
        }
        {
            Position position = new Position(4, 7);
            id = "bk";
            setPieceAtBoard(position, new King(computer, Piece.BLACK, position, id));
        }
        //==============================================================================

        //Definindo os peões
        //==============================================================================
        for (int i = 0; i <= 7; i++) {
            id = "wp" + i;
            Position position = new Position(i, 1);
            setPieceAtBoard(position, new Pawn(human, Piece.WHITE, position, id));
        }
        for (int i = 0; i <= 7; i++) {
            id = "bp" + i;
            Position position = new Position(i, 6);
            setPieceAtBoard(position, new Pawn(computer, Piece.BLACK, position, id));
        }
        //==============================================================================

        //Definindo as posições vazias
        //==============================================================================
        for (int i = 0; i <= 7; i++) {
            for (int j = 2; j <= 5; j++) {
                setPieceAtBoard(new Position(i, j), null);
            }
        }
        //==============================================================================
    }

    /**
     * Verifica se uma posição está vazia
     * @param position Posição
     * @return booleano informando se a posição está vazia
     */
    public boolean isEmpty(Position position) {
        if (getPiece(position) == null) {
            return true;
        }
        return false;
    }

    /**
     * Função que remove as posições ocupadas de um conjunto de posições
     * É usada quando um conjunto de posições destino válidas para uma peça é retornado e precisa ser "limpado"
     * das posições ocupadas por peças do próprio jogador
     * @param positions Lista de posições destino possíveis de uma peça (a posição atual é o primeiro
     * elemento da lista)
     */
    private void removeOccupiedPositions(LinkedList<Position> positions) {
        ListIterator<Position> iterator = positions.listIterator();
        Piece piece = getPiece(iterator.next());

        while (iterator.hasNext()) {
            Position position = iterator.next();
            if (!isEmpty(position)) {
                if (!getPiece(position).isEnemy(piece)) {
                    //Tem uma peça do próprio jogador no destino, então remove este destino
                    iterator.remove();
                } else { //Se for inimiga
                    if (piece instanceof Pawn) {
                        int xMove = Math.abs(position.getX() - positions.getFirst().getX());
                        int yMove = Math.abs(position.getY() - positions.getFirst().getY());
                        if ((xMove != 1) || (yMove != 1)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    /**
     * Método que remove as posições destino inatingíveis de uma peça
     * As posições inatingiveís são as que não podem ser alcançadas por terem peças na frente
     * @param positions Lista de posições destino possíveis de uma peça (a posição atual é o primeiro
     * elemento da lista)
     */
    private void removeUnreachablePositions(LinkedList<Position> positions) {
        ListIterator<Position> iterator = positions.listIterator();
        Piece piece = getPiece(positions.getFirst());
        iterator.next(); //Passa a primeira posição que é a posição da peça
        while (iterator.hasNext()) {
            Position position = iterator.next();
            if (!freeTrace(piece, position)) {
                iterator.remove();
            }
        }
    }

    /**
     * Este é o método que retorna as posições destinos válidas no tabuleiro
     * Recebe as posições possíveis da peça como parâmetro e remove as ocupadas e as inatingíveis.
     * Trata também o caso do peão que só pode se movimentar na diagonal caso esteja atacando
     * @param positions Lista de posições destino possíveis de uma peça (a posição atual é o primeiro
     * elemento da lista)
     * @return Lista de posições destino válidas no tabuleiro
     */
    public LinkedList<Position> validDestinationPositions(LinkedList<Position> positions) {
        removeOccupiedPositions(positions);
        removeUnreachablePositions(positions);
        Piece movingPiece = getPiece(positions.getFirst());

        //Trata o caso especial do peão que não pode se movimentar na diagonal caso não seja para um ataque
        if (movingPiece instanceof Pawn) {
            ListIterator<Position> iterator = positions.listIterator(); //posições destino possíveis
            iterator.next(); //passa da atual que é o peão
            while (iterator.hasNext()) {
                Position position = iterator.next();
                //se a posição destino estiver vazia, xMove deve ser igual a zero
                if (isEmpty(position)) {
                    int xMove = position.getX() - positions.getFirst().getX();
                    if (xMove != 0) {
                        iterator.remove();
                    }
                }
            }
        }
        positions.removeFirst();
        return positions;
    }

    /**
     * Método que retorna o conjunto de posições que formam o caminho de uma peça
     * até uma dada posição no tabuleiro
     * @param piece Peça
     * @param pos Posição para a qual se quer ir
     * @return A lista de posições no caminho até pos
     */
    public LinkedList<Position> wayTo(Piece piece, Position pos) {
        return piece.wayTo(pos);
    }

    /**
     * Método mais importante da classe
     * Realiza a movimentação de uma peça dada a sua posição inicial para uma
     * posição final também especificada
     * @param initialPosition Posição origem
     * @param destinationPosition Posição destino
     */
    public void moveFromTo(Position initialPosition, Position destinationPosition) {

        Piece attackedPiece = null;

        //Verifica qual é a peça
        Piece pieceMovement = getPiece(initialPosition);

        //Caso ela seja um peão, deve verificar se o peão utilizou o bônus da primeir movimentação
        if (pieceMovement instanceof Pawn && !pieceMovement.hasMoved()) {
            Pawn pawn = (Pawn) pieceMovement;
            int y0 = initialPosition.getY();
            int y = destinationPosition.getY();
            int variation = y - y0;
            variation = Math.abs(variation);
            if (variation == 2) {
                pawn.setBonusMove(true);
            }
        }
        //Se a posição destino não estiver vazia
        //Remove a peça comida das peças do jogador a quem ela pertencia
        if (!isEmpty(destinationPosition)) {
            attackedPiece = getPiece(destinationPosition);
        }


        //Completa a jogada fazendo a movimentação da peça
        Piece pieceInMovement = getPiece(initialPosition);
        pieceInMovement.pieceMoved();
        setPieceAtBoard(destinationPosition, pieceInMovement);
        setPieceAtBoard(initialPosition, null);
        this.lastAction = new Action(pieceMovement, destinationPosition,
                initialPosition, attackedPiece);
    }

    /**
     * Função que retorna se o caminho entre uma peça e uma posição está livre, ou seja,
     * todas as suas posições estão vazias
     * @param piece Peça
     * @param destinPosition Posição destino
     * @return booleano informando se o caminho para destinPosition está livre
     */
    private boolean freeTrace(Piece piece, Position destinPosition) {
        ListIterator<Position> iterator = piece.wayTo(destinPosition).listIterator();
        while (iterator.hasNext()) {
            Position position = iterator.next();
            if (!isEmpty(position)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Este método retorna o código da configuração atual do tabuleiro
     * @return Código inteiro da configuração do tabuleiro
     */
    public int configurationCode() {
        String chessBoard = new String("");
        int chessBoardSize = getSize() - 1;

        for (int i = chessBoardSize; i >= 0; i--) {
            for (int j = 0; j <= chessBoardSize; j++) {
                Position currentPosition = new Position(j, i);
                //Para o hash só importa as posiçẽos que não estejam vazias assim
                //Será importante termos na string a peça E a posição dela, não só a peça
                if (!this.isEmpty(currentPosition)) {
                    chessBoard += getPiece(currentPosition).getId();
                    chessBoard += "(" + String.valueOf(j) + "," + String.valueOf(i) + ")";
                }
            }
        }
        return chessBoard.hashCode();
    }

    /*############################## GETTER E SETTER ################################################*/
    /**
     * Adiciona uma peça no tabuleiro, dadas a peça e a posição
     * @param position Posição na qual se deseja inserir a peça
     * @param piece A peça a ser inserida
     */
    public void setPieceAtBoard(Position position, Piece piece) {
        int x = position.getX();
        int y = position.getY();
        this.board[x][y] = piece;
        if (piece != null) {
            piece.setPosition(position);
        }
    }

    /**
     * Função que retorna a peça que ocupa uma posição dada no tabuleiro
     * @param position Posição
     * @return A peça na posição
     * @throws NullPointerException
     * @throws ArrayIndexOutOfBoundsException
     */
    public Piece getPiece(Position position) throws NullPointerException, ArrayIndexOutOfBoundsException {
        try {
            int x = position.getX();
            int y = position.getY();
            Piece piece = this.board[x][y];
            return piece;
        } catch (NullPointerException e1) {
            throw e1; //Joga uma exceção se a posição tá vazia
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw e2;
        }

    }

    public int getSize() {
        return size;
    }

    public Action getLastAction() {
        return lastAction;
    }

    public void setLastAction(Action lastAction) {
        this.lastAction = lastAction;
    }

    /**
     * Metodo que retorna uma copia do tabuleiro
     * Necessario pois java so usa ponteiro com objetos
     * @return Cópia do tabuleiro
     */
    public Chessboard copy() {
        Chessboard chessboard = new Chessboard();
        //Varre o tabuleiro copiando as peças
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                Piece piece;
                Piece testPiece;
                //a peça a ser amarnazenada recebe uma copia da peça na posição i,j
                try {
                    testPiece = getPiece(new Position(i, j));
                    piece = testPiece.copy();
                    //piece = getPiece(new Position(i,j)).copy();
                } catch (NullPointerException ex) {
                    piece = null;
                }
                chessboard.setPieceAtBoard(new Position(i, j), piece);
            }
        }
        return chessboard;
    }
}
