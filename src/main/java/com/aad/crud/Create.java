package com.aad.crud;

import static java.util.Arrays.asList;

import java.util.Random;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Create {
    public static void main(String[] args) {
        // Conexion
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("users");
        MongoCollection<Document> users = database.getCollection("users");

        Random rand = new Random();
        Document student = new Document("_id", new ObjectId());
        student.append("student_id", 10000d)
                .append("class_id", 1d)
                .append("scores", asList(new Document("type", "exam").append("score", rand.nextDouble() * 100),
                        new Document("type", "quiz").append("score", rand.nextDouble() * 100),
                        new Document("type", "homework").append("score", rand.nextDouble() * 100),
                        new Document("type", "homework").append("score", rand.nextDouble() * 100)));
        users.insertOne(student);
    }

}