package csc2b.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BUKAClientPane extends GridPane //You may change the JavaFX pane layout
{
	private Socket socket;
	private Label lblName;
	private TextField txtName;
	private Label lblPassword;
	private TextField txtPassword;
	private Button btnLogin;
	private BufferedReader bReader;
	private PrintWriter pWriter;
	private DataInputStream dataInputStream;
	private Button btnList;
	private TextArea txtList;
	private Label lblFileID;
	private TextField txtFileID;
	private Button btnPDFRET;
	private TextArea txtPDFRET;
	private Button btnLogout;
	
    public BUKAClientPane(Stage primaryStage)
    {
	//Create client connection
    	try {
			socket = new Socket("localhost", 2018);
			
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pWriter = new PrintWriter(socket.getOutputStream(), true);
			dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			
		} catch (UnknownHostException he) {
			System.out.println("Host not found " + he.getMessage());
		}catch (IOException e) {
			System.out.println("Error " + e.getMessage());
		}
	//Create buttons for each command
    	setUpGUI(primaryStage);
	//Use buttons to send commands
    	  	
    }
    
    public void setUpGUI(Stage primaryStage) {
		lblName = new Label("Name: ");
		txtName = new TextField();
		
		lblPassword = new Label("Password: ");
		txtPassword = new TextField();
		
		btnLogin = new Button("Login");
		
		btnList = new Button("List of Files");
		txtList = new TextArea();
		
		lblFileID = new Label("Enter File ID:");
		txtFileID = new TextField();
				
		btnPDFRET = new Button("Requested Files");
		txtPDFRET = new TextArea();
		
		btnLogout = new Button("Logout");
		
		
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setVgap(8);
		gridPane.setHgap(10);
		
		gridPane.add(lblName, 0, 0);
		gridPane.add(txtName, 1, 0);
		
		gridPane.add(lblPassword, 0, 1);
		gridPane.add(txtPassword, 1, 1);
		
		gridPane.add(btnLogin, 0, 2);
		
		gridPane.add(btnList, 0, 3);
		gridPane.add(txtList, 0, 4, 2, 1);
		txtList.setEditable(false);
		
		gridPane.add(lblFileID, 0, 5);
		gridPane.add(txtFileID, 1, 5);
		
		gridPane.add(btnPDFRET, 0, 6);
		gridPane.add(txtPDFRET, 0, 7, 2, 1);
		txtPDFRET.setEditable(false);
		
		gridPane.add(btnLogout, 0, 8);
		
		btnLogin.setOnAction(e -> {
			String name = txtName.getText();
			String password = txtPassword.getText();
			
			pWriter.println("AUTH " + name + " " + password);
			try {
				String response = bReader.readLine();
				txtList.appendText("Server: " + response + "\n");
				
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
		});
		
		btnList.setOnAction(e -> {
			pWriter.println("LIST");
			try {
				String response;
				txtList.clear();
				while((response = bReader.readLine()) != null && !response.isEmpty()) {
					txtList.appendText(response + "\n");
					if (response.startsWith("200")) break;
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		});
		
		btnPDFRET.setOnAction(e -> {
			String fileID = txtFileID.getText().trim();
			pWriter.println("PDFRET" + fileID);
			
			try {
				String response = bReader.readLine();
				txtList.appendText("Server: " + response + "\n");
				
			} catch (Exception e4) {
				// TODO: handle exception
			}
		});
		
		btnLogout.setOnAction(e -> {
			pWriter.println("LOGOUT");
			try {
				String response = bReader.readLine();
				txtList.appendText("Server: " + response + "\n");
				if(response.startsWith("200")){
					socket.close();
				}
			} catch (Exception e4) {
				e4.printStackTrace();
			}
		});
		
		primaryStage.setTitle("BUKA Protocol");
		Scene scene = new Scene(gridPane, 600, 550);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
}

