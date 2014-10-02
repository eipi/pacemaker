package name.eipi.pacemaker.controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.AbstractDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.BaseEntity;
import name.eipi.pacemaker.models.Location;
import name.eipi.pacemaker.models.User;
import name.eipi.services.fileservice.FetchFileCmd;
import name.eipi.services.logger.Logger;
import name.eipi.services.logger.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Stream;

public class DataLodge {

    private static final Logger LOG = LoggerFactory.getInstance(DataLodge.class);

    private enum Format {
        Json(new JettisonMappedXmlDriver()), Xml(new DomDriver());
        final AbstractDriver driver;

        Format(AbstractDriver drv) {
            this.driver = drv;
        }
    }

    /*
    public static <Entity extends BaseEntity> DataLodge<Entity> getTyped(DataLodge instance, Class<Entity> typeToken){
        // instantiate the the proper factory by using the typeToken.
       DataLodge newInstance = new DataLodge<Entity>(instance.FILE_LOC);
        newInstance.fmt = instance.fmt;
        newInstance.initializeXstream();
        return newInstance;

    }
    */

    private Format fmt = Format.Json;
    private String FILE_LOC = "pacemaker.lodge";

    private Map<String, Map<Long, ? extends BaseEntity>> workingMemory = null;
    private XStream xstream = null;

    public DataLodge() {
        // Default
        initializeStorage();
    }

    public DataLodge(String s) {
        // Custom Location
        FILE_LOC = s;
        initializeStorage();
    }

    private void initializeStorage() {
        try {
            initializeXstream();
            load();
        } catch (Throwable t) {
            LOG.error("Unable to connect to file, may not be able to save.");
            reset();
        }
    }

    private void initializeXstream() {
        xstream = new XStream(fmt.driver);
        xstream.alias("user", User.class);
        xstream.alias("activity", Activity.class);
        xstream.alias("location", Location.class);
    }

    public void changeFileFormat() {
        if (fmt == Format.Json) {
            fmt = Format.Xml;
        } else if (fmt == Format.Xml) {
            fmt = Format.Json;
        }
        initializeXstream();
    }

    public boolean load() {
        try {
        	File file = new File(FILE_LOC);
        	if (file.isFile()) {
        		Object obj = xstream.fromXML(file);
                if (obj != null && obj instanceof Map) {
                    workingMemory = (Map) obj;
                    return true;
                } else {
                    reset();
                }
        	}
        } catch (Throwable t) {
            LOG.error("No data loaded from file.", t);
            reset();
        }
        return false;
    }


    public boolean save() {
    	OutputStream fileOut = null;
    	OutputStream objOut = null;
    	
        try {
        	File file = new File(FILE_LOC);
        	if (file.isFile()) {
        		fileOut = new FileOutputStream(file);
                objOut = new ObjectOutputStream(fileOut);
                xstream.toXML(workingMemory, objOut);
                return true;
        	}
        } catch (Throwable t) {
            LOG.error("Error saving to file.", t);
        } finally {
        	safelyClose(objOut);
        	safelyClose(fileOut);
        	
        }
        return false;
    }
    
    private static void safelyClose(Closeable closeable) {
    	if (closeable != null) {
    		try {
    			closeable.close();
    		} catch (Throwable  t) {
    			//  ignore
    		}
    	}
    }

    public <T extends BaseEntity> T create(T t) {
        if (workingMemory.containsKey(t.getClass().getName())) {
            Map index = workingMemory.get(t.getClass().getName());
            if (index instanceof Map) {
                if (t.getId() == null) {
                    Set<Long> ids = index.keySet();
                    Long max = ids.isEmpty() ? 0l : Collections.max(ids);
                    t.setId(max + 1);
                }
                // Add new OR overwrite existing.
                index.put(t.getId(), t);
            }
        } else {
            t.setId(1l);
            Collection<T> c = new ArrayList<T>();
            c.add(t);
            Map index = new TreeMap();
            index.put(t.getId(), t);
            workingMemory.put(t.getClass().getName(), index);
        }
        return t;
    }

    public <T extends BaseEntity> T edit(T t) {
        Map index = workingMemory.get(t.getClass().getName());
        if (index != null) {
            // Add new OR overwrite existing.
            if (t.getId() == null) {
                Set<Long> ids = index.keySet();
                Long max = ids.isEmpty() ? 0l : Collections.max(ids);
                t.setId(max + 1);
            }
            index.put(t.getId(), t);
            return t;
        } else {
            t.setId(1l);
            Collection<T> c = new LinkedList<T>();
            c.add(t);
            index = new TreeMap();
            index.put(t.getId(), t);
            workingMemory.put(t.getClass().getName(), index);
        }
        return t;
    }

    public <T extends BaseEntity> T read(Class<T> clazz, Long id) {
        if (clazz != null && id != null) {
            Map data = workingMemory.get(clazz.getName());
            if (data != null && data.containsKey(id)) {
                return (T) data.get(id);
            }
        }
        // not sure yet about this, continue use of optionals?
        return null;
    }

    public <T extends BaseEntity> Collection<T> getAll(Class c) {
        Collection<T> all = new ArrayList<T>();
        if (workingMemory.get(c.getName()) != null) {
            for (Object o : workingMemory.get(c.getName()).values()) {
                all.add((T) o);
            }
        }
        return all;
    }

    public <T extends BaseEntity> Collection<T> search(T t) {
        // search not yet implemented
        // just return all of type for now
        return getAll(t.getClass());
    }

    public <T extends BaseEntity> T delete(T t) {
        if (t.getId() != null) {
            Map index = workingMemory.get(t.getClass().getName());
            T returnObj = null;
            if (index.containsKey(t.getId())) {
                returnObj = (T) index.get(t.getId());

                // If that was the last of it's kind...
                if (index.size() == 1) {
                    workingMemory.remove(t.getClass().getName());
                    index = null;
                }
                return returnObj;
            } else {
                LOG.error("Not found for delete : " + t.getClass() + t.getId());
                return t;
            }
        } else {
            LOG.error("Bad arg in delete : " + t.getClass() + t.getId());
            return t;
        }
    }

    public <T extends BaseEntity> void reset() {
        workingMemory = new TreeMap<>();

    }

}
