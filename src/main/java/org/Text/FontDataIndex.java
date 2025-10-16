package org.Text;

import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.Objects;

public enum FontDataIndex {

    Jersey_25("/Font/Jersey_25.ttf"),
    Font_2(""),
    Font_3("");

    private static final double DEFAULT_FONT_SIZE = 12.0;
    private final Font font;

    FontDataIndex(String fontPath) {
        Font loadedFont = null;
        System.out.println(FontDataIndex.class.getSimpleName() + " | Loading font \"" + fontPath + "\"");
        try {
            InputStream stream = Objects.requireNonNull(FontDataIndex.class.getResourceAsStream(fontPath));
            loadedFont = Font.loadFont(stream, DEFAULT_FONT_SIZE);
        } catch (NullPointerException e) {
            System.err.println(FontDataIndex.class.getSimpleName() + " | Error loading font " + fontPath + ": " + e.getMessage());
        }
        font = loadedFont;
    }

    public Font getFont() {
        return font;
    }

    public String getFontName() {
        return font.getName();
    }

}
