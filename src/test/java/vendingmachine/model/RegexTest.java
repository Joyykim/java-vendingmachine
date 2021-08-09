package vendingmachine.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    private final String products = "[콜라,20,1500],[사이다,10,1000],[밀키스,30,3000]";
    private final String product = "[콜라,20,1500]";

    @Test
    void getProducts_성공1() {
        String[] split = products.split("[\\Q]\\E](.*?)[\\Q[\\E]");
        for (String s : split) {
            System.out.println(s);
        }
    }

    @Test
    void getProducts_성공2_채택() {
        Pattern pattern = Pattern.compile("[\\Q[\\E](.*?),(.*?),(.*?)[\\Q]\\E]");
        Matcher matcher = pattern.matcher(products);

        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    @Test
    void getProduct_성공1_채택() {
        String s = product
//                .replaceAll("\\Q[\\E|\\Q]\\E", ""); // "[" or "]"
                .replace("[", "")
                .replace("]", "");

        List<String> values = Arrays.asList(s.split(","));

        System.out.println(values);
    }
}
