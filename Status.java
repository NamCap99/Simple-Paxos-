/**
 * the status including both the acceptor and the proposer
 *
 */
public enum Status {
    PREPARE,
    PREPARE_OK,
    NACK,
    ACCEPT_REQUEST,
    ACCEPT_OK,
    ACCEPT_REJECT,
    DECIDE,
    DECIDE_OK
}
