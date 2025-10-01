import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GeoGebra extends JFrame {

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public GeoGebra() {
        initComponents();
    }

    public String insertStr(String firstStr, int startIndex, int endIndex, String insertedStr) {
        String finalStr = "";
        boolean isInserted = false;
        for (int i = 0; i < firstStr.length(); i++) {
            if (i < startIndex || i > endIndex) {
                finalStr += firstStr.charAt(i);
            } else if (isInserted == false) {
                finalStr += insertedStr;
                isInserted = true;
            }
        }
        return finalStr;
    }

    public String lowC(char operator, int i, String expr) {
        String temp = "";
        int j = i, k = i;
        while (j < expr.length() - 1) {
            if (!(expr.charAt(j + 1) == '+' || expr.charAt(j + 1) == '*'
                    || expr.charAt(j + 1) == '/' || expr.charAt(j + 1) == '^')) {
                j++;
            } else {
                break;
            }
        }
        while (k > 0) {
            if (!(expr.charAt(k - 1) == '+' || expr.charAt(k) == '-' || expr.charAt(k - 1) == '*'
                    || expr.charAt(k - 1) == '/' || expr.charAt(k - 1) == '^')) {
                k--;
            } else {
                break;
            }
        }
        switch (operator) {
            case '+':
                temp = (Double.parseDouble(expr.substring(k, i)) + Double.parseDouble(expr.substring(i + 1, j + 1))) + "";
                break;
            case '*':
                temp = (Double.parseDouble(expr.substring(k, i)) * Double.parseDouble(expr.substring(i + 1, j + 1))) + "";
                break;
            case '/':
                temp = (Double.parseDouble(expr.substring(k, i)) / Double.parseDouble(expr.substring(i + 1, j + 1))) + "";
                break;
            case '^':
                temp = Math.pow(Double.parseDouble(expr.substring(k, i)), Double.parseDouble(expr.substring(i + 1, j + 1))) + "";
                break;
        }
        expr = insertStr(expr, k, j, temp);
        this.setIndex(k);
        return expr;
    }

    public int highC(String expr) {
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '^') {
                expr = lowC(expr.charAt(i), i, expr);
                i = this.getIndex();
            }
        }
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '*' || expr.charAt(i) == '/') {
                expr = lowC(expr.charAt(i), i, expr);
                i = this.getIndex();
            }
        }
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '+') {
                expr = lowC(expr.charAt(i), i, expr);
                i = this.getIndex();
            }
        }
        return (int) Double.parseDouble(expr);
    }

    public String swapX(String expr, double x) {
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == 'x') {
                expr = insertStr(expr, i, i, x + "");
            }
        }
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '-' && expr.charAt(i + 1) == '-') {
                expr = insertStr(expr, i, i + 1, "");
            }
        }
        return expr;
    }

    public int funcX(String expr, double x) {
        int y = -1 * highC(swapX(expr, x));
        return y;
    }

    private boolean isFinalCharOperator() {
        return "+-*/^".contains("" + lblExpr.getText().charAt(lblExpr.getText().length() - 1));
    }

    private void writeOperator(char operator) {
        boolean condition = lblExpr.getText().isEmpty() || isFinalCharOperator();
        if (!condition ^ operator == '-') {
            lblExpr.setText(lblExpr.getText() + operator);
        }
    }

    private void writeFactor(char factor) {
        if (lblExpr.getText().isEmpty() ||
                (factor != 'x' && lblExpr.getText().charAt(lblExpr.getText().length() - 1) != 'x')
                || (factor == 'x' && isFinalCharOperator())) {
            lblExpr.setText(lblExpr.getText() + factor);
        }
    }

    private boolean isInputFactor(char c) {
        return c >= '0' && c <= '9' || c == 'x';
    }

    private boolean isInputOperator(char c) {
        return "+-*/^".contains(c + "");
    }

    private void inputCheck(char c) {
        if (isInputFactor(c)) {
            writeFactor(c);
        } else if (isInputOperator(c)) {
            writeOperator(c);
        } else if (c == (char) 8 && !lblExpr.getText().isEmpty()) {
            lblExpr.setText(lblExpr.getText().substring(0, lblExpr.getText().length() - 1));
        } else if (c == (char) 10) {
            drawGraph();
        }
    }

    private void drawGraph() {
        int x = 0, y = 0, y1 = 0;
        String expr = lblExpr.getText();
        Graphics g = pnlGraph.getGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, pnlGraph.getWidth(), pnlGraph.getHeight());
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(pnlGraph.getWidth() / 2, 0, pnlGraph.getWidth() / 2, pnlGraph.getHeight());
        g.drawLine(0, pnlGraph.getHeight() / 2, pnlGraph.getWidth(), pnlGraph.getHeight() / 2);
        g.setColor(Color.RED);
        if (!expr.equals("")) {
            for (x = -1 * pnlGraph.getWidth() / 2; x < pnlGraph.getWidth() / 2; x += 1) {
                y = funcX(expr, x);
                y1 = funcX(expr, x + 1);
                g.drawLine(x + pnlGraph.getWidth() / 2, y + pnlGraph.getHeight() / 2, x + 1 + pnlGraph.getWidth() / 2, y1 + pnlGraph.getHeight() / 2);
            }
        }
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GeoGebra().setVisible(true);
            }
        });
    }

    private int index = 0;
    private JButton btnComp;
    private JButton btnDeg;
    private JButton btnDel;
    private JButton btnDiv;
    private JButton btnMin;
    private JButton btnMulti;
    private JButton btnPlus;
    private JLabel lblExpr;
    private JPanel pnlGraph;
    private JPanel pnlTable;

    private void initComponents() {

        pnlTable = new JPanel();
        pnlTable.setFocusable(false);
        lblExpr = new JLabel();
        lblExpr.setFocusable(false);
        btnPlus = new JButton();
        btnPlus.setFocusable(false);
        btnMin = new JButton();
        btnMin.setFocusable(false);
        btnMulti = new JButton();
        btnMulti.setFocusable(false);
        btnDiv = new JButton();
        btnDiv.setFocusable(false);
        btnDeg = new JButton();
        btnDeg.setFocusable(false);
        btnDel = new JButton();
        btnDel.setFocusable(false);
        btnComp = new JButton();
        btnComp.setFocusable(false);
        pnlGraph = new JPanel();
        pnlGraph.setFocusable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JComponent content = ((JComponent) this.getContentPane());
        content.setFocusable(true);
        content.requestFocusInWindow();

        content.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                inputCheck(evt.getKeyChar());
            }
        });

        btnPlus.setText("+");
        btnPlus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputCheck('+');
            }
        });

        btnMin.setText("-");
        btnMin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputCheck('-');
            }
        });

        btnMulti.setText("*");
        btnMulti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputCheck('*');
            }
        });

        btnDiv.setText("/");
        btnDiv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputCheck('/');
            }
        });

        btnDeg.setText("^");
        btnDeg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputCheck('^');
            }
        });

        btnDel.setText("Backspace");
        btnDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputCheck((char) 8);
            }
        });

        btnComp.setText("Show graph");
        btnComp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                drawGraph();
            }
        });

        GroupLayout pnlTableLayout = new GroupLayout(pnlTable);
        pnlTable.setLayout(pnlTableLayout);
        pnlTableLayout.setHorizontalGroup(
                pnlTableLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTableLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlTableLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblExpr, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(pnlTableLayout.createSequentialGroup()
                                                .addComponent(btnPlus, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnMin, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnMulti, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnDiv, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnDeg, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnDel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnComp, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        pnlTableLayout.setVerticalGroup(
                pnlTableLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlTableLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblExpr, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlTableLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(pnlTableLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnPlus, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnMin, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnMulti, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnDiv, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnDeg, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(btnComp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnDel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        GroupLayout pnlGraphLayout = new GroupLayout(pnlGraph);
        pnlGraph.setLayout(pnlGraphLayout);
        pnlGraphLayout.setHorizontalGroup(
                pnlGraphLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 750, Short.MAX_VALUE)
        );
        pnlGraphLayout.setVerticalGroup(
                pnlGraphLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 408, Short.MAX_VALUE)
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlTable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlGraph, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pnlTable, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlGraph, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }
}
