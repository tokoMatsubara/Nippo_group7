package com.daily_app.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// ⬇️ この3つのインポートを追加します（セキュリティを徹底的にオフにするため）
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;

// ⬇️ エラーの原因になるセキュリティの自動設定を、力技で全部「除外（exclude）」します
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,           // DBエラーを防ぐ
    SecurityAutoConfiguration.class,               // セキュリティ画面を防ぐ
    UserDetailsServiceAutoConfiguration.class,     // 先ほど怒られたエラー（HttpSecurity）を防ぐ
    ReactiveSecurityAutoConfiguration.class        // 念のため予備のセキュリティもオフ
})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
