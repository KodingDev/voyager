package gg.hubblemc.voyager.fixes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.StringBuilderEncoder;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class LogPatcher {

    public static void reconfigureLog4j() throws NoSuchFieldException, IllegalAccessException {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();

        // Get fields
        Field charsetField = AbstractStringLayout.class.getDeclaredField("charset");
        charsetField.setAccessible(true);

        Field textEncoderField = AbstractStringLayout.class.getDeclaredField("textEncoder");
        textEncoderField.setAccessible(true);

        // Update
        for (Appender appender : config.getAppenders().values()) {
            if (!(appender.getLayout() instanceof AbstractStringLayout layout)) continue;

            // Set the charset to UTF-8 via reflection
            charsetField.set(layout, StandardCharsets.UTF_8);
            textEncoderField.set(layout, new StringBuilderEncoder(StandardCharsets.UTF_8));
        }

        ctx.updateLoggers();
    }

}
