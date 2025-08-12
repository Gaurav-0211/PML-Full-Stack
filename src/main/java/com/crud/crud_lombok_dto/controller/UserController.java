package com.crud.crud_lombok_dto.controller;
import com.crud.crud_lombok_dto.config.AppConstants;
import com.crud.crud_lombok_dto.dto.*;
import com.crud.crud_lombok_dto.exception.NoSuchUserExistException;
import com.crud.crud_lombok_dto.model.User;
import com.crud.crud_lombok_dto.repository.UserRepository;
import com.crud.crud_lombok_dto.security.JwtTokenHelper;
import com.crud.crud_lombok_dto.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    // Create User Controller Request
    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@RequestBody @Valid UserDto userdto){
        log.info("Register Api called in Controller");
        UserDto dto = this.service.createUser(userdto);
        Response response = Response.buildResponse(
                "SUCCESS",
                "User Registered successfully",
                dto,
                AppConstants.CREATED,
                "Request Processes Successfully"

        );
        log.info("User Registered Successfully Controller- create user");
        return ResponseEntity.ok(response);
    }

    // Login Request without authentication just for fronted Practice
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login API called");

        boolean isValid = service.validateUser(loginRequest.getEmail(), loginRequest.getPassword());

        if (isValid) {
            Response response = Response.buildResponse(
                    "SUCCESS",
                    "User Login successfully",
                    null,
                    AppConstants.OK,
                    "Request Processes Successfully"

            );
            return ResponseEntity.ok(response);
        } else {

            Response response = Response.buildResponse(
                    "FAILED",
                    "User fetch Unsuccess",
                    null,
                    AppConstants.OK,
                    "Request Processes Successfully"

            );
            return ResponseEntity.ok(response);
        }
    }

    // Get all user request without pagination and sorting
    @GetMapping("/get-all")
    public ResponseEntity<Response> getAllUsers() {
        List<UserDto> dto = this.service.getAllUsers();
        log.info("Get all Called in Controller");
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                dto,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("Get all in controller all user fetched");
        return ResponseEntity.ok(response);
    }

    // Get user By Id Request
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        log.info("Get by id called in controller");
        UserDto userDto = service.getUserById(id);
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                userDto,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("Get by id in controller user fetched");
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.OK));
    }

    // Update Existing user Request
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto user) {
        log.info("Update Put called in controller");
        UserDto userDto = service.updateUser(id,user);
        log.info("Update Put called in controller");
        Response response = Response.buildResponse(
                "SUCCESS",
                "User Updated successfully",
                userDto,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("User Updated successfully controller - Put Request");
        return ResponseEntity.ok(response);
    }

    // Delete User by Id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.info("Delete called in controller");
            service.deleteUser(id);
            log.info("Delete called in controller");
        Response response = Response.buildResponse(
                "SUCCESS",
                "User deleted successfully",
                null,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("User deleted successfully controller - Delete Request");
            return new ResponseEntity<>(HttpStatusCode.valueOf(AppConstants.OK));
    }

    // Get all user by pagination and sorting
    @GetMapping
    public ResponseEntity<Response> getAll(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = AppConstants.SORT_DIR,required = false) String sortDir)
    {
        log.info("Get All called in controller");
        UserResponse users = this.service.getAllPost(pageNumber, pageSize,sortBy, sortDir);
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                users,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("User fetched successfully controller - Get Request with pagination");
        return ResponseEntity.ok(response);
    }

    // Get all user by full name
    @GetMapping("/by-name")
    public ResponseEntity<Response> getByName(@RequestParam String name){
        log.info("Get By Name Api in controller");
        List<UserDto> allUser = this.service.getAllUserByName(name);
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                allUser,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("User fetched successfully controller - Get Request with name");
        return ResponseEntity.ok(response);
    }

    // Get all user by updated date
    @GetMapping("/by-date")
    public ResponseEntity<Response> getByDate(){
        log.info("Get user by Recent Updated in controller");
        List<UserDto> allUser = this.service.getAllUserByUpdatedDate();
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                allUser,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("Get user by Recent Updated in controller - User fetched");
        return ResponseEntity.ok(response);
    }

    // Get all user by name starting with
    @GetMapping("/by-start-name")
    public ResponseEntity<Response> getByNameStart(@RequestParam String name){
        log.info("Get By Start Name Api in controller");
        List<UserDto> allUser = this.service.getAllNameStartWith(name);
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                allUser,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("Get By Full Name api executed in controller");
        return ResponseEntity.ok(response);
    }


    // Get all user by name Ending with
    @GetMapping("/by-end-name")
    public ResponseEntity<Response> getByNameEnd(@RequestParam String name){
        log.info("Get By End Name Api in controller");
        List<UserDto> allUser = this.service.getAllNameEndWith(name);
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                allUser,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("Get By End Name controller executed");
        return ResponseEntity.ok(response);
    }

    // Get all user Order By name Desc
    @GetMapping("/orderBy-name")
    public ResponseEntity<Response> getNameDesc(){
        log.info("Get By Name Order By Descending");
        List<UserDto> allUser = this.service.getAllNameDesc();
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                allUser,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("Get all Order By Name Controller executed");
        return ResponseEntity.ok(response);
    }

    // Sent email to any user
    @PostMapping("/send-mail")
    public ResponseEntity<Response> sendMail(@RequestBody MailEntity mailEntity){
        log.info("Sending mail in controller");
        this.service.sendEmail(mailEntity);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Mail sent successfully",
                null,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("Sending mail in controller executed");
        return ResponseEntity.ok(response);
    }


    // Sent OTP on Whatsapp
    @PostMapping("/send-otp")
    public ResponseEntity<Response> sendOtp(@RequestParam String toPhoneNumber) {
        log.info("Sending OTP in controller ");
        String otp = service.generateOtp();
        boolean isSent = service.sendOtp(toPhoneNumber, otp);

        if (isSent) {
            Response response = Response.buildResponse(
                    "SUCCESS",
                    "OTP sent successfully",
                    null,
                    AppConstants.OK,
                    "Request Processes Successfully"

            );
            log.info("Sending email in controller executed");
            return ResponseEntity.ok(response);
        } else {
            Response response = Response.buildResponse(
                    "FAILED",
                    "OTP not Sent",
                    null,
                    AppConstants.OK,
                    "Request Processes Successfully"

            );
            return ResponseEntity.ok(response);
        }
    }


    // Authenticated loing for token generation
    @PostMapping("/auth-login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto request) {
        log.info("Loing user for token generation in controller");

        // 1. Authenticate credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Load UserDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // 3. Get agent from DB
        User user = userRepository.findByEmail(request.getEmail());
        if(user == null){
            throw new NoSuchUserExistException("User not exist with the given mail Id");
        }

        // 4. Compare role
        String dbRole = user.getRole().getName().name(); // assuming enum stored in DB
        String requestRole = request.getRole().name();

        if (!dbRole.equalsIgnoreCase(requestRole)) {
            throw new RuntimeException("Access denied: Role mismatch. You are not authorized as " + requestRole);
        }

        // 5. Generate JWT with role
        String token = jwtTokenHelper.generateToken(userDetails, dbRole);

        // 6. Return token and role in response
        log.info("login auth controller token generated");
        return ResponseEntity.ok(new JwtAuthResponse(token, dbRole));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Response> changePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto) {
        log.info("Change password in controller");

         this.service.changePassword(passwordChangeDto.getEmail(), passwordChangeDto.getOldPassword(), passwordChangeDto.getNewPassword());
        Response response = Response.buildResponse(
                "SUCCESS",
                "Password Changed Successfully",
                null,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        log.info("change password - controller executed");
        return ResponseEntity.ok(response);
    }

}
