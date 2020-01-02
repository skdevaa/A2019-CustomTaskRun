/*
EndSession.javaC:\Users\Stefan Karsten\Documents\AlleDateien\Demos\Packages\IBM Watson * Copyright (c) 2019 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */
/**
 * 
 */
package com.automationanywhere.botcommand.sk;



import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.bot.service.GlobalSessionContext.ChildBot;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;


import java.util.Optional;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;

import static com.automationanywhere.commandsdk.model.AttributeType.CHECKBOX;

import static com.automationanywhere.commandsdk.model.DataType.STRING;

import static com.automationanywhere.commandsdk.model.DataType.BOOLEAN;

import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;

import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.Sessions;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.commandsdk.annotations.Execute;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;



/**
 * @author Stefan Karsten
 * 
 * Each child bot should be added as dependency to the parent bot
 *
 */



@BotCommand
@CommandPkg(label="Launch Task", name="launchruntask", description="Launch Task by name", icon="alp.svg",
node_label="Custom Launch Task",
return_type=DataType.DICTIONARY,  return_required=false)
public class LaunchTask {
	
	  private static final Logger logger = LogManager.getLogger(LaunchTask.class);
	  
	  @com.automationanywhere.commandsdk.annotations.GlobalSessionContext
	  private GlobalSessionContext globalSessionContext;

	  
	  public void setGlobalSessionContext(GlobalSessionContext globalSessionContext) {
	        this.globalSessionContext = globalSessionContext;
	    }
	  
	  
	  @Sessions
	  private Map<String, Object> sessions;
	  
	@Execute
	public Value launch(@Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING, default_value = "Default" ) @NotEmpty String sessionName) throws Exception
	 {
		

	     Launchpad launchpad= ((Launchpad)this.sessions.get(sessionName));
	//     launchpad.aquire();
	     String botname = launchpad.getBotName();
	     
	    
	     LaunchTask command = new LaunchTask();
  	     command.setGlobalSessionContext(globalSessionContext);
  	     Value result = command.runTask(botname);
  	     
  	     return result;

	}
	
	
	
	public Value runTask(String botname) throws Exception 
	{
	    
	    String taskbotUri = "repository:///Automation%20Anywhere/"+botname;
	    Map<String, Value> inputValues = null;


	    
	   ChildBot bot = ((com.automationanywhere.bot.service.GlobalSessionContext) this.globalSessionContext).getChildBotWithGlobalSessionContext(taskbotUri);
	    
	    Optional<Value> outputValue = Optional.empty();
	    Notify notify = new Notify();
	    notify.notify(botname+" started","START");
	  
	      try {
	        outputValue = bot.getBot().play(bot.getGlobalSessionContext(), inputValues);
	      }
	      catch (Exception e) {
	        String message;
	        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
	          message = e.getMessage();
	        }
	        else {
	            
	            message = String.format("Task Bot %s threw an exception.", new Object[] { e.getClass().getSimpleName() });
	        } 
	          logger.error(message, e);
	          throw new BotCommandException(message, e);
	        }
	      
		    notify.notify(botname+" completed","STOP");
	       
		    return outputValue.isPresent() ? outputValue.get() : null;     
	    
	    } 
	
	
	 
		public void setSessions(Map<String, Object> sessions) {
			this.sessions = sessions;
		}
    
    
}
