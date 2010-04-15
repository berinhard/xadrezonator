/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xadrezonator;

import pieces.Piece;

public class Action {

    private Piece piece;  //Peça a ser jogada
    private Integer value; //Valor da jogada
    private Position oldPosition;
    private Position newPosition;
    private Piece attackedPiece;

    public Action(Piece piece, Position oldPosition, Position newPosition, Piece attackedPiece) {
        this.piece = piece;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.attackedPiece = attackedPiece;
    }

    public Action(){
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Position getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Position newPosition) {
        this.newPosition = newPosition;
    }

    public Position getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(Position oldPosition) {
        this.oldPosition = oldPosition;
    }

    public boolean hasAnAttackedPiece(){
        if (attackedPiece != null)
            return true;
        return false;
    }

    public Piece getAttackedPiece() {
        return attackedPiece;
    }
    
}
