package h.demo;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.authentication.SessionManager;

@SpringBootApplication
public class HDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HDemoApplication.class, args);
	}

	private static final Logger logger = LoggerFactory.getLogger(HDemoApplication.class);

	@Autowired
	private SessionManager sessionManager;

	@Value("${spring.datasource.username}")
	private String dbUser;

	@Value("${spring.datasource.password}")
	private String dbPass;

	@PostConstruct
	public void initIt() throws Exception {
		logger.info("Got Vault Token: " + sessionManager.getSessionToken().getToken());
		logger.info("Got DB User: " + dbUser);
		logger.info("Got DB Pass: " + dbPass);
	}	

//	@Autowired
//	DataSource dataSource;
//
//	@PostConstruct
//	private void postConstruct() throws Exception {
//
//		System.out.println("##########################");
//
//		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
//
//			ResultSet resultSet = statement.executeQuery("SELECT USER();");
//			resultSet.next();
//
//			System.out.println("Connection works with User: " + resultSet.getString(1));
//
//			resultSet.close();
//		}
//
//		System.out.println("##########################");
//	}
}
