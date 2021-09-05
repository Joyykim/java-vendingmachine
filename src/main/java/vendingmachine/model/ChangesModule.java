package vendingmachine.model;

import com.woowahan.techcourse.utils.Randoms;

import java.util.ArrayList;
import java.util.List;

public class ChangesModule {

    private final List<Coin> changes;

    public ChangesModule(List<Coin> changes) {
        this.changes = changes;
    }

    public ChangesModule(int initAmount) {
        this(initChanges(initAmount));
    }

    private static List<Coin> initChanges(int initAmount) {
        List<Integer> coinValues = Coin.valueList();

        List<Coin> changes = new ArrayList<>();
        while (initAmount > 0) {
            int pickedCoin = Randoms.pick(coinValues);
            if (initAmount < pickedCoin) {
                continue;
            }

            initAmount -= pickedCoin;
            changes.add(Coin.from(pickedCoin));
        }
        return changes;
    }

    public Changes getChanges(int remainingCash) {
        List<Coin> result = new ArrayList<>();

        for (Coin coin : Coin.ordered(changes)) {
            if (remainingCash >= coin.getValue()) {
                remainingCash -= coin.getValue();
                result.add(coin);
            }
        }

        return new Changes(remainingCash, result);
    }
}