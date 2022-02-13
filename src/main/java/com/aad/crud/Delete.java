package com.aad.crud;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

public class Delete {
    public static void main(String[] args) {

        // Conexion
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("users");
        MongoCollection<Document> users = database.getCollection("users");

       // delete one document
       Bson filter = eq("student_id", 10000);
       DeleteResult result = users.deleteOne(filter);
       System.out.println(result);

       // findOneAndDelete operation
       filter = eq("student_id", 10002);
       Document doc = users.findOneAndDelete(filter);
       System.out.println(doc.toJson(JsonWriterSettings.builder().indent(true).build()));
       // delete many documents

       filter = gte("student_id", 10000);
       result = users.deleteMany(filter);
       System.out.println(result);

       // delete the entire collection and its metadata (indexes, chunk metadata, etc).
       users.drop();
    }

}