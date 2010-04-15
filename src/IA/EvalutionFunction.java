package IA;

import xadrezonator.*;
import pieces.*;

public class EvalutionFunction {

    public int CountPieces(Chessboard cb, int color) {

        int quantPieces = 0;
        //conta quantas peças de uma cor
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                Position pos = new Position(i, j);
                if (null != cb && null != cb.getPiece(pos)) {  //se tiver peça na posição
                    if (cb.getPiece(pos).getColor() == color) {
                        quantPieces++;
                    }
                }
            }
        }
        return quantPieces;
    }

    public int CountSpecificPieces(Chessboard cb, int color, int piece) {
        int qtpiece = 0;

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                Position pos = new Position(i, j);
                if (null != cb.getPiece(pos)) { //se tiver peça na posição
                    if ((cb.getPiece(pos).getColor() == color) && //Compara cor
                            (cb.getPiece(pos).getPieceType() == piece)) { //Compara tipo
                        qtpiece++;
                    }
                }
            }
        }
        return qtpiece;
    }

    public int EvalutionFunction(Chessboard cb, int color, Game game) {

        int boardValue = 0; //valor do tabuleiro
        int numhorse, numrook, numqueen, numking, numpawn, numbishop = 0; //numero de cada peça

        int tempcolor = color;
        if (color == 1) {
            color = 0;
        } else {
            color = 1;
        }
        numpawn = CountSpecificPieces(cb, color, PiecesConstants.PAWN_TYPE);
        boardValue = boardValue + 100 * (8 - numpawn);
        numhorse = CountSpecificPieces(cb, color, PiecesConstants.HORSE_TYPE);
        boardValue = boardValue + 300 * (2 - numhorse);
        numbishop = CountSpecificPieces(cb, color, PiecesConstants.BISHOP_TYPE);
        boardValue = boardValue + 325 * (2 - numbishop);
        numrook = CountSpecificPieces(cb, color, PiecesConstants.ROOK_TYPE);
        boardValue = boardValue + 500 * (2 - numrook);
        numqueen = CountSpecificPieces(cb, color, PiecesConstants.QUEEN_TYPE);
        boardValue = boardValue + 900 * (1 - numqueen);
        numking = CountSpecificPieces(cb, color, PiecesConstants.KING_TYPE);
        boardValue = boardValue + 3000 * (1 - numking);
        color = tempcolor;
        //boardValue= boardValue+ evalutionPerFreePositions(cb,color,game);
        boardValue = boardValue + evalutionPerPositioning(cb, color, game);
        boardValue = boardValue + extraPointsForCheck(cb, game, color);
        return boardValue;
    }

    public int evalutionPerFreePositions(Chessboard cb, int color, Game game) {
        int value = 0;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                Position pos = new Position(i, j);
                if (null != cb.getPiece(pos)) { //se tiver peça na posição
                    if ((cb.getPiece(pos).getColor() == color)) {
                        int totalpos = game.getValidDestinationPositions(cb.getPiece(pos)).size();
                        switch (cb.getPiece(pos).getPieceType()) {
                            case 10:
                                value = value + totalpos * 2;
                                break;
                            case 11:
                                value = value + totalpos * 5;
                                break;
                            case 12:
                                value = value + totalpos * -2;
                                break;
                            case 13:
                                value = value + totalpos;
                                break;
                            case 14:
                                value = value + totalpos * 3;
                                break;
                            case 15:
                                value = value + totalpos * 3;
                                break;
                        }
                    }
                }
            }
        }
        return value;
    }

    public int evalutionPerPositioning(Chessboard cb, int color, Game game) {
        int value = 0;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                Position pos = new Position(i, j);
                if (null != cb.getPiece(pos)) { //se tiver peça na posição
                    if ((cb.getPiece(pos).getColor() == color)) {
                        if ((pos.getX() >= 3 && pos.getX() <= 4) && (pos.getY() >= 3 && pos.getY() <= 4)) {
                            switch (cb.getPiece(pos).getPieceType()) {
                                case 10:
                                    value = value + 40;
                                    break;
                                case 11:
                                    value = value + 40;
                                    break;
                                case 12:
                                    value = value - 30;
                                    break;
                                case 13:
                                    value = value + 10;
                                    break;
                                case 14:
                                    value = value + 60;
                                    break;
                                case 15:
                                    value = value + 50;
                                    break;
                            }
                        } else if ((pos.getX() >= 2 && pos.getX() <= 5) && (pos.getY() >= 2 && pos.getY() <= 5)) {
                            switch (cb.getPiece(pos).getPieceType()) {
                                case 10:
                                    value = value + 30;
                                    break;
                                case 11:
                                    value = value + 30;
                                    break;
                                case 12:
                                    value = value - 20;
                                    break;
                                case 13:
                                    value = value + 5;
                                    break;
                                case 14:
                                    value = value + 50;
                                    break;
                                case 15:
                                    value = value + 40;
                                    break;
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    public int extraPointsForCheck(Chessboard cb, Game game, int color) {
        int score = 0;
        if (color == 1) {
            if (game.isInCheck(game.getHuman())) {
                score = 200;
            } else if (game.isInMate(game.getHuman())) {
                score = 5000;
            }
        } else if (game.isInCheck(game.getComputer())) {
            score = 200;
        } else if (game.isInMate(game.getComputer())) {
            score = 5000;
        }
        return score;
    }
}
