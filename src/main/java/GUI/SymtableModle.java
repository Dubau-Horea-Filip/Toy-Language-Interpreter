package GUI;

public class SymtableModle {

    private String name = null;
    private String value = null;

    public SymtableModle(String a, String b)
    {
        this.name = a;
        this.value= b;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
