package vendingmachine.model;

import java.util.ArrayList;
import java.util.List;

public class VendingMachine {

    private final ChangesModule changesModule;
    private final List<Product> products;

    public VendingMachine(ChangesModule changesModule, List<Product> products) {
        this.changesModule = changesModule;
        this.products = products;
    }

    public VendingMachine(ChangesModule changesModule) {
        this(changesModule, new ArrayList<>());
    }

    public void addProducts(List<Product> products) {
        this.products.addAll(products);
    }

    public void buyProduct(String productName) {
        Product product = findProduct(productName);

        if (product.isAmountEmpty()) {
            throw new IllegalArgumentException("해당 물품의 남은 수량이 없음");
        }
        if (changesModule.remainChangeIsNotEnough(product.getPrice())) {
            throw new IllegalArgumentException("자판기에 투입된 금액이 부족함");
        }

        product.reduceAmount();
        changesModule.withdraw(product.getPrice());
    }

    private Product findProduct(String productName) {
        return products.stream()
                .filter(product -> product.sameName(productName))
                .findAny()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("존재하지 않는 상품명");
                });
    }

    public boolean canBuyAny() {
        int minimumPrice = products.get(0).getPrice();
        for (Product product : products) {
            if (product.getPrice() < minimumPrice) {
                minimumPrice = product.getPrice();
            }
        }
//        int minimumPrice = products.stream()
//                .mapToInt(Product::getAmount)
//                .min()
//                .orElse(0);
        return minimumPrice <= changesModule.getAmount();
    }

    public int getChanges() {
        return changesModule.getAmount();
    }

    public List<Coin> getChangesByCoin() {
        return changesModule.getChangesByCoin();
    }

    public void putMoney(int money) {
        changesModule.put(money);
    }
}
