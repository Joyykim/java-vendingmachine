package vendingmachine.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void newProduct() {
        Product product = Product.getProduct("[aa,10,1000]");
        assertThat(product.getName()).isEqualTo("aa");
        assertThat(product.getAmount()).isEqualTo(10);
        assertThat(product.getPrice()).isEqualTo(1000);
    }


}