package com.systemdesign.mindset.config;

import com.systemdesign.mindset.model.User;
import com.systemdesign.mindset.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("🔄 Initializing test data...");

        // Test kullanıcıları oluştur
        createUser("ali", "1234", new BigDecimal("5000"));
        createUser("ayse", "1234", new BigDecimal("3000"));
        createUser("mehmet", "1234", new BigDecimal("10000"));

        log.info("✅ Test data initialized successfully!");
        log.info("📝 Test Users:");
        log.info("   - ali (password: 1234, balance: 5000 TL)");
        log.info("   - ayse (password: 1234, balance: 3000 TL)");
        log.info("   - mehmet (password: 1234, balance: 10000 TL)");

    }

    private void createUser(String username, String password, BigDecimal balance) {
        if (!userRepository.existsByUsername(username)) {
            User user = User.builder()
                    .username(username)
                    .password(password)  // ⚠️ Plain text - Gerçek projede hash'le!
                    .balance(balance)
                    .build();

            userRepository.save(user);
            log.debug("Created user: {}", username);
        }
    }
}
