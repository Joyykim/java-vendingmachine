package scanner;

import org.assertj.core.util.Strings;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import vendingmachine.utils.NewScanners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ScannersTest {

    @ParameterizedTest
    @MethodSource
    void test(List<String> input) {
        // given
        final PrintStream standardOut = System.out;
        final OutputStream captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
        final byte[] buf = Strings.join(input.toArray(new String[0])).with("\n").getBytes();
        System.setIn(new ByteArrayInputStream(buf));

        // when
        TestAppForNewScanners.main(new String[]{});

        // then
        assertThat(getOutputLines(captor)).containsExactly("a" ,"b");

        // after each
        // 테스트가 끝났다고 알려줄 시점은 이때 밖에 없는 듯?
        NewScanners.close();
        System.setOut(standardOut);
        System.out.println(captor.toString());
    }

    private static Stream<Arguments> test() {
        return Stream.of(
                Arguments.of(Arrays.asList("a", "b", "c", "d")),
                Arguments.of(Arrays.asList("a", "b")),
                Arguments.of(Arrays.asList("a", "b", "c", "d"))
        );
    }

    private String[] getOutputLines(OutputStream captor) {
        return captor.toString().trim().split("\n");
    }
}