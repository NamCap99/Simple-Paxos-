/**
 * Declare the variables
 */
public class Constants {
    public static final int NUMBER_ACCEPTORS = 6;
    public static final int NUMBER_PROPOSER = 3;

    public static final int[] PORTS = {
            6001, 6002, 6003, 6004, 6005,
            6006, 6007, 6008, 6009
    };

    public static final String[] NAMES = {
            "M1", "M2", "M3", "M4", "M5",
            "M6", "M7", "M8", "M9"
    };

    public static int getPortByName(String name) {
        for (int i = 0; i < NAMES.length; ++i) {
            if (NAMES[i].equals(name)) {
                return PORTS[i];
            }
        }

        return -1;
    }

    public static final String LOCAL_HOST = "127.0.0.1";
    public static final int MAX_DELAY = 9;
}
