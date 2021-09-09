package vendingmachine;

import com.woowahan.techcourse.utils.Console;
import com.woowahan.techcourse.utils.Randoms;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mockStatic;

public class ApplicationTest {

    private static final String ERROR_PREFIX = "[ERROR]";
    private static final Duration SIMPLE_TEST_TIMEOUT = Duration.ofSeconds(5);

    private PrintStream standard;
    private OutputStream captor;

    @BeforeEach
    void setUp() {
        standard = System.out;
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standard);
        System.out.println(captor);
    }

    private void assertSimpleTest(final Executable executable) {
        assertTimeoutPreemptively(SIMPLE_TEST_TIMEOUT, executable);
    }

    @Test
    void success() {
        // given
        try (final MockedStatic<Randoms> mockRandoms = mockStatic(Randoms.class);
             final MockedStatic<Console> mockConsole = mockStatic(Console.class)) {

            mockRandoms.when(() -> Randoms.pickNumberInList(anyList())).thenReturn(100, 100, 100, 100, 50);
            List<String> args = Arrays.asList("450", "[콜라,20,1500];[사이다,10,1000]", "3000", "콜라", "사이다");
            mockConsole(args, mockConsole);

            // when
            Application.main(new String[]{});
        }

        // then
        String outPut = getOutPut();
        assertThat(outPut).contains("남은 금액: 50");
        assertThat(outPut).contains("100원 - 4개");
        assertThat(outPut).contains("50원 - 1개");
    }

    private String getOutPut() {
        return captor.toString().trim();
    }

    @ParameterizedTest
    @MethodSource({"상품여러개_입력_예외테스트", "상품한개_입력_예외테스트", "구매_예외테스트", "자판기_보유금액_예외테스트"})
    void exception(List<String> args) {
        try (final MockedStatic<Randoms> mockRandoms = mockStatic(Randoms.class);
             final MockedStatic<Console> mockConsole = mockStatic(Console.class)) {

            mockRandoms.when(() -> Randoms.pickNumberInList(anyList())).thenReturn(100, 100, 100, 100, 50);
            mockConsole(args, mockConsole);

            assertThatThrownBy(() -> Application.main(new String[]{}))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageStartingWith(ERROR_PREFIX);
        }
    }

    private void mockConsole(List<String> args, MockedStatic<Console> mockConsole) {
        mockConsole.when(Console::readLine).thenReturn(args.get(0), args.subList(1, args.size()).toArray());
    }

    private static Arguments toArguments(String... s) {
        return Arguments.of(Arrays.asList(s));
    }

    private static Stream<Arguments> 상품여러개_입력_예외테스트() {
        return Stream.of(
                toArguments("450", "콜라,20,1500;사이다,10,1000"),
                toArguments("450", "{콜라,20,1500};{사이다,10,1000}"),
                toArguments("450", "[콜라,20,1500];[사이다,10,1000"),
                toArguments("450", "[콜라,20,1500][사이다,10,1000]"),
                toArguments("450", "[콜라,20,1500],[사이다,10,1000]"),
                toArguments("450", "[콜라,20,1500];[사이다,10]"),
                toArguments("450", "[콜라,20,1500];[사이다,aa,1000]")
        );
    }

    private static Stream<Arguments> 상품한개_입력_예외테스트() {
        return Stream.of(
                toArguments("450", "콜라,20,1500"),
                toArguments("450", "{콜라,20,1500}"),
                toArguments("450", "[콜라;20;1500]"),

                toArguments("450", "[콜라,20,]"),
                toArguments("450", "[콜라,20,1500,10]"),

                toArguments("450", "[콜라,20,90]"),
                toArguments("450", "[콜라,20,101]"),

                toArguments("450", "[콜라,사이다,1500]"),
                toArguments("450", "[콜라,20,사이다]")
        );
    }

    private static Stream<Arguments> 구매_예외테스트() {
        return Stream.of(
                // 등록되지 않은 상품명
                toArguments("450", "[콜라,20,1500];[사이다,10,1000]", "3000", "밀키스"),
                // 물품 수량 없음
                toArguments("450", "[콜라,1,1500];[사이다,10,1000]", "3000", "콜라", "콜라"),
                // 투입한 금액 부족
                toArguments("450", "[콜라,20,1500];[사이다,10,1000]", "1100", "콜라", "콜라")
        );
    }

    private static Stream<Arguments> 자판기_보유금액_예외테스트() {
        return Stream.of(
                toArguments("9"),
                toArguments("1001")
        );
    }
}
