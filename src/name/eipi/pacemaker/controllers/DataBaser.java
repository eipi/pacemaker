package name.eipi.pacemaker.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.AbstractDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import name.eipi.services.logger.Logger;
import name.eipi.services.logger.LoggerFactory;

import java.io.*;

/**
 * Class to save an Object to and from disk/db/etc.
 * <p/>
 * Created by naysayer on 02/10/2014.
 */
public class DataBaser {

    private static final Logger LOG = LoggerFactory.getInstance(DataBaser.class);

    private String connString = "default.lodge";

    private XStream xstream = null;
    private Format fmt = Format.Xml;

    public DataBaser() {
        initializeStorage(connString);
    }

    public DataBaser(final String connStringIn) {
        initializeStorage(connStringIn);
    }

    private static void safelyClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable t) {
                //  ignore
            }
        }
    }

    private void initializeXstream() {
        xstream = new XStream(fmt.driver);
//        xstream.alias("user", User.class);
//        xstream.alias("activity", Activity.class);
//        xstream.alias("location", Location.class);
    }

    public void changeFileFormat() {
        // TODO a better way?
        if (fmt == Format.Json) {
            fmt = Format.Xml;
        } else if (fmt == Format.Xml) {
            fmt = Format.Json;
        }
        initializeXstream();
    }


    private void initializeStorage(String s) {
        try {
            initializeXstream();
            load();
        } catch (Throwable t) {
            LOG.error("Unable to connect to file, may not be able to save.");
        }
    }

    boolean deleteFile() {
        File file = new File(connString);
        return file.delete();
    }

    public Object load() {

        Reader reader = null;
        ObjectInputStream is = null;

        try {
            reader = new FileReader(connString);
            is = xstream.createObjectInputStream(reader);
            Object obj = is.readObject();
            if (obj != null) {
                return obj;
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);

        } finally {
            safelyClose(is);
            safelyClose(reader);
        }
        return null;
    }

    public boolean save(Object workingMemory) {
        Writer writer = null;
        ObjectOutputStream outStream = null;

        try {
            File file = new File(connString);
            writer = new FileWriter(file, false);
            outStream = xstream.createObjectOutputStream(writer);
            outStream.writeObject(workingMemory);
            return true;
        } catch (Throwable t) {
            LOG.error("Error saving to file.", t);
        } finally {
            safelyClose(outStream);
            safelyClose(writer);

        }
        return false;
    }

    private enum Format {
        Json(new JettisonMappedXmlDriver()), Xml(new DomDriver());
        final AbstractDriver driver;

        Format(AbstractDriver drv) {
            this.driver = drv;
        }
    }

}
