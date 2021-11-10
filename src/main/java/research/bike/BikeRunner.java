package research.bike;

import java.util.HashMap;
import java.util.Map;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import research.WriteToText;

public class BikeRunner {

    public static void main(String[] args) {
        BikeRunnerOptions options = PipelineOptionsFactory.fromArgs(args).withoutStrictParsing().as(BikeRunnerOptions.class);
        runRunner(options);
    }

    static void runRunner(BikeRunnerOptions options) {
        Pipeline pipeline = Pipeline.create(options);

        pipeline
            .apply("Read Input File", TextIO.read().from(options.getInputFile()))
            .apply("Parse Input", ParDo.of(new ParseBikeEventFn()))
            .apply("Extract Total Crossing", new ExtractAndSumBike())
            .apply("Write Output", new WriteToText<>(options.getOutputFile(), configureOutput(), false));
        pipeline.run().waitUntilFinish();
    }

    protected static Map<String, WriteToText.FieldFn<KV<String, Integer>>> configureOutput() {
        Map<String, WriteToText.FieldFn<KV<String, Integer>>> config = new HashMap<>();
        config.put("Date", (c, w) -> c.element().getKey());
        config.put("Total Riders", (c, w) -> c.element().getValue());
        return config;
    }
}
