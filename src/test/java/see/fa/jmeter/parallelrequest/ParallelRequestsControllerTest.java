package see.fa.jmeter.parallelrequest;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.junit.Assert.*;

public class ParallelRequestsControllerTest {

    @Test
    public void should_execute_all_samplers_upon_first_sampling() throws InterruptedException {
        ParallelRequestsController parallelRequestsController = new ParallelRequestsController();

        List<SamplerSpy> samplerSpies = Arrays.asList(new SamplerSpy(), new SamplerSpy(), new SamplerSpy());
        for (Sampler sampler : samplerSpies) {
            parallelRequestsController.addTestElement(sampler);
        }

        Sampler firstSampler = parallelRequestsController.next();
        firstSampler.sample(null);

        Thread.sleep(100);

        int numOfSamplings = 0;
        for (SamplerSpy samplerSpy : samplerSpies) {
            if (samplerSpy.wasSampled()) {
                numOfSamplings++;
            }
        }

        assertEquals("Should have sampled all samplers after the first Sampler#sample(<Entry>) invocation.",
                samplerSpies.size(), numOfSamplings);
    }

    @Test
    public void should_execute_all_samplers_in_parallel_upon_first_sampling() throws InterruptedException, ExecutionException {
        ParallelRequestsController parallelRequestsController = new ParallelRequestsController();

        AtomicInteger groupCount = new AtomicInteger();
        int numOfSamplersToSample = 3;
        for (int i = 0; i < numOfSamplersToSample; i++) {
            parallelRequestsController.addTestElement(new SamplerWaitingForOtherSamplers(groupCount, numOfSamplersToSample));
        }

        final Sampler firstSampler = parallelRequestsController.next();

        Future future = Executors.newSingleThreadExecutor().submit(new Runnable(){
            public void run() {
                firstSampler.sample(null);
            }
        });
        try {
            future.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            fail("SamplerWaitingForOtherSamplers waits for each other to be invoked before any of them can finish. " +
                "This timeout means that the thread we spawned (to do the sampling) timed out, because not all of " +
                "the SamplerWaitingForOtherSamplers were ran. ");
        }
    }

    @Test
    public void all_samplers_should_have_individual_results() throws InterruptedException, TimeoutException, ExecutionException {
        ParallelRequestsController parallelRequestsController = new ParallelRequestsController();

        List<SimpleSampler> simpleSamplers = Arrays.asList(new SimpleSampler(), new SimpleSampler(), new SimpleSampler());
        for (Sampler sampler : simpleSamplers) {
            parallelRequestsController.addTestElement(sampler);
        }

        SampleResult firstSamplerResult = parallelRequestsController.next().sample(null);
        SampleResult secondSamplerResult = parallelRequestsController.next().sample(null);
        SampleResult thirdSamplerResult = parallelRequestsController.next().sample(null);

        assertNotNull("First Sampler Result should NOT have been null.", firstSamplerResult);
        assertNotNull("Second Sampler Result should NOT have been null.", secondSamplerResult);
        assertNotNull("Third Sampler Result should NOT have been null.", thirdSamplerResult);

        assertNotSame("First Sampler Result should NOT be the same as the Second Sample Result.", firstSamplerResult, secondSamplerResult);
        assertNotSame("First Sampler Result should NOT be the same as the Third Sample Result.", firstSamplerResult, thirdSamplerResult);
        assertNotSame("Second Sampler Result should NOT be the same as the Third Sample Result.", secondSamplerResult, thirdSamplerResult);
    }

    @Test
    public void all_sampler_results_should_have_practically_the_same_start_time() throws InterruptedException, TimeoutException, ExecutionException {
        ParallelRequestsController parallelRequestsController = new ParallelRequestsController();

        List<SimpleSampler> simpleSamplers = Arrays.asList(new SimpleSampler(), new SimpleSampler(), new SimpleSampler());
        for (Sampler sampler : simpleSamplers) {
            parallelRequestsController.addTestElement(sampler);
        }

        SampleResult firstSamplerResult = parallelRequestsController.next().sample(null);
        SampleResult secondSamplerResult = parallelRequestsController.next().sample(null);
        SampleResult thirdSamplerResult = parallelRequestsController.next().sample(null);

        double earliestStart = min(min(firstSamplerResult.getStartTime(), secondSamplerResult.getStartTime()), thirdSamplerResult.getStartTime());
        double latestStart = max(max(firstSamplerResult.getStartTime(), secondSamplerResult.getStartTime()), thirdSamplerResult.getStartTime());

        assertEquals(earliestStart, latestStart, 1);
    }
}
