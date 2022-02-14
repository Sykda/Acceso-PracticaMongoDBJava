package com.aad.pojos;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Collections.singletonList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

public class MappingPOJO {
	public static void main(String[] args) {

		ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
		CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.codecRegistry(codecRegistry).build();
		try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
			MongoDatabase db = mongoClient.getDatabase("sample_training");
			MongoCollection<Grade> grades = db.getCollection("grades", Grade.class);

			// create a new grade.
			Grade newGrade = new Grade();
			newGrade.setStudentId(10003d);
			newGrade.setClassId(10d);
			Score score = new Score();
			score.setType("homework");
			score.setScore(50d);
			newGrade.setScores(singletonList(score));
			grades.insertOne(newGrade);

			// find this grade.
			Grade grade = grades.find(eq("student_id", 10003d)).first();
			System.out.println("Grade found:\t" + grade);

			// update this grade: adding an exam grade
			List<Score> newScores = new ArrayList<>(grade.getScores());
			Score score2 = new Score();
			score2.setType("exam");
			score2.setScore(42d);
			newScores.add(score2);
			grade.setScores(newScores);
			Document filterByGradeId = new Document("student_id", grade.getId());
			FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions()
					.returnDocument(ReturnDocument.AFTER);
			Grade updatedGrade = grades.findOneAndReplace(filterByGradeId, grade, returnDocAfterReplace);
			System.out.println("Grade replaced:\t" + updatedGrade);

			// delete this grade
			System.out.println(grades.deleteOne(filterByGradeId));
		}
	}
}