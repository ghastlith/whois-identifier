package blarknes.whoisidentifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import blarknes.whoisidentifier.identify.IdentifyIpWhois;
import lombok.AllArgsConstructor;
import lombok.val;

@SpringBootApplication
@AllArgsConstructor
public class Application implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationArguments appArgs;

    @Autowired
    private IdentifyIpWhois identifyIpWhois;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        val ip = appArgs.getOptionValues("ip");

        if (ip == null || ip.get(0).isBlank()) {
            shutdown("The argument IP is required", 1);
            return;
        }

        try {
            val result = identifyIpWhois.getIPDetailedData(ip.get(0));
            shutdown(result, 0);
        } catch (Exception e) {
            shutdown(e.getMessage(), 1);
        }
    }

    private void shutdown(final String message, final int code) {
        if (code == 0) {
            System.out.println(message);
        } else {
            System.err.println("Failure: " + message);
        }

        SpringApplication.exit(context, () -> code);
    }

}
