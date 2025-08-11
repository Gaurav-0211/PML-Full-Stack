package com.crud.crud_lombok_dto.service.impl;

import com.crud.crud_lombok_dto.dto.MailEntity;
import com.crud.crud_lombok_dto.dto.UserDto;
import com.crud.crud_lombok_dto.dto.UserResponse;
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

    public UserDto createUser(UserDto userDto) {

        if(this.repository.existsByEmail(userDto.getEmail())){
            throw new UserAlreadyExistException("User already exist with email : "+userDto.getEmail());
        }
        log.info("Impl create User called ");

        User user = this.mapper.map(userDto,User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findById(userDto.getRoleId()).orElseThrow(() -> new NoSuchUserExistException("No Role Found "));
        user.setRole(role);

        this.repository.save(user);
        return this.mapper.map(user,UserDto.class);

    }

    public List<UserDto> getAllUsers() {
        List<User> users =  this.repository.findAll();
        if(users.isEmpty()){
            throw new NoUserExist("User List is empty No user has registered yet!");
        }
        log.info("Get all user in Impl");
        return users.stream().map((user)->this.mapper.map(user, UserDto.class)).collect(Collectors.toList());

    }

    public UserDto getUserById(Long id) {
        User user =  this.repository.findById(id)
                        .orElseThrow(() -> new NoSuchUserExistException("User not exist with id :"+id));

        log.info("get user by id in Impl");
        return this.mapper.map(user, UserDto.class);
    }

    public UserDto updateUser(Long id, UserDto userDetails) {
        User user = this.repository.findById(id)
                .orElseThrow(()->new NoSuchUserExistException("No user exist with id :"+id));
        log.info("Update user in Impl");
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        this.repository.save(user);
        return this.mapper.map(user,UserDto.class);
    }
    //@Scheduled(cron = "*/5 * * * *")
    //@Scheduled(cron = "0 * * * * *")
    public void deleteUser(long id) {

        User user = this.repository.findById(id)
                .orElseThrow(()->new NoSuchUserExistException("No User exist with id :"+id));
        log.info("Delete use in Impl");
        this.repository.delete(user);
    }

    @Override
    public boolean validateUser(String email, String password) {

        Optional<User> optionalUser = repository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return false;
        }
        User user = optionalUser.get();
        return user.getPassword().equals(password);
    }

    @Override
    public UserResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
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

        return UserResponse.builder()
                .content(userDtos)
                .pageNumber(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPage(userPage.getTotalPages())
                .lastPage(userPage.isLast())
                .build();
    }


    @Override
    public List<UserDto> getAllUserByName(String name) {
        List<User> users = this.repository.findByName(name);
        if (users.isEmpty()){
            throw  new NoSuchUserExistException("No user exist with name :"+name);
        }
        return users.stream().map((user)->this.mapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllUserByUpdatedDate() {
        List<User> users = this.repository.findAllByOrderByUpdatedAtDesc();
        if(users.isEmpty()){
            throw new NoUserExist("No user exist List is empty no user has registered yet !");
        }
        return users.stream().map((user)->this.mapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllNameStartWith(String name) {
        List<User> users = this.repository.findByNameStartsWith(name);
        if(users.isEmpty()){
            throw new NoSuchUserExistException("No user exist with name start with :"+name);
        }
        return users.stream().map((user)->this.mapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllNameEndWith(String name) {
        List<User> users = this.repository.findByNameEndingWith(name);
        if(users.isEmpty()){
            throw new NoSuchUserExistException("No user exist with name end with :"+name);
        }
        return users.stream().map((user)-> this.mapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllNameDesc() {
        List<User> users = this.repository.findAllByOrderByNameDesc();
        if(users.isEmpty()){
            throw new NoUserExist("User List is Empty No user has registered yet!");
        }
        return users.stream().map((user)-> this.mapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    @Value("$(spring.mail.username)")
    private String fromEmailId;


    @Override
    public void sendEmail(MailEntity mailEntity){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmailId);
        mailMessage.setTo(mailEntity.getRecipient());
        mailMessage.setText(mailEntity.getBody());
        mailMessage.setSubject(mailEntity.getSubject());

        javaMailSender.send(mailMessage);
    }

    @Value("${twilio.from.number}")
    private String fromNumber;

    public String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    public boolean sendOtp(String toNumber, String otp) {
        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + toNumber), // recipient
                    new PhoneNumber(fromNumber),   // Sender
                    "Your OTP is: " + otp + " (valid for 5 minutes)"
            ).create();

            System.out.println("Message SID: " + message.getSid());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username)
                .orElseThrow(() -> new NoSuchUserExistException("User Not found with given Email Id"));

        String roleName = "ROLE_" + user.getRole().getName().name(); // Properly formatted

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(roleName))
        );
    }
}

