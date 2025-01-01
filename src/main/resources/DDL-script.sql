CREATE TABLE `workflow`.`user_master` (
  `userId` INT NOT NULL,
  `userName` VARCHAR(100) NULL,
  `mobileNumber` VARCHAR(10) NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`userId`));

CREATE TABLE `workflow`.`role_master` (
  `roleId` INT NOT NULL,
  `roleName` VARCHAR(100) NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`roleId`));

CREATE TABLE `workflow`.`user_role_master` (
  `userRoleId` INT NOT NULL,
  `userId` INT NOT NULL,
  `roleId` INT NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`userRoleId`));

CREATE TABLE `workflow`.`workflow_master` (
  `workflowId` INT NOT NULL,
  `workflowName` VARCHAR(255) NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`workflowId`));

CREATE TABLE `workflow`.`state_master` (
  `stateId` INT NOT NULL,
  `stateName` VARCHAR(255) NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`stateId`));

 CREATE TABLE `workflow`.`transition_master` (
  `transitionId` INT NOT NULL,
  `transitionName` VARCHAR(255) NOT NULL,
  `currentRoleId` INT NOT NULL,
  `nextRoleId` INT NULL,
  `previousRoleId` INT NULL,
  `tranConditionKey` VARCHAR(255) NULL,
  `tranConditionValue` VARCHAR(255) NULL,
  `transitionOrder` INT NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`transitionId`));

  CREATE TABLE `workflow`.`action_master` (
  `actionId` INT NOT NULL,
  `actionName` VARCHAR(255) NOT NULL,
  `createdDate` DATETIME NULL,
  `createdBy` VARCHAR(45) NULL,
  PRIMARY KEY (`workflowId`));

   CREATE TABLE `workflow`.`workflow_transition` (
  `workflowTransitionId` INT NOT NULL,
  `workflowId` INT NOT NULL,
  `workflowName` VARCHAR(255) NOT NULL,
  `transitionId` INT NOT NULL,
  `requestId` INT NOT NULL,
  `createdBy` INT NOT NULL,
  `modifiedBy` INT NULL,
  `status` VARCHAR(255) NOT NULL,
  `transitionOrder` INT NOT NULL,
  `transitionSubOrder` INT NOT NULL,
  `createdDate` DATETIME NULL,
  `modificationDate` DATETIME NULL,
  PRIMARY KEY (`workflowTransitionId`));







