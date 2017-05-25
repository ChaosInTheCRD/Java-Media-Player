package client;
/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2016 Caprica Software Limited.
 */



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class PlayerControlsPanel extends JPanel {
    // private static final long serialVersionUID = 1L;
    //private static final int SKIP_TIME_MS = 10 * 1000;
    //private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final EmbeddedMediaPlayer mediaPlayer;

    private JButton previousVideoButton;
    private JButton stopButton;
    private JButton playButton;
    private JButton nextVideoButton;

    private JButton toggleMuteButton;
    private JSlider volumeSlider;
    private boolean PlayOrPause = false;
    private boolean MuteorSound = false;
    ImageIcon Play = new ImageIcon("Film_Pics/Icons/Play.png");
    ImageIcon Pause = new ImageIcon("Film_Pics/Icons/Pause.png");
    ImageIcon Mute = new ImageIcon("Film_Pics/Icons/Mute.png");
    ImageIcon Sound = new ImageIcon("Film_Pics/Icons/Unmute.png");

    public PlayerControlsPanel(EmbeddedMediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        createUI();
        
    }

    private void createUI() {
        createControls();
        layoutControls();
        registerListeners();
    }

    private void createControls() {

        previousVideoButton = new JButton();
        previousVideoButton.setIcon(new ImageIcon("Film_Pics/Icons/Previous.png"));
        previousVideoButton.setToolTipText("Go to previous chapter");
        previousVideoButton.setBorderPainted(false); 
        previousVideoButton.setContentAreaFilled(false); 
        previousVideoButton.setFocusPainted(false); 
        previousVideoButton.setOpaque(false);
        previousVideoButton.setActionCommand("2");
        
        stopButton = new JButton();
        stopButton.setIcon(new ImageIcon("Film_Pics/Icons/Stop.png"));
        stopButton.setToolTipText("Stop");
        stopButton.setBorderPainted(false); 
        stopButton.setContentAreaFilled(false); 
        stopButton.setFocusPainted(false); 
        stopButton.setOpaque(false);

        /*pauseButton = new JButton();
        pauseButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/control_pause_blue.png")));
        pauseButton.setToolTipText("Play/pause");*/

        playButton = new JButton();
        playButton.setIcon(new ImageIcon("Film_Pics/Icons/Pause.png"));
        playButton.setToolTipText("Play");
        playButton.setBorderPainted(false); 
        playButton.setContentAreaFilled(false); 
        playButton.setFocusPainted(false); 
        playButton.setOpaque(false);


        nextVideoButton = new JButton();
        nextVideoButton.setIcon(new ImageIcon("Film_Pics/Icons/Next.png"));
        nextVideoButton.setToolTipText("Go to next chapter");
        nextVideoButton.setBorderPainted(false); 
        nextVideoButton.setContentAreaFilled(false); 
        nextVideoButton.setFocusPainted(false); 
        nextVideoButton.setOpaque(false);
        nextVideoButton.setActionCommand("1");


        toggleMuteButton = new JButton();
        toggleMuteButton.setIcon(new ImageIcon("Film_Pics/Icons/Mute.png"));
        toggleMuteButton.setToolTipText("Toggle Mute");
        toggleMuteButton.setBorderPainted(false); 
        toggleMuteButton.setContentAreaFilled(false); 
        toggleMuteButton.setFocusPainted(false); 
        toggleMuteButton.setOpaque(false);

        volumeSlider = new JSlider();
        volumeSlider.setOrientation(JSlider.HORIZONTAL);
        volumeSlider.setMinimum(LibVlcConst.MIN_VOLUME);
        volumeSlider.setMaximum(LibVlcConst.MAX_VOLUME);
        volumeSlider.setPreferredSize(new Dimension(100, 40));
        volumeSlider.setToolTipText("Change volume");
        volumeSlider.setOpaque(false);

    }
    private void layoutControls() {
    	
        setBorder(new EmptyBorder(0, 0, 0, 0));

        setLayout(new BorderLayout());

        JPanel positionPanel = new JPanel();
        positionPanel.setLayout(new GridLayout(1, 1));


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(8, 0));

        topPanel.add(positionPanel, BorderLayout.NORTH);


        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER,40,20));
        
       
        
        bottomPanel.add(previousVideoButton);
        bottomPanel.add(stopButton);
        bottomPanel.add(playButton);
        bottomPanel.add(toggleMuteButton);
        bottomPanel.add(nextVideoButton);

        bottomPanel.add(volumeSlider);
       
        
        add(bottomPanel, BorderLayout.NORTH);
        
        //CHANING COLORS!
        bottomPanel.setBackground(Color.BLACK);
        topPanel.setBackground(Color.BLACK);
        positionPanel.setBackground(Color.BLACK);
    }

    
    public void setButtonsActionListener(ActionListener listener)
    {

    	   nextVideoButton.addActionListener(listener);
    	   previousVideoButton.addActionListener(listener);
 
    }

    private void registerListeners() {
    	
    	
        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
            }
        });

        previousVideoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.previousChapter();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaPlayer.stop();
                playButton.setIcon(Play);
                PlayOrPause = true;
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(PlayOrPause == false)
            	{
                mediaPlayer.pause();
                playButton.setIcon(Play);
                PlayOrPause = true;
            	}
            	else
            	{
            		mediaPlayer.play();
            		playButton.setIcon(Pause);
            		PlayOrPause = false;
            	}
            }
        });
        
        toggleMuteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               	if(MuteorSound == false)
            	{
                mediaPlayer.mute();
                toggleMuteButton.setIcon(Sound);
                MuteorSound = true;
            	}
               	
            	else
            	{
            		mediaPlayer.mute();
            		toggleMuteButton.setIcon(Mute);
            		MuteorSound = false;
            	}
            }
        });

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                mediaPlayer.setVolume(source.getValue());

            }
        });
    }

    private void updateVolume(int value) {
        volumeSlider.setValue(value);
    }
}