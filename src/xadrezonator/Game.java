package xadrezonator;

import Exceptions.*;
import IA.MiniMax;
import IA.move;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import pieces.*;

/**
 * Classe responsável pelo controle do jogo.
 */
public class Game {

    private State state; //Atributo que armazena o estado ATUAL do jogo
    private Player computer; //Atributo que armazena o jogador
    private Player human; //Atributo que armazena o computador
    private MiniMax AI; //Objeto responsável pelas jogadas do computador

    /**
     * Construtor que recebe como parâmetro o nome do jogador que desafiará o computador
     * @param humanName O nome do jogador
     */
    public Game(String humanName) {
        this.state = new State(this);
        this.computer = new Player("Computador");
        this.human = new Player(humanName);
        this.AI = new MiniMax();
        this.setChessboard(new Chessboard(getHuman(), getComputer()));
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Player getComputer() {
        return computer;
    }

    public Player getHuman() {
        return human;
    }

    public Action getLastAction() {
        return this.getChessboard().getLastAction();
    }

    public MiniMax getAI() {
        return AI;
    }


    //##########################################################################
    //FUNÇÕES QUE AVALIAM ESTADOS DO JOGO
    //Delegam suas funções para a classe State
    /**
     * Armazena um tabuleiro no jogo
     * @param cb Tabuleiro
     */
    public void setChessboard(Chessboard cb) {
        this.getState().setChessboard(cb);
    }

    /**
     * Pega o tabuleiro do estado atual
     * @return o tabuleiro do estado atual
     */
    public Chessboard getChessboard() {
        return getState().getChessboard();
    }

    /**
     * Função para avaliar se o roque longo é um movimento válido
     * @param player Jogador para o qual se está testando se o roque é válido
     * @return booleano informando se o movimento é válido ou não
     */
    public boolean availLongCastling(Player player) {
        return getState().availLongCastling(player);
    }

    /**
     * Função para avaliar se o roque curto é um movimento válido
     * @param player Jogador para o qual se está testando se o roque é válido
     * @return booleano informando se o movimento é válido ou não
     */
    public boolean availShortCastling(Player player) {
        return getState().availShortCastling(player);
    }

    /**
     * Método que pega as ameaças ao rei
     * @param player Dono do rei ameaçado
     * @return Lista de peças que podem atacar o rei
     */
    public LinkedList<Piece> getKingThreats(Player player) {
        return getState().getKingThreats(player);
    }

    /**
     * Verifica se o jogador está em xeque
     * @param player Jogador testado
     * @return booleano informando se ele está em xeque
     */
    public boolean isInCheck(Player player) {
        return getState().isInCheck(player);
    }

    /**
     * Verifica se o jogador está em xeque-mate
     * @param player Jogador testado
     * @return booleano informando se ele está em xeque-mate
     */
    public boolean isInMate(Player player) {
        return getState().isInMate(player);
    }

    /**
     * Método que avisa que está na hora de promover o peão
     * @param pawn Peão testado
     * @return booleano informando se é hora de promover
     */
    public boolean timeToPromote(Pawn pawn) {
        return getState().timeToPromote(pawn);
    }

    /**
     * Método que verifica se o jogo está empatado
     * @return boolaeno informando se o jogo está empatado
     */
    public boolean matchDraw() {
        return getState().matchDraw();
    }

    /**
     * Método que retorna o número de peças de tipo e cor iguais a uma peça dada
     * presentes no tabuleiro
     * @param pieceType Tipo da peça
     * @param color Cor da peça
     * @return Número de peças do tipo e cor especificados no tabuleiro
     */
    public int getNumberOfPieces(int pieceType, int color) {
        return getState().getNumberOfPieces(pieceType, color);
    }
    //AQUI SE ENCERRA O BLOCO DE FUNÇÕES QUE AVALIAM ESTADOS DO JOGO
    //##########################################################################

    /**
     * Método que retorna uma peça do tabuleiro dada a sua posição
     * @param position Posição da qual se quer a peça
     * @return A peça na posição
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
     * Método que instancia o novo estado que terá Game após uma movimentação
     * Deve ser chamado antes da movimentação ocorrer
     * @param piece É uma instância de peça que é útil caso seja um peão pois
     * umas das condições de empate envolve o peão
     * @param strike Booleano que diz se o movimento que vai ocorrer envolve um
     * ataque, que também serve para avaliar o empate
     */
    private void changeState(Piece piece, boolean strike) {

        //Armazena-se as informações do estado atual que serão usadas pelo novo estado
        //============================================================================
        boolean humanTurn = getState().isHumanTurn();
        int fiftyMoveDrawCounter = getState().getFiftyMoveDrawCounter();
        Chessboard chessboard = getState().getChessboard().copy();
        HashMap<Integer, Integer> hashMap = getState().getBoardConfigurationHash();
        State lastState = getState();
        //============================================================================

        //Instancia o estado que será o atual
        //==========================================
        State newState = new State();
        //==========================================

        //É setado o estado corrente do jogo
        //================================================
        setState(newState);
        //A partir de agora, o método getState() retornará
        //o newState.
        //================================================

        //Seta o jogo no estado atual
        //==========================================
        newState.setGame(this);
        //==========================================

        //Seta o tabuleiro atual como tabuleiro do estado atual
        //A movimentação não foi realizada ainda
        //================================================================
        getState().setChessboard(chessboard);
        //================================================================

        //Seta o estado antes da movimentação como o lastState do estado atual
        //===============================================================================
        getState().setLastState(lastState);
        //===============================================================================

        //Verifica a situação dos 50 movimentos e atualiza o estado da maneira correta
        //====================================================================================
        try {
            if (!(piece instanceof Pawn) && (!strike)) {
                //Se a peça não é um peão e não comeu nenhuma peça, então aumenta o contador do empate
                //dos 50 movimentos
                getState().setFiftyMoveDrawCounter(fiftyMoveDrawCounter + 1);
            } else {
                //se não é um peão ou comeu alguma peça
                //reinicia o contador do empate dos 50 movimentos
                getState().setFiftyMoveDrawCounter(0);
            }
        } catch (NullPointerException ex) {
            //Para o caso de não haver uma peça particular envolvida no movimento
            //Isto acontece nos casos de roque
            //Neste caso, o fiftyMoveDrawCounter deve ser incrementado
            getState().setFiftyMoveDrawCounter(fiftyMoveDrawCounter + 1);
        }
        //====================================================================================

        //Atualiza o turno no novo estado
        //================================================
        getState().setHumanTurn(!humanTurn);
        //================================================

        //Armazena o hash de configurações do tabuleiro
        //Note que é impossível armazenar a nova configuração do tabuleiro neste método pois ele
        //é chamado antes da movimentação da peça, portanto não se sabe qual é a nova configuração
        //do tabuleiro.
        //=========================================================================================
        //Seta o hash no novo estado
        getState().setBoardConfigurationHash(hashMap);
        //=========================================================================================
    }

    /**
     * Método que atualiza a tabela de hash de Game
     * Dada a configuração atual do tabuleiro, ele avalia se esta configuração deve ser inserida ou
     * atualizada no hash
     */
    private void updateHash() {
        int configurationCode = getChessboard().configurationCode();
        if (getState().getBoardConfigurationHash().containsKey(configurationCode)) {
            //Se tiver, armazena-se em um contador o número de vezes que a configuração já apareceu
            int counter = getState().getBoardConfigurationHash().get(configurationCode);
            //Incrementa-se este contador pois a configuração apareceu mais uma vez
            counter += 1;
            //Agora remove-se a tupla da tabela e a insere novamente com o novo contador incrementado
            getState().getBoardConfigurationHash().remove(configurationCode);
            getState().getBoardConfigurationHash().put(configurationCode, counter);
        } else {
            //Caso não tenha, insere na tabela a nova configuração
            getState().getBoardConfigurationHash().put(configurationCode, 1);
        }
    }

    /**
     * Ação de jogar vinda da interface
     * ATENÇÃO: Este método é usado somente em movimentos simples
     * A execução do roque não é controlada por este método
     * @param player É o jogador que está realizando a jogada
     * @param initialPosition A posição aonda está a peça a ser movimentada
     * @param destinationPosition A posição para onde a peça irá
     * @return booleano informando se o movimento é um en passant
     * @throws DoesntBelongToPlayerException
     * @throws InvalidMoveException
     * @throws EmptyPositionException
     * @throws NoSuchPositionException
     * @throws InvalidMoveIsInCheckException
     */
    public boolean play(Player player, Position initialPosition, Position destinationPosition)
            throws DoesntBelongToPlayerException, InvalidMoveException,
            EmptyPositionException, NoSuchPositionException, InvalidMoveIsInCheckException {
        try {
            boolean enPassant = false;
            //Armazena-se a peça que está sendo movida
            Piece piece = getPieceAtChessboard(initialPosition);
            //Verifica se a peça que esta sendo movida pertence àquele jogador
            if (!piece.belongsTo(player)) {
                throw new DoesntBelongToPlayerException();
            }
            //Verifica se a peça está no conjunto de posições válidas
            if (!destinationPosition.isInList(getChessboard().validDestinationPositions(piece.possibleDestinationPositions()))) {
                //Se ela não está na lista retornada por chessboard mas está 
                //na lista retornada por game, o movimento é um En Passant
                //Então tem que verificar se ela não está no método retornado por game também
                //Para poder afirmar que o movimento é inválido
                if (!destinationPosition.isInList(getValidDestinationPositions(piece))) {
                    throw new InvalidMoveException();
                }
                //Caso esteja também na lista retornada por game é um En Passant
                enPassant = true;
            }
            //Verifica se o destino não deixa o jogador em xeque
            if (!destinationPosition.isInList(getValidDestinationPositions(piece))) {
                throw new InvalidMoveIsInCheckException();
            }

            //O movimento é válido
            //Deve-se agora armazenar as informações de estado e
            //Realizar a movimentação
            //IMPORTANTE: caso a movimentação leve um peão à última casa,
            //o estado não é atualizado pois o novo estado se dará somente
            //quando o peão for promovido

            //Variavel que verifica se a peça comeu alguma peça do oponente
            //=============================================================
            boolean strike;
            //Se a posição destino esta vazia e não teve enPassant, então não atacou
            strike = !((getChessboard().isEmpty(destinationPosition)) && (!enPassant));
            //=============================================================

            //Muda o estado do jogo caso não seja uma promoção
            //Não é possível usar o método timeToPromote aqui
            //pois a movimentação não foi ainda realizada, portanto
            //caso o peão vá ser promovido, ele está ainda na pe-
            //núltima casa
            //=====================================================
            if (piece instanceof Pawn) {
                boolean promotion;
                if (piece.isWhite()) {
                    promotion = (destinationPosition.getY() == 7);
                } else {
                    promotion = (destinationPosition.getY() == 0);
                }
                if (!promotion) {
                    changeState(piece, strike);
                }
            } else {
                changeState(piece, strike);
            }
            //=====================================================

            //REALIZA A MOVIMENTAÇÃO DA PEÇA
            //==========================================
            getChessboard().moveFromTo(initialPosition, destinationPosition);
            //Elimina o peão adversário no caso de um En Passant
            if (enPassant) {
                //Calcula a posição da peça atacada
                int x;
                if (initialPosition.getX() > destinationPosition.getX()) {
                    x = initialPosition.getX() - 1;
                } else {
                    x = initialPosition.getX() + 1;
                }
                int y = initialPosition.getY();

                getChessboard().setPieceAtBoard(new Position(x, y), null);
            }
            //Armazena novamente a peça pois o tabuleiro é uma cópia do anterior
            piece = getChessboard().getPiece(destinationPosition);
            //==========================================

            //Atualiza o hash caso não seja uma promoção
            //==========================================
            if (piece instanceof Pawn) {
                if (!timeToPromote((Pawn) piece)) {
                    updateHash();
                }
            } else {
                updateHash();
            }
            //==========================================
            return enPassant;
        } catch (NullPointerException e1) {
            throw new EmptyPositionException();
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new NoSuchPositionException();
        }
    }

    /**
     * Função para executar a troca de peças para o roque grande
     * @param player O jogador que está executando o roque grande
     */
    public void executeLongCastling(Player player) {
        changeState(null, false);

        //Posições correntes do rei e da torre
        Position kingPosition = player.getKing(getChessboard()).getPosition();
        Position rookPosition = new Position(0, kingPosition.getY());
        //Posições destino do rei e da torre
        Position kingNewPosition = new Position(2, kingPosition.getY());
        Position rookNewPosition = new Position(3, kingPosition.getY());
        //Peças do rei e da torre
        Piece king = getPieceAtChessboard(kingPosition);
        Piece rook = getPieceAtChessboard(rookPosition);
        //Movimentação do rei
        getChessboard().setPieceAtBoard(kingNewPosition, king);
        getChessboard().setPieceAtBoard(kingPosition, null);
        //Movimentação da torre
        getChessboard().setPieceAtBoard(rookNewPosition, rook);
        getChessboard().setPieceAtBoard(rookPosition, null);
        //Setando que as peças já sofreram movimentos
        king.pieceMoved();
        rook.pieceMoved();

        updateHash();
    }

    /**
     * Função para executar a troca de peças para o roque pequeno
     * @param player O jogador que está executando o roque pequeno
     */
    public void executeShortCastling(Player player) {
        changeState(null, false);

        //Posições correntes do rei e da torre
        Position kingPosition = player.getKing(getChessboard()).getPosition();
        Position rookPosition = new Position(7, kingPosition.getY());
        //Posições destino do rei e da torre
        Position kingNewPosition = new Position(6, kingPosition.getY());
        Position rookNewPosition = new Position(5, kingPosition.getY());
        //Peças do rei e da torre
        Piece king = getPieceAtChessboard(kingPosition);
        Piece rook = getPieceAtChessboard(rookPosition);
        //Movimentação do rei
        getChessboard().setPieceAtBoard(kingNewPosition, king);
        getChessboard().setPieceAtBoard(kingPosition, null);
        //Movimentação da torre
        getChessboard().setPieceAtBoard(rookNewPosition, rook);
        getChessboard().setPieceAtBoard(rookPosition, null);
        //Setando que as peças já sofreram movimentos
        king.pieceMoved();
        rook.pieceMoved();

        updateHash();
    }

    /**
     * Retorna o oponente de um jogador
     * Função importante na hora de avaliar xeque e xeque-mate
     * @param player Jogador cujo o oponente se deseja conhecer
     * @return Jogador oponente
     */
    public Player getOpponent(Player player) {
        if (player == getComputer()) {
            return getHuman();
        } else {
            return getComputer();
        }
    }

    /**
     * Esta é a avaliação final de movimento
     * Retorna o conjunto que será usado por interface e IA
     * Neste método, ela avalia se determinada posição valida leva o jogador a um estado de xeque
     * e a remove caso leve. É avaliado também a captura especial En Passant.
     * @param piece A peça cujas posições destino se deseja conhecer
     * @return A lista de posições destino válidas
     */
    public LinkedList<Position> getValidDestinationPositions(Piece piece) {
        //Armazena-se a o dono da peça e a posição do rei deste jogador
        Player owner = piece.getOwner();
        Position kingPosition = owner.getKing(getChessboard()).getPosition();
        //Armazena-se as posições destino válidas daquela peça (lista retornada pela função em Chessboard)
        LinkedList<Position> validPositions = getChessboard().validDestinationPositions(piece.possibleDestinationPositions());
        //cria-se um iterador para varrer as posições válidas da peça
        ListIterator<Position> validPositionsIterator = validPositions.listIterator();
        if (piece instanceof King) {
            //Se a peça for o rei, deve-se para cada posição destino possível verificar se ela está nas posições
            //destino válidas de alguma ameaça. Se estiver, essa posição é removida pois o rei não pode ir para lá.
            while (validPositionsIterator.hasNext()) {
                //Varre as posições destino válidas do rei
                Position position = validPositionsIterator.next();
                //Armazena-se uma lista com as peças do oponente ao rei
                LinkedList<Piece> threats = getOpponent(owner).getPieces(getChessboard());
                ListIterator<Piece> threatsIterator = threats.listIterator();
                //Booleano para controlar se aquela posição necessita ainda ser avaliada ou se ela já foi removida
                boolean removedPosition = false;
                while (threatsIterator.hasNext() && !removedPosition) {
                    //Varre as ameaças
                    Piece threat = threatsIterator.next();
                    LinkedList<Position> threatDestinationPositions = getChessboard().validDestinationPositions(threat.possibleDestinationPositions());
                    //Para deixar ele mover na frente de um peão
                    if (threat instanceof Pawn) {
                        int incY = 0;
                        if (threat.isBlack()) {
                            incY = -1;
                        } else {
                            incY = 1;
                        }
                        int pieceXPosition = threat.getPosition().getX();
                        int pieceYPosition = threat.getPosition().getY();
                        LinkedList removablePosition = new LinkedList();
                        for (int i = 0; i < threatDestinationPositions.size(); i++) {
                            Position current = threatDestinationPositions.get(i);
                            if (current.getX() == pieceXPosition &&
                                    (current.getY() == pieceYPosition + incY)) {
                                removablePosition.add(i);
                            } else {
                                if (current.getX() == pieceXPosition &&
                                        (current.getY() == pieceYPosition + (2 * incY))) {
                                    threatDestinationPositions.remove(i);
                                }
                            }
                        }
                        for (int i = 0; i < removablePosition.size(); i++) {
                            int index = ((Integer) removablePosition.get(i)).intValue();
                            threatDestinationPositions.remove(index);
                        }
                    }
                    if (position.isInList(threatDestinationPositions)) {
                        //Se a posição corrente está na lista de posições destinos da ameaça, remove-se esta posição
                        validPositionsIterator.remove();
                        removedPosition = true;
                    } else {
                        //Deve-se verificar se, caso o rei vá para essa posição, ele ainda estará em xeque
                        Piece oldPiece = getPieceAtChessboard(position);
                        Piece king = getPieceAtChessboard(kingPosition);

                        getChessboard().setPieceAtBoard(position, king);
                        getChessboard().setPieceAtBoard(kingPosition, null);

                        if (position.isInList(getChessboard().validDestinationPositions(threat.possibleDestinationPositions()))) {
                            validPositionsIterator.remove();
                            removedPosition = true;
                        }

                        getChessboard().setPieceAtBoard(position, oldPiece);
                        getChessboard().setPieceAtBoard(kingPosition, king);
                    }
                }
            }
        } else {
            //Agora verifica-se se a peça (que não é um rei como já foi comprovada)
            //está bloqueando um xeque.
            if (isBlockingCheck(piece)) {
                Position originalPosition = piece.getPosition();
                /* Para cada posição válida de piece, verifica-se o tamanho do conjunto de ameaças ao rei
                 * quando a peça está naquela posição e quando não está.
                 * se o tamanho do conjunto de ameaças aumenta quando ela não está é porque uma ameaça
                 * foi desbloqueada quando a peça foi para a posição
                 * Se o conjunto for igual, então a peça pode se mover pois a ameaça continua bloqueada
                 */
                while (validPositionsIterator.hasNext()) {
                    Position position = validPositionsIterator.next();
                    Piece pieceAtPosition = getPieceAtChessboard(position);

                    LinkedList<Piece> kingThreats1 = getKingThreats(owner);

                    getChessboard().setPieceAtBoard(position, piece);
                    getChessboard().setPieceAtBoard(originalPosition, null);

                    LinkedList<Piece> kingThreats2 = getKingThreats(owner);

                    getChessboard().setPieceAtBoard(position, pieceAtPosition);
                    getChessboard().setPieceAtBoard(originalPosition, piece);

                    if (kingThreats2.size() > kingThreats1.size()) {
                        //Quando botou a peça em tal posição, a lista de ameaças ficou maior
                        //Uma ameaça que ela estava bloqueando foi desbloqueada
                        //A posição não é válida e, portanto, removida
                        validPositionsIterator.remove();
                    } else { //kingThreats2.size() == kingThreats1.size()
                        //Tem que verificar se as ameaças são as mesmas, ou seja,
                        //se não surgiram novas ameaças quando a peça foi deslocada
                        //Deve-se verificar se as listas são iguais
                        //Caso não sejam, a posição é removida
                        if (!kingThreats1.equals(kingThreats2)) {
                            validPositionsIterator.remove();
                        }
                    }


                }


            }

            //Agora verifica-se, para cada posição destino possível da peça, se aquela
            //posição bloqueia a(s) ameaça(s) ao rei. Se não bloquear, remove-se a posição.
            while (validPositionsIterator.hasNext()) {
                Position position = validPositionsIterator.next();
                //cria-se uma lista com as peças do oponente que ameaçam o rei
                LinkedList<Piece> threats = getKingThreats(owner);
                //cria-se um iterador para percorrer as peças que ameaçam
                ListIterator<Piece> threatsIterator = threats.listIterator();
                //booleano que controla se a posição já foi dada como inválida e removida ou não
                boolean removedPosition = false;
                //Varre as ameaças verificando se a posição sendo avaliada no momento se encontra no caminho
                //entre a ameaça e o rei
                while (threatsIterator.hasNext() && !removedPosition) {
                    Piece threat = threatsIterator.next();
                    //Caso ela não se encontre, a posição deve ser removida do conjunto
                    //O booleano é setado como true para a função avaliar a próxima posição
                    LinkedList<Position> wayTo = getChessboard().wayTo(threat, kingPosition);
                    wayTo.add(threat.getPosition());
                    if (!position.isInList(wayTo)) {
                        validPositionsIterator.remove();
                        removedPosition = true;
                    }
                }
            }
        }


        //Agora verifica-se o movimento especial En Passant
        //======================================================================================
        //Primeiramente, verifica-se se a peça é um peão
        if (piece instanceof Pawn) {
            //Em seguida verifica-se se ela está em uma altura do tabuleiro
            //Que seja possívale realizar o en passant
            int y; //Altura do tabuleiro na qual a peça deverá estar para realizar o en passant
            int destinY; //Para onde a peça irá caso realize o En Passant
            if (piece.isWhite()) {
                y = 4; //Caso a peça seja branca, deverá ter Y = 4 para poder realizar o move
                destinY = 5;
            } else {
                y = 3; //Caso seja preta, será Y = 3
                destinY = 2;
            }
            if (piece.getPosition().getY() == y) { //Verifica se a peça está na altura correta
                //Agora deve-se verificar se há peões inimigos ao lado do peão
                //Primeiro verifica o lado esquerdo
                boolean existsPosition;
                Position position = null;
                if (piece.getPosition().getX() - 1 >= 0 && piece.getPosition().getX() - 1 <= 7) {
                    existsPosition = true;
                    position = new Position(piece.getPosition().getX() - 1, y);
                } else {
                    existsPosition = false;
                }
                if (existsPosition && !getChessboard().isEmpty(position)) {
                    try {
                        Piece nextPiece = getPieceAtChessboard(position);
                        if (nextPiece instanceof Pawn) {
                            //Agora deve-se verificar se o último movimento deste peão
                            //andou duas casas
                            Pawn pawn = (Pawn) nextPiece;
                            if (pawn.didBonusMove()) {
                                //Se no estado anterior a peça não tinha se movido
                                //Ela se moveu duas casas e o En Passant pode ocorrer
                                validPositions.add(new Position(position.getX(), destinY));
                            }
                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

                //Agora verifica-se o lado direito
                position = null;
                if (piece.getPosition().getX() + 1 >= 0 && piece.getPosition().getX() + 1 <= 7) {
                    existsPosition = true;
                    position = new Position(piece.getPosition().getX() + 1, y);
                } else {
                    existsPosition = false;
                }
                if (existsPosition && !getChessboard().isEmpty(position)) {
                    try {
                        Piece nextPiece = getPieceAtChessboard(position);
                        if (nextPiece instanceof Pawn) {
                            //Agora deve-se verificar se o último movimento deste peão
                            //andou duas casas
                            Pawn pawn = (Pawn) nextPiece;
                            if (pawn.didBonusMove()) {
                                //Se no estado anterior a peça não tinha se movido
                                //Ela se moveu duas casas e o En Passant pode ocorrer
                                validPositions.add(new Position(position.getX(), destinY));
                            }
                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        }
        //======================================================================================


        return validPositions;
    }

    /**
     * Método que verifica se uma peça está bloqueando um xeque, ou seja, se caso
     * aquela peça não existisse, o seu dono estaria em xeque
     * @param piece Peça sendo testada
     * @return verdadeiro caso esteja e falso se não estiver
     */
    public boolean isBlockingCheck(Piece piece) {
        Player owner = piece.getOwner();
        boolean isBlockingCheck;
        Position piecePosition = piece.getPosition();
        if (!isInCheck(owner)) {
            //Se o jogador não está em xeque, basta verificar se ele estará em xeque caso a peça desapareça
            getChessboard().setPieceAtBoard(piecePosition, null);
            isBlockingCheck = (isInCheck(owner));
            getChessboard().setPieceAtBoard(piecePosition, piece);
        } else {
            //Se ele está em xeque, deve-se verificar se o número de ameaças aumenta caso a peça desapareça
            int threatsSize = getKingThreats(owner).size();
            getChessboard().setPieceAtBoard(piecePosition, null);
            int newThreatsSize = getKingThreats(owner).size();
            isBlockingCheck = (newThreatsSize > threatsSize);
            getChessboard().setPieceAtBoard(piecePosition, piece);
        }
        return isBlockingCheck;
    }

    /**
     * Metodo que executa a promoçao do peao
     * @param pawn Peão sendo promovido
     * @param piece Para qual peça ele será promovido
     */
    public void promotePawn(Pawn pawn, Piece piece) {
        //Pode até ter ocorrido um ataque, porém não faz diferença
        //já que quando for avaliar o empate dos 50 movimentos, já
        //recomeçará a contagem pois a peça movida foi um peão
        changeState(pawn, false);

        Position pawnPosition = pawn.getPosition();
        getChessboard().setPieceAtBoard(pawnPosition, piece);

        updateHash();

    }

    /**
     * Método que invoca o objeto AI para a realização do movimento do computador
     * @return O movimento que o computador deseja fazer
     */
    public move computerMove() {
        return getAI().miniMax(getChessboard(), this, Piece.WHITE, Piece.BLACK, 2);
    }
}
