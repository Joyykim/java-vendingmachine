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
            throw new VendingMachineException("상품 최소 금액은 10으로 나누어 떨어져야 합니다.");
        }
    }

    public Product(String name, String amount, String price) {
        this(name, Integer.parseInt(amount), Integer.parseInt(price));
    }

    public static List<Product> getProducts(String str) {
        return Arrays.stream(str.split(";"))
                .map(Product::getProduct)
                .collect(Collectors.toList());
    }

    public static Product getProduct(String productStr) {
        if (!productStr.startsWith("[") || !productStr.endsWith("]")) {
            throw new VendingMachineException("올바른 형식이 아닙니다");
        }
        productStr = productStr.substring(1, productStr.length() - 1);
        String[] separatedString = productStr.split(",");
        return new Product(separatedString[0], separatedString[1], separatedString[2]);
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