import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


public class GUI {
	
	 public GUI() {
		 SwingUtilities.invokeLater(new Runnable() {
			 @Override
			 public void run() {
				 initWindow();
			 }
		 });
	 }
	 
	 private void initWindow() {
		 JFrame window = new JFrame();
	        window.setSize(800, 480);
	        window.setResizable(false);
	        JPanel mainPanel = new JPanel(new BorderLayout());
	        
	        Sobel_operator gaussBlur = new Sobel_operator("src\\res\\blueCat.jpg");
	        
	        //Image scrolableImage = gaussBlur.convertInBufferedImage(gaussBlur.getImage());
	        
	        Image scrolableImage = gaussBlur.c();
	        
	        ImageIcon scrolableImg = new ImageIcon(scrolableImage);
	        JLabel picLabel = new JLabel(scrolableImg);
	        
	        JScrollPane scroller = new JScrollPane(new JLabel(scrolableImg));
	        scroller.setAutoscrolls(true);
	        mainPanel.add(picLabel);
	        window.setLocationRelativeTo(null);
	        window.setVisible(true);
	        scroller.setPreferredSize(window.getContentPane().getSize());
	        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        window.getContentPane().add(mainPanel);
	        
	 }
	
}
