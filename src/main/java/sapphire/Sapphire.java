package sapphire;

import diamondEngine.diaUtils.DiaConsole;
import diamondEngine.diaUtils.DiaUtils;

import java.io.File;
import java.util.ArrayList;

public class Sapphire {

    /**
     * Class that manages and contains all FrontEnd/Editor functionalities and resources
     */

    // ATTRIBUTES
    private static Sapphire sapphire = null;
    private ContainerEditor container;
    private SapphireSettings settings;
    private ArrayList<File> openedFiles;
    private boolean updatedFiles;

    // CONSTRUCTORS
    private Sapphire() {
        this.container = new ContainerEditor();
        this.settings = new SapphireSettings();
        this.openedFiles = new ArrayList<>();
        this.updatedFiles = false;
    }

    // GETTERS & SETTERS
    public SapphireSettings getSettings() {
        return settings;
    }

    public ArrayList<File> getOpenedFiles() {
        return openedFiles;
    }

    public boolean hasUpdatedFiles() {
        return updatedFiles;
    }

    public void filesUpdated() {
        this.updatedFiles = false;
    }

    public void newOpenedFiles() {
        this.updatedFiles = true;
    }

    // METHODS
    public static Sapphire get() {
        if (Sapphire.sapphire == null) {
            Sapphire.sapphire = new Sapphire();
        }
        return sapphire;
    }

    public void addOpenedFile(File file) {
        if (openedFiles.contains(file)) {
            DiaConsole.log("Tried to add an already opened file", "warn");
        } else {
            openedFiles.add(file);
            updatedFiles = true;
        }
    }

    public void start() {
        // Initialize general front end an Engine
        DiaUtils.get().init();
        this.settings.init();
        if (this.container != null) {
            this.container.init();
            this.container.run();
        }
    }
}