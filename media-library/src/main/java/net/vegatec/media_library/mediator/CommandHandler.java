package net.vegatec.media_library.mediator;

/**
 * @param <TCommand>  The type of request that will be handled
 * @param <TResponse> The type of response that will be produced when handling the request.
 */
public interface CommandHandler<TCommand extends Command<TResponse>, TResponse> {
    /**
     * @param request The request instance that was passed through mediator object.
     * @return The result of handling the given request.
     */
    TResponse handle(final TCommand request);
}
