import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import java.awt.FontMetrics;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class FormInst extends JFrame {
 
    @SuppressWarnings("compatibility:769315096204669757")
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel bottomPane;
    private JTable table;
    private static DefaultTableModel model;
    private JLabel textLabel1;
    private JLabel textLabel2;
    
    private JPanel leftPane;
    private static DefaultTableModel SuplModel;
    private HashMap <String, Integer> SuplList;
    private Set<Map.Entry<String, Integer>> setSuplList;
    
    private JPanel rightPane;
    protected static String[] excludeListData = {"GOODRAM", "Team", "dvsx86"};
//      , "ProLogix", "Axle",  "SVEN ????", "??313", "Team-?????????",
//      "Gembird UA" };

    public static void CreateGUI() {
        EventQueue.invokeLater( new Runnable() {
            public void run() {
                try {
                    FormInst GUIForm = new FormInst();
                    GUIForm.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private FormInst() {
        JLabel textLabel3;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1150, 650);
        setTitle("Отправка сервисного товара");
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if(column <= 2) return false;
                    return true;
                };
            };
        model.addColumn("    Поставщик    ");
        model.addColumn("    Состояние    ");
        model.addColumn("    Дата    ");
        model.addColumn("RMA");
        model.addColumn("Наименование товра");

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        table = new JTable(model) {
                public TableCellRenderer getCellRenderer(int row, int column) {
                    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
                    Date rowDate = null;
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                    try {
                        rowDate = format.parse( getValueAt(row, 2).toString() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Calendar rightNow = Calendar.getInstance();
                    Date currentDate = new Date();
                    rightNow.setTime(currentDate);
                    rightNow.add(Calendar.DAY_OF_MONTH, -14);
                    Date weekAgo = rightNow.getTime();
                    if (rowDate.before(weekAgo)){
                        renderer.setBackground(new Color(0xE2, 0xCE, 0xCE)); //F2DEDE
                        return renderer;
                    }
                    rightNow.setTime(currentDate);
                    rightNow.add(Calendar.DAY_OF_MONTH, -7);
                    weekAgo = rightNow.getTime();
                    if (rowDate.before(weekAgo)){
                        renderer.setBackground(new Color(0xEC, 0xE8, 0xD3)); //FCF8E3
                        return renderer;
                    }
                    rightNow.setTime(currentDate);
                    rightNow.add(Calendar.DAY_OF_MONTH, -3);
                    weekAgo = rightNow.getTime();
                    if (rowDate.before(weekAgo)){
                        renderer.setBackground(new Color(0xD9, 0xED, 0xF7)); //D9EDF7
                        return renderer;
                    }
//                    renderer.setBackground(Color.WHITE);
//                    return renderer;
                    return super.getCellRenderer(row, column);
                }
            };
        table.setGridColor(new Color(0x66, 0x66, 0x66, 0x66));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        
        scrollPane.setViewportView(table);

        TableColumn column = null;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);
        String hv = null;
        JTableHeader th;
        FontMetrics fm = null;

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            hv = column.getHeaderValue().toString();
            th = table.getTableHeader();
            fm = th.getFontMetrics(th.getFont());
            column.setPreferredWidth(fm.stringWidth(hv) + 20);
            if (hv.startsWith("Поставщик")){
                column.setPreferredWidth(fm.stringWidth(hv) + 50);
            }
        }
        column.setPreferredWidth(fm.stringWidth(hv) + 300);

        rightPane = new JPanel();
        rightPane.setLayout(new BorderLayout());
        contentPane.add(rightPane, BorderLayout.EAST);

        textLabel3 = new JLabel("не включая:");
        textLabel3.setBorder(new EmptyBorder(0, 5, 5, 5));
        rightPane.add(textLabel3, BorderLayout.BEFORE_FIRST_LINE);
        
        JList rightList = new JList(excludeListData);
        rightPane.add(rightList, BorderLayout.CENTER);
        rightList.setPreferredSize(new Dimension(120, 100));
        rightList.setBorder(new EmptyBorder(5, 5, 5, 5));
        rightList.setForeground(new Color (255, 0, 0));
        rightList.setLayoutOrientation(JList.VERTICAL);
        rightList.setVisibleRowCount(0);        


        leftPane = new JPanel();
        leftPane.setLayout(new BorderLayout());
        leftPane.setBorder(new EmptyBorder(0, 5, 5, 5));
        contentPane.add(leftPane, BorderLayout.WEST);

        JScrollPane leftScrollPane = new JScrollPane();
        leftPane.add(leftScrollPane, BorderLayout.CENTER);
        
        SuplModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if(column == 0) return false;
                    return true;
                };
            };
        SuplModel.addColumn("    Поставщики    ");
        SuplModel.addColumn(" кол-во ");
        
        JTable SuplTable = new JTable(SuplModel);
        SuplTable.setAutoCreateRowSorter(true);
        leftScrollPane.setViewportView(SuplTable);
        leftScrollPane.setPreferredSize(new Dimension(200, 1000));
        leftScrollPane.setBorder(new EmptyBorder(0, 0, 0, 5));
        leftScrollPane.setForeground(new Color (255, 64, 64));
//        SuplTable.addMouseListener(new MouseListener(){
//                @Override
//                public void mousePressed(MouseEvent e) {
//                }
//            });
//        
        bottomPane = new JPanel();
        bottomPane.setLayout(new BorderLayout());
        contentPane.add(bottomPane, BorderLayout.SOUTH);

        JButton btnNewButton = new JButton("Заполнить");
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (true == DataParse.parseClipboard()) {
                    if (SuplList != null){
                        while (SuplModel.getRowCount() > 0) {
                            SuplModel.removeRow(0);
                        }
                        SuplList.clear();
                    }else {
                        SuplList = new HashMap <String, Integer>();
                    }
//                    previousRowSuplier = DataParse.shippingData.SuplierName[0];
                    for ( Shipping sh : DataParse.shippingData ) {
                        String[] data = sh.getData();
                        if (true == SuplList.containsKey( sh.SuplierName )){
                            Integer val = SuplList.get( sh.SuplierName );
                            SuplList.put( sh.SuplierName, val + 1 );
//                            System.out.println(sh.SuplierName + val.toString());
                        }else{
                            SuplList.put( sh.SuplierName, 1);
                        };
                        if (false == hasInExcludeList(sh.SuplierName)) {
                            model.addRow(data);
                        }
                    }
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                    Integer rowCount = model.getRowCount();
                    
                    if (rowCount != 0){
                        textLabel2.setText(rowCount.toString());
                        setSuplList = SuplList.entrySet();
                        for ( Map.Entry <String, Integer> sl : setSuplList ) {
                            Integer value = sl.getValue();
                            SuplModel.addRow( new String[] {sl.getKey(), value.toString()} );
                        }
                    }
                }
            }
        });
        bottomPane.add(btnNewButton, BorderLayout.EAST);

        textLabel1 = new JLabel("Актуально на отправку: ");
        textLabel2 = new JLabel("Не определено");
        bottomPane.add(textLabel1, BorderLayout.WEST);
        bottomPane.add(textLabel2, BorderLayout.CENTER);
    }

    private static boolean hasInExcludeList( String suplier ){
        for( int i=0; i < excludeListData.length; i++){
            if( suplier.equals( excludeListData[i]) ) return true;
        }
        return false;
    }

    public static void addRow(String[] data) {
        model.addRow(data);
//        if (previousRowSuplier.equals(data[0])) {
//            System.out.println(" = ");
//        };
//        previousRowSuplier = data[0];
    }

    public static void clearTableData() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }
}