package br.com.orionpay.authorizationservice.authorization.config.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MaskingPatternLayout extends PatternLayout {

    // Regex para encontrar números de cartão de 13 a 16 dígitos
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("\\b(\\d{4})\\d{5,8}(\\d{4})\\b");


    @Override
    public String doLayout(ILoggingEvent event) {
        // Formata a mensagem de log original
        String message = super.doLayout(event);

        // Aplica a máscara
        Matcher matcher = CARD_NUMBER_PATTERN.matcher(message);
        if (matcher.find()) {
            return matcher.replaceAll("$1********$2");
        }
        return message;
    }


}
