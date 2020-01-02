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
import java.util.Properties;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.engine.control.CompositeCacheManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.data.Value;
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
@CommandPkg(label = "Start session", name = "startSession", description = "Start new session", 
icon = "alp.svg", node_label = "start session {{sessionName}}|") 
public class StartSession {
 
    @Sessions
    private Map<String, Object> sessions;
    
    private static final Messages MESSAGES = MessagesFactory
			.getMessages("com.automationanywhere.botcommand.demo.messages");
    

    
    
	private Launchpad launchpad;
	private  HashMap<String, Value> bots;

    
    @Execute
    public void start(@Idx(index = "1", type = TEXT) @Pkg(label = "Session name",  default_value_type = STRING, default_value = "Default") @NotEmpty String sessionName,
    		 		  @Idx(index = "2", type = AttributeType.DICTIONARY) @Pkg(label = "Bots",   default_value_type = DICTIONARY ) @NotEmpty HashMap<String, Value> bots
    		) throws Exception {
 
        // Check for existing session
        if (sessions.containsKey(sessionName))
            throw new BotCommandException(MESSAGES.getString("Session name in use ")) ;
       
        
        
        this.bots = bots;
		this.launchpad = new Launchpad();
        this.launchpad.aquire();
        this.sessions.put(sessionName, this.launchpad);
        ;

        Platform.runLater(new Runnable() {;
            @Override
            public void run() {
                try {
                	launchPad();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}         }
       });
 
    }
 
    
    private void launchPad() throws Exception {

        this.launchpad.initLaunchpad(this.bots);
   }
    
    public void setSessions(Map<String, Object> sessions) {
        this.sessions = sessions;
    }
    
    
    public Launchpad getLaunchPad() {
        return this.launchpad;
    }
    
    
    
 
    
    
}