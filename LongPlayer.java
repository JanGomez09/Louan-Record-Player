import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;


public class LongPlayer extends JFrame implements KeyListener {

    private static GestorPersistencia gestorPersistencia;
    private static final String DATA_DIR = System.getProperty("user.home") + "/.longPlayerRecords";

    private static boolean isPlaying = false;
    private static JLabel ArmPlay1;
    private JLabel TogPlay;
    static JLabel AddPlay;
    private JPanel RectAng;
    private static RotatableLabel ArmPlay2;
    private static RotatableLabel LongPlay;
    private Timer rotationTimer;
    private double longPlayRotationAngle = 0;
    private boolean toogle = false;
    private JPanel recordsPanel;
    private JScrollPane recordsScrollPane;
    private static JLabel PlayBtn;
    private static JLabel RewindBtn;
    private static JLabel ForwardBtn;
    private static JLabel FlipBtn;
    private static JLabel StopBtn;
    private static JLabel ForwardFordBtn;
    private static JLabel RewindRewBtn;
    private static JLabel CoverBox;
    private static Boolean sided = false;
    private static String sideA;
    private static String sideB;
    private static JLabel CoverTitle = new JLabel("Space Oddity");
    private static JLabel CoverSide = new JLabel("David Bowie");
    private static JLabel CoverArtist = new JLabel("Side - A");
    
    // Variables para controlar el audio
    private static Clip currentClip;
    private static long clipPosition = 0; // Posición actual cuando se pausa
    private static String currentAudioPath = ""; // Ruta del audio actual
    private static LongPlayer instance; // Instancia única para acceder desde métodos estáticos

    // Lista global de records accesible desde otras clases
    public static List<Map<String,Object>> records = new ArrayList<>();

    public LongPlayer() {
        instance = this; // Guardar referencia a la instancia actual

        gestorPersistencia = new GestorPersistencia();
    
        // Cargar records guardados al iniciar
        records = gestorPersistencia.cargarRecords();
        
        // Configurar ventana
        setTitle("Louan Record Player");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                guardarDatos();
                System.exit(0);
            }
        });

        
        // Imagenes y texturas del Toca discos
        ImageIcon rp = new ImageIcon("text_1.png");
        Image rpl = rp.getImage().getScaledInstance(412,274,Image.SCALE_SMOOTH);
        JLabel RecPlay = new JLabel(new ImageIcon(rpl));
        RecPlay.setBounds(80,45,412,274);

        ImageIcon hdl = new ImageIcon("txt_30.png");
        Image hdll = hdl.getImage().getScaledInstance(79,163,Image.SCALE_SMOOTH);
        JLabel Handle = new JLabel(new ImageIcon(hdll));
        Handle.setBounds(398,50,79,163);

        ImageIcon ar1 = new ImageIcon("txt_2.png");
        Image arl1 = ar1.getImage().getScaledInstance(123,252,Image.SCALE_SMOOTH);
        ArmPlay1 = new JLabel(new ImageIcon(arl1));
        ArmPlay1.setBounds(365,58,123,252);

        ImageIcon ar2 = new ImageIcon("txt_2.5.png");
        Image arl2 = ar2.getImage().getScaledInstance(213,227,Image.SCALE_SMOOTH);
        ArmPlay2 = new RotatableLabel(new ImageIcon(arl2));
        ArmPlay2.setBounds(255,18,213,227);
        ArmPlay2.setVisible(false); 

        ImageIcon lp = new ImageIcon("txt_3.png");
        Image lpl = lp.getImage().getScaledInstance(243,244,Image.SCALE_SMOOTH);
        LongPlay = new RotatableLabel(new ImageIcon(lpl));
        LongPlay.setBounds(92,58,243,244);

        ImageIcon pl = new ImageIcon("txt_15.png");
        Image pll = pl.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH);
        PlayBtn = new JLabel(new ImageIcon(pll));
        PlayBtn.setBounds(265 + 2,330,32,32);

        ImageIcon fl = new ImageIcon("txt_16.png");
        Image flp = fl.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH);
        FlipBtn = new JLabel(new ImageIcon(flp));
        FlipBtn.setBounds(85,330,32,32);

        ImageIcon rw = new ImageIcon("txt_18.png");
        Image rww = rw.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH);
        RewindBtn = new JLabel(new ImageIcon(rww));
        RewindBtn.setBounds(200,330,32,32);

        ImageIcon fd = new ImageIcon("txt_17.png");
        Image fdd = fd.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH);
        ForwardBtn = new JLabel(new ImageIcon(fdd));
        ForwardBtn.setBounds(320 + 2,330,32,32);

        ImageIcon st = new ImageIcon("txt_24.png");
        Image stt = st.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH);
        StopBtn = new JLabel(new ImageIcon(stt));
        StopBtn.setBounds(320 + 135,330,32,32);

        ImageIcon rwr = new ImageIcon("txt_27.png");
        Image rwrr = rwr.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH);
        RewindRewBtn = new JLabel(new ImageIcon(rwrr));
        RewindRewBtn.setBounds(150,330,32,32);

        ImageIcon fwd = new ImageIcon("txt_26.png");
        Image fwdd = fwd.getImage().getScaledInstance(32,32,Image.SCALE_SMOOTH);
        ForwardFordBtn = new JLabel(new ImageIcon(fwdd));
        ForwardFordBtn.setBounds(370 + 2,330,32,32);

        RewindBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                RewindBtn.setIcon(new ImageIcon("txt_23.png"));
            }
            public void mouseExited(MouseEvent e) {
                RewindBtn.setIcon(new ImageIcon("txt_18.png"));
            }
            public void mousePressed(MouseEvent e) {
                BackwardAudio();
            }
        });

        ForwardBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ForwardBtn.setIcon(new ImageIcon("txt_22.png"));
            }
            public void mouseExited(MouseEvent e) {
                ForwardBtn.setIcon(new ImageIcon("txt_17.png"));
            }
            public void mousePressed(MouseEvent e) {
                ForwardAudio();
            }
        });

    
        FlipBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                FlipBtn.setIcon(new ImageIcon("txt_21.png"));
            }
            public void mouseExited(MouseEvent e) {
                FlipBtn.setIcon(new ImageIcon("txt_16.png"));
            }
          

            public void mousePressed(MouseEvent e) {
                if (LongPlay.isVisible()) {
                    sided = !sided;
                    // Usar las variables estáticas actuales
                    SidesAudio(sideA, sideB, sided);
                    System.out.println("Flipped to side: " + sided);
                }
            }
    
        });

        StopBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                StopBtn.setIcon(new ImageIcon("txt_25.png"));
            }
            public void mouseExited(MouseEvent e) {
                StopBtn.setIcon(new ImageIcon("txt_24.png"));
            }
            public void mousePressed(MouseEvent e) {
                stopAudio();
                LongPlay.setVisible(false);
                ArmPlay1.setVisible(true);
                ArmPlay2.setVisible(false);
                PlayBtn.setIcon(new ImageIcon("txt_15.png"));
            }
        });

        ForwardFordBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ForwardFordBtn.setIcon(new ImageIcon("txt_28.png"));
            }
            public void mouseExited(MouseEvent e) {
                ForwardFordBtn.setIcon(new ImageIcon("txt_26.png"));
            }
            public void mousePressed(MouseEvent e) {
                SkipAudio();
            }
        });

        RewindRewBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                RewindRewBtn.setIcon(new ImageIcon("txt_29.png"));
            }
            public void mouseExited(MouseEvent e) {
                RewindRewBtn.setIcon(new ImageIcon("txt_27.png"));
            }
            public void mousePressed(MouseEvent e) {

                RewindAudio();
            }
        });

        PlayBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {

                if (!isPlaying){
                    PlayBtn.setIcon(new ImageIcon("txt_20.png"));
                } else {
                    PlayBtn.setIcon(new ImageIcon("txt_19.png"));
                }
                
             
            }
            public void mouseExited(MouseEvent e) {
                if (!isPlaying){
                    PlayBtn.setIcon(new ImageIcon("txt_15.png"));
                } else {
                    PlayBtn.setIcon(new ImageIcon("txt_14.png"));
                }
                
            
            }
            public void mousePressed(MouseEvent e) {
                togglePlayPause();
                if (!isPlaying){
                    PlayBtn.setIcon(new ImageIcon("txt_20.png"));
                } else {
                    PlayBtn.setIcon(new ImageIcon("txt_19.png"));
                }
            }
        });

        PlayBtn.setToolTipText("Play/Pause (Space)");
        ForwardBtn.setToolTipText("Forward (Right)");
        RewindBtn.setToolTipText("Backward (Left)");
        ForwardFordBtn.setToolTipText("Skip (Right + Ctrl)");
        RewindRewBtn.setToolTipText("Rewind (Left + Ctrl)");
        FlipBtn.setToolTipText("Flip Side (Shift)");

        ImageIcon defaultCoverIcon = new ImageIcon("txt_8.png");
        Image defaultCoverImage = defaultCoverIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        CoverBox = new JLabel(new ImageIcon(defaultCoverImage));
        CoverBox.setBounds(615,75,200,200);
        CoverBox.setVisible(false);

        CoverTitle.setFont(new Font("Eras Demi ITC",Font.BOLD,25));
        CoverTitle.setBounds(610, 40, 200, 25);
        CoverTitle.setForeground(Color.WHITE);
        CoverTitle.setVisible(false);

        CoverArtist.setFont(new Font("Eras Demi ITC",Font.BOLD,15));
        CoverArtist.setBounds(610, 275, 200, 100);
        CoverArtist.setForeground(Color.WHITE);
        CoverArtist.setVisible(false);

        CoverSide.setFont(new Font("Eras Demi ITC",Font.BOLD,20));
        CoverSide.setBounds(610, 250, 200, 100);
        CoverSide.setForeground(Color.WHITE);
        CoverSide.setVisible(false);

        // Rectangulo de fondo
        RectAng = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(18, 18, 18));
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);
            }
        };
        RectAng.setBounds(25, -10, 830, 610);
        RectAng.setOpaque(false);

        // Buton Toggle
        ImageIcon tg = new ImageIcon("txt_4.png");
        Image tog = tg.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        TogPlay = new JLabel(new ImageIcon(tog));
        TogPlay.setBounds(770,385,64,64);

        

        JLabel TrackList = new JLabel("Colection");
        TrackList.setFont(new Font("Eras Demi ITC",Font.BOLD,40));
        TrackList.setBounds(620, 25, 225, 60);
        TrackList.setForeground(Color.WHITE);
        TrackList.setHorizontalAlignment(JLabel.CENTER);
        TrackList.setVerticalAlignment(JLabel.CENTER);

        // Interfaz del Toggle
        ImageIcon ad = new ImageIcon("txt_6.png");
        Image add = ad.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        AddPlay = new JLabel(new ImageIcon(add));
        AddPlay.setBounds(45,390,60,60);


        AddPlay.setToolTipText("Add Record");
        StopBtn.setToolTipText("Stop (S)");
        TogPlay.setToolTipText("Show/Hide Collection");

        // Panel para mostrar registros
        recordsPanel = new JPanel();
        recordsPanel.setLayout(null);
        recordsPanel.setBackground(Color.BLACK);
        recordsScrollPane = new JScrollPane(recordsPanel);
        recordsScrollPane.setBounds(610, 100, 270, 380);
        recordsScrollPane.setBackground(Color.BLACK);
        recordsScrollPane.setBorder(null);
        recordsScrollPane.getViewport().setBackground(Color.BLACK);
        recordsScrollPane.setVisible(false);

        // Añadiendo todo
        add(CoverTitle);
        add(CoverSide);
        add(CoverBox);
        add(CoverArtist);
        add(RewindRewBtn);
        add(ForwardFordBtn);
        add(PlayBtn);
        add(StopBtn);
        add(FlipBtn);
        add(ForwardBtn);
        add(RewindBtn);
        add(ArmPlay1);
        add(TogPlay);
        add(AddPlay);
        add(ArmPlay2);
        add(Handle);
        add(LongPlay);
        LongPlay.setVisible(false);
        add(RecPlay);
        add(RectAng);
        add(recordsScrollPane);
        
        
        TogPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                TogPlay.setIcon(new ImageIcon("txt_5.png"));
            }
            public void mouseExited(MouseEvent e) {
                TogPlay.setIcon(new ImageIcon("txt_4.png"));
            }
            public void mousePressed(MouseEvent e) {
                toogle = !toogle;
                if (toogle) {
                    add(TrackList);
                    RectAng.setBounds(25, -10, 575, 610);
                    TogPlay.setBounds(515,390,64,64);
                    recordsScrollPane.setVisible(true);
                    CoverBox.setBounds(-200,45,200,200);
                    CoverSide.setBounds(-610, 250, 200, 100);
                    CoverTitle.setBounds(-610, 225, 200, 100);
                    CoverArtist.setBounds(-610, 275, 200, 100);
                } else {
                    remove(TrackList);
                    RectAng.setBounds(25, -10, 830, 610);
                    TogPlay.setBounds(770,390,64,64);
                    recordsScrollPane.setVisible(false);



                    CoverBox.setBounds(615,75,200,200);
                    CoverTitle.setBounds(610, 40, 200, 25);
                    CoverArtist.setBounds(610, 275, 200, 100);
                    CoverSide.setBounds(610, 250, 200, 100);
                }
            }
        });
        
        // Añadir listener de teclado
        addKeyListener(this);
        setFocusable(true);
        
        // Inicializar Timer para rotación (pero no iniciarlo aún)
        rotationTimer = new Timer(16, e -> {
            if (isPlaying) {
                int rotspeed = 5;
                longPlayRotationAngle += rotspeed; // Velocidad diferente para LongPlay
                if (longPlayRotationAngle >= 360) {
                    longPlayRotationAngle = 0;
                }
                LongPlay.setRotation(longPlayRotationAngle);
            }
        });
        // No iniciamos el timer aquí, se iniciará cuando se presione play
        
        // Timer para monitorear cambios en records
        Timer recordsUpdateTimer = new Timer(500, e -> {
            updateRecordsDisplay();
        });
        recordsUpdateTimer.start();
        
        setVisible(true);
    }

    // Método para iniciar el timer de rotación
    public void startRotationTimer() {
        if (!rotationTimer.isRunning()) {
            rotationTimer.start();
        }
    }
    
    // Método para detener el timer de rotación
    public void stopRotationTimer() {
        if (rotationTimer.isRunning()) {
            rotationTimer.stop();
        }
    }
    
    // Método para obtener la instancia
    public static LongPlayer getInstance() {
        return instance;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            togglePlayPause();

        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            stopAudio();
            
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            sided = !sided;
            SidesAudio(sideA, sideB, sided);
            System.out.println("Flipped to side: " + sided);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            BackwardAudio();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            ForwardAudio();
        } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            RewindAudio();
        } else if (e.getKeyCode() == KeyEvent.VK_ALT) {
            SkipAudio();
        }
            // No se necesita implementar

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita implementar
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No se necesita implementar
    }

    private void updateRecordsDisplay() {
        recordsPanel.removeAll();
        int yPosition = 10;
        
        for (Map<String, Object> record : records) {
            RecordCard card = new RecordCard(record);
            card.setBounds(5, yPosition, 240, 190);
            recordsPanel.add(card);
            yPosition += 200;
        }
        
        recordsPanel.setPreferredSize(new java.awt.Dimension(240, yPosition));
        recordsPanel.revalidate();
        recordsPanel.repaint();
    }

    private void guardarDatos() {
        gestorPersistencia.guardarRecords(records);
        System.out.println("Datos guardados exitosamente");
    }

    // En LongPlayer.java, añade este método público:
    public void guardarDatos2() {
        if (gestorPersistencia != null) {
            gestorPersistencia.guardarRecords(records);
        }
    }
    // Método para reproducir audio
    private static void playAudio(String audioPath) {
        try {
            // Si ya está reproduciendo el mismo archivo, pausar
            if (isPlaying && audioPath.equals(currentAudioPath) && currentClip != null) {
                pauseAudio();
                return;
            }
            
            // Si es un archivo diferente, detener el actual
            if (currentClip != null && currentClip.isRunning()) {
                currentClip.stop();
                currentClip.close();
            }
            
            // Reiniciar posición si es un archivo nuevo
            if (!audioPath.equals(currentAudioPath)) {
                clipPosition = 0;
                currentAudioPath = audioPath;
            }
            
            File audioFile = new File(audioPath);
            if (!audioFile.exists()) {
                System.out.println("El archivo de audio no existe: " + audioPath);
                return;
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            
            // Configurar listener para cuando termine la reproducción
            currentClip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        SwingUtilities.invokeLater(() -> {
                            if (currentClip != null && currentClip.getMicrosecondPosition() >= currentClip.getMicrosecondLength()) {
                                // Si terminó la reproducción (no fue pausa)
                                ;
                                clipPosition = 0;
                                currentClip.close();
                                // Detener el timer de rotación
                                
                            }
                        });
                    }
                }
            });
            
            // Si había una posición guardada (de una pausa anterior), reanudar desde ahí
            if (clipPosition > 0) {
                currentClip.setMicrosecondPosition(clipPosition);
            }
            
            // Reproducir el audio
            currentClip.start();
            isPlaying = true;
            ArmPlay1.setVisible(false);
            ArmPlay2.setVisible(true);
            
            // Iniciar el timer de rotación
            if (instance != null) {
                instance.startRotationTimer();
            }
            
        } catch (Exception e) {
            System.out.println("Error al reproducir audio: " + e.getMessage());
            e.printStackTrace();
            isPlaying = false;
            ArmPlay1.setVisible(true);
            ArmPlay2.setVisible(false);
            // Detener el timer de rotación en caso de error
            if (instance != null) {
                instance.stopRotationTimer();
            }
        }
    }
    
    // Método para pausar audio
    private static void pauseAudio() {
        if (currentClip != null && currentClip.isRunning()) {
            clipPosition = currentClip.getMicrosecondPosition();
            currentClip.stop();
            isPlaying = false;
            SwingUtilities.invokeLater(() -> {
              
                // Detener el timer de rotación
                if (instance != null) {
                    instance.stopRotationTimer();
                }
            });
        }
    }
    
    // Método para reanudar audio
    private static void resumeAudio() {
        if (currentClip != null && !currentClip.isRunning() && clipPosition > 0) {
            try {
                currentClip.setMicrosecondPosition(clipPosition);
                currentClip.start();
                isPlaying = true;
                SwingUtilities.invokeLater(() -> {
                    ArmPlay1.setVisible(false);
                    ArmPlay2.setVisible(true);
                    // Iniciar el timer de rotación
                    if (instance != null) {
                        instance.startRotationTimer();
                    }
                });
            } catch (Exception e) {
                System.out.println("Error al reanudar audio: " + e.getMessage());
            }
        }
    }
    
    // Método para alternar entre play y pause
    private static void togglePlayPause() {
        if (currentClip == null) return;
        
        
        if (isPlaying && currentClip.isRunning()) {
            pauseAudio();
            PlayBtn.setIcon(new ImageIcon("txt_15.png"));
       
        } else {
            
            if (clipPosition > 0) {
                resumeAudio();
                PlayBtn.setIcon(new ImageIcon("txt_14.png"));
            } else if (!currentAudioPath.isEmpty()) {
                playAudio(currentAudioPath);
                PlayBtn.setIcon(new ImageIcon("txt_14.png"));
            }
        }
    }
    
    // Método para detener la reproducción completamente
    public static void stopAudio() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            isPlaying = false;
            clipPosition = 0;
            currentAudioPath = "";
            SwingUtilities.invokeLater(() -> {
                ArmPlay1.setVisible(true);
                ArmPlay2.setVisible(false);
                LongPlay.setVisible(false);
                ArmPlay1.setVisible(true);
                ArmPlay2.setVisible(false);
                CoverBox.setVisible(false);
                CoverSide.setVisible(false);
                CoverTitle.setVisible(false);
                CoverArtist.setVisible(false);
                PlayBtn.setIcon(new ImageIcon("txt_15.png"));
                // Detener el timer de rotación
                if (instance != null) {
                    instance.stopRotationTimer();
                }
            });
        }
    }
    public static void RewindAudio() {
        if (currentClip != null) {
            currentClip.setMicrosecondPosition(0);
            clipPosition = 0;
        }
    }

    public static void SkipAudio() {
        if (currentClip != null) {
            currentClip.setMicrosecondPosition(currentClip.getMicrosecondLength() - 1000000);
            clipPosition = 1000000000000000000L;
        }
    }

    public static void ForwardAudio() {
        if (currentClip != null) {
            long newPosition = currentClip.getMicrosecondPosition() + 5000000; // Avanzar 5 segundos
            if (newPosition < currentClip.getMicrosecondLength()) {
                currentClip.setMicrosecondPosition(newPosition);
                clipPosition = newPosition;
            } else {
                // Si se pasa del final, detener la reproducción
                stopAudio();
            }
        }
    }

    public static void BackwardAudio() {
        if (currentClip != null) {
            long newPosition = currentClip.getMicrosecondPosition() - 5000000; // Retroceder 5 segundos
            if (newPosition > 0) {
                currentClip.setMicrosecondPosition(newPosition);
                clipPosition = newPosition;
            } else {
                currentClip.setMicrosecondPosition(0);
                clipPosition = 0;
            }
        }
    }

    public static void SidesAudio(String sideA, String sideB, Boolean sided) {
        
        if (currentClip != null) {
            RewindAudio();
            if (sided) {
                playAudio(sideB);
                CoverArtist.setText("Side - B");
            } else if (!sided) {
                playAudio(sideA);
                CoverArtist.setText("Side - A");
                
            }
        }
      
    }

    public static void main(String[] args) {
        LongPlayer ventana = new LongPlayer();

        Image icon = Toolkit.getDefaultToolkit().getImage("icon.png");
        ventana.setIconImage(icon);
        ventana.setLayout(null);


        AddPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                AddPlay.setIcon(new ImageIcon("txt_7.png"));
            }
            public void mouseExited(MouseEvent e) {
                AddPlay.setIcon(new ImageIcon("txt_6.png"));
            }
            public void mousePressed(MouseEvent e) {
                AddRecord recordWindow = new AddRecord();
            }
        });
    }

    // Clase interna para rotar JLabel
    static class RotatableLabel extends JLabel {
        private double rotation = 0;

        public RotatableLabel(ImageIcon icon) {
            super(icon);
        }

        public void setRotation(double degrees) {
            this.rotation = Math.toRadians(degrees);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (rotation != 0) {
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform originalTransform = g2d.getTransform();

                int centerX = 246 / 2;
                int centerY = 246 / 2;

                g2d.rotate(rotation, centerX, centerY);

                super.paintComponent(g2d);
                g2d.setTransform(originalTransform);
            } else {
                super.paintComponent(g);
            }
        }
    }

    // Clase interna para mostrar cada registro en la colección
        
    static class RecordCard extends JPanel {
    private Map<String, Object> record;
    private int recordIndex;
    // Variables locales para cada tarjeta
    private String cardSideA;
    private String cardSideB;
    private String cardNombre;
    private String cardArtista;
    private String cardFormat;
    
    public RecordCard(Map<String, Object> record) {
        this.record = record;
        this.recordIndex = LongPlayer.records.indexOf(record);
        
        // Guardar las rutas de audio específicas de esta tarjeta
        this.cardSideA = (String) record.getOrDefault("sideA", "");
        this.cardSideB = (String) record.getOrDefault("sideB", "");
        this.cardNombre = (String) record.getOrDefault("nombre", "N/A");
        this.cardArtista = (String) record.getOrDefault("artista", "N/A");
        this.cardFormat = (String) record.getOrDefault("format", "N/A");
        
        setLayout(null);
        setBackground(new Color(30, 30, 30));
        setBorder(null);

        // Imagen de portada
        Object coverObj = record.get("coverImage");
        
        // Crear un ImageIcon por defecto para evitar null
        ImageIcon defaultIcon = new ImageIcon("txt_8.png");
        Image defaultImage = defaultIcon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
        defaultIcon = new ImageIcon(defaultImage);
        
        JLabel coverLabel = new JLabel(defaultIcon);
        
        if (coverObj != null) {
            if (coverObj instanceof JLabel) {
                JLabel coverJLabel = (JLabel) coverObj;
                javax.swing.Icon icon = coverJLabel.getIcon();
                if (icon instanceof ImageIcon) {
                    Image img = ((ImageIcon) icon).getImage();
                    Image scaled = img.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
                    coverLabel.setIcon(new ImageIcon(scaled));
                }
            } else if (coverObj instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) coverObj;
                Image img = icon.getImage();
                Image scaled = img.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
                coverLabel.setIcon(new ImageIcon(scaled));
            }
        }
        coverLabel.setBounds(5, 5, 128, 128);
        add(coverLabel);

        // Nombre
        JLabel nameLabel = new JLabel(cardNombre);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Eras Demi ITC", Font.BOLD, 15));
        nameLabel.setBounds(5, 140, 200, 25);
        add(nameLabel);

        // Artista
        JLabel artistLabel = new JLabel(cardArtista);
        artistLabel.setForeground(Color.LIGHT_GRAY);
        artistLabel.setFont(new Font("Eras Demi ITC", Font.PLAIN, 15));
        artistLabel.setBounds(5, 165, 155, 15);
        add(artistLabel);

        // Format 
        ImageIcon fd = new ImageIcon("txt_10.png");
        Image fds = fd.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
        JLabel formatLabel = new JLabel(new ImageIcon(fds));
        formatLabel.setBounds(70, 5, 128, 128);
        add(formatLabel);

        if (cardFormat.equals("Single")) {
            ImageIcon fs = new ImageIcon("txt_11.png");
            Image fss = fs.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            formatLabel.setIcon(new ImageIcon(fss));
        } else if (cardFormat.equals("EP")) {
            ImageIcon fe = new ImageIcon("txt_12.png");
            Image fes = fe.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            formatLabel.setIcon(new ImageIcon(fes));
        } else if (cardFormat.equals("LP")) {
            ImageIcon fl = new ImageIcon("txt_10.png");
            Image fls = fl.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            formatLabel.setIcon(new ImageIcon(fls));
        }

        // Botón de eliminar
        JButton deleteButton = new JButton("X") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBackground().getAlpha() < 255) {
                    super.paintComponent(g);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        deleteButton.setFont(new Font("Arial", Font.BOLD, 20));
        deleteButton.setForeground(Color.RED);
        deleteButton.setBackground(null);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        deleteButton.setBounds(205, 5, 25, 25);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setOpaque(true);
        deleteButton.setFocusPainted(false);
        deleteButton.setToolTipText("Delete Record");
        
        // Listener para el botón de eliminar
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                deleteButton.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                deleteButton.setForeground(Color.RED);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    LongPlayer.getInstance(),
                    "Are you sure you want to delete \"" + cardNombre + "\" from your colección?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Detener la reproducción si está sonando este record
                    String currentSide = sided ? cardSideB : cardSideA;
                    if (currentClip != null && 
                        (currentAudioPath.equals(cardSideA) || currentAudioPath.equals(cardSideB))) {
                        stopAudio();
                    }
                    
                    // Eliminar el record de la lista
                    LongPlayer.records.remove(recordIndex);
                    
                    // Actualizar la pantalla
                    LongPlayer.getInstance().updateRecordsDisplay();
                    
                    // Guardar los cambios en disco
                    LongPlayer.getInstance().guardarDatos();
                    
                    System.out.println("Registro eliminado: " + cardNombre);
                }
            }
        });
        
        add(deleteButton);

        // Botón Play/Pause circular
        JButton playButtonPanel = new JButton("Play") {
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
        
        playButtonPanel.setFont(new Font("Eras Demi ITC", Font.BOLD, 15));
        playButtonPanel.setForeground(Color.BLACK);
        playButtonPanel.setBackground(Color.WHITE);
        playButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        playButtonPanel.setBounds(180, 130, 50, 50);
        playButtonPanel.setContentAreaFilled(false);
        playButtonPanel.setOpaque(false);
        playButtonPanel.setFocusPainted(false);
        add(playButtonPanel);

        // Agregar listener al botón de play/pause
        playButtonPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playButtonPanel.setBackground(Color.LIGHT_GRAY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                playButtonPanel.setBackground(Color.WHITE);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                // VERIFICAR SI HAY ICONO ANTES DE USARLO
                javax.swing.Icon coverIcon = coverLabel.getIcon();
                if (coverIcon == null) {
                    ImageIcon defaultCover = new ImageIcon("txt_8.png");
                    Image scaledDefault = defaultCover.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    coverIcon = new ImageIcon(scaledDefault);
                }
                
                // Usar las variables locales de la tarjeta
                String currentSideA = cardSideA;
                String currentSideB = cardSideB;
                
                // Actualizar las variables estáticas de LongPlayer
                LongPlayer.sideA = currentSideA;
                LongPlayer.sideB = currentSideB;
                
                // Determinar qué lado reproducir
                String audioToPlay = sided ? currentSideB : currentSideA;
                
                if (audioToPlay != null && !audioToPlay.isEmpty()) {
                    // Si ya está reproduciendo este mismo archivo, pausar
                    LongPlay.setVisible(true);
                    if (cardFormat.equals("Single")) {
                        LongPlay.setIcon(new ImageIcon("txt_13.png"));
                        LongPlay.setBounds(90, 58, 243, 244);
                    } else if (cardFormat.equals("EP")) {
                        LongPlay.setIcon(new ImageIcon("txt_12.png"));
                        LongPlay.setBounds(90, 58, 243, 244);
                    } else {
                        LongPlay.setIcon(new ImageIcon("txt_3.png"));
                        LongPlay.setBounds(92, 58, 243, 244);
                    }
                    
                    CoverBox.setVisible(true);
                    CoverBox.setIcon(coverIcon);
                    
                    // ESCALAR EL ICONO
                    if (CoverBox.getIcon() instanceof ImageIcon) {
                        ImageIcon originalIcon = (ImageIcon) CoverBox.getIcon();
                        if (originalIcon != null) {
                            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                            CoverBox.setIcon(new ImageIcon(scaledImage));
                        }
                    }

                    CoverTitle.setText(cardNombre);
                    CoverTitle.setVisible(true);

                    CoverSide.setText(cardArtista);
                    CoverSide.setVisible(true);

                    CoverArtist.setText(sided ? "Side - B" : "Side - A");
                    CoverArtist.setVisible(true);
                    
                    if (isPlaying && audioToPlay.equals(currentAudioPath)) {
                        pauseAudio();
                        playButtonPanel.setText("O");
                        PlayBtn.setIcon(new ImageIcon("txt_15.png"));
                    } else {
                        playAudio(audioToPlay);
                        PlayBtn.setIcon(new ImageIcon("txt_14.png"));
                        playButtonPanel.setText("I");
                    }
                } else {
                    System.out.println("No hay archivo de audio para reproducir");
                }
            }
        });
    }
}
}