package pieces;

import java.util.LinkedList;
import java.util.ListIterator;
import xadrezonator.*;

/**
 * Classe que representa uma peça
 */

public abstract class Piece implements PiecesConstants {

    private String id; //identificador da peça
    private int color;
    private Player owner;
    private Position position;
    private boolean hasMoved;
    private String name;

    /**
     * Construtor vazio
     * Útil para a hora de copiar uma instância da peça
     */
    public Piece() {
    }

    /**
     * Construtor que inicializa a pela dada o seu dono, sua cor e sua posição
     * @param owner Dono da peça
     * @param color Cor da peça
     * @param position Posição da peça
     * @param id Identificador da peça
     */
    public Piece(Player owner, int color, Position position, String id) {
        setId(id);
        setOwner(owner);
        setColor(color);
        setPosition(position);
        hasMoved = false;
        name = "";
    }

    /**
     * Método abstrato que retorna o tipo da peça
     * @return Um inteiro representando o tipo da peça
     */
    public abstract int getPieceType();

    /**
     * Método que retorna as posições destino válidas para a peça
     * É importante afirmar que como a peça não tem informações de tabuleiro,
     * as posições podem estar ocupadas ou serem inatingíveis. Segue apenas
     * as regras de movimentação imaginando um tabuleiro vazio
     * @return A lista de posições possíveis para uma peça
     */
    public abstract LinkedList<Position> possibleDestinationPositions();

    /**
     * Método que retorna o conjunto de posições que contém o caminho da peça
     * até uma dada posição
     * @param position Posição destino
     * @return A lista de posições do caminho entre a peça e a Posição dada
     */
    public abstract LinkedList<Position> wayTo(Position position);

    /**
     * Método que retorna uma cópia da peça
     * @return A cópia da peça
     */
    public abstract Piece copy();

    /**
     * Método que verifica se a peça pertence a um jogador
     * @param player Jogador
     * @return booleano informando se a peça pertence à player
     */
    public boolean belongsTo(Player player) {
        return (player == owner); //Verifica se é o mesmo objeto
    }

    /**
     * Verifica se a peca é inimiga de outra
     * @param piece Peça para verificar se é inimiga
     * @return booleano informando se é inimiga
     */
    public boolean isEnemy(Piece piece) {
        if (piece.getOwner() == this.getOwner()) {
            return false;
        }
        return true;
    }

    /**
     * Verifica se a cor da peça é branca
     * @return booleano informando se é branca
     */
    public boolean isWhite() {
        return (getColor() == WHITE);
    }

    /**
     * Verifica se a cor da peça é preta
     * @return booleano informando se é preta
     */
    public boolean isBlack() {
        return (getColor() == BLACK);
    }

    //GETTERS E SETTERS
    //##########################################################################
    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getOwnerName() {
        return getOwner().getName();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void pieceMoved() {
        this.hasMoved = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    //##########################################################################

    /**
     * Método que verifica se uma peça está em uma lista
     * Este método é usado na parte de empate.
     * @param pieces Lista de peças
     * @return booleano informando se a peça pertence a lista de peças
     */
    public boolean isInList(LinkedList<Piece> pieces) {
        ListIterator<Piece> iterator = pieces.listIterator();

        while (iterator.hasNext()) {
            Piece piece = iterator.next();
            if (this.getId().equals(piece.getId())) {
                return true;
            }
        }
        return false;
    }
}
