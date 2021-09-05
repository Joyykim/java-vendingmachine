package vendingmachine.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ChangesModuleTest {

    @Test
    void getChanges() {
        ChangesModule changesModule = new ChangesModule(
                Arrays.asList(Coin._100_COIN, Coin._100_COIN)
        );
        Changes changes = changesModule.getChanges(250);

        assertThat(changes.getBalance()).isEqualTo(50);
        assertThat(changes.getChanges()).isEqualTo(Arrays.asList(Coin._100_COIN, Coin._100_COIN));
    }
}