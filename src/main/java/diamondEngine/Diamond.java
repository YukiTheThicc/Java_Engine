package diamondEngine;

import java.util.ArrayList;

public class Diamond {

    private static final long UID_SEED = 1000000000;
    private static long CURRENT = UID_SEED + 1;

    /**
     * Engine Class
     * This class will contain all in-engine
     */
    // ATTRIBUTES
    private static Diamond diamond = null;
    private ArrayList<DiaEnvironment> environments;
    private DiaEnvironment currentEnv;
    private int emptyEnvs = 0;

    // CONSTRUCTORS
    private Diamond() {
        this.environments = new ArrayList<>();
        this.currentEnv = null;
    }

    // GETTERS & SETTERS
    public ArrayList<DiaEnvironment> getEnvironments() {
        return environments;
    }

    public void setEnvironments(ArrayList<DiaEnvironment> environments) {
        this.environments = environments;
    }

    public DiaEnvironment getCurrentEnv() {
        return currentEnv;
    }

    public void setCurrentEnv(DiaEnvironment currentEnv) {
        this.currentEnv = currentEnv;
    }

    // METHODS
    public static Diamond get() {
        if (Diamond.diamond == null) {
            Diamond.diamond = new Diamond();
        }
        return diamond;
    }

    public static long genId() {
        CURRENT++;
        return CURRENT;
    }

    public void init() {
        // Initialize with dummy environment to draw an empty framebuffer
        currentEnv = new DiaEnvironment("empty");
    }

    public void addEmptyEnvironment() {
        DiaEnvironment newEnv = new DiaEnvironment("New Environment" + (emptyEnvs != 0 ? " " + emptyEnvs : ""));
        environments.add(newEnv);
        currentEnv = newEnv;
        emptyEnvs++;
    }

    public void update(float dt) {
        for(DiaEnvironment env : environments) {
            env.update(dt);
        }
    }
}
