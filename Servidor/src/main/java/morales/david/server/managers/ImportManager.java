package morales.david.server.managers;

import morales.david.server.Server;
import morales.david.server.utils.AccessConnection;
import morales.david.server.utils.DBConnection;

import java.io.File;

public class ImportManager {

    private File file;

    private Server server;

    private DBConnection dbConnection;
    private AccessConnection accessConnection;

    private boolean isImporting;

    public ImportManager(Server server) {
        this.server = server;
        this.isImporting = false;
        this.dbConnection = new DBConnection();
    }

    public synchronized boolean isImporting() {
        return isImporting;
    }

    public synchronized File getFile() {
        return file;
    }

    public synchronized void setFile(File file) {
        this.file = file;
    }

    public void importDatabase() {

        accessConnection = new AccessConnection(file.getAbsolutePath());

        accessConnection.open();

        if(file.exists())
            file.delete();

    }

}