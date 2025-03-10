--Inventory Modules

-- // foreign key constraints fro aster table not added yet 
CREATE TABLE gprn_master (
    process_id VARCHAR(50) NOT NULL,
    sub_process_id INT AUTO_INCREMENT PRIMARY KEY,
    po_id VARCHAR(50) NOT NULL,
    location_id VARCHAR(10) NOT NULL,
    date DATE,
    challan_no VARCHAR(50) NOT NULL,
    delivery_date DATE NOT NULL,
    vendor_id VARCHAR(50) NOT NULL,
    field_station VARCHAR(50) NOT NULL,
    indentor_name VARCHAR(50) NOT NULL,
    supply_expected_date DATE NOT NULL,
    consignee_detail VARCHAR(100) NOT NULL,
    warranty_years DECIMAL(10,1),
    project VARCHAR(50),
    received_by VARCHAR(50) NOT NULL,
    created_by Varchar(50) NOT NULL,
    updated_by VARCHAR(50),
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_id) REFERENCES vendor_master(vendor_id) ON UPDATE CASCADE,
    FOREIGN KEY (location_id) REFERENCES location_master(location_code) ON UPDATE CASCADE
    -- FOREIGN KEY (po_id) REFERENCES purchase_order(po_id) ON UPDATE CASCADE ON DELETE CASCADE,
);


CREATE TABLE gprn_material_detail (
    detail_id INT AUTO_INCREMENT PRIMARY KEY,
    process_id VARCHAR(50) NOT NULL,
    sub_process_id INT NOT NULL,
    po_id VARCHAR(50) NOT NULL,
    material_code VARCHAR(50) NOT NULL,
    material_desc VARCHAR(50) NOT NULL,
    uom_id VARCHAR(10) NOT NULL,
    received_quantity DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    make_no VARCHAR(50),
    serial_no VARCHAR(50),
    model_no VARCHAR(50),
    warranty_terms VARCHAR(100),
    note VARCHAR(100),
    photo_path VARCHAR(100),
    -- FOREIGN KEY (process_id) REFERENCES gprn_master(process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (sub_process_id) REFERENCES gprn_master(sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE
    -- FOREIGN KEY (material_code) REFERENCES material_master(material_code) ON UPDATE CASCADE ON DELETE CASCADE,
    -- FOREIGN KEY (uom_id) REFERENCES uom_master(uom_code) ON UPDATE CASCADE
);