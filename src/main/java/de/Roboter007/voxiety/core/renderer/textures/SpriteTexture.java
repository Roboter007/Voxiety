package de.Roboter007.voxiety.core.renderer.textures;

import de.Roboter007.voxiety.core.renderer.Asset;

public interface SpriteTexture extends Asset {

    void bind();

    void unbind();

    int getWidth();

    int getHeight();

    void setWidth(int width);

    void setHeight(int height);

    int getID();

    void deleteTexture();


    @Override
    int hashCode();

    @Override
    String toString();

}
