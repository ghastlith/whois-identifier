package blarknes.whoisidentifier.identify.exception;

/**
 * {@code JsonParsingException} is the exception thrown when parsing JSON
 * encountered any problem.
 */
public class JsonParsingException extends RuntimeException {

    public JsonParsingException() {
        super("There was a problem parsing the whois response into a JSON");
    }

}
