package morales.david.desktop.utils;

import morales.david.desktop.models.packets.PacketType;

public class Constants {

    public static final String WINDOW_TITLE = " (SGH)";

    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 6565;
    public static final int SERVER_FILE_TRANSFER_PORT = SERVER_PORT + 1;

    public static final String LOG_SERVER_ERROR_IO = "(X) Se ha producido un error con el flujo de E/S";
    public static final String LOG_SERVER_ERROR_IO_SEND = "(X) Se ha producido un error al enviar el mensaje por el flujo de salida";
    public static final String LOG_SERVER_ERROR_IO_READ = "(X) Se ha producido un error al leer el mensaje por el flujo de entrada";

    public static final String MESSAGES_ERROR_LOGIN = "Usuario o contraseña incorrecta";
    public static final String MESSAGES_ERROR_LOGIN_EMPTY = "Campo o campos vacíos";
    public static final String MESSAGES_ERROR_RECEIVEDFILE = "Se ha producido un error al enviar o recibir el archivo";

    public static final String MESSAGES_CONFIRMATION_RECEIVEDFILE = "El archivo se ha recibido correctamente\nIniciando la importación...";

    public static boolean FIRST_HOME_VIEW = true;

    public static final PacketType[] INIT_PACKETS = {
        PacketType.DAYS, PacketType.HOURS, PacketType.TIMEZONES, PacketType.CLASSROOMS, PacketType.TEACHERS,
        PacketType.CREDENTIALS, PacketType.COURSES, PacketType.GROUPS, PacketType.SUBJECTS, PacketType.SCHEDULES
    };

}
