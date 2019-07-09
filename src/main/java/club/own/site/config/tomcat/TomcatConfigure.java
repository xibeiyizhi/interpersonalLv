package club.own.site.config.tomcat;

import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfigure {
    @Bean
    public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addContextCustomizers(context -> {
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setUserConstraint("CONFIDENTIAL");
            SecurityCollection collection = new SecurityCollection();
            collection.addPattern("/*");
            collection.addMethod("POST");
            collection.addMethod("GET");
            securityConstraint.addCollection(collection);

            SecurityConstraint constraint = new SecurityConstraint();
            constraint.setUserConstraint("NONE");
            SecurityCollection securityCollection = new SecurityCollection();
            securityCollection.addPattern("/*");
            securityConstraint.addCollection(securityCollection);

            context.addConstraint(securityConstraint);
        });
        return factory;
    }
}
