package com.sparta.usinsa.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.presentation.auth.UserType;
import com.sparta.usinsa.presentation.common.exception.CustomException;
import com.sparta.usinsa.presentation.product.dto.request.ProductRequestDto;
import com.sparta.usinsa.presentation.product.dto.response.ProductResponseDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 등록에 성공한다.")
    void addProduct_success_test() {
        // given
        final User user = new User("test@gmail.com", "testPw", "7조", "에르메스", UserType.OWNER);
        final ProductRequestDto req = new ProductRequestDto("패딩", 1000L, "따뜻함", "test", "패딩");
        final Product product = new Product("패딩", 1000L, "따뜻함", "test", "패딩", user);
        when(productRepository.save(any())).thenReturn(product);
        // when
        final ProductResponseDto resp = productService.addProduct(req, user);

        // then
        assertThat(resp.getName()).isEqualTo("패딩");
        assertThat(resp.getPrice()).isEqualTo(1000L);
        assertThat(resp.getDescription()).isEqualTo("따뜻함");
        assertThat(resp.getProductUrl()).isEqualTo("test");
        assertThat(resp.getCategory()).isEqualTo("패딩");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 단건 조회 시 올바르게 조회가 된다.")
    void getProductById_success_test() {
        // given
        final Long productId = 1L;
        final User user = new User("test@gmail.com", "testPw", "7조", "에르메스", UserType.OWNER);
        final Product product = new Product("패딩", 1000L, "따뜻함", "test", "패딩", user);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        final Product findProduct = productService.getProductById(productId);

        // then
        assertThat(findProduct.getPrice()).isEqualTo(1000L);
        assertThat(findProduct.getDescription()).isEqualTo("따뜻함");
        assertThat(findProduct.getCategory()).isEqualTo("패딩");
        assertThat(findProduct.getProductUrl()).isEqualTo("test");
        assertThat(findProduct.getCategory()).isEqualTo("패딩");
    }

    @Test
    @DisplayName("상품 단건 조회 시 상품이 유효하지 않으면 예외가 발생한다.")
    void getProductById_not_found_product_test() {
        // given
        final Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProductById(productId)).
                isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("유저가 올린 상품을 성공적으로 조회한다.")
    void getProductsByUserId_success_test() {
        // given
        final Long userId = 1L;
        final User user = new User("test@gmail.com", "testPw", "7조", "에르메스", UserType.OWNER);
        final Product product = new Product("패딩", 1000L, "따뜻함", "test", "패딩", user);

        when(productRepository.findByUserId(userId)).thenReturn(Optional.of(List.of(product)));

        // when
        final List<Product> products = productService.getProductsByUserId(userId);

        // then
        assertThat(products.get(0).getName()).isEqualTo("패딩");
        assertThat(products.get(0).getPrice()).isEqualTo(1000L);
        assertThat(products.get(0).getDescription()).isEqualTo("따뜻함");
        assertThat(products.get(0).getProductUrl()).isEqualTo("test");
        assertThat(products.get(0).getCategory()).isEqualTo("패딩");
    }

    @Test
    @DisplayName("유저가 올린 상품이 유효하지 않으면 예외가 발생한다.")
    void getProductsByUserId_not_found_product_test() {
        // given
        final Long userId = 1L;

        when(productRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProductsByUserId(userId))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("상품 정보 수정에 성공한다.")
    void updateProduct_success_test() {
        // given
        final Long productId = 1L;
        final User user = new User("test@gmail.com", "testPw", "7조", "에르메스", UserType.OWNER);
        final ProductRequestDto req = new ProductRequestDto("패딩1", 1001L, "따뜻함1", "test1", "패딩1");
        final Product product = new Product("패딩1", 1001L, "따뜻함1", "test1", "패딩1", user);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        final Product updatedProduct = productService.updateProduct(productId, req, user);

        // then
        assertThat(updatedProduct.getName()).isEqualTo("패딩1");
        assertThat(updatedProduct.getPrice()).isEqualTo(1001L);
        assertThat(updatedProduct.getDescription()).isEqualTo("따뜻함1");
        assertThat(updatedProduct.getProductUrl()).isEqualTo("test1");
        assertThat(updatedProduct.getCategory()).isEqualTo("패딩1");
    }
}
