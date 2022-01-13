package co.jp.arche1.kdrs.common.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import co.jp.arche1.kdrs.common.security.AuthEntryPointJwt;
import co.jp.arche1.kdrs.common.security.AuthTokenFilter;
//import co.jp.arche1.kpms.common.security.service.ImplementsUserDetailsService;
import co.jp.arche1.kdrs.usermaintenance.service.LoginUserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// DBからログインユーザの情報を取得するクラスをSpringに登録して呼び出し可能にする
	@Autowired
	//ImplementsUserDetailsService userDetailsService;
	LoginUserService loginUserService;

	// 未認証のユーザーからのアクセスを拒否した際のエラー応答を行うハンドラー
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	// 認可処理を行うフィルター
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	// ログインユーザサービスとパスワードエンコーダーの登録
	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		//authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		authenticationManagerBuilder.userDetailsService(loginUserService).passwordEncoder(passwordEncoder());
	}

	// LoginControllerクラスでAuthenticationManagerを使用するために次の処理が必要になる。
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	//パスワードのハッシュ化処理
	@Bean
	public PasswordEncoder passwordEncoder() {
		//パスワードは画面側でハッシュ化するのでここではEncodeしない。
		//return new BCryptPasswordEncoder();
		//return new org.springframework.security.crypto.password.NoOpPasswordEncoder();
		// ↑コンストラクタがprivateなので不可視となる。よって、このプロジェクトのクラスを作成する。
		return new MyNoOpPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			// SpringのCORS対策の処理を有効にする
			.cors()
				.and()
			// CSRF対策は使用しない（Tokenを使用したステートレスな認証方式を使用するため）
			.csrf().disable()
			// 未認証のユーザーからのアクセスを拒否した際のエラー応答を行うハンドラーの登録
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
				.and()
			// ステートレス（Tokenを使用する）な認証方式を行う。ステートフル（Cookieを使用する）な認証方式は行わない。
			// ログイン認証できた場合、JSON Web Token(JWT)による電子署名を使用してログイン後のHTTP通信の認可処理を行う。
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			// antMatchers()の引数で指定したURLは、permitAll()によってログインしていなくてもアクセス可能になる。
			// "/api/auth/**"はログイン処理なので全てのユーザのアクセスを可能にする。
			.authorizeRequests()
				.antMatchers("/api/auth/**").permitAll()
				//.antMatchers(HttpMethod.OPTIONS,"/api/test/**").permitAll()
				//.antMatchers("/UserMaintenance/**").permitAll()
				.anyRequest().authenticated()
				.and()
			// 認可処理を行うフィルターの登録
			.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	//@Override
	//public void configure(WebSecurity web) throws Exception {
	//    web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	//}
}
