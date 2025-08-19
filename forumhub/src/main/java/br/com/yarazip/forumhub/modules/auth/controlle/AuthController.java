package br.com.yarazip.forumhub.modules.auth.controlle;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.yarazip.forumhub.modules.auth.controller.dto.LoginRequestDTO;
import br.com.yarazip.forumhub.modules.auth.usecase.LoginUseCase;
import br.com.yarazip.forumhub.modules.auth.usecase.LogoutUseCase;
import br.com.yarazip.forumhub.modules.auth.usecase.SignUpUseCase;
import br.com.yarazip.forumhub.modules.user.controller.dto.UserCreateRequestDTO;
import br.com.yarazip.forumhub.modules.user.controller.dto.UserDetailsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	private final LoginUseCase loginUseCase;
	private final SignUpUseCase signUpUseCase;
	private final LogoutUseCase logoutUseCase;

	public AuthController(LoginUseCase loginUseCase, SignUpUseCase signUpUseCase, LogoutUseCase logoutUseCase) {
		this.loginUseCase = loginUseCase;
		this.signUpUseCase = signUpUseCase;
		this.logoutUseCase = logoutUseCase;
	}

	@PostMapping("/login")
	@Operation(summary = "Login", description = "Authenticate a user and return a JWT token")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User logged in successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request data"),
			@ApiResponse(responseCode = "401", description = "Invalid credentials") })
	public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO requestDTO, HttpServletResponse response) {
		response.addCookie(loginUseCase.execute(requestDTO));

		logger.info("User {} logged in successfully", requestDTO.username());
		return ResponseEntity.ok("User logged in successfully");
	}

	@PostMapping("/signup")
	@Operation(summary = "Sign Up", description = "Register a new user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User registered successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request data"),
			@ApiResponse(responseCode = "409", description = "Username already exists") })
	public ResponseEntity<UserDetailsResponseDTO> signUp(@Valid @RequestBody UserCreateRequestDTO signUpRequest,
			HttpServletResponse response) {
		Map<String, Object> result = signUpUseCase.execute(signUpRequest);
		response.addCookie((Cookie) result.get("cookie"));

		logger.info("User {} registered successfully", signUpRequest.username());
		return ResponseEntity.ok((UserDetailsResponseDTO) result.get("user"));
	}

	@PostMapping("/logout")
	@Operation(summary = "Logout", description = "Invalidate the user's JWT token")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User logged out successfully") })
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		response.addCookie(logoutUseCase.execute());

		logger.info("User logged out successfully");
		return ResponseEntity.ok().build();
	}
}
