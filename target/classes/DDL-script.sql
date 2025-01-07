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



CREATE TABLE purchase_order (
    po_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tender_requests VARCHAR(255) NOT NULL,
    corresponding_indents VARCHAR(255) NOT NULL,
    material_description VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    unit_rate DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    exchange_rate DECIMAL(15, 4),
    gst_percentage DECIMAL(5, 2) NOT NULL,
    duties_percentage DECIMAL(5, 2) NOT NULL,
    freight_charges DECIMAL(15, 2),
    delivery_period VARCHAR(100),
    warranty VARCHAR(255),
    consignee_address VARCHAR(500),
    additional_terms_and_conditions TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

--Inventory Modules
CREATE TABLE GPRN (
    gprn_no BIGINT PRIMARY KEY AUTO_INCREMENT,
    po_no VARCHAR(50) NOT NULL,
    date VARCHAR(20) NOT NULL,
    delivery_challan_no VARCHAR(50) NOT NULL,
    delivery_challan_date VARCHAR(20) NOT NULL,
    vendor_id VARCHAR(50) NOT NULL,
    vendor_name VARCHAR(255) NOT NULL,
    vendor_email VARCHAR(255),
    vendor_contact_no BIGINT,
    field_station VARCHAR(255) NOT NULL,
    indentor_name VARCHAR(255) NOT NULL,
    expected_supply_date VARCHAR(20) NOT NULL,
    consignee_detail TEXT NOT NULL,
    warranty_years INT,
    project VARCHAR(255),
    received_by VARCHAR(255) NOT NULL,
	material_code VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    uom VARCHAR(50) NOT NULL,
    ordered_quantity INT NOT NULL,
    quantity_delivered INT,
    received_quantity INT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    net_price DECIMAL(15, 2) GENERATED ALWAYS AS (received_quantity * unit_price) STORED, -- Auto-calculated net price
    make_no VARCHAR(255),
    model_no VARCHAR(255),
    serial_no VARCHAR(255),
    warranty TEXT,
    note TEXT,
    photograph_path TEXT
);

CREATE TABLE goods_inspection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    goods_inspection_no VARCHAR(50) NOT NULL,
    installation_date varchar(20),
    commissioning_date varchar(20),
    upload_installation_report text, -- For storing PDF files
    accepted_quantity INT NOT NULL,
    rejected_quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE goods_return (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    goods_return_note_no VARCHAR(255) NOT NULL,
    rejected_quantity INT NOT NULL,
    return_quantity INT NOT NULL,
    type_of_return VARCHAR(100) NOT NULL,
    reason_of_return TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE goods_receipt_inspection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_inspection_no VARCHAR(255) NOT NULL UNIQUE,
    installation_date DATE,
    commissioning_date DATE,
    asset_code VARCHAR(255) NOT NULL UNIQUE,
    additional_material_description TEXT,
    locator VARCHAR(255) NOT NULL,
    print_label_option BOOLEAN DEFAULT FALSE,
    depreciation_rate DECIMAL(5, 2),
											-- add the book value atribute
    attach_component_popup TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE asset (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asset_code VARCHAR(255) NOT NULL UNIQUE,
    material_code VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    uom VARCHAR(50) NOT NULL,
    make_no VARCHAR(100),
    model_no VARCHAR(100),
    serial_no VARCHAR(100),
    component_name VARCHAR(255),
    component_code VARCHAR(255),
    quantity INT,
    locator VARCHAR(255) NOT NULL,
    transaction_history TEXT,
    current_condition VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);









