package com.sbasly.urlshortener.repositories;

import com.sbasly.urlshortener.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
	Optional<Url> findByShortened(String shortened);
	Optional<Url> findByOriginal(String original);
}
