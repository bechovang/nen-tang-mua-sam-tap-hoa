ALTER TABLE orders
    ADD COLUMN payment_method VARCHAR(16) NOT NULL DEFAULT 'CASH',
    ADD COLUMN payment_status VARCHAR(16) NOT NULL DEFAULT 'UNPAID',
    ADD COLUMN payment_link_id VARCHAR(64),
    ADD COLUMN payment_url VARCHAR(512),
    ADD COLUMN payment_reference VARCHAR(64),
    ADD COLUMN payment_expires_at TIMESTAMPTZ,
    ADD COLUMN paid_at TIMESTAMPTZ;

UPDATE orders
SET payment_status = 'PAID',
    paid_at = updated_at
WHERE paid = true;

CREATE UNIQUE INDEX orders_payment_link_id_unique
    ON orders(payment_link_id)
    WHERE payment_link_id IS NOT NULL;
