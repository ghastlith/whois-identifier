package blarknes.whoisidentifier.identify.exception;

/**
 * {@code JsonParsingException} Is the exception thrown when parsing JSON
 * encountered any problem.
 */
public class JsonFieldNotFoundException extends RuntimeException {

    public JsonFieldNotFoundException(final String field) {
        super("The field " + field + " does not exist on the Whois response");
    }

}
