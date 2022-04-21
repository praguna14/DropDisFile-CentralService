package com.CS6650.CentralManagementService.respository;

import com.CS6650.CentralManagementService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
