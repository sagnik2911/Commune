/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package test;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


public class LayeredPaneDemo extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLayeredPane layeredPane;
    private JLabel label;


    public LayeredPaneDemo() {
    	
    	
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        //Create and set up the layered pane.
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(451, 303));

        /*//This is the origin of the first label added.
        Point origin = new Point(10, 20);

        //This is the offset for computing the origin for the next label.
        int offset = 35;

        //Add several overlapping, colored labels to the layered pane
        //using absolute positioning/sizing.
        for (int i = 0; i < layerStrings.length; i++) {
            JLabel label = createColoredLabel(layerStrings[i],
                                              layerColors[i], origin);
            layeredPane.add(label, new Integer(i));
            origin.x += offset;
            origin.y += offset;
        }*/
                
        /*label = new JLabel("test");
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.BLUE);
        label.setForeground(Color.black);
        label.setBorder(BorderFactory.createLineBorder(Color.black));*/
        JPanel p = createSmileyPanel();
        p.setBounds(223, 104, 217, 140);
        p.setVisible(false);

        layeredPane.add(p, JLayeredPane.PALETTE_LAYER);
       
        
        
        JButton button = new JButton(":)");
        //button.setIcon(pic);
        button.setActionCommand("ON");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(e.getActionCommand().equals("ON")) {
        			button.setActionCommand("OFF");
        			p.setVisible(true);
        		}
        		else {
        			button.setActionCommand("ON");
        			p.setVisible(false);
        		}
        	}
        });
        button.setBounds(351, 244, 89, 23);
        layeredPane.add(button);
        
        //Add control pane and layered pane to this JPanel.
        //add(createSmileyPanel()); //Check panel :DEBUG
        add(layeredPane);
        
        JLabel lblShoumyaKantiDas = new JLabel("Shoumya Kanti Das");
        lblShoumyaKantiDas.setFont(new Font("Monotype Corsiva", Font.PLAIN, 11));
        lblShoumyaKantiDas.setBounds(10, 278, 102, 14);
        layeredPane.add(lblShoumyaKantiDas);
    }

    //Create the smiley JPanel.
    private JPanel createSmileyPanel() {
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridLayout(3, 3));
    	panel.setPreferredSize(new Dimension(66, 66));
    	Icon[] icons = new Icon[9];
    	JButton[] btns = new JButton[9];
    	for(int i=0;i<9;i++){
    		URL url = LayeredPaneDemo.class.getResource("/res/"+(i+1)+".png");
    		icons[i] = new ImageIcon(url);
    		btns[i] = new JButton(icons[i]);
    		btns[i].setSize(32, 32);
    		panel.add(btns[i]);
    	}
    	panel.setOpaque(true);   	
    	return panel;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("EmoticonPanelTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new LayeredPaneDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
