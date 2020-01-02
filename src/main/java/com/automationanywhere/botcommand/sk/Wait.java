/*
 * Copyright (c) 2019 Automation Anywhere.
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

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.AttributeType.VARIABLE;
import static com.automationanywhere.commandsdk.model.DataType.STRING;
import static com.automationanywhere.commandsdk.model.DataType.DICTIONARY;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.BooleanValue;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.Sessions;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;



/**
 * @author Stefan Karsten
 *
 */



@BotCommand
@CommandPkg(label = "Wait", name = "waitSession", description = "Wait to end session", 
icon = "alp.svg", node_label = "wait session {{sessionName}}|",
return_type=DataType.BOOLEAN, return_label="Continue", return_required=true) 
public class Wait {
 
    @Sessions
    private Map<String, Object> sessions;
    
    private static final Messages MESSAGES = MessagesFactory
			.getMessages("com.automationanywhere.botcommand.demo.messages");
    
    @com.automationanywhere.commandsdk.annotations.GlobalSessionContext
	  private GlobalSessionContext globalSessionContext;

	  
	  public void setGlobalSessionContext(GlobalSessionContext globalSessionContext) {
	        this.globalSessionContext = globalSessionContext;
	    }
	  
    

    
    
    @Execute
    public Value<Boolean> wait(@Idx(index = "1", type = TEXT) @Pkg(label = "Session name", 
    default_value_type = STRING, default_value = "Default") @NotEmpty String sessionName) throws Exception {
 
	     Launchpad launchpad= ((Launchpad)this.sessions.get(sessionName));
	     launchpad.aquire();
	     Boolean continued = (launchpad.getBotName() != "");
	     return (new BooleanValue(continued));
 
    }
 
    public void setSessions(Map<String, Object> sessions) {
        this.sessions = sessions;
    }
    
    

    
 
    
    
}