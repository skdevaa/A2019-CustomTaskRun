package com.automationanywhere.botcommand.sk;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.Boolean;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RunTaskCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(RunTaskCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    RunTask command = new RunTask();
    if(parameters.get("botname") == null || parameters.get("botname").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","botname"));
    }

    if(parameters.get("continueOnError") == null || parameters.get("continueOnError").get() == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","continueOnError"));
    }
    if(parameters.get("continueOnError") != null && parameters.get("continueOnError").get() != null && !(parameters.get("continueOnError").get() instanceof Boolean)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","continueOnError", "Boolean", parameters.get("continueOnError").get().getClass().getSimpleName()));
    }
    if(parameters.get("continueOnError") != null && ((Boolean)parameters.get("continueOnError").get()) == true) {
    }

    command.setGlobalSessionContext(globalSessionContext);
    if(parameters.get("botname") != null && parameters.get("botname").get() != null && !(parameters.get("botname").get() instanceof String)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","botname", "String", parameters.get("botname").get().getClass().getSimpleName()));
    }
    if(parameters.get("continueOnError") != null && parameters.get("continueOnError").get() != null && !(parameters.get("continueOnError").get() instanceof Boolean)) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","continueOnError", "Boolean", parameters.get("continueOnError").get().getClass().getSimpleName()));
    }
    try {
      Optional<Value> result =  Optional.ofNullable(command.action(parameters.get("botname") != null ? (String)parameters.get("botname").get() : (String)null ,parameters.get("continueOnError") != null ? (Boolean)parameters.get("continueOnError").get() : (Boolean)null ));
      logger.traceExit(result);
      return result;
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","action"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
