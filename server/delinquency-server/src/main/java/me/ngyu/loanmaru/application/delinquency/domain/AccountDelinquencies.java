package me.ngyu.loanmaru.application.delinquency.domain;

import java.util.List;

public record AccountDelinquencies(String accountNo, List<Delinquency> delinquencies) {
}
