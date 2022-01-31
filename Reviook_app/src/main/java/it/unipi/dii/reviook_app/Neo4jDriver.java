package it.unipi.dii.reviook_app;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

@SuppressWarnings("unchecked")
public class Neo4jDriver {
    private static Neo4jDriver neo = null;
    private final Driver driver;

    private Neo4jDriver() {
        driver = GraphDatabase.driver("bolt://172.16.4.102:7687", AuthTokens.basic("neo4j", "reviook"));
//        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "root"));
    }

    public static Neo4jDriver getInstance() {
        if (neo == null)
            neo = new Neo4jDriver();

        return neo;
    }

    public Driver getDriver() {
        if (neo == null)
            throw new RuntimeException("Connection doesn't exist.");
        else
            return neo.driver;
    }

    public void close() {
        if (neo == null)
            throw new RuntimeException("Connection doesn't exist.");
        else
            neo.driver.close();
    }


}