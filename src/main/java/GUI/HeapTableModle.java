package GUI;

public class HeapTableModle {

    private String address = null;
    private String value = null;

    public HeapTableModle(String a, String b)
    {
        this.address = a;
        this.value= b;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
