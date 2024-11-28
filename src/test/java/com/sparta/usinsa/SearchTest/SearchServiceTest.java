package com.sparta.usinsa.SearchTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.usinsa.application.service.SearchService;
import com.sparta.usinsa.domain.entity.Keywords;
import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.KeywordRepository;
import com.sparta.usinsa.domain.repository.ProductRepository;
import com.sparta.usinsa.presentation.auth.UserType;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class SearchServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private KeywordRepository keywordRepository;

  @InjectMocks
  private SearchService searchService;

  @Test
  @DisplayName("키워드 검색 기능 테스트")
  void SearchTest_success() throws Exception {
    // given
    String keyword = "패딩";

    Pageable pageable = PageRequest.of(0, 10);

    User user = User.builder()
        .email("test@test.com")
        .password("123456")
        .name("test")
        .brand("test")
        .type(UserType.OWNER)
        .build();

    Product product = new Product(
        user, "따듯한 패딩", 1000L, "따숩다", "test", "패딩");

    Field idField = Product.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(product, 1L);

    List<Product> products = List.of(product);
    Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

    when(productRepository.findAllByNameContaining(pageable, keyword))
        .thenReturn(productPage);

    // when
    Page<Product> result = productRepository.findAllByNameContaining(pageable, keyword);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isEqualTo(1);

    Product resultProduct = result.getContent().get(0);
    assertThat(resultProduct.getId()).isEqualTo(1L);
    assertThat(resultProduct.getName()).isEqualTo("따듯한 패딩");
    assertThat(resultProduct.getDescription()).isEqualTo("따숩다");
    assertThat(resultProduct.getPrice()).isEqualTo(1000L);

    verify(productRepository, times(1)).findAllByNameContaining(pageable, keyword);
  }

  @Test
  @DisplayName("인기 키워드 생성 기능 테스트")
  void popularSearchTest_success() {
    // given
    String keyword = "testKeyword";
    Keywords existingKeyword = new Keywords(keyword, 10L);
    existingKeyword
        .setLastSearched(LocalDateTime.of(2023, 1, 1, 12, 0));

    when(keywordRepository.findByKeyword(keyword)).thenReturn(Optional.of(existingKeyword));

    // When
    searchService.popularKeyword(keyword);

    // Then
    assertEquals(11L, existingKeyword.getSearchCount());
    assertTrue(existingKeyword.getLastSearched()
        .isAfter(LocalDateTime.of(2023, 1, 1, 12, 0)));
    verify(keywordRepository, times(1)).findByKeyword(keyword);
  }

}
