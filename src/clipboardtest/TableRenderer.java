//package clipboardtest;
//
//import java.awt.Color;
//import java.awt.Component;
//
//
//import javax.swing.JLabel;
//import javax.swing.JTable;
//import javax.swing.table.TableCellRenderer;
// 
//public class TableRenderer extends JLabel implements TableCellRenderer {
//    @SuppressWarnings("compatibility:2959625216677687967")
//    private static final long serialVersionUID = 2396073896765662342L;
//
//    Color color = Color.red;
//    int r = 0;
//
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
//                                                   int row, int column) {
//        setOpaque(true);
//        setBackground(color != null && r == row ? color : Color.white);
//        setText(value == null ? "" : value.toString());
//        return this;
//    }
//}