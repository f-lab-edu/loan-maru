-- 기준값 버전 2026-11-01.v3와 상품·규칙. 재시작할 때 이미 있는 행은 유일 제약에 걸려 건너뛴다.
INSERT INTO threshold_version (version_key, duplicate_loan_max_count, delinquency_lookback_months,
                               delinquency_min_overdue_days, dsr_limit_rate, reduction_unit_amount,
                               loan_term_years, effective_from)
VALUES ('2026-11-01.v3', 1, 6, 30, 40.0, 100000, 5, TIMESTAMP WITH TIME ZONE '2026-11-01 00:00:00+09:00')
ON CONFLICT DO NOTHING;

INSERT INTO loan_product (threshold_version_id, product_code, product_name, max_limit_amount,
                          interest_rate, dsr_applied, min_age, min_annual_income, employment_required)
SELECT v.id, p.product_code, p.product_name, p.max_limit_amount, p.interest_rate, p.dsr_applied,
       p.min_age, p.min_annual_income, p.employment_required
FROM threshold_version v
CROSS JOIN (VALUES ('L01', '신용대출', 100000000, 6.0, TRUE, CAST(NULL AS INTEGER), CAST(24000000 AS BIGINT), TRUE),
                   ('L03', '비상금대출', 3000000, 9.0, FALSE, 19, CAST(NULL AS BIGINT), FALSE))
    AS p (product_code, product_name, max_limit_amount, interest_rate, dsr_applied,
          min_age, min_annual_income, employment_required)
WHERE v.version_key = '2026-11-01.v3'
ON CONFLICT DO NOTHING;

INSERT INTO loan_product_rule (loan_product_id, rule_code)
SELECT p.id, r.rule_code
FROM loan_product p
JOIN threshold_version v ON v.id = p.threshold_version_id
JOIN (VALUES ('L01', 'ELIGIBILITY'),
             ('L01', 'DUPLICATE_LOAN'),
             ('L01', 'DELINQUENCY'),
             ('L01', 'PRODUCT_MAX_LIMIT'),
             ('L01', 'DSR'),
             ('L03', 'ELIGIBILITY'),
             ('L03', 'DUPLICATE_LOAN'),
             ('L03', 'DELINQUENCY'),
             ('L03', 'PRODUCT_MAX_LIMIT'))
    AS r (product_code, rule_code) ON r.product_code = p.product_code
WHERE v.version_key = '2026-11-01.v3'
ON CONFLICT DO NOTHING;
