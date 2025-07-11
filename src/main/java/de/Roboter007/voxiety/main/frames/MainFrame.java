package de.Roboter007.voxiety.main.frames;

import de.Roboter007.voxiety.core.cameras.Camera;
import de.Roboter007.voxiety.core.renderer.VoxElement;
import de.Roboter007.voxiety.core.renderer.components.Sprite;
import de.Roboter007.voxiety.core.renderer.components.SpriteRenderer;
import de.Roboter007.voxiety.core.renderer.textures.Glyph;
import de.Roboter007.voxiety.core.renderer.textures.Texture;
import de.Roboter007.voxiety.core.renderer.textures.Transform;
import de.Roboter007.voxiety.main.VoxWindow;
import de.Roboter007.voxiety.main.world.blocks.Blocks;
import de.Roboter007.voxiety.utils.VoxAssets;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MainFrame extends Frame {

    public MainFrame() {}

    public static boolean loaded = false;

    @Override
    public void init() {
        renderTexture("stone.png", 0, 0, -1, VoxWindow.tileSize(), VoxWindow.tileSize());

        renderTexture(Blocks.STONE_BLOCK.path(), 128f, 128f, Blocks.STONE_BLOCK.zIndex(), (int) Blocks.STONE_BLOCK.width(), (int) Blocks.STONE_BLOCK.height());

        this.camera = new Camera(new Vector2f());
        System.out.println("Element List: " + this.voxElementList);

        /*Text text = new Text();
        text.addText("Deine MUDDA!!!", VoxFonts.YOUTHANASIA_FONT, 24, new Vector4f());

        for(VoxElement voxElement : text.voxElementList(new Vector2f(100, 100))) {
            renderTexture(voxElement);
        } */

        Glyph glyph = new Glyph('d', 20);
        VoxElement voxElement = new VoxElement(new Transform(new Vector2f(100, 100), new Vector2f(100, 100)), 999);
        voxElement.addComponent(new SpriteRenderer(new Sprite(glyph)));

        renderTexture(voxElement);
    }


    public void update(float delta) {
        //System.out.println("FPS: " + (1.0f / delta));

        for(VoxElement vc : this.voxElementList) {
            vc.update(delta);
        }

        this.renderer.render();
    }
}
