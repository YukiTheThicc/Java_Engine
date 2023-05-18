package diamondEngine;

public abstract class EnvironmentManager {

    public abstract Environment build();
    public abstract void loadFromFile(String file);
}
