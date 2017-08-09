package com.symphony.xpodbot.bots;

import com.symphony.xpodbot.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.impl.SymphonyBasicClient;
import org.symphonyoss.client.model.SymAuth;
import org.symphonyoss.symphony.clients.AuthorizationClient;
import com.symphony.xpodbot.service.AutoConnectionAcceptListener;
import com.symphony.xpodbot.service.EchoMessageListener;

/**
 * Created by nick.tarsillo on 8/7/17.
 */
public class XpodBot {
  private static final Logger LOG = LoggerFactory.getLogger(XpodBot.class);

  private AutoConnectionAcceptListener connectionAcceptListener;
  private EchoMessageListener echoMessageListener;

  public static void main(String args[]) {
    BotConfig.init();

    new XpodBot();
  }

  public XpodBot() {
    setupBot();
  }

  void setupBot() {
    //Init client
    SymphonyClient symClient = new SymphonyBasicClient();

    AuthorizationClient authClient = new AuthorizationClient(
        System.getProperty(BotConfig.SESSIONAUTH_URL),
        System.getProperty(BotConfig.KEYAUTH_URL));

    LOG.info("Setting up auth http client...");
    try {
      authClient.setKeystores(
          System.getProperty(BotConfig.BOT_TRUSTSTORE_FILE),
          System.getProperty(BotConfig.BOT_TRUSTSTORE_PASSWORD),
          System.getProperty(BotConfig.BOT_KEYSTORE_FILE),
          System.getProperty(BotConfig.BOT_KEYSTORE_PASSWORD));
    } catch (Exception e) {
      LOG.error("Could not create HTTP Client for authentication: ", e);
    }
    LOG.info("Attempting bot auth...");
    try {
      SymAuth symAuth = authClient.authenticate();

      symClient.init(symAuth,
          System.getProperty(BotConfig.BOT_EMAIL),
          System.getProperty(BotConfig.SYMPHONY_AGENT),
          System.getProperty(BotConfig.SYMPHONY_POD));
    } catch (Exception e) {
      LOG.error("Authentication failed for bot: ", e);
    }

    connectionAcceptListener = new AutoConnectionAcceptListener(symClient.getConnectionsClient());
    symClient.getMessageService().addConnectionsEventListener(connectionAcceptListener);

    echoMessageListener = new EchoMessageListener(symClient.getMessagesClient());
    symClient.getMessageService().addMessageListener(echoMessageListener);

    LOG.info("AdminBot startup complete.");
  }

}
