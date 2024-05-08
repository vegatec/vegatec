package net.vegatec.media_library.mediator;

public interface Mediator {
    /**
     * @param command The command that will be handled by its registered handler, if it exists.
     * @param <T>     The type of the response that will be returned.
     * @return The result of the executed handler.
     */
    <T> T send(final Command<T> command);
}
