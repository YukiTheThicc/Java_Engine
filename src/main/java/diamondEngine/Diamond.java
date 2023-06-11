package diamondEngine;

import diamondEngine.diaEvents.DiaEvent;
import diamondEngine.diaEvents.DiaEventType;
import diamondEngine.diaEvents.DiaEvents;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;

import java.util.ArrayList;

public class Diamond {

    // CONSTANTS
    public static final String LIMBO_ENV_NAME = "NO ENV";

    // ATTRIBUTES
    private static Environment currentEnv = null;
    private static Environment limboEnv = null;
    private static Diamond diamond = null;
    private final ArrayList<Environment> environments;
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

    public static Environment getCurrentEnv() {
        return currentEnv;
    }

    public static void setCurrentEnv(Environment currentEnv) {
        Diamond.currentEnv = currentEnv;
        DiaEvents.notify(new DiaEvent(DiaEventType.ENV_CHANGED));
    }

    // METHODS
    public static Diamond get() {
        if (Diamond.diamond == null) {
            Diamond.diamond = new Diamond();
        }
        return diamond;
    }

    public void init() {
        // Initialize with dummy environment to draw an empty framebuffer
        limboEnv = new Environment(LIMBO_ENV_NAME);
        currentEnv = limboEnv;
        currentEnv.init();
    }

    public void addEmptyEnvironment() {
        Environment newEnv = new Environment("New Environment" + (emptyEnvs != 0 ? " " + emptyEnvs : ""));
        newEnv.init();
        addEnvironment(newEnv);
        emptyEnvs++;
    }

    public void removeEnv(Environment env) {
        if (env != null) {
            this.environmentsToRemove.add(env);
            this.isDirty = true;
        }else {
            DiaLogger.log(Diamond.class, "Tried to remove a null Environment", DiaLoggerLevel.WARN);
        }
    }

    public void addEnvironment(Environment env) {
        environments.add(env);
    }

    public void update(float dt) {
        if (isDirty) {
            for (Environment env : environmentsToRemove) {
                environments.remove(env);
                if (env == currentEnv) {
                    currentEnv = limboEnv;
                }
            }
            environmentsToRemove.clear();
            isDirty = false;
        }
        currentEnv.update(dt);
    }

    /**
     * Updates the content lists of all the environments
     */
    public void updateAllEnvLists() {
        for (Environment env : environments) {
            env.updateLists();
        }
    }
}
