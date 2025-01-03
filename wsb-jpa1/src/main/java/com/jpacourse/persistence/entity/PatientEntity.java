package com.jpacourse.persistence.entity;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "PATIENT")
public class PatientEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "First name cannot be blank")
	@Size(max = 50, message = "First name cannot exceed 50 characters")
	@Column(nullable = false)
	private String firstName;

	@NotBlank(message = "Last name cannot be blank")
	@Size(max = 50, message = "Last name cannot exceed 50 characters")
	@Column(nullable = false)
	private String lastName;

	@NotBlank(message = "Telephone number cannot be blank")
	@Pattern(regexp = "\\+?[0-9]{7,15}", message = "Invalid telephone number format")
	@Column(nullable = false)
	private String telephoneNumber;

	@Email(message = "Invalid email format")
	@Size(max = 100, message = "Email cannot exceed 100 characters")
	private String email;

	@NotBlank(message = "Patient number cannot be blank")
	@Size(max = 5, message = "Patient number cannot exceed 5 characters")
	@Column(nullable = false)
	private String patientNumber;

	@NotNull(message = "Date of birth cannot be null")
	@Past(message = "Date of birth must be in the past")
	@Column(nullable = false)
	private LocalDate dateOfBirth;

	// One-to-one relationship with AddressEntity (two-way relation)
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "address_id")
	private AddressEntity address;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<VisitEntity> visits;

	// New non-String type variable
	@NotNull(message = "Weight cannot be null")
	private Double weight;

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

	public String getPatientNumber() {
		return patientNumber;
	}

	public void setPatientNumber(String patientNumber) {
		this.patientNumber = patientNumber;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public AddressEntity getAddress() {
		return this.address;
	}

	public void setAddress(AddressEntity address) {
		this.address = address;
	}

	public List<VisitEntity> getVisits() {
		return visits;
	}
	public void setVisits(List<VisitEntity> visits) {
		this.visits = visits;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
}