package research.batch.bike;

import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.Sum;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;

public class ExtractAndSumBike extends PTransform<PCollection<Bike>, PCollection<KV<String, Integer>>> {
    @Override
    public PCollection<KV<String, Integer>> expand(PCollection<Bike> bike) {

        return bike
            .apply(
                MapElements.into(
                    TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.integers()))
                    .via((Bike b) -> KV.of(b.getDate(), b.getTotal())))
            .apply(Sum.integersPerKey());
    }
}
