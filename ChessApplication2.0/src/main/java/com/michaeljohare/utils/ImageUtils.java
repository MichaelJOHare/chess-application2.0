package com.michaeljohare.utils;

import com.michaeljohare.controller.GUIController;
import com.michaeljohare.model.pieces.PieceType;
import com.michaeljohare.model.player.Player;
import com.michaeljohare.view.ChessBoardPanel;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {
    private static final Map<String, BufferedImage> cachedImages = new HashMap<>();

    public static BufferedImage getResizedImage(GUIController guiController, PieceType type, Player player, int width, int height) throws IOException {
        String key = (player.isWhite() ? "White_" : "Black_") + type;

        if (!cachedImages.containsKey(key)) {
            String imagePath = "/png_icons/" + key + ".png";
            URL imageUrl = ChessBoardPanel.class.getResource(imagePath);

            if (imageUrl != null) {
                BufferedImage pieceImage = ImageIO.read(imageUrl);
                BufferedImage resizedImage = Thumbnails.of(pieceImage)
                        .size(width, height)
                        .asBufferedImage();
                cachedImages.put(key, resizedImage);
            } else {
                guiController.imageAccessError();
            }
        }

        return cachedImages.get(key);
    }
}
