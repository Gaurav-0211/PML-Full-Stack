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
        return ResponseEntity.ok(response);
    }

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
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        UserDto userDto = service.getUserById(id);
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                userDto,
                AppConstants.OK,
                "Request Processes Successfully"

        );

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(AppConstants.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto user) {
        UserDto userDto = service.updateUser(id,user);
        log.info("Update Put called in controller");
        Response response = Response.buildResponse(
                "SUCCESS",
                "User Updated successfully",
                userDto,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
            service.deleteUser(id);
            log.info("Delete called in controller");
        Response response = Response.buildResponse(
                "SUCCESS",
                "User deleted successfully",
                null,
                AppConstants.OK,
                "Request Processes Successfully"

        );
            return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response> getAll(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam (value = "sortDir", defaultValue = AppConstants.SORT_DIR,required = false) String sortDir)
    {
        UserResponse users = this.service.getAllPost(pageNumber, pageSize,sortBy, sortDir);
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                users,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-name")
    public ResponseEntity<Response> getByName(@RequestParam String name){
        List<UserDto> allUser = this.service.getAllUserByName(name);
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                allUser,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-date")
    public ResponseEntity<Response> getByDate(){
        log.info("Get By Date API in controller");
        List<UserDto> allUser = this.service.getAllUserByUpdatedDate();
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                allUser,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        return ResponseEntity.ok(response);
    }

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
        return ResponseEntity.ok(response);
    }


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
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orderBy-name")
    public ResponseEntity<Response> getNameDesc(){
        List<UserDto> allUser = this.service.getAllNameDesc();
        Response response = Response.buildResponse(
                "SUCCESS",
                "User fetched successfully",
                allUser,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-mail")
    public ResponseEntity<Response> sendMail(@RequestBody MailEntity mailEntity){
        this.service.sendEmail(mailEntity);
        Response response = Response.buildResponse(
                "SUCCESS",
                "Mail sent successfully",
                null,
                AppConstants.OK,
                "Request Processes Successfully"

        );
        return ResponseEntity.ok(response);
    }


    @PostMapping("/send-otp")
    public ResponseEntity<Response> sendOtp(@RequestParam String toPhoneNumber) {
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


    @PostMapping("/auth-login")
    public ResponseEntity<String> login(@RequestBody LoginDto request) {

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
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchUserExistException("User not Found"));

        String token = jwtTokenHelper.generateToken(userDetails);

        // 6. Return token and role in response
        return ResponseEntity.ok(token);
    }


}
