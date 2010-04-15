package xadrezonator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import pieces.*;

/**
 * Classe que representa um estado do jogo
 */
public class State {

    private Game game; //O jogo
    private boolean humanTurn; //Booleano que controla o turno
    private int fiftyMoveDrawCounter; //Contador para o empate pelos 50 movimentos
    private HashMap<Integer, Integer> boardConfigurationHash; //Tabela de configurações de tabuleiro
    private State lastState; //Último estado
    private Chessboard chessboard; //Tabuleiro

    /**
     * Construtor inicial, ou seja, só é usado para construir o primeiro estado do jogo
     * @param game O jogo
     */
    public State(Game game) {
        setGame(game);
        setChessboard(new Chessboard(game.getHuman(), game.getComputer()));
        setLastState(null);
        setHumanTurn(true);
        setFiftyMoveDrawCounter(0);
        setBoardConfigurationHash(new HashMap<Integer, Integer>());
        getBoardConfigurationHash().put(getConfigurationCode(), 1);
    }

    /**
     * Construtor usado para construir os outros estados
     * Usado no método play, ou seja, quando o estado do jogo
     * passa a ser outro
     */
    public State() {
    }

    //GETTERS E SETTERS
    //##########################################################################
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isHumanTurn() {
        return humanTurn;
    }

    public void setHumanTurn(boolean humanTurn) {
        this.humanTurn = humanTurn;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public void setChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public HashMap<Integer, Integer> getBoardConfigurationHash() {
        return boardConfigurationHash;
    }

    public void setBoardConfigurationHash(HashMap<Integer, Integer> boardConfigurationHash) {
        this.boardConfigurationHash = boardConfigurationHash;
    }

    public int getFiftyMoveDrawCounter() {
        return fiftyMoveDrawCounter;
    }

    public void setFiftyMoveDrawCounter(int fiftyMoveDrawCounter) {
        this.fiftyMoveDrawCounter = fiftyMoveDrawCounter;
    }

    public State getLastState() {
        return lastState;
    }

    public void setLastState(State lastState) {
        this.lastState = lastState;
    }

    public int getConfigurationCode() {
        return getChessboard().configurationCode();
    }
    //##########################################################################

    /**
     * Método que pega uma peça no tabuleiro
     * @param position Posição da peça
     * @return A peça na posição passada
     * @throws NullPointerException
     * @throws ArrayIndexOutOfBoundsException
     */
    public Piece getPieceAtChessboard(Position position) throws NullPointerException,
            ArrayIndexOutOfBoundsException {
        try {
            return getChessboard().getPiece(position);
        } catch (NullPointerException e1) { //Se a posição está vazia
            throw e1;
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw e2;
        }
    }

    /**
     * Método que verifica se um jogador está em xeque
     * @param player Jogador testado
     * @return booleano informando se o jogador está em xeque
     */
    public boolean isInCheck(Player player) {
        try {
            Position kingPosition = player.getKing(getChessboard()).getPosition();
            ListIterator<Piece> pieceIterator = getGame().getOpponent(player).getPieces(getChessboard()).listIterator();
            while (pieceIterator.hasNext()) {
                Piece piece = pieceIterator.next();
                LinkedList<Position> validPositions = getChessboard().validDestinationPositions(piece.possibleDestinationPositions());
                if (kingPosition.isInList(validPositions)) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            throw e;
        }

    }

    /**
     * Método que retorna o conjunto de ameaças ao rei
     * @param player Dono do rei avaliado
     * @return Lista com peças que ameaçam o rei
     */
    public LinkedList<Piece> getKingThreats(Player player) {
        Position kingPosition = player.getKing(getChessboard()).getPosition();
        LinkedList<Piece> threats = new LinkedList<Piece>();
        ListIterator<Piece> opponentPiecesIterator = getGame().getOpponent(player).getPieces(getChessboard()).listIterator();
        while (opponentPiecesIterator.hasNext()) {
            Piece piece = opponentPiecesIterator.next();
            if (kingPosition.isInList(getChessboard().validDestinationPositions(piece.possibleDestinationPositions()))) {
                threats.add(piece);
            }
        }
        return threats;
    }

    /**
     * Verifica se um jogador está em xeque-mate
     * Temos que realizar 2 testes:
     * 1 - Verificar se o rei pode fugir para uma posição
     * 2 - Verificar se alguma peça pode salvar o rei, bloqueando ou comendo a peça que ameaça
     * @param player Jogador testado
     * @return booleano informando se o jogador está em xeque-mate
     */
    public boolean isInMate(Player player) {

        //Se o jogador não estiver em xeque, então não está em xeque-mate
        if (!isInCheck(player)) {
            return false;
        }

        //Verifica se o rei pode fugir
        if (kingIsAbleToScape(player)) {
            return false;
        }

        //Verificação 2 - Alguma peça pode salvar o rei
        Position kingCurrentPosition = player.getKing(getChessboard()).getPosition();

        int moveCounter = 0; //Variável que verificará se precisa de apenas um movimento para salvar o rei
        //Se for necessário mais de um movimento, é xeque-mate
        Piece saverPiece = null; //Nesta variável será armazenada a peça salvadora, ou seja, a que bloqueia o ataque

        Position blockPosition = null; //Aqui ficará a posição de bloqueio da peça que bloqueia
        //List para pegar as peças que ameaçam o rei
        LinkedList threatsKing = getKingThreats(player);

        //Cria o iterador da lista de peças que ameacam o rei
        ListIterator<Piece> threatsKingIterator = threatsKing.listIterator();

        //Verifica se alguém pode bloquear ou comer as peças que ameaçam o rei
        //Varre a lista de ameaças e verifica para todas as peças do jogador vendo se pode haver bloqueio ou ataque
        boolean continueLoop = true;
        while (threatsKingIterator.hasNext() && continueLoop) {
            //Cria o iterador de peças do jogador em xeque
            LinkedList<Piece> playerPieces = player.getPieces(getChessboard());
            ListIterator<Piece> playerPiecesIterator = playerPieces.listIterator();

            Piece opponentPiece = threatsKingIterator.next();
            //Pega as posições do caminho da peça que ameaça até o rei (conjunto)
            LinkedList<Position> wayToKing = getChessboard().wayTo(opponentPiece, kingCurrentPosition);
            //Adiciona a posição do oponente na lista (para verificar se pode haver ataque)
            wayToKing.add(opponentPiece.getPosition());
            //booleano para controlar se a peça que ameaça tem caminho livre até o rei
            boolean freeWay = true;
            //Varre as peças do jogador à procura de alguma para salvar o rei
            Piece currentPlayerPiece = null;
            Position pos = null;
            while (playerPiecesIterator.hasNext() && freeWay) {
                currentPlayerPiece = playerPiecesIterator.next();
                if (currentPlayerPiece != player.getKing(getChessboard())) {
                    //cria um iterador com os destinos possíveis de currentPlayerPiece
                    //LinkedList<Position> playerPiecePositions = getChessboard().validDestinationPositions(currentPlayerPiece.possibleDestinationPositions());
                    LinkedList<Position> playerPiecePositions = getGame().getValidDestinationPositions(currentPlayerPiece);
                    ListIterator<Position> playerPiecePositionsIterator = playerPiecePositions.listIterator();
                    //varre esse iterador a procura de uma posição que esteja no caminho do rei
                    while (playerPiecePositionsIterator.hasNext() && freeWay) {
                        pos = playerPiecePositionsIterator.next();
                        //Se a posição está na lista, a ameaça pode ser bloqueada
                        freeWay = !pos.isInList(wayToKing);
                    }
                }
            }
            if (!freeWay) {
                moveCounter++;
                if (moveCounter == 1) {
                    saverPiece = currentPlayerPiece;
                    blockPosition = pos;
                    threatsKingIterator.remove();
                } else { //Ou seja, uma ameaça já foi bloqueada
                    if (saverPiece != currentPlayerPiece || blockPosition != pos) {
                        return true;//Iria ser necessário mais de um movimento para salvar o rei
                    }
                }
            } else {
                //Está em xeque mate pois foi encontrada uma peça com caminho livre até o rei
                return true;
            }
        }
        return false;
    }

    /**
     * Método que avisa que está na hora de promover o peão
     * @param pawn Peão testado
     * @return booleano informando se está na hora de promover o peão
     */
    public boolean timeToPromote(Pawn pawn) {
        if (pawn.getColor() == Piece.WHITE) {
            return (pawn.getPosition().getY() == 7);
        } else {
            return (pawn.getPosition().getY() == 0);
        }
    }


    /* Função que avalia o Roque
    1. The king must never have moved; DONE OK
    2. The chosen rook must never have moved; DONE OK
    3. There must be no pieces between the king and the chosen rook; DONE OK
    4. The king must not currently be in check. DONE OK
    5. The king must not pass through a square that is under attack by enemy pieces. DONE
     */
    /**
     * Função que avalia o Roque
     * 1. The king must never have moved;
     * 2. The chosen rook must never have moved;
     * 3. There must be no pieces between the king and the chosen rook;
     * 4. The king must not currently be in check;
     * 5. The king must not pass through a square that is under attack by enemy pieces.
     * @param kingPosition Posição do rei
     * @param rookPosition Posição da torre
     * @return booleano informando se o roque é válido
     */
    private boolean availCastling(Position kingPosition, Position rookPosition) {
        Piece kingPiece = getPieceAtChessboard(kingPosition);
        Piece rookPiece = getPieceAtChessboard(rookPosition);
        //Teste 1 e 2
        if (kingPiece.hasMoved() || rookPiece.hasMoved()) {
            return false;
        }
        int xKing = kingPosition.getX();
        int xRook = rookPosition.getX();
        int yKing = kingPosition.getY();

        //Teste 3
        LinkedList<Position> positionsBetweenCastling = new LinkedList<Position>();
        if (xKing < xRook) {
            for (int i = xKing + 1; i < xRook; i++) {
                Position currentPosition = new Position(i, yKing);
                positionsBetweenCastling.add(currentPosition);
                if (!getChessboard().isEmpty(currentPosition)) {
                    return false;
                }
            }
        } else {
            for (int i = xRook + 1; i < xKing; i++) {
                Position currentPosition = new Position(i, yKing);
                positionsBetweenCastling.add(currentPosition);
                if (!getChessboard().isEmpty(currentPosition)) {
                    return false;
                }
            }
        }
        //Teste 4
        Player player = kingPiece.getOwner();
        if (this.isInCheck(player)) {
            return false;
        }

        ListIterator<Piece> pieceIterator = getGame().getOpponent(player).getPieces(getChessboard()).listIterator();
        boolean onePositionIsAttacked = false;
        while (pieceIterator.hasNext() && !onePositionIsAttacked) {
            Piece enemyPiece = pieceIterator.next();
            LinkedList<Position> attackedPositions = getChessboard().validDestinationPositions(enemyPiece.possibleDestinationPositions());
            for (int i = 0; i < positionsBetweenCastling.size(); i++) {
                if (positionsBetweenCastling.get(i).isInList(attackedPositions)) {
                    onePositionIsAttacked = true;
                }
            }
        }
        //Teste 5
        if (onePositionIsAttacked) {
            return false;
        }
        //Castling Available
        return true;
    }

    /**
     * Função para avaliar se o roque longo é um movimento válido para um dado jogador
     * @param player Jogador avaliado
     * @return booleano informando se o jogador pode realizar o roque longo
     */
    public boolean availLongCastling(Player player) {
        Piece king = player.getKing(getChessboard());
        Position kingPosition = king.getPosition();
        Position rookPosition = new Position(0, -1);
        if (king.isBlack()) {
            rookPosition.setY(7);
        } else {
            rookPosition.setY(0);
        }
        if (!getChessboard().isEmpty(rookPosition)) {
            return availCastling(kingPosition, rookPosition);
        } else {
            return false;
        }
    }

    /**
     * Função para avaliar se o roque curto é um movimento válido para um dado jogador
     * @param player Jogador avaliado
     * @return booleano informando se o jogador pode realizar o roque curto
     */
    public boolean availShortCastling(Player player) {
        Piece king = player.getKing(getChessboard());
        Position kingPosition = king.getPosition();
        Position rookPosition = new Position(7, -1);
        if (king.isBlack()) {
            rookPosition.setY(7);
        } else {
            rookPosition.setY(0);
        }
        if (!getChessboard().isEmpty(rookPosition)) {
            return availCastling(kingPosition, rookPosition);
        } else {
            return false;
        }
    }

    /**
     * Método que avalia se o rei pode escapar das ameaças
     * @param player Dono do rei avaliado
     * @return booleano informando se o rei do jogador consegue escapar das ameaças
     */
    public boolean kingIsAbleToScape(Player player) {
        //Armazena a posição do rei em xeque
        Position kingCurrentPosition = player.getKing(getChessboard()).getPosition();
        //iterador com as posições destino possíveis do rei
        ListIterator<Position> kingPossiblesPositions = getChessboard().validDestinationPositions(player.getKing(getChessboard()).possibleDestinationPositions()).listIterator();
        boolean inDanger = true;
        //Rei pode fugir?
        while (kingPossiblesPositions.hasNext() && inDanger) {
            Position kingPossiblePos = kingPossiblesPositions.next();
            //Armazena o rei e a peça que está na posição que ele vai testar para retornar depois
            Piece oldPiece = getPieceAtChessboard(kingPossiblePos);
            Piece king = getPieceAtChessboard(kingCurrentPosition);

            getChessboard().setPieceAtBoard(kingPossiblePos, king);
            getChessboard().setPieceAtBoard(kingCurrentPosition, null);

            ListIterator<Piece> opponentPiecesIterator1 = getGame().getOpponent(player).getPieces(getChessboard()).listIterator();
            boolean positionIsAttacked = false;
            while (opponentPiecesIterator1.hasNext() && !positionIsAttacked) {
                Piece piece = opponentPiecesIterator1.next();
                positionIsAttacked = kingPossiblePos.isInList(getChessboard().validDestinationPositions(piece.possibleDestinationPositions()));
            }

            getChessboard().setPieceAtBoard(kingPossiblePos, oldPiece);
            getChessboard().setPieceAtBoard(kingCurrentPosition, king);

            if (!positionIsAttacked) {
                inDanger = false;
            }
        }
        return !inDanger;
    }

    /**
     * Método que verifica se o jogo está empatado
     * As condições de empate são as seguintes:
     * 1 - Empate por afogamento -> Um jogador não pode mais se mover, porém não está em xeque
     * 2 - Os últimos 50 lances consecutivos foram feitos pelos jogadores sem movimento de peão e sem captura
     * 3 - Material insuficiente para haver xeque-mate
     * 4 - A mesma configuração do tabuleiro aparece 3 vezes no jogo
     * 5 - Xeque perpétuo (é quando um jogador mostra que pode ficar dando xeque para sempre no outro jogador)
     */
    public boolean matchDraw() {
        //###################################################################################
        //TESTE DA CONDIÇÃO 1
        //Empate por afogamento
        boolean draw = true;
        Player avaliatedPlayer;
        if (isHumanTurn()) {
            avaliatedPlayer = getGame().getHuman();
        } else {
            avaliatedPlayer = getGame().getComputer();
        }

        if (!isInCheck(avaliatedPlayer)) {
            //Se o jogador não estiver em xeque, deve-se para toda peça verificar se o
            //conjunto válido de posições não é vazio
            //Se houver um não-vazio, já sai desse teste pois não há empate por afogamento
            LinkedList<Piece> pieces = avaliatedPlayer.getPieces(getChessboard());
            ListIterator<Piece> piecesIterator = pieces.listIterator();
            while (piecesIterator.hasNext() && draw) {
                Piece piece = piecesIterator.next();
                if (!getGame().getValidDestinationPositions(piece).isEmpty()) {
                    draw = false;
                }
            }
        } else {
            //Caso o jogador esteja em xeque, sai do teste pois não há empate por afogamento neste caso
            draw = false;
        }
        if (draw) {
            //Se houve um empate por afogamento, retorna verdadeiro
            return true;
        }
        //FIM DO TESTE DA CONDIÇÃO 1

        //###################################################################################

        //TESTE DA CONDIÇÃO 2
        //Os últimos 50 lances consecutivos foram feitos pelos jogadores sem movimento de peão e sem captura
        if (getFiftyMoveDrawCounter() >= 50) {
            return true;
        }
        //FIM DO TESTE DA CONDIÇÃO 2

        //###################################################################################

        //TESTE DA CONDIÇÃO 3
        //Material insuficiente para haver xeque-mate
        /* As configurações impossíveis de se haver xeque-mate são as seguinte:
         *      Rei x Rei
         *      Rei e Cavalo x Rei
         *      Rei e dois Cavalos X Rei
         *      Rei e Bispo x Rei
         *      Rei e Bispo x Rei e Bispo
         *      Rei e Bispo x Rei e Cavalo
         *      Rei e Cavalo x Rei e Cavalo
         *  Tiradas de: http://webdesk2.cursoanglo.com.br/Paginas/ExtranetHtml.aspx?IUGrupo=6F52B329-AF44-4FA2-BE7E-CB64A1DD7ED5&IUUsuario=BE693A65-BE4F-4A56-9603-8B92CDFC85A4&IUFerramenta=2DC4C164-691D-4DE9-B535-78D436880A67
         *              http://xadrezonline.uol.com.br/tutorial/empate.htm
         */
        LinkedList<Piece> humanPieces = getGame().getHuman().getPieces(getChessboard());
        LinkedList<Piece> computerPieces = getGame().getComputer().getPieces(getChessboard());
        //Verifica-se se algum dos jogadores tem apenas um rei
        if ((humanPieces.size() == 1) || (computerPieces.size() == 1)) {
            if ((humanPieces.size() == 1) && (computerPieces.size() == 1)) {
                //Configuração Rei x Rei detectada - Empate!
                return true;
            }

            LinkedList<Piece> avaliatePieces;
            if (humanPieces.size() == 1) {
                avaliatePieces = computerPieces;
            } else {
                avaliatePieces = humanPieces;
            }

            if (avaliatePieces.size() == 2) {
                Piece piece1 = avaliatePieces.get(0);
                Piece piece2 = avaliatePieces.get(1);

                //Testa-se agora a configuração Rei e Cavalo x Rei
                if (((piece1 instanceof King) && (piece2 instanceof Horse)) || ((piece1 instanceof Horse) && (piece2 instanceof King))) {
                    //Configuração Rei e Cavalo x Rei detectada - Empate!
                    return true;
                }

                //Testa-se agora a configuraçao Rei e Bispo x Rei
                if (((piece1 instanceof King) && (piece2 instanceof Bishop)) || ((piece1 instanceof Bishop) && (piece2 instanceof King))) {
                    //Configuração Rei e Bispo x Rei detectada - Empate!
                    return true;
                }
            } else if (avaliatePieces.size() == 3) {
                //Testa-se agora a configuração Rei e dois Cavalos x Rei
                Piece piece1 = avaliatePieces.get(0);
                Piece piece2 = avaliatePieces.get(1);
                Piece piece3 = avaliatePieces.get(2);

                if (piece1 instanceof King) {
                    if ((piece2 instanceof Horse) && (piece3 instanceof Horse)) {
                        //Configuração Rei e dois Cavalos x Rei detectada - Empate!
                        return true;
                    }
                } else if (piece2 instanceof King) {
                    if ((piece1 instanceof Horse) && (piece3 instanceof Horse)) {
                        //Configuração Rei e dois Cavalos x Rei detectada - Empate!
                        return true;
                    }
                } else if (piece3 instanceof King) {
                    if ((piece1 instanceof Horse) && (piece2 instanceof Horse)) {
                        //Configuração Rei e dois Cavalos x Rei detectada - Empate!
                        return true;
                    }
                }
            }
            //Nenhum dos jogadores tem apenas um rei
        } else if (humanPieces.size() == 2 && computerPieces.size() == 2) {
            Piece humanPiece1 = humanPieces.get(0);
            Piece humanPiece2 = humanPieces.get(1);
            Piece computerPiece1 = computerPieces.get(0);
            Piece computerPiece2 = computerPieces.get(1);

            if (humanPiece1 instanceof Bishop || humanPiece2 instanceof Bishop) {
                //Testa-se agora a configuração Rei e Bispo x Rei e Bispo
                if (computerPiece1 instanceof Bishop || computerPiece2 instanceof Bishop) {
                    //Detectada configuração Rei e Bispo x Rei e Bispo - Empate!
                    return true;

                } else if (computerPiece1 instanceof Horse || computerPiece2 instanceof Horse) { //Testa-se aqui a configuração Rei e Bispo x Rei e Cavalo
                    //Configuração Rei e Bispo x Rei e Cavalo detectada - Empate!
                    return true;
                }
            } else if (computerPiece1 instanceof Bishop || computerPiece2 instanceof Bishop) {
                //A configuração Rei e Bispo x Rei e Bispo já foi avaliada
                //Testa-se agora a configuração Rei e Bispo x Rei e Cavalo sendo o computador dono do bispo
                if (humanPiece1 instanceof Horse || humanPiece2 instanceof Horse) {
                    //Configuração Rei e Bispo x Rei e Cavalo detectada - Empate!
                    return true;
                }
            } else if (humanPiece1 instanceof Horse || humanPiece2 instanceof Horse) {
                //Testa-se agora a configuração Rei e Cavalo x Rei e Cavalo
                if (computerPiece1 instanceof Horse || computerPiece2 instanceof Horse) {
                    //Configuração Rei e Cavalo x Rei e Cavalo detectada - Empate!
                    return true;
                }
            }
        }
        //FIM DO TESTE DA CONDIÇÃO 3

        //###################################################################################

        //TESTE DA CONDIÇÃO 4
        //A mesma configuração do tabuleiro aparece 3 vezes no jogo
        int configurationCode = getConfigurationCode();
        int counter = getBoardConfigurationHash().get(configurationCode);
        if (counter == 3) {
            //Configuração do teclado já apareceu 3 vezes no jogo - Empate!
            return true;
        }
        //FIM DO TESTE DA CONDIÇÃO 4

        //###################################################################################

        //TESTE DA CONDIÇÃO 5
        //Xeque perpétuo (é quando um jogador mostra que pode ficar dando xeque para sempre no outro jogador)
        //É importante citar que este empate avalia a intenção dos jogadores, pois nem sempre o xeque perpétuo
        //é uma possibilidade que necessariamente vai ocorrer.
        //Portanto, para avaliar esta intenção, eu avalio os 4 últimos estados do jogo e verifico se as duas
        //peças envolvidas no xeque vem na mesma situação a um tempo, ou seja:
        //se nos últimos 4 estados do jogo, a peça que dá xeque no rei foi sempre a mesma e se nenhuma outra peça se
        //envolveu para salvar o rei (i.e. o rei conseguia fugir sempre)
        //resumindo, se o rei fugir pela quinta vez de um xeque dado sempre pela mesma peça, o programa interpreta
        //a situação como intenção dos jogadores de ficarem sempre ali e portanto considera um empate por
        //xeque perpétuo

        //Primeiro verifica-se qual jogador está em xeque
        Player player = null;
        boolean check;
        if (isInCheck(getGame().getComputer())) {
            player = getGame().getComputer();
            check = true;
        } else if (isInCheck(getGame().getHuman())) {
            player = getGame().getHuman();
            check = true;
        } else {
            check = false;
        }

        if (check) {
            if (getKingThreats(player).size() == 1) {
                Piece threat = getKingThreats(player).getFirst();
                if (kingIsAbleToScape(player)) {
                    int stateCounter = 1;
                    boolean perpetuousCheck = true;
                    //Armazena-se o último estado que foi a vez de player jogar
                    State localLastState = getLastState().getLastState();
                    if (localLastState == null) {
                        perpetuousCheck = false;
                    }
                    while ((stateCounter < 5) && (perpetuousCheck)) {
                        //se o último estado em que era vez do jogador jogar não estava em xeque, não é xeque perpétuo
                        if (!localLastState.isInCheck(player)) {
                            perpetuousCheck = false;
                        } //se o conjunto de ameaças ao rei NÃO tiver apenas a ameaça armazenada então não é xeque perpetuo
                        else {
                            threat = getPieceAtChessboard(threat.getPosition());
                            if (!((localLastState.getKingThreats(player).size() == 1) && (threat.isInList(localLastState.getKingThreats(player))))) {
                                perpetuousCheck = false;
                            } //se o rei nao podia escapar entao ele foi salvo e não é xeque perpetuo
                            else if (!localLastState.kingIsAbleToScape(player)) {
                                perpetuousCheck = false;
                            } else {
                                stateCounter += 1;
                                try {
                                    localLastState = localLastState.getLastState().getLastState();
                                } catch (NullPointerException ex) {
                                    perpetuousCheck = false;
                                }
                            }
                        }

                    }
                    if (perpetuousCheck) {
                        return true;
                    }
                }
            }
        }
        //FIM DO TESTE DA CONDIÇÃO 5

        //###################################################################################

        return false;
    }

    /**
     * Método que retorna o número de peças de tipo e cor iguais a uma peça dada
     * presentes no tabuleiro
     * @param pieceType Tipo da peça (rei, rainha, etc)
     * @param color Cor da peça
     * @return O número de peças daquele tipo e cor no tabuleiro
     */
    public int getNumberOfPieces(int pieceType, int color) {
        Chessboard localChessboard = getChessboard();
        int size = localChessboard.getSize();
        int counter = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                try {
                    Piece chessboardPiece = localChessboard.getPiece(new Position(i, j));
                    if (chessboardPiece.getPieceType() == pieceType) {
                        if (chessboardPiece.getColor() == color) {
                            counter += 1;
                        }
                    }
                } catch (NullPointerException ex) {
                    //apenas prossegue no loop
                }
            }
        }

        return counter;
    }
}
