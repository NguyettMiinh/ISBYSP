package com.example.identity_final.Repository;

import com.example.identity_final.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //jpa se tu dong generate 1 cau query kiem tra su ton tai cua username
    boolean existsByUsername(String username);
}
