package de.Roboter007.voxiety.main.frames;

import de.Roboter007.voxiety.core.cameras.Camera;
import de.Roboter007.voxiety.core.renderer.VoxElement;
import de.Roboter007.voxiety.main.VoxWindow;
import de.Roboter007.voxiety.main.world.World;
import de.Roboter007.voxiety.main.world.Worlds;
import de.Roboter007.voxiety.main.world.blocks.Block;
import de.Roboter007.voxiety.main.world.entities.Player;
import de.Roboter007.voxiety.utils.JavaUtils;
import de.Roboter007.voxiety.utils.VoxAssets;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class GameFrame extends Frame {

    public static Player player = new Player("Roboter007", VoxWindow.tileSize(), VoxWindow.tileSize(), 10);
    public static List<Block> blocksToRender = new ArrayList<>();

    public GameFrame() {
    }

    @Override
    public void init() {
        this.camera = player.getCamera();
        /*
    }
        for (int dx = 0; dx < 40; dx++) {
            for (int dy = 0; dy < 30; dy++) {
                System.out.println("X: " + dx + ", Y: " + dy);
                int x, y;
                x = (VoxWindow.access().tileSize() * dx);
                y = (VoxWindow.access().tileSize() * dy);
                renderElement("grass_block.png", x, y, -1, VoxWindow.access().tileSize(), VoxWindow.access().tileSize());
            }
        } */

        Worlds.loadWorlds();
        if(!Worlds.isRegistered("Test")) {
            World testWorld = new World("Test", 100, 100);
            Worlds.register(testWorld);
            player.changeWorld(testWorld);
        } else {
            World testWorld = Worlds.byName("Test");
            player.changeWorld(testWorld);
        }

        blocksInWorld = blocksInWorld();
        for(Block block : blocksInWorld) {
            renderTexture(block.voxElement());
        }

        renderPlayer(player);
        System.out.println("Element List: " + this.voxElementList);

        //player.drawGameFrame(this);
    }

    public List<Block> blocksInWorld;

    @Override
    public void update(float delta) {
        this.camera = player.getCamera();
        System.out.println("Fps: " + (1.0f / delta));

        for(VoxElement vc : this.voxElementList) {
            vc.update(delta);
        }

        player.update();


        this.renderer.render();
    }

    //ToDo: Texture Batching mit Render Batch / VoxRenderer nutzen -> may be eine Sprite List, welche die Blöcke rendered
    public List<Block> blocksInWorld() {
        World world = player.world();

        List<Block> blocks = new ArrayList<>();

        Block[][] blocksInWorld = world.getBlocks();

        for (int x = 0; x < blocksInWorld.length; x++) {
            for (int y = 0; y < blocksInWorld[x].length; y++) {
                Block block = blocksInWorld[x][y];
                if(block != null) {
                    int tileSize = VoxWindow.tileSize();

                    int worldX = x * tileSize;
                    int worldY = y * tileSize;

                    Vector4f tmp = new Vector4f(worldX, worldY, 0, 1);
                    //tmp.mul(VoxWindow.frame().camera().getProjectionMatrix()).mul(VoxWindow.frame().camera().getViewMatrix());

                    float screenX = tmp.x;
                    float screenY = tmp.y;

                    block.setScreenPosition(new Vector2f((float) player.getPosition().x + screenX, (float) player.getPosition().y + screenY));
                    block.setWidth(VoxWindow.tileSize());
                    block.setHeight(VoxWindow.tileSize());

                    blocks.add(block);
                    //renderTexture(block.voxElement());
                }
            }
        }

        return blocks;
    }

    public boolean contains(int x, int y, int w, int h, int X, int Y) {
        if ((w | h) < 0) {
            return false;
        }
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        h += y;

        return ((w < x || w > X) &&
                (h < y || h > Y));
    }
}
