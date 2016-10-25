package pl.swidurski.gui.id3;

import pl.swidurski.gui.model.DataSet;
import pl.swidurski.gui.model.Attribute;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by student on 23.10.2016.
 */
public class CSVReader {

    public static final String SEPARATOR = ",";
    private final File file;
    private final DataSet dataSet = new DataSet();


    public CSVReader(File file) {
        this.file = file;
    }

    public DataSet read() throws IOException {
        List<String> strings = Files.readAllLines(file.toPath());
        String[] header = split(strings, 0);

        for (int i = 0; i < header.length; i++) {
            Attribute attribute = new Attribute(header[i]);
            attribute.setIndex(i);
            attribute.setResult(i == header.length - 1);
            dataSet.addAttribute(attribute);
        }

        for (int i = 1; i < strings.size(); i++) {
            String[] line = split(strings, i);
            for (int j = 0; j < line.length; j++) {
                dataSet.getAttribute(j).addValue(line[j]);
            }
        }
        return dataSet;
    }

    private String[] split(List<String> strings, int i) {
        return strings.get(i).split(SEPARATOR);
    }
}
