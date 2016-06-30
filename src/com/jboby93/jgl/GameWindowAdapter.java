package com.jboby93.jgl;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

public class GameWindowAdapter extends WindowAdapter {
	public GameWindowAdapter() {
		super();
	}
	
	public void windowClosing(WindowEvent e) {
		//only show the message if the window is not shutting down
		String closePrompt = ((GameWindow) e.getWindow()).getClosePrompt();
		
		if(!((GameWindow) e.getWindow()).getIsShuttingDown()) {
			LibInfo.dbg("GameWindowAdapter.windowClosing(): preparing to show exit prompt");
			((GameWindow) e.getWindow()).exitPromptShown = true;
			switch(JOptionPane.showOptionDialog(e.getWindow(),
					closePrompt, "Confirm Exit", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null)) {
				case JOptionPane.YES_OPTION:
					//isShuttingDown = true;
					LibInfo.dbg("GameWindowAdapter.windowClosing(): user chose yes; setting isRunning to false");
					((GameWindow) e.getWindow()).setIsRunning(false);
					
					//clean up
					//Shutdown();
					//this.dispose();
					break;
				case JOptionPane.NO_OPTION:
					//do nothing
					LibInfo.dbg("GameWindowAdapter.windowClosing(): user chose no");
					//bug fix: release the ESC so the dialog isn't shown again
					((GameWindow) e.getWindow()).getWindowInputHandler().keyReleased(KeyEvent.VK_ESCAPE);
					break;
				case JOptionPane.CLOSED_OPTION:
					//user clicked X, treat the same as No
					LibInfo.dbg("GameWindowAdapter.windowClosing(): user closed the dialog");
					//bug fix: release the ESC so the dialog isn't shown again
					((GameWindow) e.getWindow()).getWindowInputHandler().keyReleased(KeyEvent.VK_ESCAPE);
					break;
			} //end switch
			((GameWindow) e.getWindow()).exitPromptShown = false;
		} else {
			e.getWindow().dispose();
		}
	} //end windowClosing()
} //end class GameWindowAdapter
