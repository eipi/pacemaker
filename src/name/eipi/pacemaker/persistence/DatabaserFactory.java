package name.eipi.pacemaker.persistence;

/**
 * Created by dbdon_000 on 03/10/2014.
 */
public class DatabaserFactory {

    private DatabaserFactory() {

    }

    public static IDataBaser getInstance(String s) {
        return new DataBaserXStreamImpl(s);
    }


    public static IDataBaser getInstance() {
        return new DataBaserXStreamImpl();
    }
}
