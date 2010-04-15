package view;

public interface InterfaceConstants {

    //Constantes que identificam os tipos de peça
    final int BISHOP = 1;
    final int HORSE = 2;
    final int QUEEN = 3;
    final int ROOK = 4;

    //Constantes que identificam os roques longo e curto
    final int LONG_CASTLING = 5;
    final int SHORT_CASTLING = 6;

    //Strings com os nomes dos movimentos especiais
    final String SHORT_CASTLING_STRING = "Roque pequeno";
    final String LONG_CASTLING_STRING = "Roque grande";
    final String PROMOTE_PAWN_STRING = "Promoção de peão";
    final String EN_PASSANT_STRING = "En Passant";

    //Constantes que identificam os estados
    final int DRAW_MATCH = 7;
    final int CHECK_MATE = 8;
    final int CHECK = 9;
}
