package hello;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@EnableMongoRepositories(basePackages = "hello")
public class MongoConfig extends AbstractMongoConfiguration {
    @Autowired
    Environment environment;

    @Override
    public MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(String.format("mongodb://%s:%s@%s/?replicaSet=%s&authSource=%s",
                environment.getProperty("mongo.userName"), environment.getProperty("mongo.password"), environment.getProperty("mongo.host"),
                environment.getProperty("mongo.replicaSetName"), environment.getProperty("mongo.authenticationDatabase")));
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        return new MongoClient(uri);
    }

    @Override
    protected String getDatabaseName() {
        return environment.getProperty("mongo.database");
    }
}