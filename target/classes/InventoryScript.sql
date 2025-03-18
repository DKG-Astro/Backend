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

CREATE TABLE asset_master(
    asset_id INT AUTO_INCREMENT PRIMARY KEY,
    material_code VARCHAR(50) NOT NULL,
    material_desc VARCHAR(50) NOT NULL,
    asset_desc VARCHAR(50) NOT NULL,
    make_no VARCHAR(50),
    serial_no VARCHAR(50),
    model_no VARCHAR(50),
    init_quantity DECIMAL(10,2),
    uom_id VARCHAR(10) NOT NULL,
    component_name VARCHAR(50),
    component_id INT,
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL,
    updated_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by INT,
    INDEX idx_material_code (material_code),
    INDEX idx_uom (uom_id),
    INDEX idx_material_desc (material_desc),
    FOREIGN KEY (material_code) REFERENCES material_master(material_code) ON UPDATE CASCADE
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
    FOREIGN KEY (sub_process_id) REFERENCES gprn_master(sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (material_code) REFERENCES material_master(material_code) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (uom_id) REFERENCES uom_master(uom_code) ON UPDATE CASCADE
);

CREATE TABLE goods_inspection_master (
    inspection_sub_process_id INT AUTO_INCREMENT PRIMARY KEY,
    gprn_process_id VARCHAR(50) NOT NULL,
    gprn_sub_process_id INT NOT NULL,
    installation_date DATE,
    commissioning_date DATE,
    location_id VARCHAR(10),
    INDEX idx_gprn_process (gprn_process_id),
    INDEX idx_gprn_subprocess (gprn_sub_process_id),
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location_master(location_code) ON UPDATE CASCADE,
    FOREIGN KEY (gprn_sub_process_id) REFERENCES gprn_master(sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE goods_inspection_detail (
    inspection_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    inspection_sub_process_id INT NOT NULL,
    gprn_sub_process_id INT NOT NULL,
    gprn_process_id INT NOT NULL,
    material_code VARCHAR(50) NOT NULL,
    material_desc VARCHAR(50) NOT NULL,
    asset_id INT,
    installation_report_filename VARCHAR(255),
    received_quantity DECIMAL(10,2) NOT NULL,
    accepted_quantity DECIMAL(10,2) NOT NULL,
    rejected_quantity DECIMAL(10,2) NOT NULL,
    INDEX idx_inspection_subprocess (inspection_sub_process_id),
    INDEX idx_gprn_subprocess (gprn_sub_process_id),
    INDEX idx_material (material_code),
    FOREIGN KEY (inspection_sub_process_id) REFERENCES goods_inspection_master(inspection_sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (gprn_sub_process_id) REFERENCES gprn_master(sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (asset_id) REFERENCES asset_master(asset_id) ON UPDATE CASCADE
);

CREATE TABLE grv_master (
    gi_sub_process_id INT NOT NULL,
    gi_process_id VARCHAR(50) NOT NULL,
    grv_process_id VARCHAR(50) NOT NULL,
    grv_sub_process_id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE,
    created_by Varchar(50) NOT NULL,
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    location_id VARCHAR(10) NOT NULL,
    INDEX idx_grv_process_id (grv_process_id),
    INDEX idx_gi_sub_process (gi_sub_process_id),
    INDEX idx_date (date),
    FOREIGN KEY (gi_sub_process_id) REFERENCES goods_inspection_master(gprn_sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES location_master(location_code) on update cascade
);

CREATE TABLE grv_material_detail (
    detail_id INT AUTO_INCREMENT PRIMARY KEY,
    grv_process_id VARCHAR(50) NOT NULL,
    grv_sub_process_id INT NOT NULL,
    gi_sub_process_id INT NOT NULL,
    material_code VARCHAR(50) NOT NULL,
    material_desc VARCHAR(50) NOT NULL,
    uom_id VARCHAR(10) NOT NULL,
    rejected_quantity DECIMAL(10,2) NOT NULL,
    return_quantity DECIMAL(10,2) NOT NULL,
    return_type VARCHAR(50) NOT NULL,
    reject_reason VARCHAR(50) NOT NULL,
    INDEX idx_grv_sub_process (grv_sub_process_id),
    INDEX idx_grv_process_id (grv_process_id),
    INDEX idx_material (material_code),
    INDEX idx_return_type (return_type),
    foreign key (grv_sub_process_id) references grv_master(grv_sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    foreign key (uom_id) references uom_master(uom_code) on update cascade,
    foreign key (material_code) references material_master(material_code) on update cascade
);

CREATE TABLE asset_master(
    asset_id INT AUTO_INCREMENT PRIMARY KEY,
    material_code VARCHAR(50) NOT NULL,
    material_desc VARCHAR(50) NOT NULL,
    asset_desc VARCHAR(50) NOT NULL,
    make_no VARCHAR(50),
    serial_no VARCHAR(50),
    model_no VARCHAR(50),
    init_quantity BIGDECIMAL(10,2),
    uom_id VARCHAR(10) NOT NULL,
    component_name VARCHAR(50),
    component_id INT,
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL,
    updated_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by INT,
    INDEX idx_material_code (material_code),
    INDEX idx_uom (uom_id),
    INDEX idx_material_desc (material_desc),
    FOREIGN KEY (material_code) REFERENCES material_master(material_code) ON UPDATE CASCADE
);

CREATE TABLE goods_inspection_master (
    inspection_sub_process_id INT AUTO_INCREMENT PRIMARY KEY,
    gprn_process_id VARCHAR(50) NOT NULL,
    gprn_sub_process_id INT NOT NULL,
    installation_date DATE,
    commissioning_date DATE,
    INDEX idx_gprn_process (gprn_process_id),
    INDEX idx_gprn_subprocess (gprn_sub_process_id),
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location_master(location_code) ON UPDATE CASCADE,
    FOREIGN KEY (gprn_sub_process_id) REFERENCES gprn_master(sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE goods_inspection_detail (
    inspection_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    inspection_sub_process_id INT NOT NULL,
    gprn_sub_process_id INT NOT NULL,
    gprn_process_id INT NOT NULL,
    material_code VARCHAR(50) NOT NULL,
    material_desc VARCHAR(50) NOT NULL,
    asset_id INT,
    installation_report_filename VARCHAR(255),
    received_quantity DECIMAL(10,2) NOT NULL,
    accepted_quantity DECIMAL(10,2) NOT NULL,
    rejected_quantity DECIMAL(10,2) NOT NULL,
    INDEX idx_inspection_subprocess (inspection_sub_process_id),
    INDEX idx_gprn_subprocess (gprn_sub_process_id),
    INDEX idx_material (material_code),
    FOREIGN KEY (inspection_sub_process_id) REFERENCES goods_inspection_master(inspection_sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (gprn_sub_process_id) REFERENCES gprn_master(sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (asset_id) REFERENCES asset_master(asset_id) ON UPDATE CASCADE,
    FOREIGN KEY (material_code) REFERENCES material_master(material_code) ON UPDATE CASCADE
);

CREATE TABLE grn_master(
    grn_process_id VARCHAR(50) NOT NULL,
    grn_sub_process_id INT AUTO_INCREMENT PRIMARY KEY,
    gprn_process_id VARCHAR(50) NOT NULL,
    gprn_sub_process_id INT NOT NULL,
    grn_date DATE,
    installation_date DATE,
    commissioning_date DATE,
    created_by VARCHAR(50) NOT NULL,
    system_created_by INT NOT NULL,
    create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    location_id VARCHAR(10) NOT NULL,

    FOREIGN KEY (location_id) REFERENCES location_master(location_code) ON UPDATE CASCADE,
    FOREIGN KEY (gprn_sub_process_id) REFERENCES gprn_master(sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE

);

CREATE TABLE grn_material_detail(
    detail_id INT AUTO_INCREMENT PRIMARY KEY,
    grn_process_id VARCHAR(50) NOT NULL,
    grn_sub_process_id INT NOT NULL,
    gi_sub_process_id INT NOT NULL,
    gi_process_id VARCHAR(50) NOT NULL,
    quantity DECIMAL(10, 2) NOT NULL,
    asset_id INT NOT NULL,
    locator_id INT NOT NULL,
    book_value DECIMAL(10,2) NOT NULL,
    depriciation_rate DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (asset_id) REFERENCES asset_master(asset_id) ON UPDATE CASCADE,
    FOREIGN KEY (grn_sub_process_id) REFERENCES grn_master(grn_sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (gi_sub_process_id) REFERENCES goods_inspection_master(inspection_sub_process_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (locator_id) references locator_master(locator_id) on update cascade
);

CREATE TABLE ohq_master (
    ohq_id INT AUTO_INCREMENT PRIMARY KEY,
    asset_id INT NOT NULL,
    locator_id INT NOT NULL,
    book_value DECIMAL(10,2) NOT NULL,
    depriciation_rate DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (asset_id) REFERENCES asset_master(asset_id) ON UPDATE CASCADE,
    FOREIGN KEY (locator_id) REFERENCES locator_master(locator_id) ON UPDATE CASCADE
);


