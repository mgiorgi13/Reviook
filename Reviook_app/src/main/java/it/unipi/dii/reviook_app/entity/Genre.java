package it.unipi.dii.reviook_app.entity;

public class Genre {
    private String type;
    private Integer value;

    public Genre(String type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public Genre(String type) {
        this(type,0);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return type;
    }
}
