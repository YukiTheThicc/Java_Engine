package sapphire;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;

public class Sapphire {

    /**
     * Class that manages and contains all FrontEnd/Editor functionalities and resources
     */

    // ATTRIBUTES
    private static Sapphire sapphire = null;
    private final ContainerEditor container;
    private SapphireSettings settings;

    // CONSTRUCTORS
    private Sapphire() {
        this.container = new ContainerEditor();
        this.settings = new SapphireSettings();
    }

    // GETTERS & SETTERS
    public SapphireSettings getSettings() {
        return settings;
    }

    public static String getLiteral(String literal) {
        return Sapphire.get().settings.getLiteral(literal);
    }

    // METHODS
    public static Sapphire get() {
        if (Sapphire.sapphire == null) {
            Sapphire.sapphire = new Sapphire();
        }
        return sapphire;
    }

    public void start() {
        // Initialize general front end an Engine
        DiaLogger.init();
        DiaUtils.init();
        this.settings.init();
        this.container.init();
        this.container.run();
    }
}