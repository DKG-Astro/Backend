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







