INSERT INTO ITEM (id, name, description)
VALUES
    (1, 'T-Shirt', 'Cotton T-shirt'),
    (2, 'Hoodie', 'Comfy warm hoodie');

INSERT INTO VARIANT (id, sku, name, price, stock, item_id, version)
VALUES
    (1, 'TSHIRT-RED-M',  'Red - M',   120000, 10, 1, 0),
    (2, 'TSHIRT-BLUE-L', 'Blue - L',  115000, 5,  1, 0),
    (3, 'HOODIE-BLACK-L','Black - L', 250000, 7,  2, 0),
    (4, 'HOODIE-GRAY-M', 'Gray - M',  245000, 12, 2, 0);
