package com.jendo.app.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User update request")
public class UserUpdateDto {

    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Email(message = "Email must be valid")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Schema(description = "User's phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "User's date of birth", example = "1990-01-15")
    private LocalDate dateOfBirth;

    @Size(max = 20, message = "Gender must not exceed 20 characters")
    @Schema(description = "User's gender", example = "Male")
    private String gender;

    @Schema(description = "URL to user's profile image", example = "https://example.com/profile.jpg")
    private String profileImage;

    @Size(max = 100, message = "Nationality must not exceed 100 characters")
    @Schema(description = "User's nationality", example = "American")
    private String nationality;

    @Schema(description = "User's address", example = "123 Main St, City, Country")
    private String address;

    @Schema(description = "User's weight in kg", example = "70.5")
    private Double weight;

    @Schema(description = "User's height in cm", example = "175.0")
    private Double height;

    @Schema(description = "List of role names for the user", example = "[\"USER\", \"PATIENT\"]")
    private List<String> roles;

    private boolean phoneSet;
    private boolean dateOfBirthSet;
    private boolean genderSet;
    private boolean nationalitySet;
    private boolean addressSet;
    private boolean weightSet;
    private boolean heightSet;

    @JsonSetter(value = "phone", nulls = Nulls.SET)
    public void setPhone(String phone) {
        this.phone = phone;
        this.phoneSet = true;
    }

    @JsonSetter(value = "dateOfBirth", nulls = Nulls.SET)
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.dateOfBirthSet = true;
    }

    @JsonSetter(value = "gender", nulls = Nulls.SET)
    public void setGender(String gender) {
        this.gender = gender;
        this.genderSet = true;
    }

    @JsonSetter(value = "nationality", nulls = Nulls.SET)
    public void setNationality(String nationality) {
        this.nationality = nationality;
        this.nationalitySet = true;
    }

    @JsonSetter(value = "address", nulls = Nulls.SET)
    public void setAddress(String address) {
        this.address = address;
        this.addressSet = true;
    }

    @JsonSetter(value = "weight", nulls = Nulls.SET)
    public void setWeight(Double weight) {
        this.weight = weight;
        this.weightSet = true;
    }

    @JsonSetter(value = "height", nulls = Nulls.SET)
    public void setHeight(Double height) {
        this.height = height;
        this.heightSet = true;
    }
}
