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

		// token.principalにUsername、token.credentialsにpasswordがセットされるだけで、tokenのauthenticatedはfalse、authoritiesはEmpty、detailsはnull。
		//UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
	/*	String compCode = "SuperUser";
		if (loginRequest.getCompanyCode() != null) {
			compCode = loginRequest.getCompanyCode().trim();
		}*/
		String emailCompanyCode = String.format("%s%s%s", loginRequest.getEmail().trim(), String.valueOf(Character.LINE_SEPARATOR),loginRequest.getCompanyCode().trim());
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(emailCompanyCode, loginRequest.getPassword());
		// authenticationManager.authenticate(token)を実行すると
		// UserDetailsService.loadUserByUsername()を実装した
		// UserDetailsServiceImpl.loadUserByUsername()が呼び出され、loginRequestのUsernameでDBを検索する。
		// loginRequestのUsernameとPasswordがDBの値と一致していたら、authenticationManager.authenticate(token)の次のステップに進むが、
		// 不一致ならauthenticationManager.authenticateで実行される
		// 	org.springframework.security.authentication.dao.DaoAuthenticationProvider.additionalAuthenticationChecks(DaoAuthenticationProvider.java:93)
		// で、org.springframework.security.authentication.BadCredentialsExceptionがスローされる。
		Authentication authentication = null;
		ImplementsUserDetails userDetails = null;
		String jwt = null;
		List<String> authorities = null;
		try {
			authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// 引数のAuthenticationからJwt（トークン）を取得する
			jwt = jwtUtils.generateJwtToken(authentication);

			userDetails = (ImplementsUserDetails) authentication.getPrincipal();
			authorities = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());

		/*	if (authentication.isAuthenticated()) {
				System.out.println("company code " + loginRequest.getCompanyCode());
				if (loginRequest.getCompanyCode()==null) { //super admin
					if (!authorities.stream().anyMatch(str -> str.trim().contains("Auth_1")) ) {
						throw new Exception("メールアドレス、またはパスワードが不正です。");
					}
				}else {
					if (!( authorities.stream().anyMatch(str -> str.trim().contains("Auth_2")) || authorities.stream().anyMatch(str -> str.trim().contains("Auth_3")) ) ) {
						throw new Exception("メールアドレス、またはパスワードが不正です。");
					}
				}
			}*/
		} catch (BadCredentialsException badCreEx) {
			logger.info("authenticate: {}", badCreEx.getMessage());
			//// throw すると AuthEntryPointJwt.commenceメソッドでエラーを返す。
			// throw(badCreEx);
			// JSONで返すエラー構造を他のエラーと同じにする。
			ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setStatus(new ErrorStatus(HttpStatus.UNAUTHORIZED.name(), "メールアドレス、またはパスワードが不正です。"));
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);	// NOT_ACCEPTABLE:401
			// 上記処理は次の処理と同じ内容を返す。
			// return new ResponseEntity<String>("{\"status\":{\"code\":\"UNAUTHORIZED\",\"message\":\"ユーザ名、またはパスワードが不正です。\"}}", HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			logger.info("authenticate: {}", ex);
			//throw e;
			// Response.createErrorResponseメソッドで、code:ex.getClass().getSimpleName()、message: ex.getMessage()がセットされる。
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

		return ResponseEntity.ok(new MessageResponse("ユーザ登録済！"));
	}
*/
}
