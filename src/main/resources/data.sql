-- =============================================
-- DATA.SQL - KHÔNG DÙNG TRUNCATE, DÙNG DELETE
-- =============================================

-- Xóa dữ liệu cũ (không dùng TRUNCATE để tránh lỗi constraint)
DELETE FROM transaction_view_entity;
DELETE FROM account_view_entity;
DELETE FROM event_store;

-- Reset auto-increment (cho H2)
ALTER TABLE event_store ALTER COLUMN id RESTART WITH 1;
ALTER TABLE transaction_view_entity ALTER COLUMN id RESTART WITH 1;

-- =============================================
-- 1. TẠO EVENT STORE (KHÔNG DÙNG foreign key)
-- =============================================

-- AccountCreated events
INSERT INTO event_store (aggregate_id, event_type, version, event_data, occurred_at) VALUES
                                                                                         ('ACC001', 'AccountCreated', 1, '{"aggregateId":"ACC001","initialBalance":10000000,"version":1}', PARSEDATETIME('2024-01-01-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                                         ('ACC002', 'AccountCreated', 1, '{"aggregateId":"ACC002","initialBalance":25000000,"version":1}', PARSEDATETIME('2024-01-15-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                                         ('ACC003', 'AccountCreated', 1, '{"aggregateId":"ACC003","initialBalance":0,"version":1}', PARSEDATETIME('2024-01-20-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                                         ('ACC004', 'AccountCreated', 1, '{"aggregateId":"ACC004","initialBalance":5000000,"version":1}', PARSEDATETIME('2024-02-10-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                                         ('ACC005', 'AccountCreated', 1, '{"aggregateId":"ACC005","initialBalance":7500000,"version":1}', PARSEDATETIME('2024-02-05-00.00.00','yyyy-MM-dd-HH.mm.ss'));

-- Additional events for ACC001
INSERT INTO event_store (aggregate_id, event_type, version, event_data, occurred_at) VALUES
                                                                                         ('ACC001', 'MoneyDeposited', 2, '{"aggregateId":"ACC001","amount":3000000,"newBalance":13000000,"version":2}', PARSEDATETIME('2024-01-15-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                                         ('ACC001', 'MoneyWithdrawn', 3, '{"aggregateId":"ACC001","amount":1000000,"newBalance":12000000,"version":3}', PARSEDATETIME('2024-01-20-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                                         ('ACC001', 'MoneyDeposited', 4, '{"aggregateId":"ACC001","amount":500000,"newBalance":12500000,"version":4}', PARSEDATETIME('2024-01-25-00.00.00','yyyy-MM-dd-HH.mm.ss'));

-- Additional events for ACC002
INSERT INTO event_store (aggregate_id, event_type, version, event_data, occurred_at) VALUES
                                                                                         ('ACC002', 'MoneyWithdrawn', 2, '{"aggregateId":"ACC002","amount":5000000,"newBalance":20000000,"version":2}', PARSEDATETIME('2024-01-20-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                                         ('ACC002', 'MoneyDeposited', 3, '{"aggregateId":"ACC002","amount":10000000,"newBalance":30000000,"version":3}', PARSEDATETIME('2024-01-25-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                                         ('ACC002', 'MoneyWithdrawn', 4, '{"aggregateId":"ACC002","amount":5000000,"newBalance":25000000,"version":4}', PARSEDATETIME('2024-01-30-00.00.00','yyyy-MM-dd-HH.mm.ss'));

-- =============================================
-- 2. TẠO ACCOUNT VIEW
-- =============================================

INSERT INTO account_view_entity (account_id, balance, last_updated) VALUES
                                                                        ('ACC001', 12500000, CURRENT_TIMESTAMP()),
                                                                        ('ACC002', 25000000, CURRENT_TIMESTAMP()),
                                                                        ('ACC003', 0, CURRENT_TIMESTAMP()),
                                                                        ('ACC004', 5000000, CURRENT_TIMESTAMP()),
                                                                        ('ACC005', 7500000, CURRENT_TIMESTAMP());

-- =============================================
-- 3. TẠO TRANSACTION VIEW
-- =============================================

INSERT INTO transaction_view_entity (account_id, type, amount, timestamp) VALUES
                                                                              ('ACC001', 'DEPOSIT', 10000000, PARSEDATETIME('2024-01-01-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC001', 'DEPOSIT', 3000000, PARSEDATETIME('2024-01-15-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC001', 'WITHDRAW', 1000000, PARSEDATETIME('2024-01-20-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC001', 'DEPOSIT', 500000, PARSEDATETIME('2024-01-25-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC001', 'WITHDRAW', 2000000, PARSEDATETIME('2024-01-10-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC001', 'WITHDRAW', 500000, PARSEDATETIME('2024-01-05-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC001', 'WITHDRAW', 200000, PARSEDATETIME('2024-01-28-00.00.00','yyyy-MM-dd-HH.mm.ss')),

                                                                              ('ACC002', 'DEPOSIT', 25000000, PARSEDATETIME('2024-01-15-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC002', 'WITHDRAW', 5000000, PARSEDATETIME('2024-01-20-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC002', 'WITHDRAW', 3000000, PARSEDATETIME('2024-01-25-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC002', 'DEPOSIT', 10000000, PARSEDATETIME('2024-01-30-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC002', 'WITHDRAW', 2000000, PARSEDATETIME('2024-02-05-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC002', 'WITHDRAW', 1000000, PARSEDATETIME('2024-02-10-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC002', 'DEPOSIT', 5000000, PARSEDATETIME('2024-02-15-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC002', 'WITHDRAW', 3000000, PARSEDATETIME('2024-02-20-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC002', 'WITHDRAW', 1000000, PARSEDATETIME('2024-02-25-00.00.00','yyyy-MM-dd-HH.mm.ss')),

                                                                              ('ACC004', 'DEPOSIT', 5000000, PARSEDATETIME('2024-02-10-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC004', 'WITHDRAW', 1000000, PARSEDATETIME('2024-02-15-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC004', 'WITHDRAW', 500000, PARSEDATETIME('2024-02-20-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC004', 'DEPOSIT', 2000000, PARSEDATETIME('2024-02-25-00.00.00','yyyy-MM-dd-HH.mm.ss')),

                                                                              ('ACC005', 'DEPOSIT', 7500000, PARSEDATETIME('2024-02-05-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC005', 'WITHDRAW', 1000000, PARSEDATETIME('2024-02-10-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC005', 'WITHDRAW', 500000, PARSEDATETIME('2024-02-15-00.00.00','yyyy-MM-dd-HH.mm.ss')),
                                                                              ('ACC005', 'DEPOSIT', 1000000, PARSEDATETIME('2024-02-20-00.00.00','yyyy-MM-dd-HH.mm.ss'));