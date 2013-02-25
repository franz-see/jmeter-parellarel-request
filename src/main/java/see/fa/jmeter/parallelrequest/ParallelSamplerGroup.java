package see.fa.jmeter.parallelrequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

/**
 *
 * @author franz
 */
public class ParallelSamplerGroup {
    private final List<Sampler> samplers;
    private final ExecutorService executorService;

    private transient boolean alreadyRunning = false;

    private transient Map<Sampler, Future<SampleResult>> futures = null;

    public ParallelSamplerGroup(List<Sampler> samplers) {
        this.samplers = samplers;
        executorService = Executors.newFixedThreadPool(samplers.size());
    }

    public SampleResult sample(Sampler sampler) {
        if (alreadyRunning) {
            return getSamplerResult(sampler);
        }

        alreadyRunning = true;
        futures = executeAllSamplersAsynchronously();
        return getSamplerResult(sampler);
    }

    private Map<Sampler, Future<SampleResult>> executeAllSamplersAsynchronously() {
        Map<Sampler, Future<SampleResult>> futures = new LinkedHashMap<Sampler, Future<SampleResult>>();
        for (Sampler sampler : samplers) {
            futures.put(sampler, executorService.submit(new SamplerCallable(sampler)));
        }
        return futures;
    }

    private SampleResult getSamplerResult(Sampler sampler) {
        try {
            return futures.get(sampler).get();
        } catch (InterruptedException ex) {
            throw new ParallelSamplerGroupException("Unable to retrieve SampleResult.", ex);
        } catch (ExecutionException ex) {
            throw new ParallelSamplerGroupException("Unable to retrieve SampleResult.", ex);
        }
    }

    public static class SamplerCallable implements Callable<SampleResult> {
        private final Sampler sampler;

        private SamplerCallable(Sampler sampler) {
            this.sampler = sampler;
        }

        public SampleResult call() throws Exception {
            return sampler.sample(null);
        }
    }

}
