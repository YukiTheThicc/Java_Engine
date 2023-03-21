package diamondEngine;

public abstract class DiaEnvironmentFactory {

    public abstract DiaEnvironment build();
    public abstract void loadFromFile(String file);
}
