package com.sparta.usinsa.presentation.search;

import com.sparta.usinsa.application.service.SearchService;
import com.sparta.usinsa.presentation.search.dto.response.KeywordResponse;
import com.sparta.usinsa.presentation.search.dto.response.SearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

  private final SearchService searchService;

  @GetMapping("/search")
  public ResponseEntity<Page<SearchResponse>> search(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String keyword) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(searchService.searches(page,size,keyword));
  }

  @GetMapping("/v1/search")
  public ResponseEntity<List<KeywordResponse>> V1PopularSearch(){
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(searchService.V1PopularSearch());
  }

  @GetMapping("/v2/search")
  public ResponseEntity<List<KeywordResponse>> V2PopularSearch(){
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(searchService.V2PopularSearch());
  }

}
