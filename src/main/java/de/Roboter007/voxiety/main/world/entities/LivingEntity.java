package de.Roboter007.voxiety.main.world.entities;

import org.joml.Vector2f;

public abstract class LivingEntity extends Entity {

    public LivingEntity(String name, Vector2f screenPos, float entityWidth, float entityHeight, int zIndex, int maxAnimationCooldown) {
        super(name, screenPos, entityWidth, entityHeight, zIndex, maxAnimationCooldown);
    }

    public LivingEntity(String name, float entityWidth, float entityHeight, int zIndex, int maxAnimationCooldown) {
        super(name, entityWidth, entityHeight, zIndex, maxAnimationCooldown);
    }
}
