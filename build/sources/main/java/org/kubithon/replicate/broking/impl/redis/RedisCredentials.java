package org.kubithon.replicate.broking.impl.redis;

import org.kubithon.replicate.broking.impl.AbstractCredentials;

/**
 * Redis-specific credentials. A basic implementation of the {@link AbstractCredentials}.
 *
 * @author Oscar Davis
 * @since 1.0.0
 */
public class RedisCredentials extends AbstractCredentials
{

    /**
     * @param host The Redis host's address.
     * @param port The Redis listening port.
     * @param password The Redis password.
     */
    public RedisCredentials(String host, int port, String password)
    {
        super(host, port, password);
    }

}
