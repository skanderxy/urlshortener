package com.sbasly.urlshortener.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "url")
public class Url {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@URL(message = "must be a valid URL")
	@NotBlank(message = "URL cannot be empty")
	@Column(nullable = false, length = 2048, unique = true)
	private String original;

	@Column(nullable = false, length = 10, unique = true)
	private String shortened;

	@Column(nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
}
