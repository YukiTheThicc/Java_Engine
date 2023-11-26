package sapphire.eventsSystem.handlers;

import diamondEngine.Entity;
import diamondEngine.Diamond;
import diamondEngine.Environment;
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
            case Add_object:
                handleAddObject(event);
                break;
            case Copy_object:
                handleCopyObject(event);
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
            case Make_current:
                handleMakeCurrent(event);
        }
    }

    private void handleNewEnv() {
        Diamond.get().addEmptyEnvironment();
    }

    private void handleAddObject(SappEvent event) {
        // ADD Entity to environment
        if (event.env != null && event.entity != null) {
            event.env.addEntity(event.entity);
        }

        // ADD Component to entity
        if (event.entity != null && event.payload instanceof Component) {
            for (Component component : event.entity.getComponents()) {
                if (component.getClass() == event.payload.getClass()) {
                    return;
                }
            }
            event.entity.addComponent((Component) event.payload);
        }
    }

    private void handleCopyObject(SappEvent event) {
        // ADD Entity to environment
        if (event.env != null && event.entity != null) {
            event.env.addEntity(event.entity.copy());
        }
    }

    private void handleDeleteObj(SappEvent event) {

        // Remove environment
        if (event.env != null && event.entity == null && event.payload == null) {
            Diamond.get().removeEnv(event.env);
            Sapphire.get().getProject().save();
            if (Sapphire.getActiveObject() == event.env) Sapphire.setActiveObject(null);
        }

        // Remove entity from environment
        if (event.entity != null && event.payload == null) {
            event.entity.getParent().removeEntity(event.entity);
        }

        // Remove component from entity
        if (event.env == null && event.entity != null && event.payload != null) {
            if (event.payload instanceof Component) {
                event.entity.removeComponent(((Component) event.payload).getUuid());
            }
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

    private void handleMakeCurrent(SappEvent event) {
        if (event.env != null) {
            Diamond.setCurrentEnv(event.env);
            Sapphire.get().getProject().setCurrentEnv(event.env.getOriginFile());
        }
    }
}
