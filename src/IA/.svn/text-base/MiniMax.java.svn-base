/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IA;

import java.util.LinkedList;
import xadrezonator.*;
import java.util.ListIterator;
import java.util.ArrayList;
import pieces.*;

/**
 *
 * @author USUÁRIO
 */
public class MiniMax {
    // todos os destinos de uma peça
    public LinkedList<Position> allDestinations(Position pos, Game game) {
        LinkedList<Position> positions = game.getValidDestinationPositions(game.getPieceAtChessboard(pos));
        return positions;
    }

    //copia de peça para um tabuleiro
    public Piece copyPieces(Piece piece, Position pos, Player p, int color) {
        Piece copy = null;
        copy = piece.copy();
        return copy;

    }

    //copia o tabuleiro para uma nova instancia
    public Chessboard getActualChessboard(Chessboard cb) {
        Chessboard currentcb = cb.copy();
        return currentcb;
    }

    //retorna todas os movimentos possiveis de um jogador
    public ArrayList<move> getPlayerLegalMoves(Game game, int color) {
        Chessboard cb = game.getChessboard();
        LinkedList<Position> parcial;
        ArrayList<move> legalmoves = new ArrayList<move>(200);

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                Position pos = new Position(i, j);
                if (null != cb && null != cb.getPiece(pos)) {  //se tiver peça na posição
                    if (cb.getPiece(pos).getColor() == color) {
                        parcial = allDestinations(pos, game);
                        ListIterator<Position> pieceIterator = parcial.listIterator();
                        while (pieceIterator.hasNext()) {
                            Position pos1 = pieceIterator.next();
                            move moviment = new move(pos, pos1);
                            legalmoves.add(moviment);
                        }
                    }
                }

            }
        }
        return legalmoves;
    }

    //função do minimax retorna um "move" que contém posição inicial e posição
    //destino, depth eh a profundidade da busca
    //depth 3 demora bastante, com depth 2 mostra algum desafio mas nao consegue ver
    //possibilidades de check mate
    //Com qualquer profundidade e da melhor maneira possivel segundo as heuristicas
    //ver 1.5
    public move miniMax(Chessboard chess_board, Game game, int current_color, int opponent_color, int depth) {
        ArrayList<move> legalMoves = getPlayerLegalMoves(game, opponent_color);
        int highest_seen_value = Integer.MIN_VALUE;
        EvalutionFunction evalue = new EvalutionFunction();
        move best_move = null;
        for (int i = 0; i <= legalMoves.size(); i++) {
            int value = 0;
            try {
                Player pla1 = new Player("play1");
                Player pla2 = new Player("play2");
                Chessboard b = new Chessboard(pla1, pla2);
                move move = legalMoves.get(i);
                b = getActualChessboard(chess_board);
                b.moveFromTo(move.getposini(), move.getposfin());
                Game tempgame = new Game("ae");
                tempgame.setChessboard(b);
                int bestboardvalue= evalue.EvalutionFunction(b, opponent_color,game);
                value = minimaxValue(b, game,opponent_color,current_color, depth - 1,bestboardvalue);

                if (current_color == 0) {
                    if (value > highest_seen_value) {
                        highest_seen_value = value;
                        best_move = move;
                    }
                }
            } catch (Exception e) {
            }
        }
        return best_move;
    }

    private int minimaxValue(
            Chessboard chess_board,
            Game game,
            int current_color,
            int opponent_color,
            int depth,
            int bestboardvalue){
        EvalutionFunction evalue = new EvalutionFunction();
        int jogadaatual = bestboardvalue;
        int ret_val = 0;
        if (depth == 0) {
            ret_val = bestboardvalue;
        }else if (current_color == 0) {
            ArrayList<move> legalMoves = new ArrayList<move>();
            Game tempgame = new Game("ae");
            tempgame.setChessboard(chess_board);
            legalMoves = getPlayerLegalMoves(tempgame, opponent_color);
            int highest_seen_value = Integer.MIN_VALUE;
            int oldvalue= Integer.MIN_VALUE;
            int value = 0;
            for (int i = 0; i < legalMoves.size(); i++) {
                try {
                    Player pla1 = new Player("play1");
                    Player pla2 = new Player("play2");
                    Chessboard b = new Chessboard(pla1, pla2);
                    b = getActualChessboard(chess_board);
                    move move = legalMoves.get(i);
                    b.moveFromTo(move.getposini(), move.getposfin());
                    value = evalue.EvalutionFunction(b, opponent_color,game);
                    tempgame.setChessboard(b);
                    if (depth>=2){
                        int valorjogada= jogadaatual + evalue.EvalutionFunction(b, opponent_color,game);
                        value= minimaxValue(b, game, opponent_color, current_color, depth - 1,valorjogada);
                        if (value>oldvalue) {
                            oldvalue=value;
                            bestboardvalue= value;
                    }
                    }else if (value > highest_seen_value) {
                        if (highest_seen_value != Integer.MIN_VALUE){
                            bestboardvalue= bestboardvalue-oldvalue;
                        }
                        oldvalue=value;
                        highest_seen_value = value;
                        bestboardvalue= bestboardvalue+value;
                    }
                } catch (Exception e) {
                }
            }
            ret_val = bestboardvalue;
        } else {
            ArrayList<move> legalMoves = new ArrayList();
            Game tempgame = new Game("ae");
            tempgame.setChessboard(chess_board);
            legalMoves = getPlayerLegalMoves(tempgame, opponent_color);
            int highest_seen_value = Integer.MIN_VALUE;
            int oldvalue= Integer.MAX_VALUE;
            int value = 0;
            for (int i = 0; i < legalMoves.size(); i++) {
                try {
                    Player pla1 = new Player("play1");
                    Player pla2 = new Player("play2");
                    Chessboard b = new Chessboard(pla1, pla2);
                    b = getActualChessboard(chess_board);
                    move move = legalMoves.get(i);
                    b.moveFromTo(move.getposini(), move.getposfin());
                    tempgame.setChessboard(b);
                    value= evalue.EvalutionFunction(b, opponent_color,game);
                if (depth>=2){
                        int valorjogada =jogadaatual - evalue.EvalutionFunction(b, opponent_color,game);
                        value= minimaxValue(b, game, opponent_color, current_color, depth - 1,valorjogada);
                        if (value<oldvalue) {
                                oldvalue=value;
                                bestboardvalue= value;
                        }
                }else if (value > highest_seen_value) {
                    if (highest_seen_value != Integer.MIN_VALUE){
                         bestboardvalue= bestboardvalue+oldvalue;
                    }
                        oldvalue=value;
                        highest_seen_value = value;
                        bestboardvalue= bestboardvalue-value;
                }
               }catch (Exception e) {
               }
            }
            ret_val = bestboardvalue;
        }

        return ret_val;
    }
}