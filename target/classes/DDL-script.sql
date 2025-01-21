CREATE TABLE `astrodatabase`.`user_master` (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(100) NULL,
  `mobileNumber` VARCHAR(10) NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`userId`));

CREATE TABLE `astrodatabase`.`role_master` (
  `roleId` INT NOT NULL AUTO_INCREMENT,
  `roleName` VARCHAR(100) NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`roleId`));

CREATE TABLE `astrodatabase`.`user_role_master` (
  `userRoleId` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `roleId` INT NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`userRoleId`));

CREATE TABLE `astrodatabase`.`workflow_master` (
  `workflowId` INT NOT NULL AUTO_INCREMENT,
  `workflowName` VARCHAR(255) NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`workflowId`));

CREATE TABLE `astrodatabase`.`state_master` (
  `stateId` INT NOT NULL AUTO_INCREMENT,
  `stateName` VARCHAR(255) NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`stateId`));

 CREATE TABLE `astrodatabase`.`transition_master` (
  `transitionId` INT NOT NULL AUTO_INCREMENT,
  `transitionName` VARCHAR(255) NOT NULL,
  `workflowId` INT NOT NULL,
  `currentRoleId` INT NOT NULL,
  `nextRoleId` INT NULL,
  `previousRoleId` INT NULL,
  `conditionId` INT NULL,
  `transitionOrder` INT NOT NULL,
  `transitionSubOrder` INT NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`transitionId`));

  CREATE TABLE `astrodatabase`.`action_master` (
  `actionId` INT NOT NULL AUTO_INCREMENT,
  `actionName` VARCHAR(255) NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`actionId`));

   CREATE TABLE `astrodatabase`.`workflow_transition` (
  `workflowTransitionId` INT NOT NULL AUTO_INCREMENT,
  `workflowId` INT NOT NULL,
  `workflowName` VARCHAR(255) NOT NULL,
  `transitionId` INT NOT NULL,
  `requestId` INT NOT NULL,
  `createdBy` INT NOT NULL,
  `modifiedBy` INT NULL,
  `status` VARCHAR(255) NOT NULL,
  `nextAction` VARCHAR(100) NULL,
  `transitionOrder` INT NOT NULL,
  `transitionSubOrder` INT NOT NULL,
  `createdDate` DATETIME NULL,
  `modificationDate` DATETIME NULL,
  PRIMARY KEY (`workflowTransitionId`));

   CREATE TABLE `astrodatabase`.`transition_condition_master` (
    `conditionId` INT NOT NULL AUTO_INCREMENT,
    `workflowId` INT NOT NULL,
    `conditionKey` VARCHAR(255) NOT NULL,
    `conditionValue` VARCHAR(255) NOT NULL,
    `createdDate` DATETIME NULL,
    `createdBy` VARCHAR(45) NULL,
    PRIMARY KEY (`conditionId`));



--Inventory Modules
CREATE TABLE gprn (
    gprn_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    po_no VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    delivery_challan_no VARCHAR(255) NOT NULL,
    delivery_challan_date DATE NOT NULL,
    vendor_id VARCHAR(255) NOT NULL,
    vendor_name VARCHAR(255) NOT NULL,
    vendor_email VARCHAR(255),
    vendor_contact_no BIGINT,
    field_station VARCHAR(255) NOT NULL,
    indentor_name VARCHAR(255) NOT NULL,
    expected_supply_date DATE NOT NULL,
    consignee_detail VARCHAR(255) NOT NULL,
    warranty_years INT,
    project VARCHAR(255),
    received_qty VARCHAR(255),
    pending_qty VARCHAR(255),
    accepted_qty VARCHAR(255),
    provisional_receipt_certificate LONGBLOB,
    received_by VARCHAR(255) NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
);
CREATE TABLE gprn_materials (
    material_code BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    uom VARCHAR(50),
    ordered_quantity INT,
    quantity_delivered INT,
    received_quantity INT,
    unit_price DOUBLE,
    net_price DECIMAL(19, 4),
    make_no VARCHAR(255),
    model_no VARCHAR(255),
    serial_no VARCHAR(255),
    warranty VARCHAR(255),
    note TEXT,
    photograph_path VARCHAR(255),
    gprn_id BIGINT,
    FOREIGN KEY (gprn_id) REFERENCES gprn(gprn_no) ON DELETE CASCADE
);



CREATE TABLE goods_inspection (
    goods_inspection_no BigInt AUTO_INCREMENT PRIMARY KEY,
	gpr_id BIGINT, -- Foreign key to Good Provisional Receipt entity
    installation_date varchar(20),
    commissioning_date varchar(20),
    upload_installation_report text, -- For storing PDF files
    accepted_quantity INT NOT NULL,
    rejected_quantity INT NOT NULL,
	goods_return_permament_or_replacement VARCHAR(255),
    goods_return_full_or_partial VARCHAR(255),
    goods_return_reason VARCHAR(255),
    created_by VARCHAR(255),
    updated_by varchar(200),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);




CREATE TABLE goods_return (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    goods_return_note_no VARCHAR(255) NOT NULL,
    rejected_quantity INT NOT NULL,
    return_quantity INT NOT NULL,
    type_of_return VARCHAR(100) NOT NULL,
    reason_of_return TEXT NOT NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by varchar(200),
    updated_by varchar(200),
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE goods_receipt_inspection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_inspection_no VARCHAR(255),
    installation_date DATE,
    commissioning_date DATE,
    asset_code VARCHAR(255),
    additional_material_description TEXT,
    locator VARCHAR(255),
    print_label_option BOOLEAN DEFAULT FALSE,
    depreciation_rate DOUBLE,
    book_value DOUBLE,
    attach_component_popup VARCHAR(255),
    updated_by VARCHAR(255),
    created_by VARCHAR(255),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE asset (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_code VARCHAR(255) ,
    material_code VARCHAR(255),
    description TEXT ,
    uom VARCHAR(50) ,
    make_no VARCHAR(100),
    model_no VARCHAR(100),
    serial_no VARCHAR(100),
    component_name VARCHAR(255),
    component_code VARCHAR(255),
    quantity INT,
    locator VARCHAR(255) ,
    transaction_history TEXT,
    current_condition VARCHAR(50) ,
    updated_by varchar(200),
    created_by varchar(200),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE tender_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title_of_tender VARCHAR(255) NOT NULL,
    opening_date DATE,
    closing_date DATE,
    indent_id VARCHAR(255),
    indent_materials varchar(200),
    mode_of_procurement VARCHAR(255),
    bid_type VARCHAR(255),
    last_date_of_submission DATE,
    applicable_taxes TEXT,
    consignes_and_billinng_address TEXT,
    inco_terms VARCHAR(255),
    payment_terms varchar(255),
    ld_clause varchar(255),
    applicable_performance varchar(255),
    bid_security_declaration BOOLEAN,
    mll_status_declaration BOOLEAN,
    upload_tender_documents BLOB,
    single_and_multiple_vendors VARCHAR(255),
    upload_general_terms_and_conditions BLOB,
    upload_specific_terms_and_conditions BLOB,
    pre_bid_disscussions TEXT,
    created_by varchar(200),
     updated_by VARCHAR(255),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE contigency_purchase (
    Contigency_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vendors_name VARCHAR(255),
    vendors_invoice_no VARCHAR(255),
    Date DATE,
    material_code VARCHAR(255),
    material_description VARCHAR(255),
    quantity DECIMAL(15, 2),
    unit_price DECIMAL(15, 2),
    remarks_for_purchase VARCHAR(255),
    amount_to_be_paid DECIMAL(15, 2),
    upload_copy_of_invoice BLOB,
    predifined_purchase_statement VARCHAR(255),
    project_detail VARCHAR(255),
    created_by varchar(255),
    updated_by VARCHAR(255),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE indent_creation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    indentor_name VARCHAR(255) NOT NULL,
    indentor_id VARCHAR(255) NOT NULL,
    indentor_mobile_no VARCHAR(20),
    indentor_email_address VARCHAR(255),
    consignes_location VARCHAR(255),
    uploading_prior_approvals VARCHAR(255),
    project_name VARCHAR(255),
    upload_tender_documents BLOB,
    is_pre_bit_meeting_required BOOLEAN,
    pre_bid_meeting_date DATE,
    pre_bid_meeting_venue VARCHAR(255),
    is_it_a_rate_contract_indent BOOLEAN,
    estimated_rate DECIMAL(10, 2),
    period_of_contract DECIMAL(10, 2),
    single_and_multiple_job VARCHAR(50),
    upload_goi_or_rfp BLOB,
    upload_pac_or_brand_pac BLOB,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
select *from  material_details
CREATE TABLE material_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_code VARCHAR(255) NOT NULL,
    material_description TEXT,
    quantity DECIMAL(10, 2),
    unit_price DECIMAL(10, 2),
    uom VARCHAR(50),
    total_price DECIMAL(10, 2),
    budget_code VARCHAR(255),
    material_category VARCHAR(255),
    material_sub_category VARCHAR(255),
    material_and_job VARCHAR(255),
    indent_creation_id BIGINT,
    FOREIGN KEY (indent_creation_id) REFERENCES indent_creation(id) ON DELETE CASCADE
);

CREATE TABLE purchase_order (
    po_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tender_id VARCHAR(255),
    indent_id VARCHAR(255),
    warranty DECIMAL(10, 2),
    consignes_address VARCHAR(255),
    billing_address VARCHAR(255),
    delivery_period DECIMAL(10, 2),
    if_ld_clause_applicable BOOLEAN,
    incoterms VARCHAR(255),
    paymentterms VARCHAR(255),
    vendor_name VARCHAR(255),
    vendor_address VARCHAR(255),
    applicable_pbg_to_be_submitted VARCHAR(255),
    transposter_and_freight_for_warder_details VARCHAR(255),
    vendor_account_number VARCHAR(255),
    vendors_zfsc_code VARCHAR(255),
    vendor_account_name VARCHAR(255),
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE purchase_order_attributes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_code VARCHAR(255),
    material_description VARCHAR(255),
    quantity DECIMAL(10, 2),
    rate DECIMAL(10, 2),
    currency VARCHAR(255),
    exchange_rate DECIMAL(10, 2),
    gst DECIMAL(10, 2),
    duties DECIMAL(10, 2),
    freight_charge DECIMAL(10, 2),
    budget_code VARCHAR(255),
    purchase_order_id BIGINT,
    FOREIGN KEY (purchase_order_id) REFERENCES purchase_order(po_id)
);


Select *from service_order
CREATE TABLE service_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tender_id VARCHAR(255),
    consignes_address VARCHAR(255),
    billing_address VARCHAR(255),
    job_completion_period DECIMAL(10, 2),
    if_ld_clause_applicable BOOLEAN,
    inco_terms VARCHAR(255),
    payment_terms VARCHAR(255),
    vendor_name VARCHAR(255),
    vendor_address VARCHAR(255),
    applicable_pbg_to_be_submitted VARCHAR(255),
    vendors_account_no VARCHAR(255),
    vendors_zrsc_code VARCHAR(255),
    vendors_account_name VARCHAR(255),
    created_by varchar(200),
    updated_by varchar(200),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
select *from service_order_material
CREATE TABLE service_order_material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_code VARCHAR(255),
    material_description VARCHAR(255),
    quantity DECIMAL(10, 2),
    rate DECIMAL(10, 2),
    exchange_rate DECIMAL(10, 2),
    currency VARCHAR(50),
    gst DECIMAL(10, 2),
    duties DECIMAL(10, 2),
    budget_code VARCHAR(255),
    service_order_id BIGINT,
    FOREIGN KEY (service_order_id) REFERENCES service_order(id) ON DELETE CASCADE
);


CREATE TABLE work_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tender_id VARCHAR(255),
    consignes_address VARCHAR(255),
    billing_address VARCHAR(255),
    job_completion_period DECIMAL(10, 2),
    if_ld_clause_applicable BOOLEAN,
    inco_terms VARCHAR(255),
    payment_terms VARCHAR(255),
    vendor_name VARCHAR(255),
    vendor_address VARCHAR(255),
    applicable_pbg_to_be_submitted VARCHAR(255),
    vendors_account_no VARCHAR(255),
    vendors_zrsc_code VARCHAR(255),
    vendors_account_name VARCHAR(255),
    created_by varchar(200),
    updated_by varchar(200),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE work_order_material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_code VARCHAR(255),
    work_description VARCHAR(255),
    quantity DECIMAL(10, 2),
    rate DECIMAL(10, 2),
    exchange_rate DECIMAL(10, 2),
    currency VARCHAR(50),
    gst DECIMAL(10, 2),
    duties DECIMAL(10, 2),
    budget_code VARCHAR(255),
    work_order_id BIGINT,
    FOREIGN KEY (work_order_id) REFERENCES work_order(id) ON DELETE CASCADE
);




















