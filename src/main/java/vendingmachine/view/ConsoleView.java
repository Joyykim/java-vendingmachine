package vendingmachine.view;

import com.woowahan.techcourse.utils.Console;
import vendingmachine.model.Changes;
import vendingmachine.model.Coin;
import vendingmachine.model.Product;
import vendingmachine.model.VendingMachine;

import java.util.List;

public class ConsoleView {

    private ConsoleView() {
    }

    public static void println(String s) {
        System.out.println("\n" + s);
    }

    public static void printInputMoney(int changes) {
        println("투입된 금액: " + changes + "원");
    }

    public static void printChanges(VendingMachine vendingMachine) {
        Changes changes = vendingMachine.getChanges();
        println("남은 금액: " + changes.getBalance());

        System.out.println("잔돈");
        List<Coin> changesByCoin = changes.getChanges();
        for (Coin coin : Coin.orderedByValue()) {
            extracted(changesByCoin, coin.getValue());
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

    public static int askVendingMachineInitialAmount() {
        println("자판기가 보유하고 있는 금액을 입력해 주세요.");
        return Integer.parseInt(Console.readLine());
    }

    public static int askInputMoney() {
        println("투입할 금액을 입력해 주세요.");
        return Integer.parseInt(Console.readLine());
    }

    public static List<Product> askProducts() {
        println("상품명과 수량, 금액을 입력해 주세요.");
        return Product.getProducts(Console.readLine());
    }

    public static String askProductToBuy() {
        System.out.println("구매할 상품명을 입력해 주세요.");
        return Console.readLine();
    }
}
