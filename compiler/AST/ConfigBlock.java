package AST;

import java.util.*;

// Config block: base_url and default headers
public class ConfigBlock {
    public String baseUrl = null;
    public Map<String, String> defaultHeaders = new LinkedHashMap<>();

    public ConfigBlock(List<ConfigItem> items) {
        for (ConfigItem item : items) {
            if (item.type.equals("base_url")) {
                this.baseUrl = item.value1;
            } else if (item.type.equals("header")) {
                this.defaultHeaders.put(item.value1, item.value2);
            }
        }
    }
}
