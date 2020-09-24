package ch.heigvd.amt.mvcsimple.business.api;

import javax.ejb.Local;

@Local
public interface CredentialRepository {

    /**
     * Returns true if the username is already registered in the application.
     */
    boolean registered(String username);

    /**
     * Sets a new username and password to the repository. If the username is already in use, the current password
     * will be erased.
     *
     * @param username the username to add.
     * @param password the associated password.
     */
    void set(String username, String password);

    /**
     * Clears the credentials associated with a specific username. If the credentials do not exist, this will result in
     * a no-op.
     *
     * @param username the username for which we're clearing the credentials.
     */
    void clear(String username);

    /**
     * Returns true if the username and the password match.
     *
     * @param username the username to check for.
     * @param password the password that should be validated for this username.
     * @return True if the username and password match (and exist).
     */
    boolean match(String username, String password);
}
