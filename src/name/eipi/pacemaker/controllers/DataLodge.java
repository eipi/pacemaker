package name.eipi.pacemaker.controllers;


import name.eipi.pacemaker.models.BaseEntity;
import name.eipi.services.logger.Logger;
import name.eipi.services.logger.LoggerFactory;

import java.util.*;

/**
 * Class to perform data operations on Entities. Uses a Databaser to manage data persistence.
 *
 * @author naysayer
 */
public class DataLodge {

    private static final Logger LOG = LoggerFactory.getInstance(DataLodge.class);
    /**
     * Databaser.
     */
    private IDataBaser db = null;

    /*
    public static <Entity extends BaseEntity> DataLodge<Entity> getTyped(DataLodge instance, Class<Entity> typeToken){
        // instantiate the the proper factory by using the typeToken.
       DataLodge newInstance = new DataLodge<Entity>(instance.FILE_LOC);
        newInstance.fmt = instance.fmt;
        newInstance.initializeXstream();
        return newInstance;

    }
    */
    private Map<String, Map<Long, ? extends BaseEntity>> workingMemory = null;

    public DataLodge() {
        // Default
        db = DatabaserFactory.getInstance();
        initWorkingMemory();
    }

    public DataLodge(String s) {
        // Custom Location
        db = DatabaserFactory.getInstance(s);
        initWorkingMemory();
    }

    private void initWorkingMemory() {
    //    db.read();
        Object data = db.pop();
        if (data != null && data instanceof Map) {
            workingMemory = (Map) data;
        } else {
            reset();
        }
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
            index = new HashMap();
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
            if (index != null && index.containsKey(t.getId())) {
                returnObj = (T) index.get(t.getId());

                // If that was the last of it's kind...
                if (index.size() == 1) {
                    workingMemory.remove(t.getClass().getName());
                    index = null;
                } else {
                    index.remove(t.getId());
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


    public void save() {
        db.push(workingMemory);
    //    db.write();
    }

    public boolean load() {
        Object obj = db.pop();
        if (obj != null && obj instanceof Map) {
            workingMemory = (Map) obj;
            return true;
        }
        return false;
    }

    public void toggleFormat() {
        db.toggleFormat();
    }

    public <T extends BaseEntity> void reset() {
        workingMemory = new HashMap<>();

    }

    void deleteFile() {
        db.cleanUp();
    }
}
