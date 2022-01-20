package it.unipi.dii.reviook_app.entity;

public class Genre {
    private String type;
    private Double value;

    public Genre(String type, Double value) {
        this.type = type;
        this.value = value;
    }

    public Genre(String type) {
        this(type,0.0);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return type;
    }
}
