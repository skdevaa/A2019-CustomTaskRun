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

import static com.automationanywhere.commandsdk.model.AttributeType.SELECT;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.AttributeType.VARIABLE;
import static com.automationanywhere.commandsdk.model.DataType.STRING;
import static com.automationanywhere.commandsdk.model.DataType.DICTIONARY;

import java.awt.Dimension;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

import javax.swing.JFrame;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.engine.control.CompositeCacheManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.bot.service.GlobalSessionContext.ChildBot;
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

import BotUtils.BotMessage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;



/**
 * @author Stefan Karsten
 *
 */



@BotCommand
@CommandPkg(label = "Notify", name = "Notify", description = "Notify Launchpad", 
icon = "alp.svg", node_label = "Notify") 
public class Notify {
 

	private static Logger logger = LogManager.getLogger(Notify.class);

    
    
    @Execute
    public void notify(@Idx(index = "1", type = TEXT) @Pkg(label = "Message", 
    default_value_type = STRING) @NotEmpty String message,
    					@Idx(index = "2", type = SELECT, options = {
    					@Idx.Option(index = "2.1", pkg = @Pkg(label = "Information", value = "INFORMATION")),
						@Idx.Option(index = "2.2", pkg = @Pkg(label = "Error", value = "ERROR")),
						@Idx.Option(index = "2.3", pkg = @Pkg(label = "Warning", value = "WARNING")),
						@Idx.Option(index = "2.4", pkg = @Pkg(label = "Question", value = "QUESTION")),
						@Idx.Option(index = "2.5", pkg = @Pkg(label = "Start", value = "START")),
						@Idx.Option(index = "2.6", pkg = @Pkg(label = "Stop", value = "STOP")),
			         	}) @Pkg(label = "Message Type", default_value = "INFORMATION", default_value_type = STRING) @NotEmpty String type) throws Exception {
        
        Platform.runLater(new Runnable() {;
        @Override
        public void run() {
        	
        	ObservableList<Window> windows = Stage.getWindows();
        	for (Window win : windows) {
        		
                String topwindow = "LaunchpadListMessages";
                
                logger.info("ChoosenWindow: {}",topwindow);
                
                logger.info("Found Window: {}",win.getScene().getRoot().toString() );
        		
				if (win.getScene().lookup("#"+topwindow) != null)
				{
					TableView<BotMessage> list =  (TableView<BotMessage>) win.getScene().lookup("#"+topwindow);
					Button startbutton = (Button) win.getScene().lookup("#LaunchpadStartButton");
					if (type == "START" && startbutton != null) {
						startbutton.setDisable(true);
					}
					
					if (type == "STOP" && startbutton != null) {
						startbutton.setDisable(false);
					}
						
	                logger.info("List: {}",list.toString());

					if (list != null) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
						LocalDateTime now = LocalDateTime.now();  
       	             	BotMessage botmessage = new BotMessage(new String(type),new String(message),new String(now.format(formatter)));
       	                logger.info("Bot Message: {}", botmessage.getMessage());
						list.getItems().add(0,botmessage);

					}
		        }
        	}
          }

        });
    }
    
 
      
    
}