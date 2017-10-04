package org.kubithon.replicate.broking;

/**
 * MessageListener represents a handler for {@link PubSubManager} messages.
 * To register one, simply call {@link PubSubManager#subscribe(String, MessageListener)}.
 *
 * @author Oscar Davis
 * @see PubSubManager
 * @since 1.0.0
 */
public interface MessageListener
{

    /**
     * Method called when a message is received to one of the subscribed topics.
     *
     * @param topic   message's topic.
     * @param message the message.
     */
    default void topicReceive(String topic, String message)
    {
    }

    /**
     * Method called when a message is received on a topic which applies to the registered pattern(s).
     *
     * @param pattern the subscribed pattern.
     * @param topic   message's topic.
     * @param message the message.
     */
    default void patternReceive(String pattern, String topic, String message)
    {
    }

}
