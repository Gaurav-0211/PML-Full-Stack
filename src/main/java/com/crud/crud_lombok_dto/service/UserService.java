package com.crud.crud_lombok_dto.service;

import com.crud.crud_lombok_dto.dto.MailEntity;
import com.crud.crud_lombok_dto.dto.UserDto;
import com.crud.crud_lombok_dto.dto.UserResponse;
import java.util.List;

public interface UserService {

    // Create or Register User
    UserDto createUser(UserDto userDto);

    // Get All User without pagination
    List<UserDto> getAllUsers();

    // Get all user By Id
    UserDto getUserById(Long id);

    // Update Existing User
    UserDto updateUser(Long id, UserDto userDto);

    // Delete existing User
    void deleteUser(long id);

    // Validate User is genuine or not
    boolean validateUser(String email, String password);

    // Get all Post By Pagination and Sorting
    UserResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    // Get all user by Name (Full Name)
    List<UserDto> getAllUserByName(String name);

    // Get all user by recent updated or recent logged-in
    List<UserDto> getAllUserByUpdatedDate();

    // Get all user whose name start with ?
    List<UserDto> getAllNameStartWith(String name);

    // Get all user whose name end with ?
    List<UserDto> getAllNameEndWith(String name);

    // Get all user by name in descending order
    List<UserDto> getAllNameDesc();

    // Send email to any user
    public void sendEmail(MailEntity mailEntity);

    // Generate otp (6 Digit)
    public String generateOtp();

    // Send otp on Whatsapp using twilio
    public boolean sendOtp(String toNumber, String otp);

    // Reset Password
    //public String resetPassword(String email, String password);

    // Change Password
    public void changePassword(String email, String oldPassword, String newPassword);

    // Forgot Password
    public void forgotPassword(String email);

    // Verify OTP
    public void verifyOtpAddPassword(String email, String otp, String newPassword, String confirmPassword);




}
