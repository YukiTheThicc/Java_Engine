package sapphire.eventsSystem.handlers;

import diamondEngine.diaUtils.DiaLogger;
import diamondEngine.diaUtils.DiaLoggerLevel;
import sapphire.eventsSystem.SappEvent;
import sapphire.eventsSystem.SappObserver;

import java.io.File;

public class FileNavigatorEventHandler implements SappObserver {

    @Override
    public void onNotify(SappEvent event) {
        switch (event.type) {
            case Delete_file:
                if (event.payload instanceof File) {
                    if (((File) event.payload).delete()) {
                        DiaLogger.log("Deleting file '" + ((File) event.payload).getAbsoluteFile() + "'");
                    } else {
                        DiaLogger.log(this.getClass(), "Failed to delete file '" + ((File) event.payload).getAbsoluteFile() + "'", DiaLoggerLevel.WARN);
                    }
                }
                break;
        }
    }
}
