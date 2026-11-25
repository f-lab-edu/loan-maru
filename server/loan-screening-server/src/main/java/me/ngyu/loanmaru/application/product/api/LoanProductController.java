package me.ngyu.loanmaru.application.product.api;

import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.product.api.dto.LoanProductCatalogResponse;
import me.ngyu.loanmaru.application.product.service.LoanProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/loan-products")
@RequiredArgsConstructor
public class LoanProductController {

  private final LoanProductService loanProductService;

  @GetMapping
  public LoanProductCatalogResponse getLoanProducts() {
    return LoanProductCatalogResponse.from(loanProductService.getCurrentCatalog());
  }
}
