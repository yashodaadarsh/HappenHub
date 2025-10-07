package org.adarsh.repository;

import org.adarsh.entities.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData,String> {
}
