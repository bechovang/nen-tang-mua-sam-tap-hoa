ALTER TABLE stores
    ADD COLUMN bank_code VARCHAR(16),
    ADD COLUMN bank_name VARCHAR(80),
    ADD COLUMN bank_account_number VARCHAR(19),
    ADD COLUMN bank_account_holder VARCHAR(80),
    ADD COLUMN bank_transfer_enabled BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE orders
    ADD COLUMN payment_content VARCHAR(32),
    ADD COLUMN payment_bank_code VARCHAR(16),
    ADD COLUMN payment_bank_name VARCHAR(80),
    ADD COLUMN payment_account_number VARCHAR(19),
    ADD COLUMN payment_account_holder VARCHAR(80);

CREATE UNIQUE INDEX orders_payment_content_unique
    ON orders(payment_content)
    WHERE payment_content IS NOT NULL;
