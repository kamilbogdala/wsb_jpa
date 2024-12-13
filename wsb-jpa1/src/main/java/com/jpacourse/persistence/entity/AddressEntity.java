package com.jpacourse.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "City cannot be blank")
	@Size(max = 50, message = "City cannot exceed 50 characters")
	private String city;

	@NotBlank(message = "Address Line 1 cannot be blank")
	@Size(max = 80, message = "Address Line 1 cannot exceed 80 characters")
	private String addressLine1;

	@Size(max = 80, message = "Address Line 2 cannot exceed 80 characters")
	private String addressLine2;

	@NotBlank(message = "Postal code cannot be blank")
	@Size(max = 6, message = "Postal code cannot exceed 6 characters")
	private String postalCode;

	// One-to-one relationship with DoctorEntity (two-way relation)
	@OneToOne(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private DoctorEntity doctor;

	// One-to-one relationship with PatientEntity (two-way relation)
	@OneToOne(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private PatientEntity patient;

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}
