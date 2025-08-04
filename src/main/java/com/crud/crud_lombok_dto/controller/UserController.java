package com.crud.crud_lombok_dto.controller;
import com.crud.crud_lombok_dto.config.AppConstants;
import com.crud.crud_lombok_dto.dto.LoginRequest;
import com.crud.crud_lombok_dto.dto.UserDto;
import com.crud.crud_lombok_dto.dto.UserResponse;
import com.crud.crud_lombok_dto.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {
            return new ResponseEntity<>(service.createUser(userdto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login API called");

        boolean isValid = service.validateUser(loginRequest.getEmail(), loginRequest.getPassword());

        if (isValid) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            List<UserDto> dto = this.service.getAllUsers();
            log.info("Get all Called in Controller");
            return new ResponseEntity<List<UserDto>>(dto, HttpStatus.OK);
        }catch (Exception e){
            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            UserDto dto = service.getUserById(id);
            log.info("Get by Id called in controller");
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto user) {
        try {
            UserDto userDto = service.updateUser(id,user);
            log.info("Update Put called in controller");
            return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            service.deleteUser(id);
            log.info("Delete called in controller");
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<UserDto>> getByDate(){
        log.info("Get By Date API in controller");
        List<UserDto> allUser = this.service.getAllUserByUpdatedDate();
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    @GetMapping("/by-start-name")
    public ResponseEntity<List<UserDto>> getByNameStart(@RequestParam String name){
        log.info("Get By Name Api in controller");
        List<UserDto> allUser = this.service.getAllNameStartWith(name);
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }


}
