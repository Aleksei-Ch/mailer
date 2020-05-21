package io.mailer.rest.data.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.mailer.rest.data.ApiKey;
import io.mailer.rest.data.ApiKey.Enabled;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long>{
    List<ApiKey> findByApiKeyAndEnabled(String apiKey, Enabled enabled);
}