package it.unipi.dii.reviook_app.entity;

public class RankingObject {
    private String name;
    private Integer count;
    private Double avg;

    public RankingObject(String user, Integer count, Double avg)
    {
        this.name = user;
        this.count= count;
        this.avg = Math.round(avg*100.0)/100.0;
    }

    @Override
    public String toString() {
        return "Username: " + name +
                "   Total Review" + count +
                "   Average Helpful: " + avg
                ;
    }
}
