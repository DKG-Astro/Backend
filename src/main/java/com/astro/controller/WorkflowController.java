package com.astro.controller;


import com.astro.service.WorkflowService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WorkflowController {

    @Autowired
    WorkflowService workflowService;

    @GetMapping("/getWorkflowByName")
    public ResponseEntity<Object> getWorkflowByName(@RequestParam String workflowName) {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(workflowService.workflowByWorkflowName(workflowName)), HttpStatus.OK);

    }

    @GetMapping("/getTransitionsByWorkflowId")
    public ResponseEntity<Object> getTransitionsByWorkflowId(@RequestParam Integer workflowId) {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(workflowService.transitionsByWorkflowId(workflowId)), HttpStatus.OK);
    }

    @GetMapping("/getTransitionByOrder")
    public ResponseEntity<Object> getTransitionByOrder(@RequestParam Integer workflowId, @RequestParam Integer order,@RequestParam Integer subOrder)  {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(workflowService.transitionsByWorkflowIdAndOrder(workflowId, order, subOrder)), HttpStatus.OK);
    }

    @PostMapping("/initiateWorkflow")
    public ResponseEntity<Object> initiateWorkflow(@RequestParam Integer requestId, @RequestParam String workflowName,@RequestParam Integer createdBy)  {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(workflowService.initiateWorkflow(requestId, workflowName, createdBy)), HttpStatus.OK);
    }

    @GetMapping("/workflowTransitionHistory")
    public ResponseEntity<Object> workflowTransitionHistory(@RequestParam Integer workflowId, @RequestParam(required = false) Integer createdBy, @RequestParam(required = false) Integer requestId, @RequestParam(required = false) String roleName)  {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(workflowService.workflowTransitionHistory(workflowId, createdBy, requestId, roleName)), HttpStatus.OK);
    }

    @GetMapping("/nextTransition")
    public ResponseEntity<Object> nextTransition(@RequestParam Integer workflowId, @RequestParam String currentRole, @RequestParam(required = false) String tranConditionKey, @RequestParam(required = false) String tranConditionValue)  {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(workflowService.nextTransition(workflowId, currentRole, tranConditionKey, tranConditionValue)), HttpStatus.OK);
    }

    @PostMapping("/performTransitionAction")
    public ResponseEntity<Object> performTransitionAction(@RequestParam Integer workflowTransitionId, @RequestParam Integer actionBy, @RequestParam String action)  {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(workflowService.performTransitionAction(workflowTransitionId, actionBy, action)), HttpStatus.OK);
    }


   /* @Autowired
    private UserService userService;

    @PostMapping("/userDetails")
    public ResponseEntity<Object> getUserDetails(@RequestBody LoginDto loginDto) {

            UserDetailDto userDetailDto = userService.getUserDetails(loginDto);
            return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(userDetailDto), HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegistrationDto registrationDto){
        UserDetailDto userDetailDto = userService.registerUser(registrationDto);
            return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(userDetailDto), HttpStatus.OK);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<Object> updateUserDetail(@RequestBody RegistrationDto registrationDto){
        UserDetailDto userDetailDto = userService.updateUserDetails(registrationDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(userDetailDto), HttpStatus.OK);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<Object> deleteUser(@RequestBody UserIdDto userIdDto){
        userService.deleteUser(userIdDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(), HttpStatus.OK);
    }

    @PostMapping("/activateUser")
    public ResponseEntity<Object> activateUser(@RequestBody UserIdDto userIdDto){
        userService.activateUser(userIdDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(), HttpStatus.OK);
    }

    @PostMapping("/deactivateUser")
    public ResponseEntity<Object> deactivateUser(@RequestBody UserIdDto userIdDto){
        userService.deactivateUser(userIdDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(), HttpStatus.OK);
    }



    @PostMapping("/addContactUs")
    public ResponseEntity<Object> addContactUs(@RequestBody ContactUsDto contactUsDto){
        userService.addContactUs(contactUsDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse("ContactUs saved."), HttpStatus.OK);
    }

    @PostMapping("/uploadProfileImage")
    public ResponseEntity<Object> uploadProfileImage(@RequestParam MultipartFile file, @RequestParam String userId){
        userService.uploadProfileImage(file, userId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse("Profile Image saved."), HttpStatus.OK);
    }

    @GetMapping("/downloadProfileImage")
    public ResponseEntity<Resource> downloadProfileImage(@RequestParam String userId){
        Map<String, Object> res = userService.downloadProfileImage(userId);
        ByteArrayResource resource = new ByteArrayResource((byte[]) res.get("content"));

        System.out.println(res.get("fileName"));


        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + res.get("fileName"));

        return ResponseEntity.ok().contentType(MediaType
                .APPLICATION_OCTET_STREAM)
                .headers(headers).body(resource);
    }

    @PostMapping("/removeProfileImage")
    public ResponseEntity<Object> removeProfileImage(@RequestParam String userId){
        boolean res = userService.removeProfileImage(userId);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(res), HttpStatus.OK);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<Object> updatePassword(@RequestBody LoginDto loginDto) {

        UserDetailDto userDetailDto = userService.updatePassword(loginDto);
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(userDetailDto), HttpStatus.OK);

    }

    @PostMapping("/getAllUserDetails")
    public ResponseEntity<Object> getAllUserDetails() {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(userService.getAllUserDetails()), HttpStatus.OK);

    }
*/
}
