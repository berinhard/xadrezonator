package view;

/*
 * MainJFrame.java
 * @author Alberto Scremin
 * Created on 15 de Outubro de 2009, 10:53
 */
import Exceptions.DoesntBelongToPlayerException;
import Exceptions.EmptyPositionException;
import Exceptions.InvalidMoveException;
import Exceptions.InvalidMoveIsInCheckException;
import Exceptions.NoSuchPositionException;
import IA.move;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import pieces.*;
import xadrezonator.Game;
import xadrezonator.Position;
import xadrezonator.Action;
import xadrezonator.Chessboard;
import xadrezonator.Player;

/**
 *
 * @author  Alberto Scremin
 */
public class MainJFrame extends javax.swing.JFrame implements
        pieces.PiecesConstants, InterfaceConstants {

    /*Booleano que controla se a janela neste momento pode receber eventos do mouse*/
    private boolean enabledToMouseEvents;

    /*Tamanho das celulas do tabuleiro*/
    private final int cellWidth = 82;
    private final int cellHeigth = 80;

    /*Label para controlar as cores, apagando o que foi colorido*/
    private javax.swing.JLabel emptyLabel;

    /*Label para controlar e colorir as posicoes que podem ser atigindas por
     * dada peca que foi clicada*/
    private javax.swing.JLabel coloredLabel;

    /*Labels que controlam o Drag e Drop
     * jLabel4 armazena o label arrastado
     * jLabel5 armazena o ultimo label que passou*/
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;

    /*Variaves que armazenam a ultima posica que foi passada pelo Drag
     * para que possamos colocar o label que estava la caso a peca nao seja largada*/
    private Integer previousX;
    private Integer previousY;

    /*Variavel que controla se existe uma peca no label que esta sendo carregado
     * para evitar que algum espaco sem peca possa ser carregado*/
    private Boolean invalidLabel = true;

    /*Posicoes que serao passadas para a engine para fazer a jogada
     * tambem e controlada alguns erros e voltar o tabuleiro como estava com elas*/
    private Position initialPosition;
    private Position finalPosition;

    /*Variavel que de inicio controlava o turno*/
    private Boolean turnHuman = true;

    /*Cores de fundo, apenas c sera renderizada para mostrar as posicoes validas
     * d e apenas para apagar e deixar o tabuleiro normal
     * c e uma cor em tom de verde
     * d e um preto*/
    private Color c = new Color(124, 255, 121, 200);
    private Color d = new Color(0, 0, 0, 0);

    /*Variaveis que controlam o tamanho da tela e fazem com que o jogo fique numa
     * posicao confortavel para a visualizacao*/
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Dimension dm = tk.getScreenSize();

    /** Creates new form MainJFrame */
    public MainJFrame(String playerName) {

        initComponents();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        jLabel4.setVisible(false);

        /*Aqui fazemos a tela ficar numa posicao confortavel para a vista
        e depois ajustamos algumas condicoes necessarias para os objetos na tela*/
        setBounds((int) ((dm.getWidth() - getWidth()) / 2),
                (int) ((dm.getHeight() - getHeight()) / 4),
                getWidth(), getHeight());

        jTable1.getTableHeader().setBackground(this.getBackground());
        jTable1.getTableHeader().setFont(jLabel12.getFont());


        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setCellRenderer(new ChessRenderer());
        }

        jTable1.setCellEditor(new ChessCellEditor());

        //Habilita a janela para receber eventos do mouse
        //======================================================================================================
        jTable1.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                //Se o mouse for arrastado e a janela está disponível para
                //eventos do mouse, a função de arrastar a label é acionada
                if (isEnabledToMouseEvents()) {
                    dragLabel(e);
                }
            }

            public void mouseMoved(MouseEvent e) {
            }
        });

        jTable1.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                //Se o mouse for arrastado e a janela está disponível para
                //eventos do mouse, a função de selecionar o label é acionada
                if (isEnabledToMouseEvents()) {
                    clickLabel((javax.swing.JLabel) jTable1.getValueAt(e.getY() / cellHeigth, e.getX() / cellWidth), e);
                }
            }

            public void mouseReleased(final MouseEvent e) {
                //Se o mouse for liberado e a janela estiver disponível para
                //eventos do mouse, uma thread é acionada com a função de liberação
                //de uma label. Trava também a janela para eventos do mouse pois o computador jogará logo em seguida
                //Este método roda em uma thread diferente por questões técnicas do swing.
                //Se estivesse na mesma thread de onde roda a janela, esta não seria
                //atualizada durante a função drop como deve ocorrer e, sim, somente após ela.
                //Após o drop, a janela pode novamente receber eventos do mouse.
                if (isEnabledToMouseEvents()) {
                    setEnabledToMouseEvents(false);
                    Thread thread = new Thread() {

                        @Override
                        public void run() {
                            dropLabel(e);
                            setEnabledToMouseEvents(true);
                        }
                    };
                    thread.start();
                }
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        //======================================================================================================

        //Criar o jogo de verdade na engine
        //Perceba que desenhar nao esta ligado ao jogo em si.
        setGame(new Game(playerName));
        turnHuman = true;

        game.getComputer().setName("Xadrezonator");

        printMessage("Nome do jogador: " + playerName + "\n");

        //Imprime a mensagem dizendo que o jogo começou com o jogador (informa o nome do jogador)
        printMessage("Jogo começou com: " + game.getHuman().getName() + "\n");

        //Desenha o tabuleiro
        drawChessboard(game.getChessboard());

        /*Iniciando o Label que controlara as cores e colocando a cor dele de fundo como c*/
        coloredLabel = new javax.swing.JLabel();
        coloredLabel.setBackground(c);
        enabledToMouseEvents = true;
    }

    /**
     * Funcao que e chamada quando temos um carregamento de um label
     * o jLabel4 carrega a que voce esta movendo e o jLabel5 armazena
     * o ultimo label passado
     * @param e Evento de drag do mouse
     */
    private void dragLabel(java.awt.event.MouseEvent e) {
        try {
            /*Verifica se foi uma posicao valida para arrastar, ou seja, tinha peca*/
            if (!invalidLabel) {
                /*Verifica se esta dentro do tabuleiro*/
                if (outOfBounds(new Position(e.getX(), e.getY()))) {
                    return;
                }

                /*Caso ele troque de posicao, temos que colocar a ultima posicao como
                 * estava e armazenar o valor da proxima, note que esse valor esta sempre
                 * armzenado no jLabel5*/
                if (previousX != e.getX() / cellWidth || previousY != e.getY() / cellHeigth) {
                    jTable1.setValueAt(jLabel5, previousY, previousX);
                    previousX = e.getX() / cellWidth;
                    previousY = e.getY() / cellHeigth;
                    jLabel5 = (JLabel) jTable1.getValueAt(e.getY() / cellHeigth, e.getX() / cellWidth);
                    jTable1.setValueAt(jLabel4, e.getY() / cellHeigth, e.getX() / cellWidth);
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Dragging Invalid Position");
            ex.getMessage();
        }
    }

    /**
     * Método que toma os procedimentos para botar uma peça selecionada em uma posição
     * escolhida
     * @param e Evento de liberação do botão do mouse
     */
    private void dropLabel(java.awt.event.MouseEvent e) {
        try {
            /*Quando um Label e largado entao temos que deixar as cores do tabuleiro
             * sem ter nada em destaque, entao e feito isso aqui*/
            jLabel4.setBackground(d);
            jLabel5.setBackground(d);

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    emptyLabel = (JLabel) jTable1.getValueAt(i, j);
                    emptyLabel.setBackground(d);
                    jTable1.setValueAt(emptyLabel, i, j);
                }
            }

            /*Verificamos se e realmente uma peca que foi pega*/
            if (!invalidLabel) {

                /*
                 * O label é então setado como falso para ser usado somente nesta jogada
                 * Até a nova seleção de label (em clickLabel), nenhuma outra função que
                 * precise do jLabel4 poderá será acionada pois ele é inválido
                 */
                invalidLabel = true;

                /* Verificamos se a peca nao foi largada onde foi pega para nao passarmos
                 * o turno */
                if (initialPosition.getX() == e.getX() / cellWidth && initialPosition.getY() == (7 - e.getY() / cellHeigth)) {
                    printMessage("Você não pode largar a peça no mesmo lugar que pegou.\n");
                    finalPosition = translatePosition(initialPosition);
                    jTable1.setValueAt(jLabel4, finalPosition.getY(), finalPosition.getX());
                    return;
                }

                /* Verifica-se se a peça foi largada fora do tabuleiro */
                if (outOfBounds(new Position(e.getX(), e.getY()))) {
                    printMessage("Você não pode largar a peça fora do tabuleiro.\n");
                    jTable1.setValueAt(jLabel5, previousY, previousX);
                    finalPosition = translatePosition(initialPosition);
                    jTable1.setValueAt(jLabel4, finalPosition.getY(), finalPosition.getX());
                    return;
                }

                /* Caso chegue aqui o carregamento em termos de desenho foi valido, verifica-se agora o turno */
                finalPosition = translatePosition(new Position(e.getX() / cellWidth, e.getY() / cellHeigth));
                if (turnHuman) {

                    boolean legalMove = false; //Booleano que indicará se a jogada foi legal

                    /*Verificamos se o movimento foi valido para um jogo de xadrez*/
                    try {

                        //Realiza a jogada e verifica se o movimento foi um En Passant
                        boolean enPassant = game.play(game.getHuman(), initialPosition, finalPosition);

                        legalMove = true; //Chegou aqui sem nenhuma exceção capturada, portanto o movimento foi legal

                        //Caso o movimento tenha sido realizado com sucesso, armazena-se a peça para propósitos de desenho
                        Piece piece = game.getPieceAtChessboard(finalPosition);

                        //Verifica se o movimento é um En Passant
                        String specialMove = null;
                        if (enPassant) {
                            //Se for, imprime a mensagem e desenha o movimento
                            specialMove = EN_PASSANT_STRING;
                            drawEnPassant(translatePosition(finalPosition), turnHuman);
                        }

                        //Imprime o movimento realizado
                        printMove(piece, initialPosition, finalPosition, specialMove);

                        //Se a peça for um peão, avalia se é hora de promover
                        if (piece instanceof Pawn) {
                            if (game.timeToPromote((Pawn) piece)) {
                                //Se é hora de promover o peão, cria a janela de promoção
                                PromotionJFrame promotionWindow = new PromotionJFrame(this.getBounds());
                                promotionWindow.setVisible(true);

                                //Desenha a promoção
                                finalPosition = translatePosition(finalPosition);
                                jTable1.setValueAt(interfacePromotePawn(promotionWindow.getSelectedPiece(), piece), finalPosition.getY(), finalPosition.getX());
                            }
                        }

                        jButton1.setEnabled(false);

                        //Verifica agora o estado do jogo e toma providências necessárias (vide função checkState)
                        int state = checkState(turnHuman);
                        if (state != -1 && state != CHECK) {
                            dispose();
                        }

                        //Turno do humano é setado como falso pois é a vez do computador jogar
                        turnHuman = false;

                        //Bloqueia os eventos do mouse pois é agora vez do computador jogar
                        setEnabledToMouseEvents(false);

                        //Imprime a mensagem de que o computador está pensando
                        printMessage(game.getComputer().getName() + " está pensando.\n");

                        //Realiza a jogada do computador
                        computerPerformMove();

                    } catch (InvalidMoveException ex) {
                        //Exceção lançada caso o Movimento não tenha seguido as regras do xadrez
                        printMessage("Movimento Inválido \n");
                    } catch (DoesntBelongToPlayerException ex) {
                        //Exceção lançada caso o Movimento tenha sido feito com uma peça que não pertença ao jogador
                        printMessage("Essa peça não te pertence \n");
                    } catch (InvalidMoveIsInCheckException ex) {
                        //Exceção lançada caso o jogador tenha tentado se mover para um estado de xeque
                        printMessage("Movimento Inválido: você não pode ficar em xeque \n");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (!legalMove) {
                        //Se posicao invalida retornar o label para a posicao inicial
                        initialPosition = translatePosition(initialPosition);
                        jTable1.setValueAt(jLabel4, initialPosition.getY(), initialPosition.getX());
                        if (outOfBounds(new Position(e.getX(), e.getY()))) {
                            jTable1.setValueAt(new javax.swing.JLabel(), previousY, previousX);
                            return;
                        }
                        finalPosition = translatePosition(finalPosition);
                        jTable1.setValueAt(jLabel5, finalPosition.getY(), finalPosition.getX());
                    }
                } 

            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Dropped in a Invalid Position");
            ex.getMessage();
        }
    }

    /**
     * Método que verifica os estados do jogo (Xeque-mate, Empate e xeque)
     * Se estiver em alguns desses estados, toma as medidas necessárias
     * (abertura de janelas ou impressão de mensagens)
     * @param humanTurn Informa quem jogou. Se for o turno do ser humano, verifica se o computador está em xeque-mate
     * @return Inteiro informando qual estado foi encontrado. Se não estiver em nenhum dos estados terminais, retorna -1.
     */
    private int checkState(boolean humanTurn) {
        //Constante para definir qual foi a escolha para avaliar xeque e xeque-mate
        final int HUMAN = 1000;
        final int COMPUTER = 2000;

        int choice;
        Player avaliatedPlayer;
        if (humanTurn) {
            avaliatedPlayer = game.getComputer();
            choice = COMPUTER;
        } else {
            avaliatedPlayer = game.getHuman();
            choice = HUMAN;
        }

        if (game.isInMate(avaliatedPlayer)) {
            //Caso esteja, cria a janela de vitória e encerra a função
            if (choice == COMPUTER) {
                YouWinWindow victoryWindow = new YouWinWindow(this.getBounds());
                victoryWindow.setVisible(true);
            } else {
                YouLoseWindow defeatWindow = new YouLoseWindow(this.getBounds());
                defeatWindow.setVisible(true);
            }
            return CHECK_MATE;
        }
        //Verifica-se se o jogo está empatado
        if (game.matchDraw()) {
            //Caso esteja, cria a janela de empate e encerra a função
            DrawWindow drawWindow = new DrawWindow(this.getBounds());
            drawWindow.setVisible(true);
            return DRAW_MATCH;
        }
        //Verfica-se se o jogador está em xeque
        if (game.isInCheck(avaliatedPlayer)) {
            if (choice == HUMAN) {
                printMessage(game.getHuman().getName() + " está em xeque! \n");
            } else {
                printMessage("Xadrezonator está em xeque! \n");
            }
            return CHECK;
        }

        return -1;
    }

    /**
     * Neste método a interface chama a IA e desenha o movimento do computador
     */
    private void computerPerformMove() {
        boolean enPassant = false;
        move computerAction = game.computerMove();
        Position initialComputerPosition = computerAction.getposini();
        Position finalComputerPosition = computerAction.getposfin();
        boolean computerMoved = true;
        try {
            enPassant = game.play(game.getComputer(), initialComputerPosition, finalComputerPosition);
        } catch (DoesntBelongToPlayerException ex) {
            computerMoved = false;
            Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidMoveException ex) {
            computerMoved = false;
            Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyPositionException ex) {
            computerMoved = false;
            Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPositionException ex) {
            computerMoved = false;
            Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidMoveIsInCheckException ex) {
            computerMoved = false;
            Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (computerMoved) {
            Position drawInitialPosition = translatePosition(initialComputerPosition);
            Position drawFinalPosition = translatePosition(finalComputerPosition);
            drawMove(drawInitialPosition, drawFinalPosition);
            Piece piece = getGame().getPieceAtChessboard(finalComputerPosition);

            //Verifica se o movimento é um En Passant
            String specialMove = null;
            if (enPassant) {
                //Se for, desenha o movimento
                specialMove = EN_PASSANT_STRING;
                drawEnPassant(translatePosition(finalComputerPosition), turnHuman);
            }

            printMove(piece, initialComputerPosition, finalComputerPosition, specialMove);

            //A IA sempre promove o peão para uma rainha
            //Neste momento verifica-se, caso a peça seja um peçao, se é hora
            //de promover
            if (piece instanceof Pawn) {
                if (game.timeToPromote((Pawn) piece)) {
                    //É hora de promover

                    //Dá-se um identificador à nova rainha
                    int number = game.getNumberOfPieces(Piece.QUEEN_TYPE, Piece.BLACK) + 1;
                    String id = "bq" + number;
                    
                    //Chama o método de promoção da engine
                    game.promotePawn((Pawn) piece, new Queen(game.getComputer(), Piece.BLACK, piece.getPosition(), id));
                    
                    //Desenha
                    Position position = translatePosition(piece.getPosition());
                    JLabel newLabel = new JLabel();
                    newLabel.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "/src/view/resource/black/queen.png"));
                    jTable1.setValueAt(newLabel, position.getY(), position.getX());
                }
            }

            int state = checkState(turnHuman);
            if (state != -1 && state != CHECK) {
                dispose();
            }

            printMessage("Turno de " + game.getHuman().getName() + " \n");

            if (game.availLongCastling(game.getHuman()) ||
                    game.availShortCastling(game.getHuman())) {
                jButton1.setEnabled(true);
                printMessage(game.getHuman().getName() + " pode executar o roque. \n");
            } else {
                jButton1.setEnabled(false);
            }

            turnHuman = true;
        }
    }

    /**
     * Método responsável por selecionar a label a ser arrastada e destacar as posições para onde ela pode ir
     * @param labelIn
     * @param e Evento de pressionamento do botão do mouse
     */
    private void clickLabel(javax.swing.JLabel labelIn, java.awt.event.MouseEvent e) {
        /*Armazena a ultima posicao, no caso do clique e a primeira*/
        previousX = e.getX() / cellWidth;
        previousY = e.getY() / cellHeigth;

        /* Sempre temos que criar um Label novo, porque senao a interface reage
         * de uma maneira nao controlada (vale para todos os labels)*/
        jLabel4 = new javax.swing.JLabel();

        try {
            jLabel4.setIcon(labelIn.getIcon());
            jLabel5 = new javax.swing.JLabel();
            invalidLabel = false;
            initialPosition = translatePosition(new Position(e.getX() / cellWidth, e.getY() / cellHeigth));

            Piece p = game.getChessboard().getPiece(initialPosition);

            if (jLabel4.getIcon() == null) {
                invalidLabel = true;
            } else {
                try {

                    if (!p.belongsTo(game.getHuman())) {
                        //Se a peça não é do jogador, não se deve colorir nenhuma posição
                        //e o label deve ser inválido
                        invalidLabel = true;
                        return;
                    }

                    /*Processo para colorir as casas que podem ser visitadas*/
                    LinkedList<Position> positions = game.getValidDestinationPositions(p);

                    for (Position r : positions) {
                        r = translatePosition(r);
                        coloredLabel = (JLabel) jTable1.getValueAt(r.getY(), r.getX());
                        coloredLabel.setBackground(c);
                        jTable1.setValueAt(coloredLabel, r.getY(), r.getX());
                    }
                } catch (java.util.NoSuchElementException ex) {
                    System.out.println("Empty List");
                }
            }
        } catch (NullPointerException ex) {
            System.out.println("Invalid Label");
            invalidLabel = true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Desenha o Roque Curto
     * @param human Booleano que informa se é o humano que está fazendo o roque
     */
    public void drawShortCastling(boolean human) {
        Position initialRookPosition;
        Position destRookPosition;
        Position initialKingPosition;
        Position destKingPosition;
        Piece king;

        if (human) {
            Position kingPositionAtChessboard = new Position(6, 0);
            king = game.getPieceAtChessboard(kingPositionAtChessboard);
            initialKingPosition = translatePosition(new Position(4, 0));
            destKingPosition = translatePosition(new Position(6, 0));
            initialRookPosition = translatePosition(new Position(7, 0));
            destRookPosition = translatePosition(new Position(5, 0));
        } else {
            Position kingPositionAtChessboard = new Position(6, 7);
            king = game.getPieceAtChessboard(kingPositionAtChessboard);
            initialRookPosition = translatePosition(new Position(7, 7));
            destRookPosition = translatePosition(new Position(5, 7));
            initialKingPosition = translatePosition(new Position(4, 7));
            destKingPosition = translatePosition(new Position(6, 7));
        }

        printMove(king, null, null, SHORT_CASTLING_STRING);
        drawMove(initialKingPosition, destKingPosition);
        drawMove(initialRookPosition, destRookPosition);
    }

    /**
     * Desenha o Roque Longo
     * @param human Booleano que informa se é o humano que está fazendo o roque
     */
    public void drawLongCastling(boolean human) {
        Position initialRookPosition;
        Position destRookPosition;
        Position initialKingPosition;
        Position destKingPosition;
        Piece king;

        if (human) {
            Position kingPositionAtChessboard = new Position(2, 0);
            king = game.getPieceAtChessboard(kingPositionAtChessboard);
            initialRookPosition = translatePosition(new Position(0, 0));
            destRookPosition = translatePosition(new Position(3, 0));
            initialKingPosition = translatePosition(new Position(4, 0));
            destKingPosition = translatePosition(new Position(2, 0));
        } else {
            Position kingPositionAtChessboard = new Position(2, 7);
            king = game.getPieceAtChessboard(kingPositionAtChessboard);
            initialRookPosition = translatePosition(new Position(0, 7));
            destRookPosition = translatePosition(new Position(3, 7));
            initialKingPosition = translatePosition(new Position(4, 7));
            destKingPosition = translatePosition(new Position(2, 7));
        }

        printMove(king, null, null, LONG_CASTLING_STRING);
        drawMove(initialKingPosition, destKingPosition);
        drawMove(initialRookPosition, destRookPosition);
    }

    /**
     * Desenha o movimento
     * @param initialPos Posição inicial do movimento
     * @param finalPos Posição final
     */
    private void drawMove(Position initialPos, Position finalPos) {
        JLabel pieceLabel = (JLabel) jTable1.getValueAt(initialPos.getY(), initialPos.getX());
        jTable1.setValueAt(pieceLabel, finalPos.getY(), finalPos.getX());

        emptyLabel = new javax.swing.JLabel();
        jTable1.setValueAt(this.emptyLabel, initialPos.getY(), initialPos.getX());
    }

    /**
     * Desenha o En Passant. Este método serve para eliminar a peça capturada
     * pelo movimento do En Passant.
     * @param position Posição da peça eliminada
     * @param humanTurn Booleano que informa se é a vez do humano jogar, ou seja, diz quem realizou o en passant
     */
    private void drawEnPassant(Position position, boolean humanTurn) {
        emptyLabel = new javax.swing.JLabel();
        if (humanTurn) {
            jTable1.setValueAt(emptyLabel, position.getY() + 1, position.getX());
        } else {
            jTable1.setValueAt(emptyLabel, position.getY() - 1, position.getX());
        }
    }

    /**
     * Imprime mensagem do jogo. Se é o turno de alguém, se alguém está em xeque, xeque-mate, etc.
     * @param message Mensagem a ser impressa
     */
    private void printMessage(String message) {
        jTextArea2.setText(jTextArea2.getText() + "----------\n" + message);
        try {
            jTextArea2.setCaretPosition(jTextArea2.getText().length());
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Método que imprime o movimento. Exemplo - Xadrezonator: Cavalo moveu de B8 para C6
     * @param piece Peça envolvida no movimento
     * @param initialPos Posição inicial do movimento (No formato de posição da engine e não da Interface)
     * @param finalPos Posição final do movimento (No formato de posição da engine e não da Interface)
     * @param specialMove Informa se ocorreu algum movimento especial (Promoção, Roque ou En Passant)
     */
    public void printMove(Piece piece, Position initialPos,
            Position finalPos, String specialMove) {
        String message = "";
        if (specialMove != null) {
            if (specialMove.equals(SHORT_CASTLING_STRING) ||
                    specialMove.equals(LONG_CASTLING_STRING)) {
                message = piece.getOwnerName() + ": executou o " + specialMove + "\n";
            } else if (specialMove.equals(PROMOTE_PAWN_STRING)) {
                message = piece.getOwnerName() + ": executou a " + specialMove + " para " + piece.getName() + "\n";
            } else if (specialMove.equals(EN_PASSANT_STRING)) {
                message = piece.getOwnerName() + ": executou o " + specialMove + "\n";
            }
        } else {
            message = piece.getOwnerName() + ": ";
            Action playerAction = game.getLastAction();
            if (!playerAction.hasAnAttackedPiece()) {
                message += piece.getName() + " moveu de " + convertPosition(initialPos) +
                        " para " + convertPosition(finalPos) + "\n";
            } else {
                Piece enemyPiece = playerAction.getAttackedPiece();
                message += " atacou a peça " + enemyPiece.getName() + " na posição " +
                        convertPosition(finalPos) + "\n";
            }
        }
        jTextArea1.setText(jTextArea1.getText() + "----------\n" + message);

        try {
            jTextArea1.setCaretPosition(jTextArea1.getText().length());
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Converte a posição do movimento para uma String representando esta posição
     * nas convenções do xadrez
     * @param pos Posição a ser convertida
     * @return Uma String contendo o nome da posição nas convenções do xadrez
     */
    private String convertPosition(Position pos) {
        String stringPosition = "";
        switch (pos.getX()) {
            case 0:
                stringPosition = "A";
                break;
            case 1:
                stringPosition = "B";
                break;
            case 2:
                stringPosition = "C";
                break;
            case 3:
                stringPosition = "D";
                break;
            case 4:
                stringPosition = "E";
                break;
            case 5:
                stringPosition = "F";
                break;
            case 6:
                stringPosition = "G";
                break;
            case 7:
                stringPosition = "H";
                break;
        }
        stringPosition += (pos.getY() + 1);
        return stringPosition;
    }

    /**
     * Traduz a posicao do tabuleiro para a do jogo e vice-versa
     * utilizado sempre que tiver que mandar algo da interface para o
     * chessboard ou o contrario
     * @param p Posição a ser traduzida
     * @return Posição já traduzida
     */
    public Position translatePosition(Position p) {
        Position position = new Position();
        position.setY(7 - p.getY());
        position.setX(p.getX());
        return position;
    }

    /**
     * Verifica se a posicao esta fora do tabuleiro
     * @param p Posição testada
     * @return Booleano informando se a posição está fora do tabuleiro
     */
    private Boolean outOfBounds(Position p) {
        if (p.getX() >= 8 * (cellWidth) || p.getY() >= 8 * (cellHeigth) || p.getX() < 0 || p.getY() < 0) {
            return true;
        }
        return false;
    }

    /**
     * Função que desenha um tabuleiro da Engine
     * @param cb Tabuleiro a ser desenhado
     */
    private void drawChessboard(Chessboard cb) {
        Position p;
        Piece piece;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                javax.swing.JLabel label = new javax.swing.JLabel();
                p = new Position(i, j);
                piece = cb.getPiece(p);

                if (piece != null) {
                    if (piece.isWhite()) {
                        label.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "/src/view/resource/white/" + iconizePiece(piece)));
                    } else {
                        label.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "/src/view/resource/black/" + iconizePiece(piece)));
                    }

                }

                p = translatePosition(p);
                jTable1.setValueAt(label, p.getY(), p.getX());

            }
        }
    }

    /**
     * Funcao que retorna o nome da imagem da peca
     * @param piece Peça testada
     * @return Nome da imagem da peça
     */
    private String iconizePiece(Piece piece) {
        if (piece instanceof Pawn) {
            return "pawn.png";
        } else if (piece instanceof Rook) {
            return "rook.png";
        } else if (piece instanceof Horse) {
            return "horse.png";
        } else if (piece instanceof Bishop) {
            return "bishop.png";
        } else if (piece instanceof Queen) {
            return "queen.png";
        } else {
            return "king.png";
        }
    }

    /**
     * Método que verifica qual peça foi escolhida para se promover um dado peão e
     * chama o método de promoção na engine
     * @param selectedPiece Peça escolhida para promover o peão
     * @param pawn Peão que será promovido
     * @return Um Label contendo a imagem da peça selecionada
     */
    private javax.swing.JLabel interfacePromotePawn(int selectedPiece, Piece pawn) {

        javax.swing.JLabel newLabel = new JLabel();
        int number;
        String id;
        switch (selectedPiece) {
            case BISHOP:
                number = game.getNumberOfPieces(Piece.BISHOP_TYPE, Piece.WHITE) + 1;
                id = "bq" + number;
                getGame().promotePawn((Pawn) pawn, new Bishop(pawn.getOwner(), Piece.WHITE, pawn.getPosition(), id));
                newLabel.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "/src/view/resource/white/bishop.png"));
                break;
            case HORSE:
                number = game.getNumberOfPieces(Piece.HORSE_TYPE, Piece.WHITE) + 1;
                id = "bq" + number;
                getGame().promotePawn((Pawn) pawn, new Horse(pawn.getOwner(), Piece.WHITE, pawn.getPosition(), id));
                newLabel.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "/src/view/resource/white/horse.png"));
                break;
            case QUEEN:
                number = game.getNumberOfPieces(Piece.QUEEN_TYPE, Piece.WHITE) + 1;
                id = "bq" + number;
                getGame().promotePawn((Pawn) pawn, new Queen(pawn.getOwner(), Piece.WHITE, pawn.getPosition(), id));
                newLabel.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "/src/view/resource/white/queen.png"));
                break;
            case ROOK:
                number = game.getNumberOfPieces(Piece.ROOK_TYPE, Piece.WHITE) + 1;
                id = "bq" + number;
                getGame().promotePawn((Pawn) pawn, new Rook(pawn.getOwner(), Piece.WHITE, pawn.getPosition(), id));
                newLabel.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "/src/view/resource/white/rook.png"));
                break;
        }

        return newLabel;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Xadrezonator");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "A", "B", "C", "D", "E", "F", "G", "H"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setMaximumSize(new java.awt.Dimension(640, 640));
        jTable1.setMinimumSize(new java.awt.Dimension(640, 640));
        jTable1.setPreferredSize(new java.awt.Dimension(600, 640));
        jTable1.setRowHeight(80);
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable1);

        jLabel1.setText("Tabuleiro:");

        jButton2.setText("Reiniciar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("Roque");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Mensagens:");

        jTextArea2.setEditable(false);
        jScrollPane2.setViewportView(jTextArea2);

        jTextArea1.setEditable(false);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel2.setText("Últimas Jogadas:");

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 12));
        jLabel6.setText("8");

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 0, 12));
        jLabel7.setText("7");

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 0, 12));
        jLabel8.setText("6");

        jLabel9.setFont(new java.awt.Font("Trebuchet MS", 0, 12));
        jLabel9.setText("5");

        jLabel10.setFont(new java.awt.Font("Trebuchet MS", 0, 12));
        jLabel10.setText("4");

        jLabel11.setFont(new java.awt.Font("Trebuchet MS", 0, 12));
        jLabel11.setText("3");

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 0, 12));
        jLabel12.setText("2");

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 0, 12));
        jLabel13.setText("1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10)
                                .addComponent(jLabel9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)))
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 659, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(138, 138, 138))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)
                                .addGap(24, 24, 24))))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1)
                                    .addComponent(jButton2))
                                .addGap(11, 11, 11))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método que trata de realizar o roque caso o botão seja selecionado
     * @param evt Evento de botão selecionado
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        final CastlingJFrame castlingOption = new CastlingJFrame(this.game, this.game.getHuman(), this.getBounds());
        castlingOption.setVisible(true);

        setEnabledToMouseEvents(false);

        Thread thread = new Thread() {

            @Override
            public void run() {
                if (castlingOption.getChosenCastling() == -1) {
                    return;
                } else if (castlingOption.getChosenCastling() == InterfaceConstants.LONG_CASTLING) {
                    game.executeLongCastling(game.getHuman());
                    setTurnHuman(false);
                    drawLongCastling(true);
                } else if (castlingOption.getChosenCastling() == InterfaceConstants.SHORT_CASTLING) {
                    game.executeShortCastling(game.getHuman());
                    setTurnHuman(false);
                    drawShortCastling(true);
                }
                disableCastlingButton();
                printMessage(game.getComputer().getName() + " está pensando.\n");
                if (game.isInCheck(game.getComputer())) {
                    printMessage("O computador está em xeque! \n");
                }
                computerPerformMove();
                invalidLabel = true;
                setEnabledToMouseEvents(true);
            }
        };
        thread.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Método que trata de realizar a reinicialização do jogo caso o botão seja selecionado
     * @param evt Evento de botão selecionado
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ReinitializeWindow reinitializeWindow = new ReinitializeWindow(this.getBounds());
        reinitializeWindow.setVisible(true);

        if (reinitializeWindow.isReinitialize()) {
            String playerName = getGame().getHuman().getName();
            MainJFrame newMainJFrame = new MainJFrame(playerName);
            newMainJFrame.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    //GETTERS E SETTERS
    //==========================================================================
    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setTurnHuman(Boolean turnHuman) {
        this.turnHuman = turnHuman;
    }

    public void disableCastlingButton() {
        this.jButton1.setEnabled(false);
    }

    public JTable getjTable1() {
        return jTable1;
    }

    public boolean isEnabledToMouseEvents() {
        return enabledToMouseEvents;
    }

    public void setEnabledToMouseEvents(boolean enabledToMouseEvents) {
        this.enabledToMouseEvents = enabledToMouseEvents;
    }
    //==========================================================================

    private Game game;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane jTextArea1;
    private javax.swing.JTextPane jTextArea2;
    // End of variables declaration//GEN-END:variables
}
