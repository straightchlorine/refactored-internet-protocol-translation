import org.junit.Test;
import static org.junit.Assert.assertEquals;

import ip.translation.input.processing.Segmentation;
import ip.translation.input.InputModule;

import java.util.List;

public class InputModuleTest {

    List<Integer> correct = Segmentation.segment("192.168.1.1");

    @Test
    public void testRead() {
        System.out.println("In the testclass");
        assertEquals(InputModule.read(), correct);
    }
}
