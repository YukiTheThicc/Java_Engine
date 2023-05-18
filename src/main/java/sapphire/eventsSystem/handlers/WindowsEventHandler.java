package sapphire.eventsSystem.handlers;

import diamondEngine.Entity;
import diamondEngine.Diamond;
import diamondEngine.diaComponents.Component;
import diamondEngine.diaUtils.DiaAssetManager;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.Sapphire;
import sapphire.SappProject;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.imgui.SappDrawable;

import java.io.File;

public class WindowsEventHandler implements SappObserver {

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
            case Delete_file:
                handleDeleteFile(event);
                break;
            case Play:
                Sapphire.get().setDiaRunning(true);
                break;
            case Stop:
                Sapphire.get().setDiaRunning(false);
                break;
            case Add_asset:
                handleAddAssets();
                break;
        }
    }

    private void handleNewEnv() {
        Diamond.get().addEmptyEnvironment();
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
            Sapphire.get().getProject().save();
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

    private void handleDeleteFile(SappEvent event) {
        if (event.payload instanceof File) {
            if (((File) event.payload).delete()) {
                DiaLogger.log("Deleting file '" + ((File) event.payload).getAbsoluteFile() + "'");
            } else {
                DiaLogger.log(this.getClass(), "Failed to delete file '" + ((File) event.payload).getAbsoluteFile() + "'", DiaLoggerLevel.WARN);
            }
        }
    }

    private void handleAddAssets() {
        DiaLogger.log(Sapphire.getProjectDir() + "/" + SappProject.SFX_DIR + "/");
        String[] assetPaths = DiaUtils.selectFiles("", Sapphire.getProjectDir());
        if (assetPaths != null) {
            for (String assetPath : assetPaths) {
                if (DiaAssetManager.loadResource(assetPath)) Sapphire.get().getProject().addResource(assetPath);
            }
        }
    }
}
