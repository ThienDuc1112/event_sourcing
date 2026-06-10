# event_sourcing

# Giới thiệu
Hệ thống Banking Event Sourcing & CQRS là một ứng dụng quản lý tài khoản ngân hàng đơn giản, áp dụng các mẫu kiến trúc hiện đại:

Event Sourcing: Mọi thay đổi trạng thái đều được lưu dưới dạng events

CQRS (Command Query Responsibility Segregation): Tách biệt command (ghi) và query (đọc)

Clean Architecture: Phân tách rõ ràng các layer

Decider Pattern: Tập trung logic nghiệp vụ vào một nơi

Optimistic Locking: Đảm bảo tính nhất quán dữ liệu khi có concurrent operations

# Tính năng chính
Tạo tài khoản ngân hàng

Nạp tiền vào tài khoản

Rút tiền từ tài khoản

Xem lịch sử giao dịch

# Clean Architecture Layers

| Layer | Responsibility | Components |
|-------|----------------|------------|
| **Interfaces** | Giao tiếp với bên ngoài | Controllers, DTOs, Projectors |
| **Application** | Xử lý use cases | Commands, Queries, Handlers, Ports |
| **Domain** | Business logic cốt lõi | Aggregates, Events, Deciders |
| **Infrastructure** | Technical implementation | Event Store, Repositories, Adapters |

### Event Sourcing Flow


# Event Sourcing Flow
Command → CommandHandler → Decider → Events → Event Store → EventBus → Projector → Read Model
   ↑                         ↓
   └─────── Replay Events ───┘

# Database Schema
event_store (
    id BIGINT PRIMARY KEY,
    aggregate_id VARCHAR(255),
    event_type VARCHAR(255),
    version INT,
    event_data TEXT,
    occurred_at TIMESTAMP,
    UNIQUE(aggregate_id, version)
)

account_view_entity (
    account_id VARCHAR(255) PRIMARY KEY,
    balance DOUBLE,
    last_updated TIMESTAMP
)

transaction_view_entity (
    id BIGINT PRIMARY KEY,
    account_id VARCHAR(255),
    type VARCHAR(50),
    amount DOUBLE,
    timestamp TIMESTAMP
)
   


