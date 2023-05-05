package diamondEngine;

import java.util.ArrayList;

public class Diamond {

    private static final long UID_SEED = 1000000000;
    private static long CURRENT = UID_SEED + 1;

    // ATTRIBUTES
    public static Environment currentEnv = null;
    private static Diamond diamond = null;
    private ArrayList<Environment> environments;
    private final ArrayList<Environment> environmentsToRemove;
    private boolean isDirty = false;
    private int emptyEnvs = 0;

    // CONSTRUCTORS
    private Diamond() {
        this.environments = new ArrayList<>();
        this.environmentsToRemove = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public ArrayList<Environment> getEnvironments() {
        return environments;
    }

    public void setEnvironments(ArrayList<Environment> environments) {
        this.environments = environments;
    }

    public void setDirty() {
        isDirty = true;
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
        currentEnv = new Environment("empty");
        currentEnv.init();
    }

    public void addEmptyEnvironment() {
        Environment newEnv = new Environment("New Environment" + (emptyEnvs != 0 ? " " + emptyEnvs : ""));
        newEnv.init();
        environments.add(newEnv);
        currentEnv = newEnv;
        emptyEnvs++;
    }

    public void removeEnv(Environment env) {
        this.environmentsToRemove.add(env);
        this.isDirty = true;
    }

    public void addEnvironment(Environment env) {
        environments.add(env);
    }

    public void update(float dt) {
        if (isDirty) {
            for (Environment env : environmentsToRemove) {
                environments.remove(env);
            }
            environmentsToRemove.clear();
            isDirty = false;
        }
        for(Environment env : environments) {
            env.update(dt);
        }
    }
}
