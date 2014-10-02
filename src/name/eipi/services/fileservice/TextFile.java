package name.eipi.services.fileservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by dbdon_000 Date: 10/08/13
 */
public class TextFile extends File {

    private static final long serialVersionUID = 1L;
    private boolean isNew = false;

    public TextFile(String pathname) {
        super(pathname);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void write(String message) throws Exception {
        FileWriter writer = new FileWriter(this, true);
        writer.append(message);
        writer.close();
    }

    public void clear() throws Exception {
        FileWriter writer = new FileWriter(this);
        writer.write("");
        writer.close();
    }

    public String read() throws Exception {
        StringBuffer buffer = new StringBuffer();
        FileReader reader = new FileReader(this);
        BufferedReader bufferedReader = new BufferedReader(reader);
        while (bufferedReader.ready()) {
            buffer.append(bufferedReader.readLine());
        }
        bufferedReader.close();
        reader.close();
        return buffer.toString();
    }

}
