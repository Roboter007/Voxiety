package de.Roboter007.voxiety.core.renderer;

import de.Roboter007.voxiety.core.renderer.components.SpriteRenderer;
import de.Roboter007.voxiety.core.renderer.components.Spritesheet;
import de.Roboter007.voxiety.core.renderer.textures.RenderBatch;
import de.Roboter007.voxiety.core.renderer.textures.SpriteTexture;
import de.Roboter007.voxiety.core.renderer.textures.Texture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoxRenderer {

    private final int MAX_BATCH_SIZE = 1000;
    private final List<RenderBatch> batches;

    public VoxRenderer() {
        this.batches = new ArrayList<>();
    }

    public void set(List<VoxElement> voxElements) {
        batches.clear();
        for(VoxElement voxElement : voxElements) {

        }
    }

    public void add(VoxElement voxElement) {
        SpriteRenderer spr = voxElement.getComponent(SpriteRenderer.class);
        if(spr != null) {
            add(spr);
        }
    }

    public void add(SpriteRenderer sprite) {
        boolean added = false;

        for(RenderBatch batch : batches) {
            if(batch.hasRoom() && batch.zIndex() == sprite.voxElement.zIndex()) {
                SpriteTexture texture = sprite.getTexture();
                if(texture == null || (batch.hasTexture(texture) || batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if(!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.voxElement.zIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void clearBatches() {
        batches.clear();
    }

    public void render() {
        for(RenderBatch batch : batches) {
            batch.render();
        }
    }
}
