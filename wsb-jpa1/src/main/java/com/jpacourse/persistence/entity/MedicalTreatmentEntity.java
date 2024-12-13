package com.jpacourse.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.jpacourse.persistence.enums.TreatmentType;

@Entity
@Table(name = "MEDICAL_TREATMENT")
public class MedicalTreatmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Description cannot be blank")
	@Size(max = 255, message = "Description cannot exceed 255 characters")
	private String description;

	@NotNull(message = "Treatment type cannot be null")
	@Enumerated(EnumType.STRING)
	private TreatmentType type;

	// Many-to-one relationship with VisitEntity (one-way relation from children)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "visit_id")
	private VisitEntity visit;

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TreatmentType getType() {
		return type;
	}

	public void setType(TreatmentType type) {
		this.type = type;
	}

	public VisitEntity getVisit() {
		return this.visit;
	}

	public void setVisit(VisitEntity visit) {
		this.visit = visit;
	}
}
