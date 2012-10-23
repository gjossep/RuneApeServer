package server;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Screen {

	private JFrame frmRuneapeServer;
	static JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void start(JTextArea text) {
		textArea = text;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Screen window = new Screen();
					window.frmRuneapeServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Screen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRuneapeServer = new JFrame();
		frmRuneapeServer.setTitle("RuneApe Server");
		frmRuneapeServer.setBounds(100, 100, 585, 443);
		frmRuneapeServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRuneapeServer.getContentPane().setLayout(null);
		
		JLabel lblRuneapeServer = new JLabel("RuneApe Server");
		lblRuneapeServer.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblRuneapeServer.setBounds(204, 6, 173, 35);
		frmRuneapeServer.getContentPane().add(lblRuneapeServer);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 43, 537, 331);
		frmRuneapeServer.getContentPane().add(scrollPane);
		
		
		scrollPane.setViewportView(textArea);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				System.exit(0);
			}
		});
		btnStop.setBounds(239, 386, 117, 29);
		frmRuneapeServer.getContentPane().add(btnStop);
	}
	
	static void updateTextArea(final String text) {
		  SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		      textArea.append(text);
		    }
		  });
		}
}
