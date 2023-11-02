/**
 * This is an enumeration of message type
 */
public enum Type {
    REQUEST,
    RESPONSE,
    OFFLINE;

    public static Type getTypeFromData(String data) {
        String typeStr = data.split(";")[0];
        return valueOf(typeStr);
    }
}
