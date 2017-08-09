package com.symphony.xpodbot.service;

import com.symphony.xpodbot.commons.BotConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.MessagesException;
import org.symphonyoss.client.services.MessageListener;
import org.symphonyoss.symphony.clients.MessagesClient;
import org.symphonyoss.symphony.clients.model.SymMessage;

/**
 * Created by nick.tarsillo on 8/7/17.
 */
public class EchoMessageListener implements MessageListener {
  private static final Logger LOG = LoggerFactory.getLogger(EchoMessageListener.class);

  private MessagesClient messagesClient;

  public EchoMessageListener(MessagesClient messagesClient) {
    this.messagesClient = messagesClient;
  }

  @Override
  public void onMessage(SymMessage symMessage) {
    try {
      SymMessage echoMessage = symMessage;
      echoMessage.setMessage(BotConstants.ECHO_MESSAGE + symMessage.getMessage() + "\"");
      messagesClient.sendMessage(symMessage.getStream(), symMessage);
    } catch (MessagesException e) {
      LOG.error("Echo message failed: ", e);
    }
  }

}
