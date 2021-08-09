package vendingmachine.model;

import com.woowahan.techcourse.utils.Randoms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChangesModule {

    private int amount;
    private List<Coin> changes;

    public ChangesModule(List<Coin> changes) {
        this.changes = changes;
    }

    public ChangesModule(int initAmount) {
        this(initChanges(initAmount));
    }

    private static List<Coin> initChanges(int initAmount) {
        List<Integer> coinValues = Coin.valueList();

        List<Coin> changes = new ArrayList<>();
        while (initAmount < 0) {
            int pickedCoin = Randoms.pick(coinValues);
            if (initAmount < pickedCoin) {
                continue;
            }

            initAmount -= pickedCoin;
            changes.add(Coin.from(pickedCoin));
        }
        return changes;
    }

    public int getAmount() {
        return this.amount;
    }

    public void withdraw(int amount) {
        this.amount -= amount;
    }

    public void put(int amount) {
        this.amount += amount;
    }

    public List<Coin> getChangesByCoin() {
        int remainingChanges = this.amount;
        List<Coin> result = new ArrayList<>();

        for (Coin coin : Coin.orderedByValue()) {
            while (coin.isSmallerThan(remainingChanges)) {
                result.add(coin);
                remainingChanges -= coin.getValue();
            }
        }

        return result;
    }

    public void clear() {
        amount = 0;
    }

    public boolean remainChangeIsNotEnough(int price) {
        return amount < price;
    }
}