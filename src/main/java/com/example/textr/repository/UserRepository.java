package com.example.textr.repository;

import com.example.textr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "select * from tb_users tu where tu.email = :email",nativeQuery = true)
    public User getUserByEmail(@Param("email") String email);

    @Query(value = "select * from tb_users tu where tu.id  = :id", nativeQuery = true)
    public User getUserById(@Param("id") Long id);

    @Query(value = "select * from tb_users tu \n" +
            "where tu.id in (select distinct tm.fk_receiver_id  \n" +
            "from tb_messages tm \n" +
            "where tm.fk_sender_id = :id \n" +
            "and tm.fk_receiver_id is not NULL\n" +
            ")\n", nativeQuery = true)
    public List<User> getAllTextedUsers(@Param("id") Long id);
}
