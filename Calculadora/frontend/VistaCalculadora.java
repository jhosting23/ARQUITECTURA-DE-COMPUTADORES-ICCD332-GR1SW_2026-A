package Calculadora.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class VistaCalculadora extends JFrame {

    // ── Colors  (Blues palette × Grises palette) ──────────────────────────────
    // BG: Gris Antracita (#3C3C3C) + subtono naval → pizarra azul oscuro
    private static final Color BG           = new Color(50, 63, 80);
    // Display: Gris Ártico (#E8E8E8) + tinte #DFE9F5 → azul-blanco suave
    private static final Color DISPLAY_BG   = new Color(232, 241, 250);
    // Números: Gris Siberia (#D0D0D0) + tinte #C5D4EB → gris-azul claro
    private static final Color BTN_NUM      = new Color(210, 224, 240);
    // CE: #007AA2  (teal medio de la paleta azul)
    private static final Color BTN_CE       = new Color(0, 122, 162);
    // C:  #002F5C  (azul marino profundo, Gris Lobo + navy)
    private static final Color BTN_C        = new Color(0, 47, 92);
    // =:  #009DCF  (cian brillante de la paleta azul)
    private static final Color BTN_EQ       = new Color(0, 157, 207);
    // Hex: #90B1DB  (azul-gris medio, Gris Pizarra + blue)
    private static final Color BTN_HEX      = new Color(144, 177, 219);
    // Base normal: #004987 (azul medio-oscuro)
    private static final Color BTN_BASE     = new Color(0, 73, 135);
    // Base seleccionada: #001A5F (azul más oscuro de la paleta)
    private static final Color BTN_BASE_SEL = new Color(0, 26, 95);
    // Texto input: #00216D (navy oscuro legible)
    private static final Color TEXT_DARK    = new Color(0, 33, 109);
    // Texto resultado: #008DC0 (cian de la paleta)
    private static final Color TEXT_RESULT  = new Color(0, 141, 192);
    // Texto error: rojo ladrillo (contraste necesario fuera de la paleta)
    private static final Color TEXT_ERROR   = new Color(180, 50, 50);

    // ── Bases ──────────────────────────────────────────────────────────────────
    private static final Integer[] BASES     = {2, 8, 10, 16};
    private static final String[] BASE_SHORT = {"BIN", "OCT", "DEC", "HEX"};
    private static final String[] BASE_LONG  = {"Binario", "Octal", "Decimal", "Hexadecimal"};

    // ── State ──────────────────────────────────────────────────────────────────
    private String inputBuffer = "";
    private int    baseOrigen  = 10;
    private int    baseDestino = 2;

    // ── UI ─────────────────────────────────────────────────────────────────────
    private JLabel         labelInfo;
    private JLabel         labelInput;
    private JLabel         labelResult;
    private JButton        botonConvertir;
    private JButton[]      baseBtnsOrigen  = new JButton[4];
    private JButton[]      baseBtnsDestino = new JButton[4];
    private List<DigitBtn> digitButtons    = new ArrayList<>();

    // ══════════════════════════════════════════════════════════════════════════
    public VistaCalculadora() {
        setTitle("Conversión de Bases");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG);

        initComponents();
        pack();
        setLocationRelativeTo(null);
        refreshDisplay();
        refreshBaseButtons();
        refreshDigitStates();
    }

    // ── Layout ─────────────────────────────────────────────────────────────────

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        add(buildDisplay(), BorderLayout.NORTH);
        add(buildPad(),     BorderLayout.CENTER);
    }

    private JPanel buildDisplay() {
        JPanel p = new JPanel(new BorderLayout(0, 3));
        p.setBackground(DISPLAY_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 122, 162)),
            BorderFactory.createEmptyBorder(12, 16, 10, 16)
        ));
        p.setPreferredSize(new Dimension(350, 108));

        labelInfo = new JLabel("De: Decimal (10)   →   A: Binario (2)");
        labelInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelInfo.setForeground(new Color(0, 122, 162));

        labelInput = new JLabel("0", SwingConstants.RIGHT);
        labelInput.setFont(new Font("Segoe UI", Font.BOLD, 40));
        labelInput.setForeground(TEXT_DARK);

        labelResult = new JLabel("", SwingConstants.RIGHT);
        labelResult.setFont(new Font("Consolas", Font.PLAIN, 15));
        labelResult.setForeground(TEXT_RESULT);

        p.add(labelInfo,   BorderLayout.NORTH);
        p.add(labelInput,  BorderLayout.CENTER);
        p.add(labelResult, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildPad() {
        JPanel pad = new JPanel(new GridBagLayout());
        pad.setBackground(BG);
        pad.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.BOTH;
        c.weightx = 1; c.weighty = 1;
        c.insets  = new Insets(4, 4, 4, 4);

        Dimension dNum = new Dimension(58, 52);
        Dimension dWid = new Dimension(120, 52);
        Dimension dBas = new Dimension(58, 38);

        // ── Row 0: 7  8  9  [CE]  [C] ─────────────────────────────────────────
        slot(pad, c, numBtn("7"),        0, 0); slot(pad, c, numBtn("8"), 1, 0);
        slot(pad, c, numBtn("9"),        2, 0);
        slot(pad, c, actionBtn("CE", BTN_CE, e -> backspace()), 3, 0);
        slot(pad, c, actionBtn("C",  BTN_C,  e -> clearAll()),  4, 0);

        // ── Row 1: 4  5  6  A  B ──────────────────────────────────────────────
        slot(pad, c, numBtn("4"), 0, 1); slot(pad, c, numBtn("5"), 1, 1);
        slot(pad, c, numBtn("6"), 2, 1);
        slot(pad, c, hexBtn("A"), 3, 1); slot(pad, c, hexBtn("B"), 4, 1);

        // ── Row 2: 1  2  3  C  D ──────────────────────────────────────────────
        slot(pad, c, numBtn("1"), 0, 2); slot(pad, c, numBtn("2"), 1, 2);
        slot(pad, c, numBtn("3"), 2, 2);
        slot(pad, c, hexBtn("C"), 3, 2); slot(pad, c, hexBtn("D"), 4, 2);

        // ── Row 3: [  0  ]  E  F  [gap] ───────────────────────────────────────
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; c.gridheight = 1;
        JButton b0 = numBtn("0"); b0.setPreferredSize(dWid); pad.add(b0, c);
        c.gridwidth = 1;
        slot(pad, c, hexBtn("E"), 2, 3); slot(pad, c, hexBtn("F"), 3, 3);
        c.gridx = 4; c.gridy = 3; pad.add(new JPanel() {{ setOpaque(false); }}, c);

        // ── Row 4: base origen ─────────────────────────────────────────────────
        for (int i = 0; i < 4; i++) {
            baseBtnsOrigen[i] = baseBtn(BASE_SHORT[i], BASES[i], true, dBas);
            slot(pad, c, baseBtnsOrigen[i], i, 4);
        }
        c.gridx = 4; c.gridy = 4; c.gridwidth = 1; c.gridheight = 1;
        pad.add(miniLabel("DE:"), c);

        // ── Row 5: base destino ────────────────────────────────────────────────
        for (int i = 0; i < 4; i++) {
            baseBtnsDestino[i] = baseBtn(BASE_SHORT[i], BASES[i], false, dBas);
            slot(pad, c, baseBtnsDestino[i], i, 5);
        }
        c.gridx = 4; c.gridy = 5; c.gridwidth = 1; c.gridheight = 1;
        pad.add(miniLabel("A:"), c);

        // ── Row 6: = CONVERTIR ────────────────────────────────────────────────
        botonConvertir = eqBtn();
        c.gridx = 0; c.gridy = 6; c.gridwidth = 5; c.gridheight = 1;
        c.insets = new Insets(4, 4, 6, 4);
        pad.add(botonConvertir, c);

        return pad;
    }

    // ── Button factories ───────────────────────────────────────────────────────

    private JButton numBtn(String digit) {
        int val = digit.charAt(0) - '0';
        DigitBtn btn = new DigitBtn(digit, val, BTN_NUM, TEXT_DARK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(58, 52));
        btn.addActionListener(e -> appendDigit(digit));
        digitButtons.add(btn);
        return btn;
    }

    private JButton hexBtn(String letter) {
        int val = letter.charAt(0) - 'A' + 10;
        DigitBtn btn = new DigitBtn(letter, val, BTN_HEX, TEXT_DARK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(58, 52));
        btn.addActionListener(e -> appendDigit(letter));
        digitButtons.add(btn);
        return btn;
    }

    private JButton actionBtn(String label, Color color, ActionListener action) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()  ? color.darker()   :
                           getModel().isRollover() ? color.brighter() : color;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(58, 52));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false);      btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private JButton baseBtn(String name, int base, boolean isOrigen, Dimension size) {
        JButton btn = new JButton(name) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = isOrigen ? baseOrigen == base : baseDestino == base;
                Color bg = sel ? BTN_BASE_SEL :
                           getModel().isPressed()  ? BTN_BASE.darker()   :
                           getModel().isRollover() ? BTN_BASE.brighter() : BTN_BASE;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(size);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false);      btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            if (isOrigen) { baseOrigen = base; inputBuffer = ""; labelResult.setText(""); }
            else          { baseDestino = base; }
            refreshDisplay(); refreshBaseButtons(); refreshDigitStates();
        });
        return btn;
    }

    private JButton eqBtn() {
        JButton btn = new JButton("=   CONVERTIR") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()  ? BTN_EQ.darker()   :
                           getModel().isRollover() ? BTN_EQ.brighter() : BTN_EQ;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btn.setForeground(new Color(55, 42, 8));
        btn.setPreferredSize(new Dimension(300, 48));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false);      btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel miniLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(new Color(174, 211, 227));   // #AED3E3 — gris-ártico azulado, visible sobre BG oscuro
        return l;
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private void slot(JPanel p, GridBagConstraints c, Component comp, int x, int y) {
        c.gridx = x; c.gridy = y; c.gridwidth = 1; c.gridheight = 1;
        p.add(comp, c);
    }

    private void appendDigit(String digit) {
        inputBuffer += digit;
        refreshDisplay();
        refreshDigitStates();
    }

    private void backspace() {
        if (!inputBuffer.isEmpty()) {
            inputBuffer = inputBuffer.substring(0, inputBuffer.length() - 1);
            refreshDisplay(); refreshDigitStates();
        }
    }

    private void clearAll() {
        inputBuffer = "";
        labelResult.setText("");
        refreshDisplay(); refreshDigitStates();
    }

    private void refreshDisplay() {
        labelInput.setText(inputBuffer.isEmpty() ? "0" : inputBuffer);
        int oi = baseIdx(baseOrigen), di = baseIdx(baseDestino);
        labelInfo.setText("De: " + BASE_LONG[oi] + " (" + baseOrigen + ")"
                        + "   →   "
                        + "A: " + BASE_LONG[di] + " (" + baseDestino + ")");
    }

    private void refreshBaseButtons() {
        for (JButton b : baseBtnsOrigen)  b.repaint();
        for (JButton b : baseBtnsDestino) b.repaint();
    }

    private void refreshDigitStates() {
        for (DigitBtn db : digitButtons) {
            boolean ok = db.digitValue < baseOrigen;
            db.setEnabled(ok);
        }
    }

    private int baseIdx(int base) {
        for (int i = 0; i < BASES.length; i++) if (BASES[i] == base) return i;
        return 0;
    }

    // ── DigitBtn inner class ───────────────────────────────────────────────────

    private class DigitBtn extends JButton {
        final int   digitValue;
        final Color bgColor;

        DigitBtn(String text, int val, Color bg, Color fg) {
            super(text);
            this.digitValue = val;
            this.bgColor    = bg;
            setForeground(fg);
            setContentAreaFilled(false); setBorderPainted(false);
            setFocusPainted(false);      setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (!isEnabled()) {
                g2.setColor(new Color(198, 200, 207));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            } else {
                Color fill = getModel().isPressed()  ? bgColor.darker()   :
                             getModel().isRollover() ? new Color(245,248,255) : bgColor;
                GradientPaint gp = new GradientPaint(0, 0, fill, 0, getHeight(),
                        fill.equals(bgColor) ? new Color(163, 196, 228) : fill.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawRoundRect(0, getHeight() - 4, getWidth() - 1, 3, 4, 4);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Public API (ControlCalculadora) ───────────────────────────────────────

    public String getTextoEntrada() { return inputBuffer; }
    public int    getBaseOrigen()   { return baseOrigen; }
    public int    getBaseDestino()  { return baseDestino; }

    public void setTexto(String resultado) {
        labelResult.setText("→  " + resultado);
        labelResult.setForeground(TEXT_RESULT);
    }

    public void mostrarError(String mensajeError) {
        labelResult.setText("Error");
        labelResult.setForeground(TEXT_ERROR);
        JOptionPane.showMessageDialog(this, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void addListenerConvertidor(ActionListener listener) {
        botonConvertir.addActionListener(listener);
    }
}
