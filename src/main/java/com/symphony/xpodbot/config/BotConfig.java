package com.symphony.xpodbot.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nick.tarsillo on 8/7/17.
 */
public class BotConfig {
  private static final Logger LOG = LoggerFactory.getLogger(BotConfig.class);
  private static final Set<EnvironmentConfigProperty> PROPERTY_SET = new HashSet<>();

  //Env
  public final static String BOT_KEYSTORE_FILE_ENV = "BOT_KEYSTORE_FILE";
  public final static String BOT_KEYSTORE_PASSWORD_ENV = "BOT_KEYSTORE_PASSWORD";
  public final static String SESSIONAUTH_URL_ENV = "SESSION_AUTH";
  public final static String KEYAUTH_URL_ENV = "KEY_AUTH";
  public final static String SYMPHONY_POD_ENV = "SYMPHONY_POD";
  public final static String SYMPHONY_AGENT_ENV = "SYMPHONY_AGENT";
  public final static String BOT_TRUSTSTORE_FILE_ENV = "BOT_TRUSTSTORE_FILE";
  public final static String BOT_TRUSTSTORE_PASSWORD_ENV = "BOT_TRUSTSTORE_PASSWORD";
  public final static String BOT_EMAIL_ENV = "BOT_EMAIL";

  //_____________________________Properties_____________________________//
  /**
   * Config
   */
  public final static String CONFIG_DIR = "bot.config.dir";
  public final static String CONFIG_FILE = "bot.properties";

  /**
   * Names
   */
  public static final String BOT_EMAIL = "bot.email";

  /**
   * Passwords
   */
  public final static String BOT_KEYSTORE_PASSWORD = "bot.keystore.password";
  public final static String BOT_TRUSTSTORE_PASSWORD = "bot.truststore.password";

  /**
   * URLS
   */
  public final static String SESSIONAUTH_URL = "sessionauth.url";
  public final static String KEYAUTH_URL = "keyauth.url";
  public final static String SYMPHONY_POD = "symphony.agent.pod.url";
  public final static String SYMPHONY_AGENT = "symphony.agent.agent.url";

  /**
   * FILES
   */
  public final static String BOT_KEYSTORE_FILE = "bot.keystore.file";
  public final static String BOT_TRUSTSTORE_FILE = "bot.truststore.file";

  static {
    PROPERTY_SET.add(new EnvironmentConfigProperty(BOT_KEYSTORE_FILE_ENV, BOT_KEYSTORE_FILE));
    PROPERTY_SET.add(new EnvironmentConfigProperty(BOT_KEYSTORE_PASSWORD_ENV, BOT_KEYSTORE_PASSWORD));
    PROPERTY_SET.add(new EnvironmentConfigProperty(SESSIONAUTH_URL_ENV, SESSIONAUTH_URL));
    PROPERTY_SET.add(new EnvironmentConfigProperty(KEYAUTH_URL_ENV, KEYAUTH_URL));
    PROPERTY_SET.add(new EnvironmentConfigProperty(SYMPHONY_POD_ENV, SYMPHONY_POD));
    PROPERTY_SET.add(new EnvironmentConfigProperty(SYMPHONY_AGENT_ENV, SYMPHONY_AGENT));
    PROPERTY_SET.add(new EnvironmentConfigProperty(BOT_TRUSTSTORE_FILE_ENV, BOT_TRUSTSTORE_FILE));
    PROPERTY_SET.add(new EnvironmentConfigProperty(BOT_TRUSTSTORE_PASSWORD_ENV, BOT_TRUSTSTORE_PASSWORD));
    PROPERTY_SET.add(new EnvironmentConfigProperty(BOT_EMAIL_ENV, BOT_EMAIL));
  }

  /**
   * Init properties and envs
   */
  public static void init(){
    String configDir = null;
    String propFile = null;

    Configuration c = null;
    try {
      if (configDir == null) {
        configDir = System.getProperty(CONFIG_DIR);
        if (configDir == null) {
          configDir = "com/symphony/xpodbot/config";
        }
      }

      if (propFile == null) {
        propFile = CONFIG_FILE;
      }
      propFile = configDir + "/" + propFile;

      LOG.info("Using bot.properties file location: {}", propFile);

      c = new PropertiesConfiguration(propFile);

      for(EnvironmentConfigProperty property: PROPERTY_SET){
        property.initProperty(c);
      }
    } catch (ConfigurationException e) {
      LOG.error("Configuration init exception: ", e);
    }
  }

  /**
   * If env exists, use env, otherwise use default config property
   */
  static class EnvironmentConfigProperty {
    private String envName;
    private String propertyName;

    EnvironmentConfigProperty(String envName, String propertyName){
      this.envName = envName;
      this.propertyName = propertyName;
    }

    void initProperty(Configuration configuration){
      if (System.getProperty(propertyName) == null) {
        if (System.getenv(envName) != null) {
          System.setProperty(propertyName, System.getenv(envName));
        } else {
          System.setProperty(propertyName, configuration.getString(propertyName));
        }
      }
    }
  }

}
