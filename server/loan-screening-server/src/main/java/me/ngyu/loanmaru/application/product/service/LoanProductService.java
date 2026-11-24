package me.ngyu.loanmaru.application.product.service;

import lombok.RequiredArgsConstructor;
import me.ngyu.loanmaru.application.product.domain.LoanProductCatalog;
import me.ngyu.loanmaru.application.product.domain.LoanProductCatalogRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanProductService {

  private final LoanProductCatalogRepository loanProductCatalogRepository;

  public LoanProductCatalog getCurrentCatalog() {
    return loanProductCatalogRepository.findCurrent();
  }
}
