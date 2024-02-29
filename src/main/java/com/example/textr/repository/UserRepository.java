package com.example.textr.repository;

import com.example.textr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "select * from tb_users tu where tu.email = :email",nativeQuery = true)
    public User getUserByEmail(String email);

    @Query(value = "select * from tb_users tu where tu.id  = :id", nativeQuery = true)
    public User getUserById(Long id);
}
