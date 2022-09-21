package fr.gameurduxvi.crystal.components;

import java.awt.*;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class JPlaceholderTextField extends JTextField {

    private String ph;

    public JPlaceholderTextField(String ph) {
        this.ph = ph;
    }

    public JPlaceholderTextField() {
        this.ph = null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (super.getText().length() > 0 || ph == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.gray);
        g2.drawString(ph, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
    }
}