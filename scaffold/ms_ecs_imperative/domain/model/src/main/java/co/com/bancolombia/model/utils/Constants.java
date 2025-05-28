package co.com.bancolombia.model.utils;

public class Constants {
    private Constants() {
    }

    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss:SSSS";
    public static final String MESSAGE_ID_HEADER = "message-id";
    public static final String REGEX_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}" +
        "-[0-9a-fA-F]{12}$";
}
