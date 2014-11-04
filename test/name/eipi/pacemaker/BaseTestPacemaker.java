package name.eipi.pacemaker;

import name.eipi.pacemaker.persistence.DataBaserXStreamImpl;

/**
 * Created by naysayer on 19/10/2014.
 */
public class BaseTestPacemaker {

    public BaseTestPacemaker() {
        DataBaserXStreamImpl.DELETE_ON_EXIT = Boolean.TRUE;
    }

}
