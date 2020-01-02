package com.automationanywhere.botcommand.sk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automationanywhere.botcommand.data.Value;

import BotUtils.BotMessage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class Launchpad {


	private  Semaphore sem;
	
    private Integer width = 400;
    private Integer height = 700;

    private GridPane grid ;


    private static Logger logger = LogManager.getLogger(Launchpad.class);
    
    private String botname ;
    
    private String message  ;
	
    public Launchpad()
    {		
    	
    	
      if (this.sem == null) {
	      this.sem = new Semaphore(1);
	  }
	  this.message = "";
    	
    }
	
	public void initLaunchpad(HashMap<String, Value> botmap) throws Exception {
		
         JFrame frame = new JFrame("Attended Bot Launchpad");  
         
		 UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");


     	 SwingUtilities.updateComponentTreeUI(frame);
         
         frame.addWindowListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        		    setBotName("");
            		release();
             }
         });

	        frame.setSize(this.width, this.height);
	        frame.setLocation(100, 100);
	        frame.setVisible(true);
	        frame.setResizable(false);
	        frame.setAlwaysOnTop(true);
	        ImageIcon icon = new ImageIcon(this.getClass().getResource("/icons/logo.png"));
	        frame.setIconImage(icon.getImage());
	        JFXPanel fxPanel = new JFXPanel();
	        fxPanel.setLayout(new BorderLayout());
	        fxPanel.setOpaque(false);
	        Border border = BorderFactory.createMatteBorder(2,14,2,2,new java.awt.Color(19,58,101));
	        fxPanel.setBorder(border);
	        fxPanel.setPreferredSize(new Dimension(this.width*10, this.height*10));
	        frame.add(fxPanel);
	        
	     
	        this.grid  = new GridPane();
	        this.grid.setUserData(new String("LaunchPadFX"));
	        this.grid.setPadding(new Insets(4, 4, 4,  4));
	    	this.grid.setHgap(5);
	        this.grid.setVgap(5);
	    	this.grid.setAlignment(Pos.TOP_CENTER);


	        VBox box = new VBox();
	        box.setSpacing(10);
	        box.setPadding(new Insets(5, 5, 5, 5)); 
	        
	        class Bot {
	        	private String botname;
	        	private String botpath;
	        	
	        	Bot(String name, String path){
	        		this.botname = name;
	        		this.botpath = path;
	        	}
	        	
	        	
	        	@Override
	        	public String toString() {
	        		return this.botname;
	        	}
	        }
	        
	        ComboBox<Bot> optionsBox = new ComboBox<Bot>();
	        optionsBox.setPrefWidth(370);
	        ObservableList<Bot> options = FXCollections.observableArrayList();
	        for (HashMap.Entry<String, Value> bot : botmap.entrySet()) {
	        	Bot newbot = new Bot(bot.getKey().toString(),bot.getValue().toString());
	        	options.add(newbot);
	        }
        	optionsBox.getItems().addAll(options);
        	optionsBox.getSelectionModel().select(0);
        	
        	box.getChildren().add(optionsBox);

	        Button start = new Button();
	        start.setMaxWidth(Double.MAX_VALUE);
	    	Image image1 = new Image(this.getClass().getResourceAsStream("/icons/startbot.png"));
	    	start.setGraphic(new ImageView(image1));
	    	start.setText("Start Bot");
	    	start.setId("LaunchpadStartButton");
	        start.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                setBotName(optionsBox.getValue().botpath);
	                release();
	            }
	        });
        	box.getChildren().add(start);

	    	 Button stop= new Button();
	    	 image1 = new Image(this.getClass().getResourceAsStream("/icons/stoppad.png"));
	    	 stop.setGraphic(new ImageView(image1));
		     stop.setText("Close");
	    	 stop.setOnAction((e)->{
    	    	 setBotName("");
	    	     release();
	    	 });
 	         stop.setMaxWidth(Double.MAX_VALUE);	
 	         
 	         HBox buttonbox = new HBox();
 	         buttonbox.setSpacing(10);
 	         buttonbox.setAlignment(Pos.CENTER);
 	         buttonbox.getChildren().add(start);
 	         buttonbox.getChildren().add(stop);
    	     box.getChildren().add(buttonbox);
    	     
 
	     
	         
	    	 
    	     TableView<BotMessage> list = new TableView<BotMessage>();
	         list.setPrefWidth(390);
	         list.setPrefHeight(600);
	         list.setId("LaunchpadListMessages");
	         list.setEditable(false);
	         TableColumn<BotMessage, String> statusColumn = new TableColumn<BotMessage,String>();
	         statusColumn.setPrefWidth(40);
	         statusColumn.setResizable(false);
	         statusColumn.setEditable(false);
	         statusColumn.setSortable(false);
	         statusColumn.setStyle("-fx-alignment: CENTER");
	         statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
	    
	        
	         statusColumn.setCellFactory(column -> {
	        	    return new TableCell<BotMessage,String>() {
	        	        @Override
	        	        protected void updateItem(String item, boolean empty) {
	        	            super.updateItem(item, empty);
	                   logger.info("Cell {}",item);
	        	            if (item == null || empty) {
	        	                setText(null);
	        	                setStyle("");
	        	                setGraphic(null);
	        	            } else {
	        	             Image image; 
	        	             String[] items = item.split(",");
	        	             String type = items[0];
	        	             String timestamp = items[1];
	        	             logger.info("Type{}",type);
	        	             switch (type) {
	        	             case "ERROR":
	        	            	 image = new Image(this.getClass().getResourceAsStream("/icons/error.png"));
	        	            	 break;
	         	             case "WARNING":
	        	            	 image = new Image(this.getClass().getResourceAsStream("/icons/warning.png"));
	        	            	 break;
	         	             case "QUESTION":
	        	            	 image = new Image(this.getClass().getResourceAsStream("/icons/question.png"));
	        	            	 break;
	         	             case "START":
	        	            	 image = new Image(this.getClass().getResourceAsStream("/icons/start.png"));
	        	            	 break;
	         	             case "STOP":
	        	            	 image = new Image(this.getClass().getResourceAsStream("/icons/stop.png"));
	        	            	 break;
	         	             default:
	        	            	 image = new Image(this.getClass().getResourceAsStream("/icons/info.png"));
	        	             }
	        	             
	        	            
	        	             VBox box = new VBox();
	        	             box.setAlignment(Pos.CENTER);
	        	             ImageView imageView = new ImageView(image);
	        	             Label dateLabel = new Label(timestamp);
	        	             dateLabel.setStyle("-fx-font-size:6pt");
	        	             box.getChildren().add(imageView);	   
	        	             box.getChildren().add(dateLabel);     	     
	        		    	 setGraphic(box);
	        	               
	        	//	    	   setGraphic(new ImageView(image));
	        	              }
	                }
	            };
	        });
	         
	         
	         TableColumn<BotMessage,String> messageColumn = new TableColumn<BotMessage,String>();
	         messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
	         messageColumn.setPrefWidth(300);
	         messageColumn.setResizable(false);
	         messageColumn.setEditable(false);
	         messageColumn.setSortable(false);
	         messageColumn.setStyle("-fx-alignment: CENTER-LEFT");
	         messageColumn.setCellFactory(column -> {
	        	    return new TableCell<BotMessage,String>() {
	        	        @Override
	        	        protected void updateItem(String item, boolean empty) {
	        	            super.updateItem(item, empty);
	              //     logger.info("Cell {}",item);
	        	            if (item == null || empty) {
	        	                setText(null);
	        	                setStyle("");
	        	                setGraphic(null);
	        	            } else {
	        	                 Text text = new Text(item);
	        	                 text.setStyle("-fx-text-alignment:justify;");                      
	        	                 text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(20));
	        	                 setGraphic(text);

	        	              }
	                }
	            };
	        });
	         list.getColumns().addAll(statusColumn, messageColumn);
	         
	         list.widthProperty().addListener(new ChangeListener<Number> () {
	             @Override
	             public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
	                 // Get the table header
	                 Pane header = (Pane)list.lookup("TableHeaderRow");
	                 if(header!=null && header.isVisible()) {
	                   header.setMaxHeight(0);
	                   header.setMinHeight(0);
	                   header.setPrefHeight(0);
	                   header.setVisible(false);
	                   header.setManaged(false);
	                 }
	             }
	         });
	         list.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
	
    	     box.getChildren().add(list);
    	     this.grid.getChildren().add(box);

	    	 Scene  scene  =  new  Scene(this.grid, Color.WHITE);
	    	 URL url = this.getClass().getResource("/css/styles.css");
    	     scene.getStylesheets().add(url.toExternalForm());
	         fxPanel.setScene(scene);
	         	        
	
	}

	public  void release() {

		 this.sem.release();
	}
	
	
	public  void aquire() throws Exception {

		  this.sem.acquire();
	}
	
	
	public String getBotName() {

		  return this.botname;
	}
	
	public void setBotName(String name) {

		this.botname=name;
	}
	
	public void  setGrid(GridPane grid) {

		this.grid = grid;
	}
	
	public GridPane getGrid() {

		return this.grid;
	}
	
	
	public void setMessage(String message) {

	  	this.message = message; 

        }

	
}