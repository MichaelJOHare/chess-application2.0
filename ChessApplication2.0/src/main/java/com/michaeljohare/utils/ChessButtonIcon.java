package com.michaeljohare.utils;

import javax.swing.*;
import java.awt.*;

public class ChessButtonIcon extends ImageIcon {
    private final float opacity;

    public ChessButtonIcon(Image image, float opacity) {
        super(image);
        this.opacity = opacity;
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        super.paintIcon(c, g2d, x, y);
        g2d.dispose();
    }
}
