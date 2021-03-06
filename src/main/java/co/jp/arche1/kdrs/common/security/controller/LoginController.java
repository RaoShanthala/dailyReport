package co.jp.arche1.kdrs.common.security.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.jp.arche1.kdrs.common.security.ImplementsUserDetails;
import co.jp.arche1.kdrs.common.security.JwtUtils;
import co.jp.arche1.kdrs.common.security.dto.ErrorResponse;
import co.jp.arche1.kdrs.common.security.dto.ErrorStatus;
import co.jp.arche1.kdrs.common.security.dto.JwtResponseDto;
import co.jp.arche1.kdrs.common.security.dto.LoginRequestDto;
import co.jp.arche1.kdrs.common.security.dto.Response;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	//@Autowired
	//PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		//
		//Authentication authentication = authenticationManager.authenticate(
		//		new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		// token.principal???Username???token.credentials???password?????????????????????????????????token???authenticated???false???authorities???Empty???details???null???
		//UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
	/*	String compCode = "SuperUser";
		if (loginRequest.getCompanyCode() != null) {
			compCode = loginRequest.getCompanyCode().trim();
		}*/
		String emailCompanyCode = String.format("%s%s%s", loginRequest.getEmail().trim(), String.valueOf(Character.LINE_SEPARATOR),loginRequest.getCompanyCode().trim());
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(emailCompanyCode, loginRequest.getPassword());
		// authenticationManager.authenticate(token)??????????????????
		// UserDetailsService.loadUserByUsername()???????????????
		// UserDetailsServiceImpl.loadUserByUsername()?????????????????????loginRequest???Username???DB??????????????????
		// loginRequest???Username???Password???DB?????????????????????????????????authenticationManager.authenticate(token)????????????????????????????????????
		// ???????????????authenticationManager.authenticate??????????????????
		// 	org.springframework.security.authentication.dao.DaoAuthenticationProvider.additionalAuthenticationChecks(DaoAuthenticationProvider.java:93)
		// ??????org.springframework.security.authentication.BadCredentialsException????????????????????????
		Authentication authentication = null;
		ImplementsUserDetails userDetails = null;
		String jwt = null;
		List<String> authorities = null;
		try {
			authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// ?????????Authentication??????Jwt?????????????????????????????????
			jwt = jwtUtils.generateJwtToken(authentication);

			userDetails = (ImplementsUserDetails) authentication.getPrincipal();
			authorities = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());

		/*	if (authentication.isAuthenticated()) {
				System.out.println("company code " + loginRequest.getCompanyCode());
				if (loginRequest.getCompanyCode()==null) { //super admin
					if (!authorities.stream().anyMatch(str -> str.trim().contains("Auth_1")) ) {
						throw new Exception("??????????????????????????????????????????????????????????????????");
					}
				}else {
					if (!( authorities.stream().anyMatch(str -> str.trim().contains("Auth_2")) || authorities.stream().anyMatch(str -> str.trim().contains("Auth_3")) ) ) {
						throw new Exception("??????????????????????????????????????????????????????????????????");
					}
				}
			}*/
		} catch (BadCredentialsException badCreEx) {
			logger.info("authenticate: {}", badCreEx.getMessage());
			//// throw ????????? AuthEntryPointJwt.commence????????????????????????????????????
			// throw(badCreEx);
			// JSON???????????????????????????????????????????????????????????????
			ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setStatus(new ErrorStatus(HttpStatus.UNAUTHORIZED.name(), "??????????????????????????????????????????????????????????????????"));
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);	// NOT_ACCEPTABLE:401
			// ??????????????????????????????????????????????????????
			// return new ResponseEntity<String>("{\"status\":{\"code\":\"UNAUTHORIZED\",\"message\":\"?????????????????????????????????????????????????????????\"}}", HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			logger.info("authenticate: {}", ex);
			//throw e;
			// Response.createErrorResponse??????????????????code:ex.getClass().getSimpleName()???message: ex.getMessage()????????????????????????
			ErrorResponse errResponse = Response.createErrorResponse(ex);
			return new ResponseEntity<ErrorResponse>(errResponse, HttpStatus.NOT_ACCEPTABLE);	// NOT_ACCEPTABLE:406
		}


		return ResponseEntity.ok(new JwtResponseDto(jwt,
												 userDetails.getId(),
												 userDetails.getUsername(),
												 userDetails.getCompanyCode(),
												 userDetails.getCompanyId(),
												 authorities));
	}
/*
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("?????????????????????"));
	}
*/
}
