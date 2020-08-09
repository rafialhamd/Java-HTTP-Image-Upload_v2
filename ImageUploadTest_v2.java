/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageuploadtest_v2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.*;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 *
 * @author Administrator
 */
public class ImageUploadTest_v2 {
    
    
    

    public static String ConvertByteArrayToString(byte[] data, int size)
    {
        String result = "";

        for (int i = 0; i < size; i++)
        {
            result += (char)data[i];
        }

        return result;
    }

    public static String GetFileContents(String filename)
    {
        String result = "";

        try
        {
            
           String path = System.getProperty("user.dir") + "\\src\\imageuploadtest_v2\\" + filename;
           RandomAccessFile f = new RandomAccessFile(path, "rw");

           while (true)
           {
               byte[] data = new byte[1000];
               int size = f.read(data);


               if (size == -1) 
               {
                   break;
               }

               result += ConvertByteArrayToString(data, size);

           }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return result;
    }
    
    public static void SaveToFile(String filename, String string)
    {
        try
        {
            
           String path = System.getProperty("user.dir") + "\\" + filename;
           
           
           
           
           File fl = new File(path);
           if (fl.exists())
           {
               fl.delete();
           }
           
           
          RandomAccessFile f = new RandomAccessFile(path, "rw");
           byte[] imageByteArray = Base64.getMimeDecoder().decode(string);
           
           
          f.write(imageByteArray);
          
           
           f.close();
           
           
           
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    

    
    public static void StartServer()
    {
        try
        {
                
                                
		ServerSocket server = new ServerSocket(80);
                
                
                while (true)
                {
                    
                    Socket socket = server.accept();
                                        
                    new Thread(() -> 
                    {
                        boolean image_data = false;
                        String string = "";
                        
                        
                        while (true)
                        {

                            try
                            {
                                
                                
                                if (socket.isClosed() == true || socket.isConnected() == false) 
                                {                   
                                    
                                    break;
                                } 
                                
                                

                                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                               // ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                                BufferedOutputStream dataOut = new BufferedOutputStream(socket.getOutputStream());
                                
                                PrintWriter out = new PrintWriter(socket.getOutputStream());

                            //    System.out.println(ois.available());

                                String input = "";

                                while (true)
                                {
                                    byte[] data = new byte[10000];
                                    int size = bis.read(data);
                                    if (size == -1) break;
                                    
                                    for (int i = 0; i < size; i++)
                                    {
                                        char c = (char)data[i];
                                        
                                        
                                        
                                        if (c != '\n')
                                        {                                            
                                            input += c;
                                        }
                                        else
                                        {
                                            
                                            
                                              
                                              if (image_data == false)
                                              {
                                              System.out.println(input);
                                              
                                                if (input.contains("Content-Disposition: form-data; name=\"afile\""))
                                                {
                                                    image_data = true;
                                                    
                                                      System.out.println("upload started: " + Calendar.getInstance().getTime());
                                                }
                                              }
                                              else
                                              {
                                                  if (input.contains("-----"))
                                                  {
                                                      
                                                      
                                                      SaveToFile("mydata.jpg", string);
                                                      
                                                      
//                                                      for (int k = 0; k < string.length(); k++)
//                                                      {
//                                                          System.out.print(string.charAt(k));
//                                                      }
//                                                      
//                                                      System.out.println();
                                                      
                                                      
                                                      System.out.println("upload complete: " + Calendar.getInstance().getTime());
                                                      image_data = false;
                                                      break;
                                                  }
                                                  else
                                                  {
                                                      if (input.length() > 1)
                                                      {
                                                          if (string.length() == 0)
                                                          {
//                                                              for (int i2 = 0; i2 < 100; i2++)
//                                                              {
//                                                                  System.out.print((int)input.charAt(i2) + "  ");
//                                                              }
                                                              
                                                              for (int i2 = 0; i2 < input.length(); i2++)
                                                              {
                                                                  if (input.charAt(i2) == ',')
                                                                  {
                                                                      input = input.substring(i2 + 1, input.length());
                                                                      break;
                                                                  }
                                                              }
                                                              
                                                              string += input;
                                                              
                                                          }
                                                          else
                                                          {
                                                                string += input;
                                                          }
                                                      }
                                                      
                                                  }
                                              }
                                              
                                             
                                            /*
                                            if (image_data == false)
                                            {
                                              //  System.out.println(input);
                                                
                                                
                                               // if (input.contains("Content-Disposition: form-data; name=\"the_file\""))
                                                //if (input.contains("Content-Disposition: form-data; name=\"afile\""))
                                              if (input.contains("Content-Type: image/jpeg"))
                                                {
                                                   // System.out.println(input);
                                                    image_data = true;
                                                }
                                            }
                                            else
                                            {
                                                
                                                 if (input.contains("-----"))
                                                {
                                                   // os.
                                                    
                                                    int dsize = string.length() / 1024;
                                                    System.out.print("file size: " + dsize);
                                                    System.out.print("string size: " + string.length());
                                                    
                                                    SaveToFile("mydata.jpg", string);
                                                
                                                    image_data = false;
                                                    
                                                    String response = GetFileContents("index.html");

                                                    int responseLength = response.length();

                                                        // send HTTP Headers
                                                    out.println("HTTP/1.1 200 OK");
                                                    out.println("Server: RBTrading_version4");
                                                    out.println("Date: " + new Date());
                                                    out.println("Content-type: " + "text/html");
                                                   out.println("Content-length: " + responseLength);
                                                    out.println(); 
                                                    out.flush(); 

                                                    dataOut.write(response.getBytes(), 0, responseLength);
                                                    dataOut.flush();
                                                
                                                    System.out.println("upload complete");
                                                    break;
                                                }
                                                 else
                                                 {
                                                     
                                                     string += input;
                                              //       os.write(input.getBytes());
                                                     
                                                     if (input.length() > 0)
                                                     {
                                                         
                                                     }
                                                 }
                                            }
                                            
                                            */
                                            
                                            
                                            if (input.contains("GET / HTTP/1.1"))
                                            {
                                                
                                                String response = GetFileContents("index.html");

                                                int responseLength = response.length();
                                                
                                              //  System.out.println(responseLength);

                                                // send HTTP Headers
                                                out.println("HTTP/1.1 200 OK");
                                                out.println("Server: RBTrading_version4");
                                                out.println("Date: " + new Date());
                                                out.println("Content-type: " + "text/html");
                                                out.println("Content-length: " + responseLength);
                                                out.println(); 
                                                out.flush(); 

                                                dataOut.write(response.getBytes(), 0, responseLength);
                                                dataOut.flush();
                                            }
                                            input = "";
                                            
                                        }
                                    }
                                    
                                }

                                

                            }
                            catch (Exception ex)
                            {
                                
                                if (ex.getMessage() != null && ex.getMessage().contains("Connection reset"))
                                {
                                    break;
                                }
                                
                            }

                        }
                        
                        
                        
                        
                    }).start();

                    
                }
                
                
        }
        catch (Exception ex)
        {
            System.out.println("ex 2");
            ex.printStackTrace();
        }
    }

    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        // **** remember to compile before running..
        
        StartServer();
        
        
    }
    
    
    
    
    
}

