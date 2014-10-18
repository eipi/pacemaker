package name.eipi.pacemaker.controllers;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Created by naysayer on 18/10/2014.
 */
public class Response<T> extends ArrayList<T> {

    @Getter
    @Setter
    private Boolean success;

    @Getter
    @Setter
    private String message;

    public T value() {
        if (this.size() == 1) {
            return this.iterator().next();
        } else {
            return null;
        }

    }

}
