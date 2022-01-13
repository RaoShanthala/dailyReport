package co.jp.arche1.kdrs.usermaintenance.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import co.jp.arche1.kdrs.common.security.ImplementsUserDetails;
import co.jp.arche1.kdrs.usermaintenance.mapper.PvCompanyUserAuthorityMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PvUserCompanyMapper;
import co.jp.arche1.kdrs.usermaintenance.repository.PvCompanyUserAuthorityRepository;
import co.jp.arche1.kdrs.usermaintenance.repository.PvUserCompanyRepository;

//import org.bouncycastle.asn1.x509.X509Name;

@Service
public class LoginUserService implements UserDetailsService {
	final static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	PvUserCompanyMapper pvUserCompanyMapper;

	@Autowired
	PvCompanyUserAuthorityMapper pvCompanyUserAuthorityMapper;

	@Override
	//@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String emailCompanyCode) throws UsernameNotFoundException {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		String[] emailAndCompanyCode = StringUtils.split( emailCompanyCode, String.valueOf(Character.LINE_SEPARATOR));

		// このLoginUserService.loadUserByUsername()は、ログインでLoginController.authenticateUser()から
		// authenticationManager.authenticate()経由でユーザ対するパスワードを検証するために呼び出される。
		// ログインでない全ての処理でAuthTokenFilter.doFilterInternal()からユーザのRoleを取得するために
		// 呼び出されていたのはRoleService.loadUserRoleByUsername()に変更した。

		Integer userId = null;
		/*String sei= null;
		String mei = null;
		String seiKana = null;
		String meiKana = null;*/
		String email= null;
		String password= null;
		String companyCode = null;
		Integer companyId = null;
		Byte status = null;
		List<GrantedAuthority> authorities = new ArrayList<>();

	//	List<PvLoginUserRoleRepository> listPvUserRoleRepository = pvLoginUserRoleMapper.selectMany(userName);

		//PvUserCompanyRepository> listPvUserCompanyRepository = pvUserCompanyMapper.selectMany(email);
		//for(Iterator<PvUserCompanyRepository> it = listPvUserCompanyRepository.iterator(); it.hasNext();) {
		String compCode = null;
		if (!emailAndCompanyCode[1].equalsIgnoreCase("SuperUser")) {
			compCode = emailAndCompanyCode[1];
		}

		PvUserCompanyRepository pvUserCompanyRepository =  pvUserCompanyMapper.selectOne(emailAndCompanyCode[0],compCode);

			    userId = pvUserCompanyRepository.getUserId();
				email = pvUserCompanyRepository.getEmail();
				password = pvUserCompanyRepository.getPassword();
				companyCode = pvUserCompanyRepository.getCompanyCode();
				status = pvUserCompanyRepository.getStatus();
				companyId = pvUserCompanyRepository.getCompanyId();
			/*	sei = pvUserCompanyRepository.getSei();
				mei = pvUserCompanyRepository.getMei();
				seiKana = pvUserCompanyRepository.getSeiKana();
				meiKana = pvUserCompanyRepository.getMeiKana();*/

			List<PvCompanyUserAuthorityRepository> listPvCompanyUserAuthorityRepository = pvCompanyUserAuthorityMapper.selectAllAuthority(pvUserCompanyRepository.getUserId(),pvUserCompanyRepository.getCompanyId());
			for(Iterator<PvCompanyUserAuthorityRepository> it = listPvCompanyUserAuthorityRepository.iterator(); it.hasNext();) {
				PvCompanyUserAuthorityRepository pvCompanyUserAuthorityRepository = it.next();
			// ログインでは、authoritiesは使用していないので次の処理はコメントにする（Authorityの取得では必要だが別のメソッドにした）。
			  /* StringBuilder sb  = new StringBuilder("");
			   sb.append(pvCompanyUserAuthorityRepository.getAuthorityType()).append("_").append(pvCompanyUserAuthorityRepository.getName());
			   authorities.add(new SimpleGrantedAuthority(sb.toString())); */
				authorities.add(new SimpleGrantedAuthority(pvCompanyUserAuthorityRepository.getAuthorityType() + "_" + pvCompanyUserAuthorityRepository.getName()));
		    }
		// スレッドローカルオブジェクト(RequestContextHolder)からリクエスト情報を取り出す
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String loginEmail = getCert(req);
		System.out.println("LoginUserService.loadUserByUsername loginEmail=" + loginEmail + ", email=" + email);
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " commonName=" + loginEmail);

		if (loginEmail.isEmpty()) {
			loginEmail = email;
		}

		return ImplementsUserDetails.build(userId, loginEmail, password, companyCode,companyId,status,authorities);
	}

	private String getCert(HttpServletRequest request) {
		// -- クライアント証明書情報の取得 --//
		java.security.cert.X509Certificate[] certs = (java.security.cert.X509Certificate[]) request
				.getAttribute("javax.servlet.request.X509Certificate");

		String commonName = "";
		if (certs != null) {
			// HTTPSのときのみ、HTTPのときcertsはnull
			// クライアント証明書からクライアント証明書識別子を取得
			Principal principal = certs[0].getSubjectX500Principal();

			String principalName = principal.getName();
			// System.out.println(principalNames);
			// CN=cli001.kitani55.co.jp,O=kitaniCO.\,LTD.,L=Himi,ST=Toyama,C=JP

			String[] prinNames = principalName.split(",");
			for (String prinName : prinNames) {
				String[] names = prinName.split("=");
				if (names[0].equals("CN")) {
					commonName = names[1];
					break;
				}
			}

			// クライアント証明書識別子を取得
			// X509Name name = new X509Name(principal.getName());
			// Vector<?> cert =
			// name.getValues(X509ObjectIdentifiers.commonName);
			// if (cert.size() == 1) {
			// String code = cert.get(0);
			// }
		}
		return commonName;
	}

}
