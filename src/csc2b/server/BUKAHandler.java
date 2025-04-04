package csc2b.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BUKAHandler implements Runnable
{
	private Socket socket;
	private BufferedReader bReader;
	private PrintWriter pWriter;
	private DataOutputStream dataOutputStream;
	private boolean isAuthenticated;
	
    public BUKAHandler(Socket newConnectionToClient)
    {	
	//Bind streams
    	this.socket = newConnectionToClient;
    }
    
    public void run()
    {
	//Process commands from client
    	try {
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pWriter = new PrintWriter(socket.getOutputStream(), true);
			dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			String response = bReader.readLine();
			System.out.println(response);
			
			StringTokenizer sTokenizer = new StringTokenizer(response);
			String command = sTokenizer.nextToken();
			
			switch (command){
			case "AUTH":
				String Name = sTokenizer.nextToken();
				String Password = sTokenizer.nextToken();
				boolean check = matchUser(Name, Password);
				if (check) {
					isAuthenticated = true;
					pWriter.println("200 Logged in successfully!");
				}else {
					pWriter.println("500 Failed logging in");
				}
				break;
				
			case "LIST":
				if(isAuthenticated) {
					ArrayList<String> files = getFileList();
					if(files.isEmpty()) {
						pWriter.println("500 No files available");
					}else {
						pWriter.println("200 File list: ");
						for(String file : files) {
							pWriter.println(file);
						}
					}
				}else {
					pWriter.println("500 Not Authenticated");
				}
				break;
				
			case "PDFRET":
				if(isAuthenticated) {
					String id = sTokenizer.nextToken();
					String fileName = idToFile(id);
					if(!fileName.isEmpty()) {
						File file = new File("data/server/PdfList.txt" + fileName);
						if(file.exists()) {
							long fileSize = file.length();
							pWriter.println("200 File found: " + fileName + " Size: " + fileSize + " bytes");
							
							FileInputStream fis = new FileInputStream(file);
							byte[] buffer = new byte[4096];
							int bytesread;
							while((bytesread = fis.read(buffer)) != -1) {
								dataOutputStream.write(buffer, 0, bytesread);
							}
							dataOutputStream.flush();
							fis.close();
						}else {
							pWriter.println("500 File not found");
						}
					}else {
						pWriter.println("500 Invalid file ID");
					}
				}else {
					pWriter.println("500 Not Authenticated");
				}
				break;
				
			case "LOGOUT":
				if(isAuthenticated) {
					pWriter.println("200 Logged out successfully");
					isAuthenticated = false;
					socket.close();
					return;
				}else {
					pWriter.println("500 Not Authenticated");
				}
				break;
				
			default:
				pWriter.println("500 Unknown command");
				break;	
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private boolean matchUser(String username,String password)
    {
	boolean found = false;
	File userFile = new File("/RANTLE_B_221072246_CSC02B2_2024_P06/data/server/users.txt"/*OMITTED - Enter file location*/);
	try(Scanner scan = new Scanner(userFile))
	{
		//Code to search users.txt file for match with username and password
	    while(scan.hasNextLine()&&!found)
	    {
		String line = scan.nextLine();
		String lineSec[] = line.split("\\s");
    		
		//***OMITTED - Enter code here to compare user*** 
		if(lineSec[0].equals(username) && lineSec[1].equals(password)) {
			found = true;
		}
		
	    }
	}catch(IOException ex)
	{
	    ex.printStackTrace();
	}
	
	return found;
    }
    
    private ArrayList<String> getFileList()
    {
		ArrayList<String> result = new ArrayList<String>();
		//Code to add list text file contents to the arraylist.
		File lstFile = new File("data/server/PdfList.txt"/*OMITTED - Enter file location*/);
		try(Scanner scan = new Scanner(lstFile))
		{
			//***OMITTED - Read each line of the file and add to the arraylist***
			while(scan.hasNextLine()) {
				result.add(scan.nextLine());
			}
			
		}	    
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		return result;
    }
    
    private String idToFile(String ID)
    {
    	String result = "";
    	//Code to find the file name that matches strID
    	File lstFile = new File("data/server/PdfList.txt"/*OMITTED - Enter file location*/);
    	try(Scanner scan = new Scanner(lstFile))
    	{
    		while(scan.hasNextLine()) {
    			String line = scan.nextLine();
        		//***OMITTED - Read filename from file and search for filename based on ID***
        		String[] parts = line.split("\\s+");
        		if (parts[0].equals(ID)) {
    				result = parts[1];
    				break;
    			}
    		}

    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    	return result;
    }
}

