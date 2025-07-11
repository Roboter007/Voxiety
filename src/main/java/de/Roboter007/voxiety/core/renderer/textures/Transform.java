package de.Roboter007.voxiety.core.renderer.textures;

import org.joml.Vector2f;

public class Transform {

    public final Vector2f position;
    public final Vector2f scale;

    public Transform(Vector2f position, Vector2f scale) {
        this.scale = scale;
        this.position = position;
    }

    public Transform(Vector2f position) {
        this(position, new Vector2f());
    }

    public Transform() {
        this(new Vector2f(), new Vector2f());
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof Transform transform)) return false;

        return transform.position.equals(this.position) && transform.scale.equals(this.scale);
    }

    @Override
    public String toString() {
        return "Transform{" +
                "position=" + position +
                ", scale=" + scale +
                '}';
    }
}
