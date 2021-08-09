package vendingmachine.view;

import vendingmachine.model.Coin;

import java.util.List;

public class OutputView {

    private OutputView() {
    }

    public static void println(String s) {
        System.out.println();
        System.out.println(s);
    }

    public static void printInputMoney(int changes) {
        System.out.println();
        System.out.println("투입된 금액: " + changes + "원");
        System.out.println("구매할 상품명을 입력해 주세요.");
    }

    public static void printChanges(List<Coin> coins) {
        for (Coin coin : Coin.orderedByValue()) {
            extracted(coins, coin.getValue());
        }
    }

    private static void extracted(List<Coin> coins, int value) {
        int count = 0;
        for (Coin coin : coins) {
            if (coin.getValue() == value) {
                count++;
            }
        }
        System.out.println(value + "원 - " + count + "개");
    }
}
