/**
 * Generate Id auto
 */
public class GenerateId {
    private static int ID = 0;

    public static synchronized int getId() {
        ID++;
        return ID;
    }
}
