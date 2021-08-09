package vendingmachine.model;

import com.woowahan.techcourse.utils.Randoms;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import vendingmachine.App;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class AppTest {
    private static final Duration SIMPLE_TEST_TIMEOUT = Duration.ofSeconds(1L);
    private static final String ERROR_MESSAGE = "[ERROR]";

    private void subject(final String... args) {
        command(args);
        App.main(new String[]{});
    }

    private void command(final String... args) {
        final byte[] buf = Strings.join(args).with("\n").getBytes();
        System.setIn(new ByteArrayInputStream(buf));
    }

    private void assertSimpleTest(final Executable executable) {
        assertTimeoutPreemptively(SIMPLE_TEST_TIMEOUT, executable);
    }

    @DisplayName("기능 테스트")
    @Nested
    class FunctionTest {
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
            System.setOut(standardOut);
            System.out.println(captor.toString());
        }

        private List<String> getOutputLines() {
            return Arrays.asList(captor.toString().trim().split("\n"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "2"})
        void 전체기능_테스트1(String i) {
            assertSimpleTest(() -> {
                try (MockedStatic<Randoms> utilities = Mockito.mockStatic(Randoms.class)) {
                    utilities.when(() -> Randoms.pick(Mockito.any())).thenReturn(100);

                    subject(
                            // 자판기가 보유하고 있는 금액을 입력해 주세요.
                            "10000",
                            // 상품명과 수량, 금액을 입력해 주세요.
                            "[콜라,20,1500],[사이다,10,1000]",
                            // 투입할 금액을 입력해 주세요.
                            "3000",
                            // 구매할 상품명을 입력해 주세요.
                            "콜라", "사이다"
                    );
                    List<String> outputLines = getOutputLines();
                    assertThat(outputLines.get(outputLines.size() - 4)).isEqualTo("500원 - 1개");
                    assertThat(outputLines.get(outputLines.size() - 3)).isEqualTo("100원 - 0개");
                    assertThat(outputLines.get(outputLines.size() - 2)).isEqualTo("50원 - 0개");
                    assertThat(outputLines.get(outputLines.size() - 1)).isEqualTo("10원 - 0개");
                }
            });
        }
    }
}