package com.sparta.usinsa.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.presentation.auth.UserType;
import com.sparta.usinsa.presentation.product.dto.product.ProductSearchResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("키워드로 검색을 하면 올바르게 조회된다.")
    void productSearchTest_success() {
        // given
        final User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .name("test")
                .brand("에르메스")
                .type(UserType.OWNER)
                .build();

        final Product product = new Product(
                user, "엄청난 패딩", 1000L, "따뜻", "test", "패딩");

        when(productRepository.searchByKeyword(any())).thenReturn(List.of(product));

        // when
        final List<ProductSearchResponse> products = productService.search("엄청난");

        // then
        assertThat(products.get(0).productName()).isEqualTo("엄청난 패딩");
        assertThat(products.get(0).price()).isEqualTo(1000L);
        assertThat(products.get(0).brand()).isEqualTo("에르메스");
    }
}

