//Ref: https://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java#2433454
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class MakeSound {
   JFrame frame;
   SPanel panel;
   public static void main(String[] args)
   {
      (new MakeSound()).playSound("lr.wav");
   }
   
   //The buffer decided how much we read/write at a time.
   private final int BUFFER_SIZE = (int)(44100*4/50);
   private File soundFile;
   private AudioInputStream audioStream;
   private AudioFormat audioFormat;
   private SourceDataLine sourceLine;
   private boolean debug = false;

   public MakeSound()
   {
      frame = new JFrame();
      frame.setResizable(false);
      frame.setSize(600, 300);
      frame.setTitle("MakeSound");
	  frame.setLayout(null);
      frame.setVisible(true);
      //We will close Java when the screen is exited
      frame.addWindowListener(
                new WindowAdapter()
                {
                   public void windowClosed(WindowEvent e)
                   {
                      if(debug) System.out.println("windowClosed");
                      Runtime.getRuntime().exit(0);
                   }
                
                   public void windowClosing(WindowEvent e)
                   {
                      if(debug) System.out.println("windowClosing");
                      Runtime.getRuntime().exit(0);
                   }
                });
      panel = new SPanel(600, 300);
      panel.setBounds(0, 0, 600, 300);
      frame.add(panel);
   
   }
    /**
     * @param filename the name of the file that is going to be played
     */
   public void playSound(String filename){
   
      String strFilename = filename;
   
      try {
         soundFile = new File(strFilename);
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
   
      try {
         audioStream = AudioSystem.getAudioInputStream(soundFile);
      } catch (Exception e){
         e.printStackTrace();
         System.exit(1);
      }
   
      audioFormat = audioStream.getFormat();
      if(debug) System.out.println("Format: " + audioFormat.toString());
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
      try {
         sourceLine = (SourceDataLine) AudioSystem.getLine(info);
         sourceLine.open(audioFormat);
      } catch (LineUnavailableException e) {
         e.printStackTrace();
         System.exit(1);
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
   
      sourceLine.start();
   
      int nBytesRead = 0;
      byte[] abData = new byte[BUFFER_SIZE];
      byte a, b,c, d;
      long avgright = 0, avgleft = 0, tright, tleft, right = 0, left = 0;
      int first = 0;
      long time = 0;
      int sleeptime = 0;
      while (nBytesRead != -1) {
         try {
            if(debug) System.out.println("Reading data..");
            nBytesRead = audioStream.read(abData, 0, abData.length);
         } catch (IOException e) {
            e.printStackTrace();
         }
         right = 0; left = 0; a = 0; b = 0; c = 0; d = 0; tleft = 0; tright = 0; avgleft = 0; avgright = 0;
         if (nBytesRead >= 0) {
            for(int i = 0; i < nBytesRead-4; i=i+4)
            {
               a = abData[i+0];
               b = abData[i+1];
               c = abData[i+2];
               d = abData[i+3]; 
               if(audioFormat.isBigEndian())
               {
                  right += ((abData[i+0] & 0xffL) <<  8) |  (abData[i+3] & 0xffL);
                  left += ((abData[i+1] & 0xffL) <<  8) |  (abData[i+2] & 0xffL);
               }
               else
               {
                  tleft = ((long)abData[i+0] & 0xFF) + ((long)(abData[i+1] & 0xFF) <<  8);
                  tright = ((long)abData[i+2] & 0xFF) + (((long)abData[i+3] & 0xFF) <<  8);
                  avgright += Math.abs(tright);
                  avgleft += Math.abs(tleft);
                  //right = tright;
                  if(tright > right && a >= 0 && d >= 0)
                  {
                     right = tright;
                     if(debug) System.out.println(Long.toString(time) + ": " + "Right: " + left + " " + right + " " + Long.toBinaryString(tright) + " " + Long.toBinaryString(a) + " " + Long.toBinaryString(b) + " " + Long.toBinaryString(c) + " " + Long.toBinaryString(d));
                  }

                  //left = tleft;
                  if(tleft > left && b >= 0 && c >= 0)
                  {
                     left = tleft;
                     if(debug) System.out.println(Long.toString(time) + ": " + "Left: " + left + " " + right + " " + Long.toBinaryString(tleft) + " " + Long.toBinaryString(a) + " " + Long.toBinaryString(b) + " " + Long.toBinaryString(c) + " " + Long.toBinaryString(d));
                  }
               
               }
            }
            if(first < 5)
            {
               try
               {
                  sleeptime = (int)(((double)BUFFER_SIZE/(44100*4))*1000);
                  if(debug) System.out.println("Sleeptime " + sleeptime);
                  Thread.sleep(sleeptime);
               }
               catch(Exception e){}
            }
            time += sleeptime;
         
            panel.setLeft(avgleft/(nBytesRead/4));
            panel.setRight(avgright/(nBytesRead/4));
            //panel.repaint();
         
            panel.setPosition();
            first++;
            @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
         
         }
      }
   
      sourceLine.drain();
      sourceLine.close();
   }
    
   public static long byteToShortLE(long b) 
   {
      long value = 0;
   
      value = (b & 0x000000FF) << (8);           
   
      return value;
   }    
}

class SPanel extends JPanel
{
   int width = 0;
   int height = 0;
   int[] amplitudesleft;
   int[] amplitudesright;
   int position;
   BufferedImage img;
   int left = 0;
   int right = 0;
   public static final int MAXAMPLITUDE = 65534; // 2^16
   
   public SPanel()
   {
      this(1300, 700);
   }
   
   public SPanel(int _width, int _height)
   {
      width = _width;
      height = _height-30;
      position = 0;
      amplitudesleft = new int[_width];
      amplitudesright = new int[_width];
      for(int i = 0; i < _width; i++)
      {
         amplitudesleft[i] = 0;
         amplitudesright[i] = 0;
      }
      img = new BufferedImage(_width, _height, BufferedImage.TYPE_3BYTE_BGR);
      Graphics g = img.getGraphics();
      g.setColor(Color.CYAN);
      g.dispose();
   }
   
   public void paint(Graphics g)
   {
      g.drawImage(img, 0, 0, null);   
   }
   
   public void setPosition()
   {
      position++;
      Graphics g = img.getGraphics();
      g.drawImage(img, -1, 0, null);
         //amplitudesleft: Yellow
         //amplitudesright: blå
         //Overlap: Green      
         if(left > right)
         {
            g.setColor(Color.BLACK);
            g.drawLine(width-1,(int)(0), width-1, height);
            g.setColor(Color.YELLOW);
            g.drawLine(width-1,(int)(height-left), width-1, height);
            g.setColor(Color.GREEN);
            g.drawLine(width-1,(int)(height-right), width-1, height);
         }
         else if(left == right)
         {
            g.setColor(Color.BLACK);
            g.drawLine(width-1,(int)(0), width-1, height);
            g.setColor(Color.GREEN);
            g.drawLine(width-1,(int)(height-right), width-1, height);
         }
         else
         {
            g.setColor(Color.BLACK);
            g.drawLine(width-1,(int)(0), width-1, height);
            g.setColor(Color.BLUE);
            g.drawLine(width-1,(int)(height-right), width-1, height);
            g.setColor(Color.GREEN);
            g.drawLine(width-1,(int)(height-left), width-1, height);
         }
      repaint();         
   }
   
   public void setLeft(long _left)
   {  
      left = (int)Math.min((((double)_left/(double)MAXAMPLITUDE) * height), height);
   }
   
   public void setRight(long _right) 
   {  
      right = (int)Math.min((((double)_right/(double)MAXAMPLITUDE) * height), height);
   }
}