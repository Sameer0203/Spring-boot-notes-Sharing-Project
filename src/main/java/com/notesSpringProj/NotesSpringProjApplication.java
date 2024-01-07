package com.notesSpringProj;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.notesSpringProj.bean.Role;
import com.notesSpringProj.config.AppConstants;
import com.notesSpringProj.repository.RoleRepo;

@SpringBootApplication
@EnableAspectJAutoProxy
public class NotesSpringProjApplication implements CommandLineRunner{

	@Autowired
	private RoleRepo roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(NotesSpringProjApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role role = new Role(AppConstants.NORMAL_USER, "NORMAL_USER");
			Role role1 = new Role(AppConstants.ADMIN_USER, "ADMIN_USER");
			
			List<Role> roles = List.of(role, role1);
			List<Role> result = this.roleRepo.saveAll(roles);
	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
