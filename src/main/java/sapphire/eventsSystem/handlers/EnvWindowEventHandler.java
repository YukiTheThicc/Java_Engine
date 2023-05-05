package sapphire.eventsSystem.handlers;

import diamondEngine.Entity;
import diamondEngine.Diamond;
import diamondEngine.diaComponents.Component;
import sapphire.Sapphire;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.imgui.SappDrawable;

public class EnvWindowEventHandler implements SappObserver {

    @Override
    public void onNotify(SappEvent event) {
        switch (event.type) {
            case New_root_env:
            case Add_env:
                handleNewEnv();
                break;
            case Add_component:
                handleAddComponent(event);
                break;
            case Add_entity:
                handleAddEntity(event);
                break;
            case Selected_object:
                if (event.payload instanceof SappDrawable) Sapphire.setActiveObject((SappDrawable) event.payload);
                break;
            case Delete_object:
                handleDeleteObj(event);
                break;
        }
    }

    private void handleNewEnv() {
        Diamond.get().addEmptyEnvironment();
        if (Sapphire.get().getProject() != null) {
            Sapphire.get().getProject().save();
        }
    }

    private void handleAddComponent(SappEvent event) {
        if (event.env != null && event.payload instanceof Component) {
            for (Component component : event.env.getComponents()) {
                if (component.getClass() == event.payload.getClass()) {
                    return;
                }
            }
            event.env.addComponent((Component) event.payload);
        }
    }

    private void handleAddEntity(SappEvent event) {
        if (event.env != null && event.payload instanceof Entity) {
            for (Component component : event.env.getComponents()) {
                if (component.getClass() == event.payload.getClass()) {
                    return;
                }
            }
            event.env.addComponent((Component) event.payload);
        }
    }

    private void handleDeleteObj(SappEvent event) {
        if (event.env != null && event.payload == null) {
            // An environment has been removed
            Diamond.get().removeEnv(event.env);
            if (Sapphire.getActiveObject() == event.env) Sapphire.setActiveObject(null);
        } else if (event.env != null) {
            if (event.payload instanceof Entity) {
                event.env.removeEntity((Entity) event.payload);
            } else if (event.payload instanceof Component) {
                event.env.removeComponent((Component) event.payload);
            }
            if (Sapphire.getActiveObject() == event.payload) Sapphire.setActiveObject(null);
        }
    }
}
