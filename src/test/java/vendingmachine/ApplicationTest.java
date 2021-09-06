package vendingmachine;

import com.woowahan.techcourse.utils.Randoms;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import vendingmachine.utils.NewScanners;

import java.io.*;
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
    private static final Duration SIMPLE_TEST_TIMEOUT = Duration.ofSeconds(3);

    private PrintStream standard;
    private OutputStream captor;

    @AfterEach
    void tearDown() {
        NewScanners.close(); // 꼭 해줘야함!
        System.setOut(standard);
        System.out.println(captor);
    }

    private void given(String... args) {
        final List<String> arguments = Arrays.asList(args);
        final byte[] buf = String.join("\n", arguments).getBytes();
        final InputStream inputStream = new ByteArrayInputStream(buf);
        System.setIn(inputStream);

        standard = System.out;
        captor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captor));
    }

    private String getOutPut() {
        return captor.toString().trim();
    }

    private void assertSimpleTest(final Executable executable) {
        assertTimeoutPreemptively(SIMPLE_TEST_TIMEOUT, executable);
    }

    @Test
    void success() {
        // given
        given("450", "[콜라,20,1500];[사이다,10,1000]", "3000", "콜라", "사이다");

        // when
        try (final MockedStatic<Randoms> mock = mockStatic(Randoms.class)) {
            mock.when(() -> Randoms.pick(anyList())).thenReturn(100, 100, 100, 100, 50);
//            assertSimpleTest(() -> );
            Application.main(new String[]{});
        }

        // then
        String outPut = getOutPut();
        assertThat(outPut).contains("남은 금액: 50");
        assertThat(outPut).contains("100원 - 4개");
        assertThat(outPut).contains("50원 - 1개");
    }

    @ParameterizedTest
    @MethodSource({"상품여러개_입력_예외테스트", "상품한개_입력_예외테스트", "구매_예외테스트", "자판기_보유금액_예외테스트"})
    void exception(List<String> args) {
        // given
        given(args.toArray(String[]::new));

        // when
        try (final MockedStatic<Randoms> mock = mockStatic(Randoms.class)) {
            mock.when(() -> Randoms.pick(anyList())).thenReturn(100, 100, 100, 100, 50);
            assertSimpleTest(() -> assertThatThrownBy(() -> Application.main(new String[0]))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageStartingWith(ERROR_PREFIX));
        }
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

    private static Arguments toArguments(String... s) {
        return Arguments.of(Arrays.asList(s));
    }
}
