package se.sundsvall.remindandinform;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class RemindAndInformApplication {
	private static final Logger LOG = LoggerFactory.getLogger(RemindAndInformApplication.class);

	void onStart(@Observes final StartupEvent event) {
		LOG.info("Starting with profile:'{}'", ProfileManager.getActiveProfile());
	}
}
