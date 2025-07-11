package de.Roboter007.voxiety.core.renderer;

import de.Roboter007.voxiety.core.renderer.textures.Transform;

import java.util.ArrayList;
import java.util.List;

public class VoxElement {

    private final List<VoxComponent> componentList;
    public Transform transform;
    private final int zIndex;

    public VoxElement() {
        this(new Transform(), 0);
    }

    public VoxElement(Transform transform, int zIndex) {
        this.zIndex = zIndex;
        this.componentList = new ArrayList<>();
        this.transform = transform;
    }

    public <T extends VoxComponent> T getComponent(Class<T> componentClass) {
        for(VoxComponent vc : componentList) {
            if(componentClass.isAssignableFrom(vc.getClass())) {
                try {
                    return componentClass.cast(vc);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }
        return null;
    }

    public <T extends VoxComponent> void removeComponent(Class<T> componentClass) {
        for(int i=0; i< componentList.size(); i++) {
            VoxComponent vc = componentList.get(i);
            if(componentClass.isAssignableFrom(vc.getClass())) {
                componentList.remove(i);
                return;
            }
        }
    }

    public <T extends VoxComponent> boolean containsComponent(Class<T> componentClass) {
        for (VoxComponent vc : componentList) {
            if (componentClass.isAssignableFrom(vc.getClass())) {
                return true;
            }
        }
        return false;
    }

    public void addComponent(VoxComponent vc) {
        this.componentList.add(vc);
        vc.voxElement = this;
    }

    //ToDo: Check if enhanced for loop makes a difference
    public void update(float delta) {
        for (VoxComponent voxComponent : componentList) {
            voxComponent.update(delta);
        }
    }

    public void start() {
        for (VoxComponent voxComponent : componentList) {
            voxComponent.start();
        }
    }

    public int zIndex() {
        return zIndex;
    }

    @Override
    public String toString() {
        return "VoxElement{" +
                "componentList=" + componentList +
                ", transform=" + transform +
                ", zIndex=" + zIndex +
                '}';
    }
}
