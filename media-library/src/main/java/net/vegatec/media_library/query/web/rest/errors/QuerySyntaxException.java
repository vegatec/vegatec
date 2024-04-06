package net.vegatec.media_library.query.web.rest.errors;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class QuerySyntaxException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public QuerySyntaxException() {
        super("Invalid query syntax!", "elasticseach", "querySyntaxError");
    }
}
