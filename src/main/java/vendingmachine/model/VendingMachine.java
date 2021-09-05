package vendingmachine.model;

import vendingmachine.exception.VendingMachineException;

import java.util.ArrayList;
import java.util.List;

public class VendingMachine {

    private final ChangesModule changesModule;
    private final List<Product> products;
    private int remainingCash = 0;

    public VendingMachine(ChangesModule changesModule, List<Product> products) {
        this.changesModule = changesModule;
        this.products = products;
    }

    public VendingMachine(ChangesModule changesModule) {
        this(changesModule, new ArrayList<>());
    }

    public VendingMachine(int initAmount) {
        this(new ChangesModule(initAmount));
    }

    public void addProducts(List<Product> products) {
        this.products.addAll(products);
    }

    public void buyProduct(String productName) {
        Product product = findProduct(productName);
        validateAvailableToBuy(product);
        buy(product);
    }

    private Product findProduct(String productName) {
        return products.stream()
                .filter(product -> product.sameName(productName))
                .findAny()
                .orElseThrow(() -> {
                    throw new VendingMachineException("존재하지 않는 상품명");
                });
    }

    private void validateAvailableToBuy(Product product) {
        if (product.isSoldOut()) {
            throw new VendingMachineException("해당 물품의 남은 수량이 없음");
        }
        if (product.getPrice() > remainingCash) {
            throw new VendingMachineException("자판기에 투입된 금액이 부족함");
        }
    }

    private void buy(Product product) {
        remainingCash -= product.getPrice();
        product.reduceAmount();
    }

    public boolean canBuyAny() {
        if (allProductsIsSoldOut()) {
            return false;
        }
        return minimumPrice() <= remainingCash;
    }

    private boolean allProductsIsSoldOut() {
        return products.stream()
                .allMatch(Product::isSoldOut);
    }

    private int minimumPrice() {
        return products.stream()
                .filter(product -> !product.isSoldOut())
                .mapToInt(Product::getPrice)
                .min().orElse(0);
    }

    public int getRemainingCash() {
        return remainingCash;
    }

    public Changes getChanges() {
        return changesModule.getChanges(remainingCash);
    }

    public void putCash(int cash) {
        remainingCash += cash;
    }
}
