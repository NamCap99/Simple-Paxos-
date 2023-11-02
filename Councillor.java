/**
 * This abstract class Councillor
 * resembles a general councillor, which
 * will be implemented by Acceptor
 * and Proposer
 *
 * @see Acceptor
 * @see Proposer
 */
public abstract class Councillor {

    /**
     * Need to take action when receiving message
     * either from acceptor or proposer.
     * @param message
     */
    public abstract void receive(String message);

    /**
     * Get councillor's name.
     *
     * @return councillor's name
     */
    public abstract String getName();

    /**
     * Get proposal value.
     *
     * @return a string representation of proposal value
     */
    public abstract String getValue();
}
