package diamondEngine;

public abstract class EnvironmentFactory {

    public abstract Environment build();
    public abstract void loadFromFile(String file);
}
