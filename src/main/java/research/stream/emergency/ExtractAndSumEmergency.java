package research.stream.emergency;

import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.Sum;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;
import research.entities.Emergency;

public class ExtractAndSumEmergency extends PTransform<PCollection<Emergency>, PCollection<KV<String, Long>>> {
    @Override
    public PCollection<KV<String, Long>> expand(PCollection<Emergency> emergency) {
        return emergency
            .apply(
                MapElements.into(
                    TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.longs()))
                    .via((Emergency e) -> KV.of(e.getDate(), e.getCount())))
            .apply(Sum.longsPerKey());
    }
}
