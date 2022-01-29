package it.unipi.dii.reviook_app.entity;

public class RankingObject {
    private String name;
    private Integer count;
    private Double avg;

    public RankingObject(String name, Integer count, Double avg) {
        this.name = name;
        this.count = count;
        this.avg = Math.round(avg * 100.0) / 100.0;
    }

    public RankingObject(String name, Integer count) {
        this(name, count, 0.0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    @Override
    public String toString() {
        return /*"username: " +*/ name +
                "\n\n   - " + count + " reviews" +
                "\n\n   - " + avg + " average helpful";
    }

}
