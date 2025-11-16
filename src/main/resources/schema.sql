CREATE TABLE IF NOT EXISTS exchange_rate (
    id_exchange_rate UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    origin_currency VARCHAR(3) NOT NULL,
    final_currency VARCHAR(3) NOT NULL,
    date_to DATE NOT NULL,
    value_to DECIMAL(10,4) NOT NULL
);