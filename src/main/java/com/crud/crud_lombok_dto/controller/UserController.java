package com.crud.crud_lombok_dto.controller;
import com.crud.crud_lombok_dto.config.AppConstants;
import com.crud.crud_lombok_dto.dto.*;
import com.crud.crud_lombok_dto.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userdto){
        log.info("Register Api called in Controller");
        UserDto dto = this.service.createUser(userdto);
        return new ResponseEntity<UserDto>(dto,HttpStatusCode.valueOf(AppConstants.CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login API called");

        boolean isValid = service.validateUser(loginRequest.getEmail(), loginRequest.getPassword());

        if (isValid) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(AppConstants.UNAUTHORISED).body("Invalid credentials.");
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> dto = this.service.getAllUsers();
        log.info("Get all Called in Controller");
        return new ResponseEntity<List<UserDto>>(dto, HttpStatusCode.valueOf(AppConstants.OK));

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto dto = this.service.getUserById(id);
        return new ResponseEntity<>(dto, HttpStatusCode.valueOf(AppConstants.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto user) {
        UserDto userDto = service.updateUser(id,user);
        log.info("Update Put called in controller");
        return new ResponseEntity<UserDto>(userDto, HttpStatusCode.valueOf(AppConstants.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
            service.deleteUser(id);
            log.info("Delete called in controller");
            return new ResponseEntity<>(HttpStatusCode.valueOf(AppConstants.OK));
    }

    @GetMapping
    public ResponseEntity<UserResponse> getAll(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = AppConstants.SORT_DIR,required = false) String sortDir)
    {
        UserResponse users = this.service.getAllPost(pageNumber, pageSize,sortBy, sortDir);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<UserDto>> getByName(@RequestParam String name){
        List<UserDto> allUser = this.service.getAllUserByName(name);
        return new ResponseEntity<>(allUser, HttpStatusCode.valueOf(AppConstants.OK));
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<UserDto>> getByDate(){
        log.info("Get By Date API in controller");
        List<UserDto> allUser = this.service.getAllUserByUpdatedDate();
        return new ResponseEntity<>(allUser, HttpStatusCode.valueOf(AppConstants.OK));
    }

    @GetMapping("/by-start-name")
    public ResponseEntity<List<UserDto>> getByNameStart(@RequestParam String name){
        log.info("Get By Start Name Api in controller");
        List<UserDto> allUser = this.service.getAllNameStartWith(name);
        return new ResponseEntity<>(allUser, HttpStatusCode.valueOf(AppConstants.OK));
    }


    @GetMapping("/by-end-name")
    public ResponseEntity<List<UserDto>> getByNameEnd(@RequestParam String name){
        log.info("Get By End Name Api in controller");
        List<UserDto> allUser = this.service.getAllNameEndWith(name);
        return new ResponseEntity<>(allUser, HttpStatusCode.valueOf(AppConstants.OK));
    }

    @GetMapping("/orderBy-name")
    public ResponseEntity<List<UserDto>> getNameDesc(){
        List<UserDto> allUser = this.service.getAllNameDesc();
        return new ResponseEntity<>(allUser,HttpStatusCode.valueOf(AppConstants.OK));
    }

    @PostMapping("/send-mail")
    public ResponseEntity<String> sendMail(@RequestBody MailEntity mailEntity){
        this.service.sendEmail(mailEntity);
        return new ResponseEntity<String>("Sent Successful",HttpStatusCode.valueOf(AppConstants.OK));
    }


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String toPhoneNumber) {
        String otp = service.generateOtp();
        boolean isSent = service.sendOtp(toPhoneNumber, otp);

        if (isSent) {
            return ResponseEntity.ok("OTP sent successfully to WhatsApp number: " + toPhoneNumber);
        } else {
            return ResponseEntity.status(AppConstants.INTERNAL_SERVER_ERROR).body("Failed to send OTP");
        }
    }


}
