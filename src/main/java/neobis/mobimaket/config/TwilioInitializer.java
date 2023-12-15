package neobis.mobimaket.config;

import com.twilio.Twilio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TwilioInitializer {

    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioInitializer(TwilioConfig twilioConfig) {
        this.twilioConfig= twilioConfig;
        Twilio.init(
                twilioConfig.getAccountSid(),
                twilioConfig.getAuthToken()
        );
        log.info("Twilio initialized ... with account sid {} ", twilioConfig.getAccountSid());
    }
}
