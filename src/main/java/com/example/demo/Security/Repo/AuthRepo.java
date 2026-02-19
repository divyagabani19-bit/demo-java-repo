package com.example.demo.Security.Repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Security.Model.AuthUser;
import java.util.Optional;



@Repository
public interface AuthRepo extends JpaRepository<AuthUser,Integer> { 

	 Optional<AuthUser> findByName(String name);
}
