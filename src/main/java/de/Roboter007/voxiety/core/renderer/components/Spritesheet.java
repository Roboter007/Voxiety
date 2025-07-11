package de.Roboter007.voxiety.core.renderer.components;

import de.Roboter007.voxiety.core.renderer.Asset;
import de.Roboter007.voxiety.core.renderer.textures.SpriteTexture;
import de.Roboter007.voxiety.core.renderer.textures.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet implements Asset {

    public Texture texture;
    private final List<Sprite> sprites;

    public Spritesheet(String path, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        this.sprites = new ArrayList<>();

        this.texture = new Texture(path);
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for (int i=0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite(this.texture, texCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public void deleteSpritesheet() {
        for(Sprite sprite : sprites) {
            SpriteTexture texture = sprite.texture();
            if(texture != null) {
                texture.deleteTexture();
            }
        }
    }

    @Override
    public String path() {
        return texture.path();
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }
}
