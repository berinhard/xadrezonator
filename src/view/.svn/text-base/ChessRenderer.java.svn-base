/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Alberto Scremin
 */
public class ChessRenderer implements TableCellRenderer{

    private javax.swing.JLabel jLabel1 = new DefaultTableCellRenderer();
    
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (column % 2 == 0){
            if (row % 2 == 0)
                jLabel1.setBackground(Color.GRAY);
            else
                jLabel1.setBackground(Color.BLACK);
        }
        else
        {
            if (row % 2 == 0)
                jLabel1.setBackground(Color.BLACK);
            else
                jLabel1.setBackground(Color.GRAY);
        }
        
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        
        
        try{
            jLabel2 = (JLabel) value;
            jLabel1.setIcon(jLabel2.getIcon());
        }
        catch(Exception e){
            jLabel1.setIcon(null);
        }
        
        try{
            jLabel2 = (JLabel) value;
            
            if (jLabel2.getBackground().getAlpha() == 200){
                jLabel1.setBackground(jLabel2.getBackground());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return jLabel1;
    }

}
