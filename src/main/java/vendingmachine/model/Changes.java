package vendingmachine.model;

import java.util.List;

public class Changes {

    private final int balance;
    private final List<Coin> changes;

    public Changes(int leftAmount, List<Coin> changes) {
        this.balance = leftAmount;
        this.changes = changes;
    }

    public int getBalance() {
        return balance;
    }

    public List<Coin> getChanges() {
        return changes;
    }
}
