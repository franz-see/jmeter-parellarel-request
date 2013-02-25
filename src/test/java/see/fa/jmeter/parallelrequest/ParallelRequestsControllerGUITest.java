package see.fa.jmeter.parallelrequest;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class ParallelRequestsControllerGUITest {

    @BeforeClass
    public static void given_locale_is_set() {
        Locale dummyLocale = Locale.ENGLISH;
        JMeterUtils.setLocale(dummyLocale);
    }

    @Test
    public void createTestElement_should_create_a_ParallelRequestsController() {
        ParallelRequestsControllerGUI gui = new ParallelRequestsControllerGUI();
        TestElement te = gui.createTestElement();
        assertThat(te, instanceOf(ParallelRequestsController.class));
    }
}
