package name.eipi.pacemaker.persistence;

/**
 * Created by naysayer on 02/10/2014.
 */
public interface IDataBaser {

    void write(Object o);

    Object read();

    // Should be in interface? Kind of Impl specific.
    void changeFormat(String format);

    // Should be in interface? Kind of Impl specific.
    void cleanUp();

}
