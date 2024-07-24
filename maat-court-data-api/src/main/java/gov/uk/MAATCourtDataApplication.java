package gov.uk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ConfigurationPropertiesScan
@EnableTransactionManagement
public class MAATCourtDataApplication {

    public static void main(String[] args) {
    /*    Sentry.init(options -> {
    TODO finish this
            options.setDsn("your-dsn");
            // You can fetch the version number from your application properties or any other source
            String version = Application.class.getPackage().getImplementationVersion();
            options.setRelease(version);
            options.setTrans
            //options.set
            // use VersionMetaData to get version number etc
            .
        });*/
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(MAATCourtDataApplication.class);
    }
}
