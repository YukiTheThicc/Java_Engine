package diamondEngine.diaAssets;

public abstract class Asset {

    // ATTRIBUTES
    private final String id;

    // CONTRUCTORS
    public Asset(String id) {
        this.id = id;
    }

    // GETTERS & SETTERS
    public String getId() {
        return id;
    }
}
