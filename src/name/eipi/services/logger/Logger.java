package name.eipi.services.logger;

/**
 * Created by dbdon_000 Date: 10/08/13
 */
public interface Logger {

    void debug(String message);

    void debug(String message, Object object);

    void error(String message);

    void error(String message, Object object);

}
