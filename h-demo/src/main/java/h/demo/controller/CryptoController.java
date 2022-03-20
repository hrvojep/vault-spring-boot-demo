package h.demo.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;
import org.springframework.vault.core.VaultVersionedKeyValueTemplate;
import org.springframework.vault.support.VaultMount;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import h.demo.HDemoApplication;
import lombok.Data;

@RestController
public class CryptoController {
	private static final Logger log = LoggerFactory.getLogger(CryptoController.class);

	
	String KEY_NAME = "foo-key";

	@Autowired
	private VaultTemplate vaultTemplate;
	
	
	VaultOperations vaultOperations;	
	VaultTransitOperations transitOperations;
	

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {		
		transitOperations = vaultTemplate.opsForTransit();
		VaultSysOperations sysOperations = vaultTemplate.opsForSys();
		if (!sysOperations.getMounts().containsKey("transit/")) {
			sysOperations.mount("transit", VaultMount.create("transit"));
			transitOperations.createKey(KEY_NAME);
		}
	}
	
	@GetMapping("/encrypt")
	public String encrypt(@RequestParam(value = "s") String s) {
		return transitOperations.encrypt(KEY_NAME, s);			
	}

	@GetMapping("/decrypt")
	public String decrypt(@RequestParam(value = "s") String s) {
		return transitOperations.decrypt(KEY_NAME, s);				
	}
	
	@GetMapping("/read-secret")
	public String read_secret(@RequestParam(value = "s") String s) {

		
//		VaultResponse response = vaultTemplate.read("secret/data/myapps/vault-quickstart/config");
//	    String a = (String) response.getData().get("a-private-key");
	    

		MySecretData mySecretData = new MySecretData();
		mySecretData.setUsername("walter");
		mySecretData.setPassword("white");
		log.info("Getting started:----------->");

		vaultTemplate.write(
				"secret/data/myapps/vault-quickstart/config", mySecretData);
		log.info("Wrote data to Vault");

		VaultResponseSupport<MySecretData> response = vaultTemplate.read(
				"secret/data/myapplication/user/3128", MySecretData.class);

		log.info("Retrieved data {} from Vault", response.getData().getUsername());

		
		return "";
	}
	

	@Data
	static class MySecretData {

		String username;
		String password;
	}
	
}
