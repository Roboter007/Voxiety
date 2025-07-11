package de.Roboter007.voxiety.main.world.entities;

import de.Roboter007.voxiety.Voxiety;
import de.Roboter007.voxiety.core.cameras.Camera;
import de.Roboter007.voxiety.core.keys.KeyBinds;
import de.Roboter007.voxiety.core.listeners.KeyListener;
import de.Roboter007.voxiety.core.renderer.components.Sprite;
import de.Roboter007.voxiety.core.renderer.components.SpriteRenderer;
import de.Roboter007.voxiety.core.renderer.components.Spritesheet;
import de.Roboter007.voxiety.core.renderer.textures.Texture;
import de.Roboter007.voxiety.core.renderer.textures.TexturePack;
import de.Roboter007.voxiety.main.VoxWindow;
import de.Roboter007.voxiety.main.frames.GameFrame;
import de.Roboter007.voxiety.main.world.World;
import de.Roboter007.voxiety.main.world.blocks.Block;
import de.Roboter007.voxiety.main.world.entities.animation.Direction;
import de.Roboter007.voxiety.main.world.registry.RegistryEntry;
import de.Roboter007.voxiety.utils.VoxAssets;
import de.Roboter007.voxiety.utils.VoxPaths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Player extends LivingEntity {

    private final String name;
    private final Camera camera;

    private List<String> playerData = new ArrayList<>();
    public int[] hotbar = new int[10];

    private int animationCounter = 0;
    //public Block targetedBlock = null;

    //public IntPosition cursor = null;

    public Player(String name, float entityWidth, float entityHeight, int zIndex) {
        super(name, new Vector2f(0, 0), entityWidth, entityHeight, zIndex, 15);
        setDefault();

        this.camera = new Camera(new Vector2f());
        this.name = name;

        float x = (Camera.TARGETED_WINDOW_WIDTH - entityWidth) / 2f;
        float y = (Camera.TARGETED_WINDOW_HEIGHT - entityHeight) / 2f;


        this.screenPosition = new Vector2f(x, y);


        System.out.println("Player X: " + x + ", Y: " + y);

        // - moves picture right
        // + moves picture left
        // picture -> player texture
        //this.camera = new Camera(new Vector2f( - ((float) VoxWindow.windowWidth() / 2) - (width / 2),  -height /2));

        /*collisionBox = new CollisionBox(this.world);
        collisionBox.x = 8;
        collisionBox.y = 16;
        collisionBox.width = 32;
        collisionBox.height = 32; */

        this.loadVoxElement();
    }

    public String getName() {
        return name;
    }

    public Camera getCamera() {
        return camera;
    }

    public int getSelectedSlot() {
        return 1; /*keyHandler.selectedSlot; */
    }

    public int getBlockIdFromSelectedSlot() {
        return hotbar[getSelectedSlot()];
    }

    public void setBlockIdFromSelectedSlot(int id) {
        hotbar[getSelectedSlot()] = id;

        if(playerData.isEmpty()) {
            playerData.add("0000000000");
        }

        String hotbarContent = this.playerData.getFirst();
        if (hotbarContent != null && !hotbarContent.isBlank()) {
            char[] chars = hotbarContent.toCharArray();
            chars[getSelectedSlot()] = (char) (id + '0');
            hotbarContent = new String(chars);
        } else {
            StringBuilder invInfoBuilder = new StringBuilder();
            for (int i : hotbar) {
                invInfoBuilder.append(i);
            }
            hotbarContent = invInfoBuilder.toString();
        }
        this.playerData.set(0, hotbarContent);
        savePlayerData();
    }

    public void setDefault() {
        int x = 25;
        int y = 25;
        this.position.set(x * VoxWindow.tileSize(), y * VoxWindow.tileSize());
        speed += 4;
    }

    public void update() {
        if(VoxWindow.frame() instanceof GameFrame) {
            if(isWalking()) {
                if(KeyBinds.WALK_FORWARDS.isPressed()) {
                    direction = Direction.UP;
                    this.position.sub(0, speed);
                }
                if(KeyBinds.WALK_LEFT.isPressed()) {
                    direction = Direction.LEFT;
                    this.position.add(0, speed);
                }
                if(KeyBinds.WALK_BACKWARDS.isPressed()) {
                    direction = Direction.DOWN;
                    this.position.sub(speed, 0);
                }
                if(KeyBinds.WALK_RIGHT.isPressed()) {
                    direction = Direction.RIGHT;
                    this.position.add(speed, 0);
                }

                System.out.println("New Pos: " + this.position.x + " - " + this.position.y);

                super.update();

            } else {
                if (animationCounter == 20) {
                    animationState = 1;
                    animationCounter = 0;
                }

                animationCounter++;
            }

            updateSprite();
        }
    }

    @Override
    public void updateSprite() {
        //screenPosition = new Vector2f(2 * ((float) (VoxWindow.windowWidth() / 2) - (width /2)), ((float) (VoxWindow.windowHeight() / 2) - (height /2)));

        //screenPosition = new Vector2f();

        super.updateSprite();
    }

    public boolean isWalking() {
//        return KeyListener.keyPressed(GLFW.GLFW_KEY_W) || KeyListener.keyPressed(GLFW.GLFW_KEY_A) || KeyListener.keyPressed(GLFW.GLFW_KEY_S) || KeyListener.keyPressed(GLFW.GLFW_KEY_D);
        return KeyBinds.WALK_FORWARDS.isPressed() || KeyBinds.WALK_LEFT.isPressed() || KeyBinds.WALK_BACKWARDS.isPressed() || KeyBinds.WALK_RIGHT.isPressed();
    }

    public Sprite sprite() {
        int id = 0;

        if (this.direction == Direction.UP) {
            id = 5 + animationState;
        } else if (this.direction == Direction.LEFT) {
            id = 1 + animationState;
        } else if (this.direction == Direction.DOWN) {
            id = -1 + animationState;
        } else if (this.direction == Direction.RIGHT) {
            id = 3 + animationState;
        }


        Spritesheet spritesheet = this.spritesheet("napoleon_spritesheet.png");
        if (spritesheet == null) {
            System.out.println("Spritesheet for player not found!");
            return new Sprite((Texture) VoxAssets.getDefaultTP().locateAsset("test.png"));
        }

        Sprite sprite = spritesheet.getSprite(id);

        if(sprite == null) {
            System.out.println("Error Sprite for player is null!");
        }

        //System.out.println(sprite + "-texID: ");

        return sprite;
    }

    public Path getPlayerDataFolder() {
        if(this.world != null) {
            return Path.of(world.getWorldFolder().toString(), "/playerData");
        } else {
            return null;
        }
    }

    public Path getPlayerData() {
        Path playerDataFolder = getPlayerDataFolder();
        if(playerDataFolder != null) {
            playerDataFolder.toFile().mkdirs();
            return Path.of(playerDataFolder.toString(),getName() + ".json");
        } else {
            return null;
        }
    }

    public void createPlayerDataFile() {
        try {
            Path playerDataPath = getPlayerData();
            if(!playerDataPath.toFile().exists()) {
                if (getPlayerData().toFile().createNewFile()) {
                    System.out.println("File created: " + getPlayerData().toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readPlayerData() {
        if(getPlayerData().toFile().exists()) {
            System.out.println("Reading PlayerData...");
            this.playerData = VoxPaths.access().readLines(getPlayerData().toString());

            System.out.println("Player Data: " + this.playerData);
        }
    }

    public void savePlayerData() {
        Thread saveThread = new Thread(() -> {
            try {
                Files.write(getPlayerData(), playerData);
                System.out.println("saved Player Data: " + playerData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        Voxiety.SERVICE.submit(saveThread);

    }

    public boolean showPauseScreen;
    public boolean showInfoScreen;

    @Override
    public void tick() {

    }

    /*public void drawPlayer(Graphics2D graphics2D) {
        pathLocation.drawImage(graphics2D, screenPos.getX(), screenPos.getY());
        if(VSKey.DEBUG_COLLISION.isPressed()) {
            graphics2D.setColor(Color.BLUE);
            graphics2D.drawRect(screenPos.getX() + collisionBox.x, screenPos.getY() + collisionBox.y, collisionBox.width, collisionBox.height);
        }
    }

    public void drawSelectionBar(Graphics2D graphics2D) {
        Texture texture = Texture.voxelSociety("gui", "hotbar.png", 180 * VoxelPanel.scale, 18 * VoxelPanel.scale);
        RecCalculator recCalculator = new RecCalculator();
        recCalculator.calc(180 * VoxelPanel.scale, 18 * VoxelPanel.scale);

        graphics2D.setColor(newColorWithAlpha(Color.decode("#9d9d9d"), 127));
        graphics2D.fillRect(recCalculator.getX(), VoxelPanel.screenHeight - 100, 180 * VoxelPanel.scale, 18 * VoxelPanel.scale);

        for (int slot = 0; slot < 10; slot++) {
            BlockEntry entry = BlockRegistry.getBlockById(hotbar[slot]);
            if(entry.getBlockId() != 0) {
                entry.getTexture().drawImage(graphics2D, recCalculator.getX() + (18 * VoxelPanel.scale) * slot + VoxelPanel.scale, VoxelPanel.screenHeight - 100 + VoxelPanel.scale);
            }
        }

        texture.drawImage(graphics2D, recCalculator.getX(), VoxelPanel.screenHeight - 100);

        graphics2D.setColor(Color.RED);
        Stroke oldStroke = graphics2D.getStroke();

        graphics2D.setStroke(new BasicStroke(4));
        graphics2D.drawRect(recCalculator.getX() + (18 * VoxelPanel.scale) * getSelectedSlot(), VoxelPanel.screenHeight - 100, VoxelPanel.scale * 18, VoxelPanel.scale * 18);
        graphics2D.setStroke(oldStroke);

    } */

    public static Color newColorWithAlpha(Color original, int alpha) {
        return new Color(original.getRed(), original.getGreen(), original.getBlue(), alpha);
    }

    /*@Override
    public void updateTexture() {
        Texture newTextureLocation = Texture.voxelSociety("player", "player_" + this.animationState.getName() + "_" + spriteNumber + ".png");
        if(!newTextureLocation.equals(pathLocation)) {
            pathLocation = newTextureLocation;
        }
    } */

    public void updateInv() {
        int i = 0;
        if(!this.playerData.isEmpty() && this.playerData.getFirst() != null && !this.playerData.getFirst().isEmpty()) {
            for (char c : this.playerData.getFirst().toCharArray()) {
                this.hotbar[i] = Integer.parseInt(c + "");
                i++;
            }
        } else {
            this.hotbar = new int[]{0,0,0,0,0,0,0,0,0,0};
        }
    }

    public boolean isInWorld() {
        return this.world != null;
    }

    public void changeWorld(World toWorld) {
        if(this.world != null) {
            this.world.stop();
        }
        this.world = toWorld;
        readPlayerData();
        this.createPlayerDataFile();

        updateInv();

        toWorld.start();
    }

}
