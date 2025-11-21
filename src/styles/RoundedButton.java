package styles;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int cornerRadius;
    private Color backgroundColor;
    private Color hoverColor;
    private boolean isHovered = false;

    public RoundedButton(String text, int cornerRadius) {
        super(text);
        this.cornerRadius = cornerRadius;
        this.backgroundColor = getBackground();
        this.hoverColor = backgroundColor.darker();

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        super.setBackground(bg);
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        Color bgColor = isHovered ? hoverColor : backgroundColor;
        // Darken when pressed for simple press feedback
        if (getModel().isPressed()) {
            bgColor = bgColor.darker();
        }
        g2.setColor(bgColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Draw the text ourselves to guarantee it's visible over the rounded background
        String text = getText();
        if (text != null && !text.isEmpty()) {
            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int stringWidth = fm.stringWidth(text);
            int stringAscent = fm.getAscent();
            int x = (getWidth() - stringWidth) / 2;
            int y = (getHeight() + stringAscent) / 2 - fm.getDescent();
            g2.drawString(text, x, y);
        }

        g2.dispose();
        // Do not call super.paintComponent(g) because we handled background and text
        // drawing.
    }

    @Override
    public boolean contains(int x, int y) {
        return new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius).contains(x, y);
    }
}
