package com.sparta.usinsa.product;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.usinsa.application.service.ProductService;
import com.sparta.usinsa.domain.entity.Product;
import com.sparta.usinsa.domain.entity.User;
import com.sparta.usinsa.domain.repository.UserRepository;
import com.sparta.usinsa.presentation.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Mock
  private ProductService productService;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ProductController productController;

  private User testUser;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // Mockito 초기화

    // 테스트 유저 생성
    testUser = new User();
    testUser.setUsername("testUser");
    testUser.setPassword("password123");
    userRepository.save(testUser);
  }


  @Test
    // 상품 추가 API 테스트
  void testAddProduct() throws Exception {

    Product product = new Product(
        testUser,
        "Test Product",
        100L,
        "Test Description",
        "http://example.com",
        "아우터");

    when(productService.addProduct(any(Product.class))).thenReturn(product);

    // POST 요청 보내기
    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product)))
        .andExpect(status().isCreated())  // HTTP 상태 201 (Created) 확인
        .andExpect(jsonPath("$.name").value("Test Product"))
        .andExpect(jsonPath("$.price").value(100L));  // 응답 값 검증
  }

  @Test
    // 상품 단건 조회 API 테스트
  void testGetProduct() throws Exception {

    Product product = new Product(
        testUser,
        "Test Product",
        100L,
        "Test Description",
        "http://example.com",
        "Electronics");

    when(productService.getProductById(anyLong())).thenReturn(product);

    // GET 요청 보내기
    mockMvc.perform(get("/api/products/{id}", 1))
        .andExpect(status().isOk())  // HTTP 상태 200 (OK) 확인
        .andExpect(jsonPath("$.name").value("Test Product"))
        .andExpect(jsonPath("$.price").value(100L));  // 응답 값 검증
  }

  @Test
    // 상품 수정 API 테스트
  void testUpdateProduct() throws Exception {

    Product product = new Product(
        testUser,
        "Updated Product",
        200L,
        "Updated Description",
        "http://updated.com",
        "Updated Category");

    when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product);

    mockMvc.perform(put("/api/products/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product)))
        .andExpect(status().isOk())  // HTTP 상태 200 (OK) 확인
        .andExpect(jsonPath("$.name").value("Updated Product"))
        .andExpect(jsonPath("$.price").value(200L));  // 응답 값 검증
  }

  @Test
    // 상품 삭제 API 테스트
  void testDeleteProduct() throws Exception {

    // 응답 상태가 No Content (HTTP 204)인지 확인
    mockMvc.perform(delete("/api/products/{id}", 1))
        .andExpect(status().isNoContent());  // HTTP 상태 204 (No Content) 확인

    // deleteProduct 메서드가 1번 호출되었는지 검증
    verify(productService, times(1)).deleteProduct(1L);
  }
}
