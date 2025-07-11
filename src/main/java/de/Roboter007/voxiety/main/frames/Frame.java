package de.Roboter007.voxiety.main.frames;

import de.Roboter007.voxiety.core.cameras.Camera;
import de.Roboter007.voxiety.core.renderer.components.Spritesheet;
import de.Roboter007.voxiety.core.renderer.textures.Texture;
import de.Roboter007.voxiety.core.renderer.textures.Transform;
import de.Roboter007.voxiety.core.renderer.VoxElement;
import de.Roboter007.voxiety.core.renderer.VoxRenderer;
import de.Roboter007.voxiety.core.renderer.components.Sprite;
import de.Roboter007.voxiety.core.renderer.components.SpriteRenderer;
import de.Roboter007.voxiety.main.world.blocks.Block;
import de.Roboter007.voxiety.main.world.blocks.Blocks;
import de.Roboter007.voxiety.main.world.entities.Player;
import de.Roboter007.voxiety.utils.VoxAssets;
import de.Roboter007.voxiety.utils.VoxPaths;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class Frame {

    public VoxRenderer renderer = new VoxRenderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<VoxElement> voxElementList = new ArrayList<>();

    public Frame() {

    }

    public void init() {

    }

    public void start() {
        for(VoxElement voxElement : voxElementList) {
            voxElement.start();
            this.renderer.add(voxElement);
        }
        isRunning = true;
    }

    public void addVoxElementToFrame(VoxElement voxElement) {
        //System.out.println("Pos: " + voxElement.transform.position.x + " - " + voxElement.transform.position.y);
        //System.out.println("Scale: " + voxElement.transform.scale.x + " - " + voxElement.transform.scale.y);
        //System.out.println("Path: " + voxElement.getComponent(SpriteRenderer.class).getTexture().path());
        voxElementList.add(voxElement);
        if(isRunning) {
            voxElement.start();
            this.renderer.add(voxElement);
        }
    }


    public VoxElement renderPlayer(Player player) {
        VoxElement voxElement = player.voxElement();
        voxElement.addComponent(new SpriteRenderer(player.sprite()));
        this.addVoxElementToFrame(voxElement);
        return voxElement;
    }

    public VoxElement renderTexture(String name, float x, float y, int zIndex, int width, int height) {
        VoxElement voxElement = new VoxElement(new Transform(new Vector2f(x, y), new Vector2f(width, height)), zIndex);
        voxElement.addComponent(new SpriteRenderer(new Sprite((Texture) VoxAssets.getAsset(name))));
        this.addVoxElementToFrame(voxElement);
        return voxElement;
    }

    public void renderBlocks(List<Block> blocks) {
        for(Block block : blocks) {
            this.addVoxElementToFrame(block.voxElement());
        }
    }

    public VoxElement renderTexture(VoxElement voxElement, String name) {
        voxElement.addComponent(new SpriteRenderer(new Sprite((Texture) VoxAssets.getAsset(name))));
        this.addVoxElementToFrame(voxElement);
        return voxElement;
    }

    public void renderTexture(VoxElement voxElement) {
        if (voxElement == null) {
            System.out.println("WARNING: Tried to render null VoxElement!");
            return;
        }
        this.addVoxElementToFrame(voxElement);
    }

    public void reloadFrame() {
        this.voxElementList.clear();
        this.renderer.clearBatches();

        this.init();
        this.start();
    }

    public abstract void update(float delta);

    public Camera camera() {
        return camera;
    }
}
