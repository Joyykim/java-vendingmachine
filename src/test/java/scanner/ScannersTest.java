package scanner;

import org.assertj.core.util.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private PrintStream standardOut;

    private OutputStream captor;

    @BeforeEach
    void setUp() {
        standardOut = System.out;
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
    }

    @AfterEach
    void tearDown() {
        // 테스트가 끝났다고 알려줄 시점은 이때 밖에 없는 듯?
        NewScanners.close();
        System.setOut(standardOut);
        System.out.println(captor.toString());
    }

    private void subject(final String... args) {
        final byte[] buf = Strings.join(args).with("\n").getBytes();
        System.setIn(new ByteArrayInputStream(buf));
        TestAppForNewScanners.main(new String[]{});
    }

    private List<String> getOutputLines() {
        return Arrays.asList(captor.toString().trim().split("\n"));
    }

    @ParameterizedTest
    @MethodSource("e2eTestSource")
    void e2eTest(List<String> input) {
        // given
        setInput(input);

        // when
        TestAppForNewScanners.main(new String[]{});

        // then
        List<String> outputLines = getOutputLines();
        assertThat(outputLines).containsExactly("a" ,"b");
    }

    private void setInput(List<String> input) {
        final byte[] buf = Strings
                .join(input.toArray(new String[0]))
                .with("\n").getBytes();
        System.setIn(new ByteArrayInputStream(buf));
    }

    private static Stream<Arguments> e2eTestSource() {
        return Stream.of(
                Arguments.of(Arrays.asList("a", "b", "c", "d")),
                Arguments.of(Arrays.asList("a", "b")),
                Arguments.of(Arrays.asList("a", "b", "c", "d"))
        );
    }
}