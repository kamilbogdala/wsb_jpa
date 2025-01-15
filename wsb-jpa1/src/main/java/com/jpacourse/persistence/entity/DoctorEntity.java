package com.jpacourse.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.jpacourse.persistence.enums.Specialization;

@Entity
@Table(name = "DOCTOR")
public class DoctorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "First name cannot be blank")
	@Size(max = 50, message = "First name cannot exceed 50 characters")
	private String firstName;

	@NotBlank(message = "Last name cannot be blank")
	@Size(max = 50, message = "Last name cannot exceed 50 characters")
	private String lastName;

	@NotBlank(message = "Telephone number cannot be blank")
	@Pattern(regexp = "\\+?[0-9]{7,15}", message = "Telephone number must be a valid number (7-15 digits)")
	private String telephoneNumber;

	@Email(message = "Email must be valid")
	@Size(max = 100, message = "Email cannot exceed 100 characters")
	private String email;

	@NotBlank(message = "Doctor number cannot be blank")
	@Size(max = 5, message = "Doctor number cannot exceed 5 characters")
	private String doctorNumber;

	@NotNull(message = "Specialization cannot be null")
	@Enumerated(EnumType.STRING)
	private Specialization specialization;

	// One-to-one relationship with AddressEntity (two-way relation)
	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "address_id")
	private AddressEntity address;

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDoctorNumber() {
		return doctorNumber;
	}

	public void setDoctorNumber(String doctorNumber) {
		this.doctorNumber = doctorNumber;
	}

	public Specialization getSpecialization() {
		return specialization;
	}

	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

	public AddressEntity getAddress() {
		return this.address;
	}

	public void setAddress(AddressEntity address) {
		this.address = address;
	}
}
