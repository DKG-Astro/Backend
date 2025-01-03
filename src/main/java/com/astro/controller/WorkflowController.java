package com.astro.controller;


import com.astro.service.WorkflowService;
import com.astro.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WorkflowController {

    @Autowired
    WorkflowService workflowService;

    @GetMapping("/getWorkflowByName")
    public ResponseEntity<Object> getAllUserDetails(@RequestParam String workflowName) {
        return new ResponseEntity<Object>(ResponseBuilder.getSuccessResponse(workflowService.workflowByWorkflowName(workflowName)), HttpStatus.OK);

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
