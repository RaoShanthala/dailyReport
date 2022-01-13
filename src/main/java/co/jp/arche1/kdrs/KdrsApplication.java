package co.jp.arche1.kdrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class KdrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KdrsApplication.class, args);
	}

	// 次のaddCorsMappings()の処理を削除するとログイン後のGetで次のエラーになる。
	// 		Access to XMLHttpRequest at 'http://localhost:8080/api/test/users'
	//			from origin 'null' has been blocked by CORS policy:
	//				Response to preflight request doesn't pass access control check:
	//					No 'Access-Control-Allow-Origin' header is present on the requested resource.
	// 		GET http://localhost:8080/api/test/users net::ERR_FAILED
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			// グローバルCORS構成の定義
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
					// アプリケーション全体でCORSを有効にする（コントローラ全体でCORSを有効にするにはContollerで設定）
					.addMapping("/**")
					//registry.addMapping("/actor")

					// APIデプロイメントへのアクセスが許可されているオリジンの必須のカンマ区切りリスト
					// Origin をまたいだ XMLHttpRequest で Cookie を送りたい場合、CORS を許可する Origin を明示的にする必要がある。
					.allowedOrigins("*")
					//.allowedOrigins(CrossOrigin.DEFAULT_ORIGINS)

					// APIデプロイメントへの実際のリクエストで許可されるHTTPヘッダーのオプションのカンマ区切りリスト
					.allowedHeaders("Content-Type", "X-Requested-With", "Accept", "Origin", "Authorization"
									, "Access-Control-Request-Method"
									, "Access-Control-Request-Headers")
									//, "Accept-Encoding", "Accept-Language", "Connection"
									//, "Host", "Sec-Fetch-Dest", "Sec-Fetch-Mode", "Sec-Fetch-Site", "User-Agent")
					//.allowedHeaders("*")
					//.allowedHeaders(CrossOrigin.DEFAULT_ALLOWED_HEADERS)

					// 実際のリクエストに対するAPIデプロイメントのレスポンスでクライアントがアクセスできる、HTTPヘッダーのオプションのカンマ区切りリスト
					//.exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
					//.exposedHeaders("*")	-> Error!

					// APIデプロイメントへの実際のリクエストで許可されるHTTPメソッドのオプションのカンマ区切りリスト
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
					//.allowedMethods("*")	-> Error!

					// ブラウザがCookieなどの認証情報をクロスドメインリクエストとともに注釈付きエンドポイントに送信する必要があるかどうか
					.allowCredentials(false)

					// プリフライトのリクエストからの応答をクライアントがキャッシュできる時間を秒単位で設定
					.maxAge(3600L);
			}
		};
	}
}
