package com.aad.crud;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class Select {

	public static void main(String args[]) {

		// Conexion
		MongoClient client = MongoClients.create("mongodb://localhost:27017");
		MongoDatabase database = client.getDatabase("users");
		MongoCollection<Document> users = database.getCollection("users");

		// show user1
		Document user = users.find(new Document("user", "user1")).first();
		System.out.println(user.toJson());

		// find one document with new Document
		Document student1 = users.find(new Document("student_id", 10000)).first();
		System.out.println("Student 1: " + student1.toJson());

		// find one document with Filters.eq()
		Document student2 = users.find(eq("student_id", 10000)).first();
		System.out.println("Student 2: " + student2.toJson());

		// find a list of documents and iterate throw it using an iterator.
		FindIterable<Document> iterable = users.find(gte("student_id", 10000));
		MongoCursor<Document> cursor = iterable.iterator();
		System.out.println("Student list with a cursor: ");
		while (cursor.hasNext()) {
			System.out.println(cursor.next().toJson());
		}
		// find a list of documents and use a List object instead of an iterator
		List<Document> studentList = users.find(gte("student_id", 10000)).into(new ArrayList<>());
		System.out.println("Student list with an ArrayList:");
		for (Document student : studentList) {
			System.out.println(student.toJson());
		}
		// find a list of documents and print using a consumer
		System.out.println("Student list using a Consumer:");
		Consumer<Document> printConsumer = document -> System.out.println(document.toJson());
		users.find(gte("student_id", 10000)).forEach(printConsumer);

		// find a list of documents with sort, skip, limit and projection
		List<Document> docs = users.find(and(eq("student_id", 10001), lte("class_id", 5)))
				.projection(fields(excludeId(), include("class_id", "student_id")))
				.sort(descending("class_id"))
				.skip(2)
				.limit(2)
				.into(new ArrayList<>());
		System.out.println("Student sorted, skipped, limited and projected: ");
		for (Document student : docs) {
			System.out.println(student.toJson());

		}
	}
}