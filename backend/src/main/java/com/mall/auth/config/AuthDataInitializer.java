package com.mall.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mall.auth.entity.Role;
import com.mall.auth.entity.User;
import com.mall.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthDataInitializer implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${auth.default-admin-full-name}")
	private String defaultAdminFullName;

	@Value("${auth.default-admin-email}")
	private String defaultAdminEmail;

	@Value("${auth.default-admin-password}")
	private String defaultAdminPassword;

	@Override
	public void run(String... args) {
		if (defaultAdminEmail == null || defaultAdminEmail.isBlank() || userRepository.existsByEmailIgnoreCase(defaultAdminEmail)) {
			return;
		}
		String[] names = defaultAdminFullName == null ? new String[0] : defaultAdminFullName.trim().split("\\s+", 2);
		String firstName = names.length > 0 ? names[0] : "Admin";
		String lastName = names.length > 1 ? names[1] : "User";

		User admin = User.builder()
				.firstName(firstName)
				.lastName(lastName)
				.email(defaultAdminEmail)
				.password(passwordEncoder.encode(defaultAdminPassword))
				.role(Role.ROLE_ADMIN)
				.isActive(true)
				.build();
		userRepository.save(admin);
	}
}