package de.Roboter007.voxiety.core.cameras;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    public static float TARGETED_WINDOW_WIDTH = 32.0f * 40.0f;
    public static float TARGETED_WINDOW_HEIGHT = 32.0f * 21.0f;

    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f eyePosition;
    public Vector2f centerPosition;

    public Camera(Vector2f eyePosition, Vector2f centerPosition) {
        this.eyePosition = eyePosition;
        this.centerPosition = centerPosition;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public Camera(Vector2f position) {
        this(position, position);
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0F, TARGETED_WINDOW_WIDTH, 0.0F, TARGETED_WINDOW_HEIGHT, 0.0F, 100.0F);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0F, 0.0F, -1.0F);
        Vector3f cameraUp = new Vector3f(0.0F, 1.0F, 0.0F);
        this.viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(eyePosition.x, eyePosition.y, 20.0F),
                                       cameraFront.add(centerPosition.x, centerPosition.y, 0.0F),
                                       cameraUp);
        this.viewMatrix.invert(inverseView);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return inverseProjection;
    }

    public Matrix4f getInverseView() {
        return inverseView;
    }

    public void updateEyePosition(Vector2f position) {
        this.eyePosition = position;
    }

    public void updateCenterPosition(Vector2f position) {
        this.centerPosition = position;
    }

    public void updatePosition(Vector2f position) {
        this.centerPosition = position;
        this.eyePosition = position;
    }
}