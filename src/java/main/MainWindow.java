package src.java.main;

import src.java.exceptions.InvalidPathException;
import src.java.utils.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class MainWindow {
    private File vanillaPath = null;
    private File modPath = null;

    private GreatOrganizer greatOrganizer;

    private double dragStartX, dragStartY;
    private int imageCursorX, imageCursorY;

    private MapManager mapManager;

    private final JButton provinceButton = new JButton("Provinces");
    private final JButton stateButton = new JButton("States");
    private final JButton countryButton = new JButton("Countries");

    public MainWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();        // TODO
        }

        JFrame frame = new JFrame("Custom Layout with Images");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.GRAY);

        createFrameBody(frame);

        frame.setSize(1080, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createFrameBody(JFrame frame) {
        JLabel coordsPanel = createCoordsPanel();

        frame.add(createNorthPanel(), BorderLayout.NORTH);
        frame.add(createEastPanel(), BorderLayout.EAST);
        frame.add(createWestPanel(frame, coordsPanel), BorderLayout.WEST);
        frame.add(createSouthPanel(coordsPanel), BorderLayout.SOUTH);
    }


    private JPanel createEastPanel() {
        return createSidePanel(Color.DARK_GRAY);
    }
    private JPanel createWestPanel(JFrame frame, JLabel coordsPanel) {
        JPanel westPanel =  createSidePanel(Color.DARK_GRAY);
        westPanel.setLayout(new GridLayout(10,1));
        westPanel.add(createVanillaPathButton(frame));
        westPanel.add(createModPathButton(frame));
        westPanel.add(createLoadButton(frame, coordsPanel));
        return westPanel;
    }
    private JPanel createSouthPanel(JLabel coordsPanel) {
        JPanel southPanel = createSidePanel(Color.DARK_GRAY);
        southPanel.setLayout(new BorderLayout());
        JPanel subPanel = createSouthSubPanel();
        subPanel.add(coordsPanel, BorderLayout.NORTH);
        southPanel.add(subPanel, BorderLayout.EAST);
        return southPanel;
    }
    private JPanel createNorthPanel() {
        return createButtonPanel();
    }


    private JPanel createSouthSubPanel() {
        JPanel subPanel = new JPanel();

        subPanel.setLayout(new BorderLayout());
        subPanel.setPreferredSize(new Dimension(100, 50));
        subPanel.setBackground(Color.DARK_GRAY);

        return subPanel;

    }
    private JLabel createCoordsPanel() {
        JLabel coordsPanel = new JLabel();

        coordsPanel.setText("-, -");
        coordsPanel.setForeground(Color.LIGHT_GRAY);

        return coordsPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(provinceButton);
        buttonPanel.add(stateButton);
        buttonPanel.add(countryButton);
        return buttonPanel;
    }

    private JPanel createSidePanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(100, 100));
        return panel;
    }

    private void resetVariables() {
        dragStartX = 0;
        dragStartY = 0;
        imageCursorX = 0;
        imageCursorY = 0;
    }

    private void setupImagePanelListeners(ImagePanel imagePanel, JLabel coordsPanel) {
        imagePanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                imageCursorX = (int)((e.getX() - imagePanel.getOffsetX()) / imagePanel.getZoomFactor());
                imageCursorY = (int)((e.getY() - imagePanel.getOffsetY()) / imagePanel.getZoomFactor());
                coordsPanel.setText(String.format("[%d, %d]", imageCursorX, imageCursorY));
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                imageCursorX = (int)((e.getX() - imagePanel.getOffsetX()) / imagePanel.getZoomFactor());
                imageCursorY = (int)((e.getY() - imagePanel.getOffsetY()) / imagePanel.getZoomFactor());
                coordsPanel.setText(String.format("[%d, %d]", imageCursorX, imageCursorY));
            }
        });

        imagePanel.addMouseWheelListener(imagePanel::increaseZoomFactor);

        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON2) {
                    dragStartX = e.getX() - imagePanel.getOffsetX();
                    dragStartY = e.getY() - imagePanel.getOffsetY();
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    imagePanel.repaintPixel(imageCursorX, imageCursorY, Color.CYAN);
                }
            }
        });

        imagePanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) != 0) {
                    imagePanel.setOffsetX(e.getX() - dragStartX);
                    imagePanel.setOffsetY(e.getY() - dragStartY);
                    imagePanel.repaint();
                } else if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                    imagePanel.repaintPixel(imageCursorX, imageCursorY, Color.CYAN);
                }
            }
        });
    }
    private JButton createVanillaPathButton(Frame frame) {
        JButton vanillaPathButton = new JButton("<html><body style='text-align:center;'>Choose vanilla path</body></html>");

        vanillaPathButton.addActionListener(_ ->{
            Message msg = new Message("vanillaPathButton");
            msg.sendStartMessage("Started choosing path...");

            JFileChooser chooser = new JFileChooser();

            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int response = chooser.showOpenDialog(frame);

            if (response == JFileChooser.APPROVE_OPTION) {
                vanillaPath = chooser.getSelectedFile();
                msg.sendFormattedMessage("Path chosen: " + vanillaPath.getAbsolutePath());
                try {
                    greatOrganizer = new GreatOrganizer(vanillaPath);
                } catch (InvalidPathException e) {
                    JOptionPane.showMessageDialog(frame, "Invalid path: " + e.getMessage());
                }
            } else {
                msg.sendFormattedMessage("No path chosen");
            }
            msg.sendEndMessage();
        });

        return vanillaPathButton;
    }

    private JButton createModPathButton(Frame frame) {
        JButton modPathButton = new JButton("<html><body style='text-align:center;'>Choose mod path</body></html>");

        // TODO:
        modPathButton.addActionListener(_ ->{
            Message msg = new Message("modPathButton");
            msg.sendStartMessage("Started choosing path...");

            JFileChooser chooser = new JFileChooser();

            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int response = chooser.showOpenDialog(frame);

            if (response == JFileChooser.APPROVE_OPTION) {
                modPath = chooser.getSelectedFile();
                msg.sendFormattedMessage("Path chosen: " + modPath.getAbsolutePath());
            } else {
                msg.sendFormattedMessage("No path chosen");
            }
            msg.sendEndMessage();
        });

        return modPathButton;
    }

    // TODO memory leak
    private JButton createLoadButton(JFrame frame, JLabel coordsPanel) {
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(_ -> {
            if (greatOrganizer == null) {
                JOptionPane.showMessageDialog(frame, "No vanilla path");
                return;
            }

            resetVariables();

            if (mapManager != null) {
                frame.getContentPane().remove(mapManager.getImagePanel());
//                frame.revalidate();
            }

            try {
                mapManager = new MapManager(greatOrganizer);
            } catch (IOException e) {
                throw new RuntimeException(e); // TODO better error handling
            }


            ImagePanel imagePanel = mapManager.getImagePanel();
            setupImagePanelListeners(imagePanel, coordsPanel);

            frame.getContentPane().remove(imagePanel);
            frame.add(imagePanel, BorderLayout.CENTER);
            imagePanel.setBackground(Color.LIGHT_GRAY);

            for (ActionListener listener : provinceButton.getActionListeners())
                provinceButton.removeActionListener(listener);

            provinceButton.addActionListener(_ -> mapManager.changeToProvinces());

            for (ActionListener listener : stateButton.getActionListeners())
                stateButton.removeActionListener(listener);

            stateButton.addActionListener(_ -> mapManager.changeToStates());

            for (ActionListener listener : countryButton.getActionListeners())
                countryButton.removeActionListener(listener);

            countryButton.addActionListener(_ -> mapManager.changeToCountries());

            frame.revalidate();
            frame.repaint();
            frame.setVisible(true);
        });
        return loadButton;
    }
    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        editMenu.add(cutItem);
        JMenuItem copyItem = new JMenuItem("Copy");
        editMenu.add(copyItem);
        JMenuItem pasteItem = new JMenuItem("Paste");
        editMenu.add(pasteItem);
        return editMenu;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        fileMenu.add(newItem);
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        return fileMenu;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        return menuBar;
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}