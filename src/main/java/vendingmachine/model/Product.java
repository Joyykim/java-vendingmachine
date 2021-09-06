package vendingmachine.model;

import vendingmachine.exception.VendingMachineException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Product {

    private final String name;
    private int amount;
    private final int price;

    public Product(String name, int amount, int price) {
        validatePrice(price);
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    private void validatePrice(int price) {
        if (price < 100) {
            throw new VendingMachineException("상품 최소 금액은 100원입니다.");
        }
        if (price % 10 != 0) {
            throw new VendingMachineException("상품 금액은 10으로 나누어 떨어져야 합니다.");
        }
    }

    public static List<Product> getProducts(String str) {
        return Arrays.stream(str.split(";"))
                .map(Product::getProduct)
                .collect(Collectors.toList());
    }

    public static Product getProduct(String productStr) {
        productStr = stripBracket(productStr);
        String[] separatedString = productStr.split(",");

        if (separatedString.length != 3) {
            throw new VendingMachineException("올바른 형식이 아닙니다");
        }

        String name = separatedString[0];
        int amount = parseInt(separatedString[1]);
        int price = parseInt(separatedString[2]);
        return new Product(name, amount, price);
    }

    private static String stripBracket(String productStr) {
        if (!productStr.startsWith("[") || !productStr.endsWith("]")) {
            throw new VendingMachineException("올바른 형식이 아닙니다");
        }
        return productStr.substring(1, productStr.length() - 1);
    }

    private static int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new VendingMachineException("올바른 형식이 아닙니다");
        }
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

    public boolean sameName(String name) {
        return this.name.equals(name);
    }

    public boolean isSoldOut() {
        return amount == 0;
    }

    public void reduceAmount() {
        amount--;
    }
}