package org.jaudiotagger.tag.images;

import java.io.IOException;

/**
 * BufferedImage methods
 * <p>
 * Not compatible with Android, delete from your source tree.
 */
public class Images {
    public static Object getImage(Artwork artwork) throws IOException {
        return artwork.getImage();
    }
}
