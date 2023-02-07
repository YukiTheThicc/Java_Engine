package diaEditor;

public class FrontEnd {

    /**
     * Class that manages and contains all FrontEnd/Editor functionalities and resources
     */
    private static FrontEnd frontEnd = null;
    private ContainerEditor container;

    private FrontEnd () {
        this.container = new ContainerEditor();
    }

    public static FrontEnd get() {
        if (FrontEnd.frontEnd == null) {
            FrontEnd.frontEnd = new FrontEnd();
        }
        return frontEnd;
    }

    public void start() {
        if (this.container != null) {
            this.container.init();
            this.container.run();
        }
    }
}