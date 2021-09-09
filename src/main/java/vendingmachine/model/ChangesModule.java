package vendingmachine.model;

import com.woowahan.techcourse.utils.Randoms;
import vendingmachine.exception.VendingMachineException;

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

        if (initAmount % 10 != 0) {
            throw new VendingMachineException("자판기 투입금액은 10으로 나누어 떨어져야 합니다.");
        }

        List<Coin> changes = new ArrayList<>();
        while (initAmount > 0) {
            int pickedCoin = Randoms.pickNumberInList(coinValues);
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