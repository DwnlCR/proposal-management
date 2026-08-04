package br.com.dwnl.proposalmanagement.auth.infrastructure.persistence.repository;

import br.com.dwnl.proposalmanagement.auth.infrastructure.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
}
