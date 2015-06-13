package project61592;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

/**
 * The User class represents the users that are registered in the server.
 * Implements the {@link Serializable} interface.
 */
public class User implements Serializable {

    /**
     * A {@link String} instance representing the user's name.
     */
    private String name;
    /**
     * A {@link String} instance representing the user's password.
     */
    private String password;
    /**
     * A {@link String} instance representing the user's access level.
     */
    private String accessLevel;

    /**
     * Creates an instance of the {@link User} class. Used when registering a
     * new user.
     *
     * @param name {@link String} the user's name.
     * @param password {@link String} the user's password.
     * @param accessLevel {@link String} the user's access level;
     */
    public User(String name, String password, String accessLevel) {
        setName(name);
        setPassword(password);
        setAccessLevel(accessLevel);
    }

    /**
     * Creates an instance of the {@link User} class. Used when a client is
     * trying to log in.
     *
     * @param name {@link String} the user's name.
     * @param password {@link String} the user's password.
     */
    public User(String name, String password) {
        setName(name);
        setPassword(password);
    }

    /**
     * Gets the {@link String} instance representing the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the {@link String} instance representing the user's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the {@link String} instance representing the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the {@link String} instance representing the user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the {@link String} instance representing the user's access level.
     */
    public synchronized String getAccessLevel() {
        return accessLevel;
    }

    /**
     * Sets the {@link String} instance representing the user's access level.
     */
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Converts the {@link User} to {@link String}.
     *
     * @return {@link String} representation of an instance of the {@link User}
     * class.
     */
    @Override
    public String toString() {
        return String.format("User: %s, Password: %s, Access Level: %s",
                name, password, accessLevel);
    }

}
