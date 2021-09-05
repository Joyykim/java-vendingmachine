package vendingmachine;

import vendingmachine.model.VendingMachine;
import vendingmachine.view.ConsoleView;

public class Application {

    public static void main(String[] args) {
        final VendingMachine vendingMachine = new VendingMachine(ConsoleView.askVendingMachineInitialAmount());
        vendingMachine.addProducts(ConsoleView.askProducts());
        vendingMachine.putCash(ConsoleView.askInputMoney());

        while (vendingMachine.canBuyAny()) {
            ConsoleView.printInputMoney(vendingMachine.getRemainingCash());
            vendingMachine.buyProduct(ConsoleView.askProductToBuy());
        }

        ConsoleView.printChanges(vendingMachine);
    }
}
