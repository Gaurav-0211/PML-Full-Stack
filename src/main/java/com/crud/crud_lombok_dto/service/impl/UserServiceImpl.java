package com.crud.crud_lombok_dto.service.impl;

import com.crud.crud_lombok_dto.dto.MailEntity;
import com.crud.crud_lombok_dto.dto.UserDto;
import com.crud.crud_lombok_dto.dto.UserResponse;
import com.crud.crud_lombok_dto.exception.InvalidPasswordException;
import com.crud.crud_lombok_dto.exception.NoSuchUserExistException;
import com.crud.crud_lombok_dto.exception.NoUserExist;
import com.crud.crud_lombok_dto.exception.UserAlreadyExistException;
import com.crud.crud_lombok_dto.model.Role;
import com.crud.crud_lombok_dto.model.User;
import com.crud.crud_lombok_dto.repository.RoleRepository;
import com.crud.crud_lombok_dto.repository.UserRepository;
import com.crud.crud_lombok_dto.service.UserService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
@Slf4j
@Service
@Validated
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder; // to encode the password

    @Autowired
    private RoleRepository roleRepository;

    // Application Start here Register for the first time
    public UserDto createUser(UserDto userDto) {

        log.info("User Impl Create User method start");
        if (this.repository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistException("User already exist with email : " + userDto.getEmail());
        }


        User user = this.mapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findById(userDto.getRoleId()).orElseThrow(() -> new NoSuchUserExistException("No Role Found "));
        user.setRole(role);

        this.repository.save(user);
        log.info("User Impl Create User method end and user registered");
        return this.mapper.map(user, UserDto.class);

    }

    // Get ALL User without paging Api
    public List<UserDto> getAllUsers() {
        log.info("Get all user in Impl");
        List<User> users = this.repository.findAll();
        if (users.isEmpty()) {
            throw new NoUserExist("User List is empty No user has registered yet!");
        }
        log.info("Get all user in Impl executed");
        return users.stream().map((user) -> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());

    }

    // Get User By Id API
    public UserDto getUserById(Long id) {
        log.info("get user by id in Impl");
        User user = this.repository.findById(id)
                .orElseThrow(() -> new NoSuchUserExistException("User not exist with id :" + id));

        log.info("get user by id in Impl executed");
        return this.mapper.map(user, UserDto.class);
    }

    // Update Existing user api
    public UserDto updateUser(Long id, UserDto userDetails) {
        log.info("Update user in Impl");
        User user = this.repository.findById(id)
                .orElseThrow(() -> new NoSuchUserExistException("No user exist with id :" + id));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        this.repository.save(user);
        log.info("Update user in Impl executed");
        return this.mapper.map(user, UserDto.class);
    }

    // Cron-Scheduling Implemented here auto user get deleted at particular schedule time
    //@Scheduled(cron = "*/5 * * * *")
    //@Scheduled(cron = "0 * * * * *")
    public void deleteUser(long id) {

        log.info("Delete use in Impl Cron- Scheduling api");
        User user = this.repository.findById(id)
                .orElseThrow(() -> new NoSuchUserExistException("No User exist with id :" + id));
        log.info("Delete use in Impl user deleted Cron - Scheduling Api");
        this.repository.delete(user);
    }

    // Validate User while login from mail and password
    @Override
    public boolean validateUser(String email, String password) {

        log.info("Validate user in Impl");
        User user = repository.findByEmail(email);
        if (user == null) {
            return false;
        }

        return user.getPassword().equals(password);
    }

    // Get all post by paging api implemented
    @Override
    public UserResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("get All Post in User Impl start - Paging");
        Sort sort = sortDir != null && sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> userPage = repository.findAll(pageable);

        List<UserDto> userDtos = userPage
                .getContent()
                .stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        log.info("get-All-Post in User Impl end page based user fetched");
        return UserResponse.builder()
                .content(userDtos)
                .pageNumber(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPage(userPage.getTotalPages())
                .lastPage(userPage.isLast())
                .build();
    }


    // Get all user by name api implemented
    @Override
    public List<UserDto> getAllUserByName(String name) {
        log.info("get All User By Name in Impl");
        List<User> users = this.repository.findByName(name);
        if (users.isEmpty()) {
            throw new NoSuchUserExistException("No user exist with name :" + name);
        }
        log.info("Get all user by name in Impl Executed");
        return users.stream().map((user) -> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    // Get all user by updated date api implemented
    @Override
    public List<UserDto> getAllUserByUpdatedDate() {
        log.info("get All User By Updated Date in Impl");
        List<User> users = this.repository.findAllByOrderByUpdatedAtDesc();
        if (users.isEmpty()) {
            throw new NoUserExist("No user exist List is empty no user has registered yet !");
        }
        log.info("get All User By Updated Date in Impl Executed");
        return users.stream().map((user) -> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    // Get all UserName start with implemented
    @Override
    public List<UserDto> getAllNameStartWith(String name) {
        log.info("get All User By Name Start With in Impl");
        List<User> users = this.repository.findByNameStartsWith(name);
        if (users.isEmpty()) {
            throw new NoSuchUserExistException("No user exist with name start with :" + name);
        }
        log.info("get All User By Name Start With in Impl Executed");
        return users.stream().map((user) -> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    // Gat all username ends with implemented
    @Override
    public List<UserDto> getAllNameEndWith(String name) {
        log.info("get All User By Name End With in Impl");
        List<User> users = this.repository.findByNameEndingWith(name);
        if (users.isEmpty()) {
            throw new NoSuchUserExistException("No user exist with name end with :" + name);
        }
        log.info("get All User By Name End With in Impl Executed");
        return users.stream().map((user) -> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    // Get all user by Name in descending Order api
    @Override
    public List<UserDto> getAllNameDesc() {
        log.info("get All User By Name Desc in Impl");
        List<User> users = this.repository.findAllByOrderByNameDesc();
        if (users.isEmpty()) {
            throw new NoUserExist("User List is Empty No user has registered yet!");
        }
        log.info("get All User By Name Desc in Impl Executed");
        return users.stream().map((user) -> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    // Validate maile annotation for sending email
    @Value("$(spring.mail.username)")
    private String fromEmailId;


    // Sending email api
    @Override
    public void sendEmail(MailEntity mailEntity) {
        log.info("send Email in Impl");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmailId);
        mailMessage.setTo(mailEntity.getRecipient());
        mailMessage.setText(mailEntity.getBody());
        mailMessage.setSubject(mailEntity.getSubject());
        log.info("Send mail in Impl executed and mail sent successful");

        javaMailSender.send(mailMessage);
    }

    // Validating phone Number for sending otp on Whatsapp
    @Value("${twilio.from.number}")
    private String fromNumber;

    // Method to generate OTP
    public String generateOtp() {
        log.info("Generate OTP in Impl");
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    // Method to send OTP
    public boolean sendOtp(String toNumber, String otp) {
        log.info("Sent Otp method call in Impl");
        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + toNumber), // recipient
                    new PhoneNumber(fromNumber),   // Sender
                    "Your OTP is: " + otp + " (valid for 5 minutes)"
            ).create();

            System.out.println("Message SID: " + message.getSid());
            log.info("Otp sent successful in Impl");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    // Changing password
    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = this.repository.findByEmail(email);
        if (user == null) {
            throw new NoSuchUserExistException("No user exist with email :" + email);
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Invalid old password");}
        if (oldPassword.equals(newPassword)) {
            throw new InvalidPasswordException("New password cannot be same as old password");
        }
            user.setPassword(passwordEncoder.encode(newPassword));
            this.repository.save(user);
            log.info("Password changed successful in Impl");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("load User By Username in Impl");

        User user1 = repository.findByEmail(username);
        if (user1 == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        String roleName = "ROLE_" + user1.getRole().getName();

        log.info("Role assigned: {}", roleName);

        return new org.springframework.security.core.userdetails.User(
                user1.getEmail(),
                user1.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(roleName))
        );
    }
}
