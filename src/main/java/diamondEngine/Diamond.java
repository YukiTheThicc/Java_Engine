package diamondEngine;

import diamondEngine.diaEvents.DiaEvent;
import diamondEngine.diaEvents.DiaEventType;
import diamondEngine.diaEvents.DiaEvents;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Diamond {

    // ATTRIBUTES
    private static Environment currentEnv = null;
    private static Diamond diamond = null;
    private static HashMap<Long, String> environmentMap;
    private ArrayList<Environment> environments;
    private final ArrayList<Environment> environmentsToRemove;
    private boolean isDirty = false;
    private int emptyEnvs = 0;

    // CONSTRUCTORS
    private Diamond() {
        this.environments = new ArrayList<>();
        this.environmentsToRemove = new ArrayList<>();
        environmentMap = new HashMap<>();
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

    public HashMap<Long, String> getEnvironmentMap() {
        return environmentMap;
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
        if (env != null) {
            if (environmentMap.get(env.getUid()) != null) {
                this.environmentsToRemove.add(env);
                this.isDirty = true;
            } else {
                DiaLogger.log("Tried to remove an unlisted Environment '" + env.getName() + "' (" + env.getUid() + ")", DiaLoggerLevel.WARN);
            }
        }else {
            DiaLogger.log("Tried to remove a null Environment", DiaLoggerLevel.WARN);
        }
    }

    public void addEnvironment(Environment env) {
        if (environmentMap.get(env.getUid()) == null) {
            environments.add(env);
        } else {
            DiaLogger.log("Tried to add an environment with an already existing UID, a new ID is going to be generated", DiaLoggerLevel.WARN);
        }
    }

    public void update(float dt) {
        if (isDirty) {
            for (Environment env : environmentsToRemove) {
                environmentMap.remove(env.getUid());
                environments.remove(env);
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
