CREATE DATABASE storedb;

CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE,
                       description VARCHAR(255),
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,
                       created_by VARCHAR(50),
                       updated_by VARCHAR(50)
);

CREATE TABLE store_users (
                             id BIGSERIAL PRIMARY KEY,
                             username VARCHAR(50) NOT NULL UNIQUE,
                             password VARCHAR(255) NOT NULL,
                             email VARCHAR(100) NOT NULL UNIQUE,
                             first_name VARCHAR(50) NOT NULL,
                             last_name VARCHAR(50) NOT NULL,
                             active BOOLEAN NOT NULL DEFAULT TRUE,
                             created_at TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP NOT NULL,
                             created_by VARCHAR(50),
                             updated_by VARCHAR(50)
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES store_users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(500),
                          price DECIMAL(19, 2) NOT NULL CHECK (price >= 0),
                          quantity INTEGER NOT NULL DEFAULT 0 CHECK (quantity >= 0),
                          sku VARCHAR(50) NOT NULL UNIQUE,
                          category VARCHAR(50) NOT NULL,
                          active BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          created_by VARCHAR(50),
                          updated_by VARCHAR(50)
);

INSERT INTO roles (name, created_at, updated_at, created_by, updated_by)
VALUES
    ('ADMIN', NOW(), NOW(), 'system', 'system'),
    ('MANAGER', NOW(), NOW(), 'system', 'system'),
    ('EMPLOYEE', NOW(), NOW(), 'system', 'system'),
    ('CUSTOMER',  NOW(), NOW(), 'system', 'system');

-- insert default admin user (password: admin123)
INSERT INTO store_users (username, password, email, first_name, last_name, active, created_at, updated_at, created_by, updated_by)
VALUES ('admin', '$2a$10$VmOZGGIkEULYM7pJD6vyGOvVp8B5RBDkIp5FFNwKDOaMOZgR2i4oK', 'admin@example.com', 'Admin', 'User', TRUE, NOW(), NOW(), 'system', 'system');

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);

-- Sample products for store management database
INSERT INTO products (name, description, price, quantity, sku, category, active, created_at, updated_at, created_by, updated_by)
VALUES
    -- Electronics
    ('Samsung Galaxy S22', 'Latest Samsung smartphone with 6.1" Dynamic AMOLED display, 128GB storage', 799.99, 50, 'ELEC-1001', 'ELECTRONICS', true, NOW(), NOW(), 'system', 'system'),
    ('MacBook Pro 14"', 'Apple M1 Pro chip, 16GB RAM, 512GB SSD storage, 14-inch Liquid Retina XDR display', 1999.99, 25, 'ELEC-1002', 'ELECTRONICS', true, NOW(), NOW(), 'system', 'system'),
    ('Sony WH-1000XM4', 'Wireless noise-cancelling headphones with 30-hour battery life', 349.99, 100, 'ELEC-1003', 'ELECTRONICS', true, NOW(), NOW(), 'system', 'system'),
    ('Dell XPS 15', '11th Gen Intel Core i7, 16GB RAM, 1TB SSD, NVIDIA GeForce RTX 3050 Ti', 1799.99, 15, 'ELEC-1004', 'ELECTRONICS', true, NOW(), NOW(), 'system', 'system'),
    ('iPad Air', '10.9-inch Liquid Retina display, A14 Bionic chip, 64GB storage', 599.99, 35, 'ELEC-1005', 'ELECTRONICS', true, NOW(), NOW(), 'system', 'system'),

    -- Clothing

    ('Unisex Hooded Sweatshirt', 'Warm fleece lining with front kangaroo pocket', 29.99, 100, 'CLTH-2003', 'CLOTHING', true, NOW(), NOW(), 'system', 'system'),
    ('Winter Parka Jacket', 'Waterproof and windproof with thermal insulation', 129.99, 75, 'CLTH-2004', 'CLOTHING', true, NOW(), NOW(), 'system', 'system'),
    ('Cotton Socks 6-Pack', 'Breathable cotton blend, one size fits most', 12.99, 300, 'CLTH-2005', 'CLOTHING', true, NOW(), NOW(), 'system', 'system'),

    -- Books
    ('The Midnight Library', 'Novel by Matt Haig about the choices that make a life', 14.99, 50, 'BOOK-3001', 'BOOKS', true, NOW(), NOW(), 'system', 'system'),

    ('Project Hail Mary', 'Science fiction novel by Andy Weir', 18.99, 30, 'BOOK-3003', 'BOOKS', true, NOW(), NOW(), 'system', 'system'),
    ('Python Crash Course', 'A hands-on, project-based introduction to programming', 29.99, 25, 'BOOK-3004', 'BOOKS', true, NOW(), NOW(), 'system', 'system'),

    -- Food
    ('Organic Quinoa 1kg', 'Premium organic quinoa, high in protein and fiber', 8.99, 100, 'FOOD-4001', 'FOOD', true, NOW(), NOW(), 'system', 'system'),
    ('Extra Virgin Olive Oil 500ml', 'Cold-pressed Italian olive oil', 12.99, 80, 'FOOD-4002', 'FOOD', true, NOW(), NOW(), 'system', 'system'),
    ('Dark Chocolate Bar 70%', 'Ethically sourced cocoa, no artificial flavors', 3.99, 150, 'FOOD-4003', 'FOOD', true, NOW(), NOW(), 'system', 'system'),
    ('Organic Honey 16oz', 'Raw, unfiltered wildflower honey', 9.99, 60, 'FOOD-4004', 'FOOD', true, NOW(), NOW(), 'system', 'system'),
    ('Ground Coffee 12oz', 'Medium roast Arabica beans, fine ground', 11.99, 75, 'FOOD-4005', 'FOOD', true, NOW(), NOW(), 'system', 'system'),

    -- Beverages
    ('Sparkling Mineral Water 12-Pack', 'Natural carbonated spring water', 15.99, 120, 'BEVR-5001', 'BEVERAGES', true, NOW(), NOW(), 'system', 'system'),
    ('Green Tea 100 Bags', 'Pure green tea leaves, individually wrapped', 9.99, 90, 'BEVR-5002', 'BEVERAGES', true, NOW(), NOW(), 'system', 'system'),
    ('Craft IPA 6-Pack', 'India Pale Ale with citrus notes, 6.2% ABV', 13.99, 50, 'BEVR-5003', 'BEVERAGES', true, NOW(), NOW(), 'system', 'system'),
    ('Red Wine Cabernet', 'Full-bodied California Cabernet Sauvignon', 24.99, 30, 'BEVR-5004', 'BEVERAGES', true, NOW(), NOW(), 'system', 'system'),
    ('Fresh Orange Juice 64oz', '100% pure squeezed, not from concentrate', 5.99, 40, 'BEVR-5005', 'BEVERAGES', true, NOW(), NOW(), 'system', 'system'),

    -- Home Appliances
    ('Blender 1000W', 'High-performance blender with multiple speed settings', 79.99, 25, 'APPL-6001', 'HOME_APPLIANCES', true, NOW(), NOW(), 'system', 'system'),
    ('Toaster 2-Slice', 'Stainless steel with 6 browning settings', 39.99, 30, 'APPL-6002', 'HOME_APPLIANCES', true, NOW(), NOW(), 'system', 'system'),
    ('Coffee Maker 12-Cup', 'Programmable with auto shut-off feature', 59.99, 20, 'APPL-6003', 'HOME_APPLIANCES', true, NOW(), NOW(), 'system', 'system'),
    ('Robot Vacuum', 'Smart mapping, wifi-enabled with app control', 249.99, 15, 'APPL-6004', 'HOME_APPLIANCES', true, NOW(), NOW(), 'system', 'system'),
    ('Air Fryer 5.8Qt', 'Digital touchscreen with 8 cooking presets', 129.99, 40, 'APPL-6005', 'HOME_APPLIANCES', true, NOW(), NOW(), 'system', 'system'),

    -- Furniture
    ('Sectional Sofa', 'L-shaped modular design with chaise lounge', 899.99, 5, 'FURN-7001', 'FURNITURE', true, NOW(), NOW(), 'system', 'system'),
    ('Queen Size Bed Frame', 'Modern platform bed with wooden slats', 349.99, 10, 'FURN-7002', 'FURNITURE', true, NOW(), NOW(), 'system', 'system'),
    ('Coffee Table', 'Mid-century inspired with storage shelf', 199.99, 15, 'FURN-7003', 'FURNITURE', true, NOW(), NOW(), 'system', 'system'),
    ('Office Desk', 'Computer desk with cable management system', 229.99, 20, 'FURN-7004', 'FURNITURE', true, NOW(), NOW(), 'system', 'system'),
    ('Bookshelf 5-Tier', 'Open geometric design with metal frame', 159.99, 25, 'FURN-7005', 'FURNITURE', true, NOW(), NOW(), 'system', 'system'),

    -- Toys
    ('Building Blocks 250pcs', 'Compatible with all major building block brands', 29.99, 50, 'TOYS-8001', 'TOYS', true, NOW(), NOW(), 'system', 'system'),
    ('RC Car', 'Remote controlled off-road vehicle with rechargeable battery', 59.99, 25, 'TOYS-8002', 'TOYS', true, NOW(), NOW(), 'system', 'system'),
    ('Stuffed Teddy Bear', 'Soft plush bear, 18 inches tall', 19.99, 60, 'TOYS-8003', 'TOYS', true, NOW(), NOW(), 'system', 'system'),
    ('Board Game Collection', 'Family pack with 6 classic board games', 39.99, 30, 'TOYS-8004', 'TOYS', true, NOW(), NOW(), 'system', 'system'),
    ('Educational Science Kit', 'STEM learning with 20+ experiments', 34.99, 20, 'TOYS-8005', 'TOYS', true, NOW(), NOW(), 'system', 'system'),

    -- Sports
    ('Yoga Mat', 'Non-slip surface, 6mm thick, eco-friendly material', 24.99, 100, 'SPRT-9001', 'SPORTS', true, NOW(), NOW(), 'system', 'system'),
    ('Dumbbell Set 20kg', 'Adjustable weights with carrying case', 89.99, 15, 'SPRT-9002', 'SPORTS', true, NOW(), NOW(), 'system', 'system'),
    ('Basketball', 'Official size and weight, indoor/outdoor use', 29.99, 40, 'SPRT-9003', 'SPORTS', true, NOW(), NOW(), 'system', 'system'),
    ('Tennis Racket', 'Graphite composite frame with vibration dampening', 79.99, 25, 'SPRT-9004', 'SPORTS', true, NOW(), NOW(), 'system', 'system'),
    ('Hiking Backpack 35L', 'Waterproof with multiple compartments and hydration compatibility', 69.99, 30, 'SPRT-9005', 'SPORTS', true, NOW(), NOW(), 'system', 'system'),

    -- Other
    ('Indoor Plant Pot Set', 'Set of 3 ceramic pots with drainage holes', 39.99, 50, 'OTHR-0001', 'OTHER', true, NOW(), NOW(), 'system', 'system'),
    ('Scented Candle Collection', 'Set of 4 seasonal fragrances, 40hr burn time each', 32.99, 60, 'OTHR-0002', 'OTHER', true, NOW(), NOW(), 'system', 'system'),
    ('Wall Art Print', 'Contemporary abstract design, 24"x36"', 49.99, 25, 'OTHR-0003', 'OTHER', true, NOW(), NOW(), 'system', 'system'),
    ('Digital Alarm Clock', 'LED display with USB charging port', 19.99, 40, 'OTHR-0004', 'OTHER', true, NOW(), NOW(), 'system', 'system'),
    ('Stainless Steel Water Bottle', 'Vacuum insulated, keeps drinks hot/cold for 24hrs', 24.99, 75, 'OTHR-0005', 'OTHER', true, NOW(), NOW(), 'system', 'system');