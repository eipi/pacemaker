package name.eipi.services.fileservice;

import java.io.IOException;

/**
 * Created by dbdon_000 Date: 10/08/13
 */
public class FetchFileCmd {

    public static TextFile fetchTextFile(final String pathName)
            throws IOException {

        TextFile file = new TextFile(pathName);
        if (file.createNewFile()) {
            file.setNew(true);
        } else {
            file.setNew(false);
        }
        return file;
    }
}
