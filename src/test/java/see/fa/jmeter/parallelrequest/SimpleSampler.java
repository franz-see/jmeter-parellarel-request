package see.fa.jmeter.parallelrequest;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;

import java.util.Random;

public class SimpleSampler extends AbstractSampler {

    private final long randomWait;

    public SimpleSampler() {
        randomWait = Math.abs(new Random().nextLong() % 1000);
    }

    public SampleResult sample(Entry e) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        try {
            Thread.sleep(randomWait);
        } catch (InterruptedException e1) {
            throw new RuntimeException("Unable to simulate long execution time.", e1);
        }
        sampleResult.sampleEnd();
        return sampleResult;
    }
}
