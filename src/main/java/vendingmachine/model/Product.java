package vendingmachine.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product {

    private final String name;
    private int amount;
    private final int price;

    public Product(String name, int amount, int price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public static List<Product> getProducts(String str) {
        Pattern pattern = Pattern.compile("[\\Q[\\E](.*?),(.*?),(.*?)[\\Q]\\E]");
        Matcher matcher = pattern.matcher(str);

        List<Product> result = new ArrayList<>();
        while (matcher.find()) {
            Product product = getProduct(matcher.group());
            result.add(product);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    private static Product getProduct(String productString) {
        productString = productString
                .replace("[", "")
                .replace("]", "");

        List<String> productValues = Arrays.asList(productString.split(","));

        return new Product(
                productValues.get(0),
                Integer.parseInt(productValues.get(1)),
                Integer.parseInt(productValues.get(2))
        );
    }

    public boolean sameName(String name) {
        return this.name.equals(name);
    }

    public boolean isAmountEmpty() {
        return amount == 0;
    }

    public void reduceAmount() {
        amount--;
    }
}