package vendingmachine;

import com.woowahan.techcourse.utils.Scanners;
import vendingmachine.model.ChangesModule;
import vendingmachine.model.Product;
import vendingmachine.model.VendingMachine;
import vendingmachine.view.OutputView;

import java.util.List;

public class App {
    public static void main(String[] args) {

        OutputView.println("자판기가 보유하고 있는 금액을 입력해 주세요.");
        int inputMoney = getMoneyFromInput();
        VendingMachine vendingMachine = new VendingMachine(new ChangesModule(inputMoney));

        OutputView.println("상품명과 수량, 금액을 입력해 주세요.");
        vendingMachine.addProducts(getProductsFromInput());

        OutputView.println("투입할 금액을 입력해 주세요.");
        vendingMachine.putMoney(getMoneyFromInput());

        while (vendingMachine.canBuyAny()) {
            System.out.println();
            System.out.println("투입된 금액: " + vendingMachine.getChanges() + "원");
            System.out.println("구매할 상품명을 입력해 주세요.");
            String productName = getProductNameFromInput();
            vendingMachine.buyProduct(productName);
        }

        OutputView.println("잔돈");
        OutputView.printChanges(vendingMachine.getChangesByCoin());
    }

    private static int getMoneyFromInput() {
        return Integer.parseInt(Scanners.nextLine());
    }

    private static List<Product> getProductsFromInput() {
        String input = Scanners.nextLine();
        return Product.getProducts(input);
    }

    private static String getProductNameFromInput() {
        return Scanners.nextLine();
    }
}
