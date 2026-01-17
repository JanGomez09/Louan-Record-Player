import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddRecord {

    public int screen = 1;

    public String name = " ";
    public String artist = " ";
    public String year = " ";
    public String genre = " ";
    public Image cover = null;
    public String format = " ";

    public String sideAPath = "";
    public String sideBPath = "";

    

    public AddRecord() {
        // Configurar ventana
        JFrame frame = new JFrame("Add Record");
        frame.setSize(700, 425);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(18, 18, 18));
        frame.setLayout(null);
        

        JLabel setTitle = new JLabel("Add a new record to your collection");
        setTitle.setForeground(Color.WHITE);
        setTitle.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 25));
        setTitle.setBounds(20, 20, 450, 30);
        frame.add(setTitle);

        // Nombre 

        JLabel RecName = new JLabel("Record Name:");
        RecName.setForeground(Color.WHITE);
        RecName.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 20));
        RecName.setBounds(32, 75, 200, 30);
        frame.add(RecName);

        JTextField InputName = new JTextField();
        InputName.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 20));
        InputName.setBounds(32, 105, 275, 30);
        frame.add(InputName);

        // Artista

        JLabel ArtiName = new JLabel("Artist Name:");
        ArtiName.setForeground(Color.WHITE);
        ArtiName.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 20));
        ArtiName.setBounds(32, 150, 200, 30);
        frame.add(ArtiName);

        JTextField InputArtist = new JTextField();
        InputArtist.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 20));
        InputArtist.setBounds(32, 180, 275, 30);
        frame.add(InputArtist);
        // Año

        JLabel ReleasedIn = new JLabel("Released in:");
        ReleasedIn.setForeground(Color.WHITE);
        ReleasedIn.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 20));
        ReleasedIn.setBounds(32, 150 + 75, 200, 30);
        frame.add(ReleasedIn);

        JTextField InputYear = new JTextField();
        InputYear.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 20));
        InputYear.setBounds(32, 180 + 75, 200, 30);
        frame.add(InputYear);

        // Genero

        JLabel Genre = new JLabel("Genre:");
        Genre.setForeground(Color.WHITE);
        Genre.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 20));
        Genre.setBounds(32, 150 + 150, 200, 30);
        frame.add(Genre);

        JTextField InputGenre = new JTextField();
        InputGenre.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 20));
        InputGenre.setBounds(32, 180 + 150, 200, 30);
        frame.add(InputGenre);  

        // Caratula

        JLabel Image2 = new JLabel("Click here to upload cover image");
        Image2.setForeground(Color.WHITE);   
        Image2.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 15));
        Image2.setBounds(410, 275, 250, 30);
        frame.add(Image2);

        ImageIcon cov = new ImageIcon("txt_8.png");
        Image cover = cov.getImage().getScaledInstance(192, 192, java.awt.Image.SCALE_SMOOTH);
        JLabel covHolder = new JLabel(new ImageIcon(cover));
        covHolder.setBounds(460, 75, 192, 192);
        frame.add(covHolder);

        ImageIcon dis = new ImageIcon("txt_10.png");
        Image disc = dis.getImage().getScaledInstance(192, 192, java.awt.Image.SCALE_SMOOTH);
        JLabel vinDisc = new JLabel(new ImageIcon(disc));
        vinDisc.setBounds(360, 75, 192, 192);
        frame.add(vinDisc);
        vinDisc.setVisible(false);

        Image2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Image2.setForeground(Color.YELLOW);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                Image2.setForeground(Color.WHITE);
            }
            @Override
            public void mousePressed(MouseEvent e) {
            
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Selecciona una imagen");
                chooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Archivos de imagen (jpg, png)", "jpg", "jpeg", "png");
                chooser.addChoosableFileFilter(filter);

                int result = chooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    vinDisc.setVisible(true);
                    try {
                        Image img = ImageIO.read(selectedFile);
                        if (img != null) {
                            Image scaled = img.getScaledInstance(covHolder.getWidth(), covHolder.getHeight(), java.awt.Image.SCALE_SMOOTH);
                            covHolder.setIcon(new ImageIcon(scaled));
                            
                           
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Formato y tracklists

        JButton formatInput1 = new JButton("Single") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBackground().getAlpha() < 255) {
                    super.paintComponent(g);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        formatInput1.setFont(new Font("Eras Demi ITC", Font.BOLD, 25));
        formatInput1.setForeground(Color.BLACK);
        formatInput1.setBackground(Color.WHITE);
        formatInput1.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        formatInput1.setBounds(32, 105, 110, 40);
        formatInput1.setContentAreaFilled(false);
        formatInput1.setOpaque(false);
        formatInput1.setFocusPainted(false);

        formatInput1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                formatInput1.setBackground(Color.LIGHT_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                formatInput1.setBackground(Color.WHITE);
            }
            public void mousePressed(MouseEvent e) {
                format = "Single";
                RecName.setText("Format: " + format);
                vinDisc.setIcon(new ImageIcon("txt_11.png"));
            }
        });

        JButton formatInput2 = new JButton("EP") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBackground().getAlpha() < 255) {
                    super.paintComponent(g);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        formatInput2.setFont(new Font("Eras Demi ITC", Font.BOLD, 25));
        formatInput2.setForeground(Color.BLACK);
        formatInput2.setBackground(Color.WHITE);
        formatInput2.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        formatInput2.setBounds(148, 105, 100, 40);
        formatInput2.setContentAreaFilled(false);
        formatInput2.setOpaque(false);
        formatInput2.setFocusPainted(false);

        formatInput2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                formatInput2.setBackground(Color.LIGHT_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                formatInput2.setBackground(Color.WHITE);
            }
            public void mousePressed(MouseEvent e) {
                format = "EP";
                RecName.setText("Format: " + format);
                vinDisc.setIcon(new ImageIcon("txt_12.png"));
            }
        });

        JButton formatInput3 = new JButton("LP") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBackground().getAlpha() < 255) {
                    super.paintComponent(g);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        formatInput3.setFont(new Font("Eras Demi ITC", Font.BOLD, 25));
        formatInput3.setForeground(Color.BLACK);
        formatInput3.setBackground(Color.WHITE);
        formatInput3.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        formatInput3.setBounds(255, 105, 100, 40);
        formatInput3.setContentAreaFilled(false);
        formatInput3.setOpaque(false);
        formatInput3.setFocusPainted(false);
        
        formatInput3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                formatInput3.setBackground(Color.LIGHT_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                formatInput3.setBackground(Color.WHITE);
            }
            public void mousePressed(MouseEvent e) {
                format = "LP";
                RecName.setText("Format: " + format);
                vinDisc.setIcon(new ImageIcon("txt_10.png"));
            }
        });
        
        JLabel sideA = new JLabel("Click here to select the Side A");
        sideA.setForeground(Color.WHITE);   
        sideA.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 18));

        sideA.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sideA.setForeground(Color.YELLOW);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                sideA.setForeground(Color.WHITE);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Select an audio file for Side A");
                chooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter audioFilter = new FileNameExtensionFilter(
                        "Audio files (wav, ogg, flac, m4a)","wav", "ogg", "flac", "m4a", "aac");
                chooser.addChoosableFileFilter(audioFilter);

                int result = chooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    sideAPath = selectedFile.getAbsolutePath();
                    sideA.setText(selectedFile.getName());
                    sideA.setForeground(Color.WHITE);
                    System.out.println("Selected Side A: " + sideAPath);
                }
            }
        });
       

        JLabel sideB = new JLabel("Click here to select the Side B");
        sideB.setForeground(Color.WHITE);   
        sideB.setFont(new java.awt.Font("Eras Demi ITC", java.awt.Font.BOLD, 18));

        sideB.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    sideB.setForeground(Color.YELLOW);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    sideB.setForeground(Color.WHITE);
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Select an audio file for Side B");
                    chooser.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter audioFilter = new FileNameExtensionFilter(
                            "Audio files (wav, ogg, flac, m4a)", "wav", "ogg", "flac", "m4a", "aac");
                    chooser.addChoosableFileFilter(audioFilter);

                    int result = chooser.showOpenDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = chooser.getSelectedFile();
                        sideBPath = selectedFile.getAbsolutePath();
                        sideB.setText(selectedFile.getName());
                        sideB.setForeground(Color.WHITE);
                        System.out.println("Selected Side B: " + sideBPath);
                    }
                }
            });
            
       
        
        JButton nexButton = new JButton("Next") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBackground().getAlpha() < 255) {
                    super.paintComponent(g);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        nexButton.setFont(new Font("Eras Demi ITC", Font.BOLD, 25));
        nexButton.setForeground(Color.BLACK);
        nexButton.setBackground(Color.WHITE);
        nexButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        nexButton.setBounds(480, 325, 170, 40);
        nexButton.setContentAreaFilled(false);
        nexButton.setOpaque(false);
        nexButton.setFocusPainted(false);
        frame.add(nexButton);

        nexButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nexButton.setBackground(Color.LIGHT_GRAY);
                
            }
            @Override
            public void mouseExited(MouseEvent e) {
                nexButton.setBackground(Color.WHITE);
          
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if ((screen == 1) && !InputName.getText().isEmpty() && !InputArtist.getText().isEmpty() && !InputYear.getText().isEmpty() && !InputGenre.getText().isEmpty()) {
                    // Cambiar a pantalla 2 (tracklist)
                    screen = 2;

                    nexButton.setText("Add");
                    setTitle.setText("Add a Tracklist");

                    RecName.setText("Format: " + format);
                    frame.add(formatInput1);
                    frame.add(formatInput2);
                    frame.add(formatInput3);

                    ArtiName.setText("Side A");
                    sideA.setBounds(32, 190, 275, 30);
                    frame.add(sideA);

                    ReleasedIn.setText("Side B");
                    sideB.setBounds(32, 260, 275, 30);
                    frame.add(sideB);

                    Image2.setBounds(-909, 325, 170, 40);
                    
                    InputGenre.setBounds(-909, 325, 170, 40);
                    InputArtist.setBounds(-909, 325, 170, 40);
                    InputName.setBounds(-909, 325, 170, 40);
                    InputYear.setBounds(-909, 325, 170, 40);
                    Genre.setBounds(-909, 325, 170, 40);

                } else     if ((screen == 2) && !sideAPath.isEmpty() && !sideBPath.isEmpty() && !format.isEmpty()) {
            // Recolectar datos
            String name = InputName.getText().trim();
            String artist = InputArtist.getText().trim();
            String añoStr = InputYear.getText().trim();
            Object añoVal;
            try {
                añoVal = Integer.parseInt(añoStr);
            } catch (NumberFormatException ex) {
                añoVal = añoStr;
            }
            String genero = InputGenre.getText().trim();
            
            // Guardar archivos en el sistema
            GestorPersistencia gestor = new GestorPersistencia();
            
            // Guardar imagen de portada
            String coverImagePath = "";
            if (covHolder.getIcon() != null && covHolder.getIcon() instanceof ImageIcon) {
                // Obtener la ruta original si existe, o guardar la imagen actual
                coverImagePath = gestor.guardarImageIcon((ImageIcon) covHolder.getIcon(), name);
            }
            
            // Guardar audios
            String sideAPathGuardado = gestor.guardarAudio(sideAPath, name, "A");
            String sideBPathGuardado = gestor.guardarAudio(sideBPath, name, "B");
            
            // Crear el record con las rutas guardadas
            Map<String, Object> rec = new HashMap<>();
            rec.put("nombre", name);
            rec.put("artista", artist);
            rec.put("año", añoVal);
            rec.put("genero", genero);
            rec.put("format", format);
            rec.put("sideA", sideAPathGuardado);
            rec.put("sideB", sideBPathGuardado);
            
            // Guardar el ImageIcon
            if (covHolder.getIcon() != null) {
                rec.put("coverImage", covHolder.getIcon());
            }
            
            // Añadir al listado global en LongPlayer
            LongPlayer.records.add(rec);
            
            // Guardar todos los records inmediatamente
            LongPlayer.getInstance().guardarDatos2();
            
            System.out.println("Added and saved record: " + rec);
            
            // Cerrar la ventana
            frame.dispose();
        }
        // ... resto del código ...
    }
});
        frame.setVisible(true);

        
    }
}

