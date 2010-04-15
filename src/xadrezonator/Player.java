package xadrezonator;

import java.util.LinkedList;
import java.util.ListIterator;
import pieces.King;
import pieces.Piece;

/**
 * Classe para representar um jogador
 */

public class Player {

    private String name; //Nome do jogador

    /**
     * Construtor
     * @param name Nome do jogador
     */
    public Player(String name) {
        setName(name);
    }

    //GETTER E SETTER
    //=========================================================================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //=========================================================================

    /**
     * Método que retorna as peças do jogador dado o tabuleiro
     * Varre o tabuleiro inteiro e, caso a peça pertença a ele,
     * adiciona no conjunto que será retornado
     * @param chessboard Tabuleiro
     * @return Lista de peças do jogador
     */
    public LinkedList<Piece> getPieces(Chessboard chessboard) {
        LinkedList<Piece> pieces = new LinkedList<Piece>();
        for (int i = 0; i < chessboard.getSize(); i++) {
            for (int j = 0; j < chessboard.getSize(); j++) {
                try {
                    if (chessboard.getPiece(new Position(i,j)).belongsTo(this)) {
                        pieces.add(chessboard.getPiece(new Position(i,j)));
                    }
                } catch (NullPointerException ex) {
                    //Nao faz nada, apenas continua no loop
                }
            }
        }
        return pieces;
    }

    /**
     * Método que retorna o rei do jogador dado o tabuleiro
     * @param chessboard Tabuleiro
     * @return O rei do jogador
     * @throws NullPointerException
     */
    public Piece getKing(Chessboard chessboard) throws NullPointerException {
        ListIterator<Piece> iterator = getPieces(chessboard).listIterator();

        while (iterator.hasNext()) {
            Piece piece = iterator.next();
            if (piece instanceof King) {
                return piece;
            }
        }
        throw new NullPointerException();
    }
}
