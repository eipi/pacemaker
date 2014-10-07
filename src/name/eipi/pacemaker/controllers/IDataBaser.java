package name.eipi.pacemaker.controllers;

/**
 * Created by naysayer on 02/10/2014.
 */
public interface IDataBaser {

    void push(Object o);

    Object pop();

    void write() throws Exception;

    void read() throws Exception;

    void toggleFormat();

    void cleanUp();

}
