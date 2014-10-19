package name.eipi.pacemaker;

import name.eipi.pacemaker.persistence.DataBaserXStreamImpl;
import name.eipi.services.logger.LoggerFactory;

/**
 * Created by naysayer on 19/10/2014.
 */
public class BaseTestPacemaker {

    public BaseTestPacemaker() {
        LoggerFactory.setTestMode(Boolean.TRUE);
        DataBaserXStreamImpl.DELETE_ON_EXIT = Boolean.TRUE;
    }

}
