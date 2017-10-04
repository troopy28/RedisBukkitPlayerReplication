package org.kubithon.replicate.broking.impl;

import org.kubithon.replicate.broking.Credentials;

/**
 * @author Oscar Davis
 * @since 1.0.0
 */
public abstract class AbstractCredentials implements Credentials {

    protected String host;
    protected int port;
    protected String password;

    /**
     * Default constructor.
     *
     * @param host     the host.
     * @param port     the port.
     * @param password the password
     */
    public AbstractCredentials(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    /**
     * @return Return the host where the Redis is located.
     */
    @Override
    public String host() {
        return host;
    }

    /**
     * @return Return the port on which the Redis is listening.
     */
    @Override
    public int port() {
        return port;
    }

    /**
     * @return Return the password used to connect the plugin to the Redis.
     */
    @Override
    public String password() {
        return password;
    }

}
