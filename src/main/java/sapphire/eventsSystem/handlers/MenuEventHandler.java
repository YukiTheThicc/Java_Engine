package sapphire.eventsSystem.handlers;

import diamondEngine.Diamond;
import diamondEngine.Environment;
import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaUtils;
import sapphire.Sapphire;
import sapphire.SappDir;
import sapphire.SappProject;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;
import sapphire.imgui.SappImGuiLayer;
import sapphire.imgui.windows.FileWindow;
import sapphire.utils.SappProjectCreator;
import java.io.File;

public class MenuEventHandler implements SappObserver {

    // ATTRIBUTES
    private SappImGuiLayer layer;

    // CONSTRUCTORS
    public MenuEventHandler(SappImGuiLayer layer) {
        this.layer = layer;
    }

    @Override
    public void onNotify(SappEvent event) {
        switch(event.type) {
            case New_file:
                newFile();
                break;
            case Open_file:
                openFile();
                break;
            case Save_file:
                saveFile();
                break;
            case Save_as:
                saveFileAs();
                break;
            case Create_project:
                createProject();
                break;
            case Open_project:
                openProject();
                break;
            case Export_project:
                exportProject();
                break;
            case Import_env:
                importEnv();
                break;
            case Save_env:
                saveEnv();
                break;
            case Save_env_as:
                saveEnvAs();
                break;
            case Settings:
                layer.getWindows().get("settings").setActive(true);
                break;
        }
    }

    private void newFile() {
        String fileName = Sapphire.getLiteral("new_file");
        if (layer.getWindows().get(fileName) != null) {
            // If the new file name has a number, retrieve that number and add 1 to it. If not, add ' 1' to the
            // new files name
            String[] splitName = fileName.split("(\\d+)(?!.*\\d)");
            if (splitName.length > 1) {
                int fileNumber = Integer.parseInt(splitName[splitName.length - 1]) + 1;
                fileName += " " + fileNumber;
            } else {
                fileName += "New File";
            }
        }
        layer.addWindow(new FileWindow(fileName, new File("")));
    }

    private void openFile() {
        String[] paths = DiaUtils.selectFiles();
        if (paths != null) {
            for (String path : paths) {
                DiaLogger.log("Trying to open file on path '" + path + "'...");
                File newFile = new File(path);
                layer.addWindow(new FileWindow(newFile.getName(), newFile));
            }
        }
    }

    private void saveFile() {
        if (layer.getLastFocusedFile() != null) {
            layer.getLastFocusedFile().saveFile();
        }
    }

    private void saveFileAs() {
        FileWindow focusedFile = layer.getLastFocusedFile();
        if (focusedFile != null) {
            File file = DiaUtils.saveFile(focusedFile.getFile().getAbsolutePath());
            if (file != null && file.isFile()) {
                focusedFile.setFile(file);
                focusedFile.saveFile();
            }
        }
    }

    private void createProject() {
        String path = DiaUtils.selectDirectory(Sapphire.getLiteral("create_project"), Sapphire.get().getSettings().getWorkspace());
        if (path != null && !path.isEmpty()) {
            DiaLogger.log("Creating project in '" + path + "'");
            SappProject project = SappProjectCreator.create(path);
            if (project != null) {
                Sapphire.get().setProject(project);
            }
        }
    }

    private void openProject() {
        String path = DiaUtils.selectDirectory(Sapphire.getLiteral("open_project"), Sapphire.get().getSettings().getWorkspace());
        if (path != null && !path.isEmpty()) {
            DiaLogger.log("Opening project '" + path + "'");
            SappProject project = new SappProject(new SappDir(path));
            if (project.load()) {
                Sapphire.get().setProject(project);
            }
        }
    }

    private void exportProject() {

    }

    private void importEnv() {
        String path = DiaUtils.selectFile();
        if (path != null && !path.isEmpty()) {
            Environment env = new Environment("IMPORTED ENV");
            env.init();
            env.load(path);
            Diamond.get().addEnvironment(env);
            if (Sapphire.get().getProject() != null) Sapphire.get().getProject().save();
        }
    }

    private void saveEnv() {
        Environment env = Sapphire.getActiveObject() instanceof Environment ? (Environment) Sapphire.getActiveObject() : null;
        if (env != null) {
            File file = null;
            if (env.getOriginFile() != null) {
                file = new File(env.getOriginFile());
            } else {
                file = DiaUtils.saveFile(Sapphire.getProjectDir() + "/" + SappProject.ENVS_DIR, Environment.ENVS_EXT);
            }

            if (file != null && file.isFile()) {
                env.save(file.getAbsolutePath());
                if (Sapphire.get().getProject() != null) Sapphire.get().getProject().save();
            }
        }
    }

    private void saveEnvAs() {
        Environment env = Sapphire.getActiveObject() instanceof Environment ? (Environment) Sapphire.getActiveObject() : null;
        if (env != null) {
            File file = DiaUtils.saveFile(Sapphire.getProjectDir() + "/" + SappProject.ENVS_DIR, Environment.ENVS_EXT);
            if (file != null && file.isFile()) {
                env.save(file.getAbsolutePath());
            }
        }
    }
}