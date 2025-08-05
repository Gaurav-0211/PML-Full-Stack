package com.crud.crud_lombok_dto.service.impl;

import com.crud.crud_lombok_dto.dto.MailEntity;
import com.crud.crud_lombok_dto.dto.UserDto;
import com.crud.crud_lombok_dto.dto.UserResponse;
import com.crud.crud_lombok_dto.model.User;
import com.crud.crud_lombok_dto.repository.UserRepository;
import com.crud.crud_lombok_dto.service.UserService;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
@Validated
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JavaMailSender javaMailSender;

    //public int x = 20;


    public UserDto createUser(UserDto userDto) {

        if(this.repository.existsByEmail(userDto.getEmail())){
            throw new RuntimeException("Email already exist!");
        }
        log.info("Impl create User called ");

        User user = this.mapper.map(userDto,User.class);

        // Optional if applicable
        this.repository.save(user);
        UserDto userDto1 = this.mapper.map(user,UserDto.class);

        return userDto1;
    }

    public List<UserDto> getAllUsers() {
        List<User> users =  this.repository.findAll();
        log.info("Get all user in Impl");
        return users.stream().map((user)->this.mapper.map(user, UserDto.class)).collect(Collectors.toList());

    }

    public UserDto getUserById(Long id) {
        User user =  repository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not exist"));
        log.info("get user by id in Impl");
        return this.mapper.map(user, UserDto.class);
    }

    public UserDto updateUser(Long id, UserDto userDetails) {
        User user = this.repository.findById(id)
                .orElseThrow(()->new RuntimeException("User Not found "));
        log.info("Update user in Impl");
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return this.mapper.map(user,UserDto.class);
    }
    //@Scheduled(cron = "*/5 * * * *")
    //@Scheduled(cron = "0 * * * * *")
    public void deleteUser(long id) {

        User user = this.repository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));
        log.info("Delete use in Impl");
        this.repository.delete(user);
        //x = x-1;
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

        Sort sort = null;
        if(sortDir.equals("asc")){
            sort = Sort.by(sortBy).ascending();
        }else{
            sort = Sort.by(sortBy).descending();
        }

        Pageable p = PageRequest.of(pageNumber, pageSize, sort); // Asc or Desc choose based on requirement
        Page<User> userPost = this.repository.findAll(p);
        List<User> allUsers = userPost.getContent();
        List<UserDto> users = allUsers.stream().map((user)->this.mapper.map(user,UserDto.class)).collect(Collectors.toList());
        UserResponse userResponse = new UserResponse();
        userResponse.setTotalElements(userPost.getTotalElements());
        userResponse.setTotalPage(userPost.getTotalPages());
        userResponse.setLastPage(userPost.isLast());
        userResponse.setContent(users);
        userResponse.setPageNumber(userPost.getNumber());
        userResponse.setPageSize(userPost.getSize());

        return userResponse;
    }

    @Override
    public List<UserDto> getAllUserByName(String name) {
        List<User> users = this.repository.findByName(name);
        if (users.isEmpty()){
            throw  new RuntimeException("User not found with given name");
        }
        return users.stream().map((user)->this.mapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllUserByUpdatedDate() {
        List<User> users = this.repository.findAllByOrderByUpdatedAtDesc();
        if(users.isEmpty()){
            throw new RuntimeException("User not found at given date and time");
        }
        return users.stream().map((user)->this.mapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllNameStartWith(String name) {
        List<User> users = this.repository.findByNameStartsWith(name);
        if(users.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return users.stream().map((user)->this.mapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllNameEndWith(String name) {
        List<User> users = this.repository.findByNameEndingWith(name);
        if(users.isEmpty()){
            throw new RuntimeException("User Not found");
        }
        return users.stream().map((user)-> this.mapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllNameDesc() {
        List<User> users = this.repository.findAllByOrderByNameDesc();
        if(users.isEmpty()){
            throw new RuntimeException("Name not sorted");
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

}

