/*
 * From "Guest_Tom W" on http://www.dreamincode.net/forums/topic/53734-java-code-to-modify-properties-file-and-preserve-comments/page__st__15
 * based on a Forum Post!
 */
package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PropertiesChanger {

    private final List<String> lines = new ArrayList<String>();
    private final Map<String, String> props = new HashMap<String, String>();

    public void load(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
        String line;

        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        is.close();
    }

    public void setProperty(String name, String value) {
        props.put(name, value);
    }

    public void save(OutputStream os) {
        PrintStream ps = new PrintStream(os);
        Pattern p = Pattern.compile("([^=]*)=.*");

        // Keep track of properties that were set
        List<String> usedProps = new ArrayList<String>();

        for (String line : lines) {
            Matcher matcher = p.matcher(line);
            if (matcher.find()) {
                String propName = matcher.group(1);
                if (props.containsKey(propName)) {
                    String value = props.get(propName);
                    ps.println(propName + "=" + value);
                    usedProps.add(propName);
                } else {
                    ps.println(line);
                }
            } else {
                ps.println(line);
            }
        }

        // Add any new properties
        for (Map.Entry<String, String> entry : props.entrySet()) {
            if (!usedProps.contains(entry.getKey())) {
                ps.println(entry.getKey() + "=" + entry.getValue());
            }
        }
        ps.close();
    }
}

