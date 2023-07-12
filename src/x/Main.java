package x;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.nio.charset.Charset;

public class Main {

    public static void main(String[] args) {
        PluginManager.addPackage(Main.class.getPackage().getName());

        System.setProperty("envVar", "envValue");
        Logger logger = LogManager.getLogger("bug");
        logger.info("hello");
    }

    @Plugin(name = "CustomLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
    public static class CustomLayout extends AbstractStringLayout {
        private final PatternLayout patternLayout;

        public CustomLayout(String type, Charset charset, String[] overrideFields) {
            super(charset);
            this.patternLayout = PatternLayout.newBuilder()
                    .withPattern("%d ${sys:envVar} %msg%n")
                    .build();
        }

        @Override
        public String toSerializable(final LogEvent event) {
            return patternLayout.toSerializable(event);
        }

        @PluginFactory
        public static CustomLayout createLayout(String type, Charset charset, String[] overrideFields) {
            return new CustomLayout(type, charset, overrideFields);
        }
    }
}