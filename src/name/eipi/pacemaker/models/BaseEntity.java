package name.eipi.pacemaker.models;

import com.google.gson.Gson;

import java.lang.reflect.Field;

public abstract class BaseEntity {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 101;

        int result = 1;

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                Object value = f.get(this);
                result = prime * result + ((value == null) ? 0 : value.hashCode());
            } catch (Throwable t) {
                // should never happen
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Field[] fields = this.getClass().getDeclaredFields();
            try {
                for (Field f : fields) {
                    Object thisValue = f.get(this);
                    Object thatValue = f.get(obj);

                    if (thisValue.equals(thatValue)) {
                        continue;
                    } else if (thisValue != null && !thisValue.equals(thatValue)) {
                        return false;
                    }
                }
            } catch (IllegalAccessException ex) {
                return false;
            }
        }
        return true;
    }

}
