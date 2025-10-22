package AST;

public class ConfigItem {
    public String type;   // "base_url" or "header"
    public String value1; // url or header key
    public String value2; // header value (null for base_url)

    public ConfigItem(String type, String value1) {
        this(type, value1, null);
    }

    public ConfigItem(String type, String value1, String value2) {
        this.type = type;
        this.value1 = value1;
        this.value2 = value2;
    }
}
