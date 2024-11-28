package com.sparta.usinsa.presentation.search;

import com.sparta.usinsa.application.service.SearchService;
import com.sparta.usinsa.presentation.search.dto.response.KeywordResponse;
import com.sparta.usinsa.presentation.search.dto.response.SearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

  private SearchService searchService;

  @GetMapping("/search/{keyword}")
  public ResponseEntity<Page<SearchResponse>> search(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam String keyword) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(searchService.searches(page,size,keyword));
  }

  @GetMapping("/search")
  public ResponseEntity<List<KeywordResponse>> popularSearch(){
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(searchService.popularSearch());
  }

}
