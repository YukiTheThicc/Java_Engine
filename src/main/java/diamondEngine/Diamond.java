package diamondEngine;

import java.util.ArrayList;

public class Diamond {

    /**
     * Engine Class
     * This class will contain all in-engine
     */
    // ATTRIBUTES
    private static Diamond diamond = null;
    private ArrayList<DiaEnvironment> environments;

    // CONSTRUCTORS
    private Diamond() {
        this.environments = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public ArrayList<DiaEnvironment> getEnvironments() {
        return environments;
    }

    public void setEnvironments(ArrayList<DiaEnvironment> environments) {
        this.environments = environments;
    }

    // METHODS
    public static Diamond get() {
        if (Diamond.diamond == null) {
            Diamond.diamond = new Diamond();
        }
        return diamond;
    }

    public static void init() {

    }

    public void addEmptyEnvironment() {
        this.environments.add(new DiaEnvironment("New Environment"));
    }

    public void update() {
        for(DiaEnvironment env : environments) {
            env.update();
        }
    }
}
