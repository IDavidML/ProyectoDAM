package morales.david.server.utils;

public class Constants {

    public static final int SERVER_PORT = 6565;

    public static final String DB_URL = "jdbc:mysql://localhost/db_proyecto";
    public static final String DB_USER = "david";
    public static final String DB_PASS = "161100";

    public static final String ACCESS_URL = "jdbc:ucanaccess://";

    public static final String DB_QUERY_CREDENTIAL = "SELECT id_cred FROM credencial WHERE usuario = ? AND passwd_hash = ?";
    public static final String DB_QUERY_DETAILS = "SELECT * FROM credencial, profesor WHERE usuario = ? AND profesor = id_prof";
    public static final String DB_QUERY_TEACHERS = "SELECT * FROM profesor";

    public static final String LOG_SERVER_INIT = "(+) Servidor iniciado en el puerto '%d'";
    public static final String LOG_SERVER_ERROR = "(X) Se ha producido un error con el servidor";
    public static final String LOG_SERVER_ERROR_IO = "(X) Se ha producido un error con el flujo de E/S";
    public static final String LOG_SERVER_ERROR_IO_SEND = "(X) Se ha producido un error al enviar el mensaje por el flujo de salida";
    public static final String LOG_SERVER_ERROR_IO_READ = "(X) Se ha producido un error al leer el mensaje por el flujo de entrada";
    public static final String LOG_SERVER_USER_CONNECTED = "(+) Nuevo usuario conectado";
    public static final String LOG_SERVER_USER_DISCONNECTED = "(+) Se ha desconectado un usuario";

    public static final String REQUEST_LOGIN = "R1-LOGIN";
    public static final String REQUEST_DISCONNECT = "R2-DISCONNECT";
    public static final String REQUEST_SENDACCESSFILE = "R3-SENDACCESSFILE";
    public static final String REQUEST_TEACHERS = "R4-TEACHERS";

    public static final String CONFIRMATION_LOGIN = "CF1-LOGIN";
    public static final String CONFIRMATION_DISCONNECT = "CF2-DISCONNECT";
    public static final String CONFIRMATION_SENDACCESSFILE = "CF3-SENDACCESSFILE";
    public static final String CONFIRMATION_TEACHERS = "CF4-TEACHERS";

    public static final String ERROR_LOGIN = "E1-LOGIN";
    public static final String ERROR_DISCONNECT = "E2-DISCONNECT";
    public static final String ERROR_SENDACCESSFILE = "E3-SENDACCESSFILE";
    public static final String ERROR_TEACHERS = "E4-TEACHERS";

}
