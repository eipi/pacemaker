package name.eipi.pacemaker.persistence;


import name.eipi.pacemaker.models.BaseEntity;
import name.eipi.services.logger.Logger;
import name.eipi.services.logger.LoggerFactory;

import java.util.*;

/**
 * Class to perform data operations on Entities.
 * Uses an IDatabaser implementation to manage data persistence.
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
        if (!load()) {
            reset();
        }
    }

    public void reset() {
        workingMemory = new HashMap<>();

    }

    // BEGIN DATA OPS
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
    // END DATA OPS


    // BEGIN FILE OPS
    public boolean load() {
        Object obj = db.read();
        if (obj != null && obj instanceof Map) {
            workingMemory = (Map) obj;
            return true;
        }
        return false;
    }

    public boolean save() {
        return db.write(workingMemory);
    }

    public boolean changeFormat(String format) {
        if (db.changeFormat(format)) {
            db.write(workingMemory);
            return true;
        } else {
            return false;
        }
    }

    // END FILE OPS

}
