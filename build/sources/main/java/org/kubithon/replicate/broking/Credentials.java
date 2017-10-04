package org.kubithon.replicate.broking;

/**
 * @author Oscar Davis
 * @since 1.0.0
 */
public interface Credentials
{

    /**
     * Returns server's host.
     *
     * @return server's host.
     */
    String host();

    /**
     * Returns server's port.
     *
     * @return server's port.
     */
    int port();

    /**
     * Returns credentials' password.
     *
     * @return credentials' password.
     */
    String password();

}
