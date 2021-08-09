package vendingmachine.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Coin {
    _500_COIN(500),
    _100_COIN(100),
    _50_COIN(50),
    _10_COIN(10);

    private final int value;

    Coin(int value) {
        this.value = value;
    }

    public static Coin from(int value) {
        return Arrays.stream(values())
                .filter(coin -> coin.value == value)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 금액의 동전은 존재하지 않습니다."));
    }

    public static List<Coin> orderedByValue() {
        return Arrays.stream(values())
                .sorted((o1, o2) -> Integer.compare(o2.value, o1.value))
                .collect(Collectors.toList());
    }


    public static List<Integer> valueList() {
        return Arrays.stream(Coin.values())
                .map(Coin::getValue)
                .collect(Collectors.toList());
    }



    public boolean isSmallerThan(int remainingChanges) {
        return this.value <= remainingChanges;
    }

    public int getValue() {
        return value;
    }
}