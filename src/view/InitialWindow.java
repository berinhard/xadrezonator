/*
 * InitialWindow.java
 *
 * Created on 20/10/2009, 16:10:25
 */
package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Gabriel Menezes
 */
public class InitialWindow extends javax.swing.JFrame {

    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Dimension d = tk.getScreenSize();

    /** Creates new form InitialWindow */
    public InitialWindow() {

        initComponents();

        this.setBounds((int) ((d.getWidth() - this.getWidth()) / 2),
                (int) ((d.getHeight() - this.getHeight()) / 4),
                this.getWidth(),
                this.getHeight());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Xadrezonator");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Trebuchet MS", 1, 24));
        jLabel1.setText("Bem vindo ao Xadrezonator!");

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 11));
        jLabel2.setText("Nome:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Trebuchet MS", 0, 14));
        jButton1.setText("Jogar!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                enterButtonTyped(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String playerName = jTextField1.getText();
        //O jogador so consegue entrar se colocar algum nome
        if (playerName.equals("")) {
            return;
        }
        MainJFrame mainWindow = new MainJFrame(playerName);

        mainWindow.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        String playerName = jTextField1.getText();
        //O jogador so consegue entrar se colocar algum nome
        if (playerName.equals("")) {
            return;
        }
        MainJFrame mainWindow = new MainJFrame(playerName);

        mainWindow.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void enterButtonTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_enterButtonTyped
        String playerName = jTextField1.getText();
        //O jogador so consegue entrar se colocar algum nome
        if (playerName.equals("")) {
            return;
        }
        MainJFrame mainWindow = new MainJFrame(playerName);

        mainWindow.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_enterButtonTyped

    /**
     * Este é o método principal do programa
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ImageIcon img = new ImageIcon(System.getProperty("user.dir") +
                        "/src/resource/LogoIC-final-full.jpg");
                JOptionPane.showMessageDialog(null,
                        "Universidade Federal Fluminense \n" +
                        "Ciência da Computação\n" +
                        "Engenharia de Software II \n" +
                        "Prof. Leonardo Murta\n" +
                        "2009 - Segundo Semestre\n\n" +
                        "Projeto de curso: Xadrezonator\n\n" +
                        "Equipe:\n" +
                        "Gabriel da Silva Menezes (Gerente)\n" +
                        "Alberto Martinez Scremin (Programador)\n" +
                        "Bernardo Botelho Fontes (Programador)\n" +
                        "Felipi Euzébio (Programador)\n" +
                        "Rodrigo Trigo (Testes)",
                        "Xadrezonator",
                        JOptionPane.INFORMATION_MESSAGE,
                        img);
                new InitialWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
