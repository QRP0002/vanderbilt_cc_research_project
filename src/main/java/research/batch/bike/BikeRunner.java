package research.batch.bike;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.bson.Document;
import org.apache.beam.sdk.io.mongodb.MongoDbIO;

public class BikeRunner {

    public static void main(String[] args) {
        BikeRunnerOptions options = PipelineOptionsFactory.fromArgs(args).withoutStrictParsing().as(BikeRunnerOptions.class);
        runRunner(options);
    }

    static void runRunner(BikeRunnerOptions options) {
        Pipeline pipeline = Pipeline.create(options);
            try {
                pipeline
                        .apply("Read Input File", TextIO.read().from(options.getInputFile()))
                        .apply("Parse Input", ParDo.of(new ParseBikeEventFn()))
                        .apply("Extract Total Crossing", new ExtractAndSumBike())
                        .apply("Convert to Json", MapElements.via(new SimpleFunction<KV<String, Integer>, Document>() {
                            public Document apply(KV<String, Integer> input) {
                                return Document.parse("{\"" + input.getKey() + "\": " + input.getValue() + "}");
                            }
                        }))
                        .apply("Write To Mongo",
                                MongoDbIO.write()
                                        .withUri(options.getMongoUri())
                                        .withDatabase(options.getMongoDatabase())
                                        .withCollection(options.getMongoCollection())
                        );
            } catch (Exception e) {
                System.out.println("LOG: This is an issue " + e);
            }

            pipeline.run();
    }
}