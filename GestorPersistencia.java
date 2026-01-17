import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GestorPersistencia {
    private static final String DATA_DIR = System.getProperty("user.home") + "/.longPlayerRecords";
    private static final String IMAGES_DIR = DATA_DIR + "/images";
    private static final String AUDIOS_DIR = DATA_DIR + "/audios";
    private static final String CONFIG_FILE = DATA_DIR + "/records.dat";
    
    public GestorPersistencia() {
        // Crear directorios si no existen
        new File(IMAGES_DIR).mkdirs();
        new File(AUDIOS_DIR).mkdirs();
    }
    
    // Guardar imagen y devolver la ruta guardada
    public String guardarImagen(String originalPath, String recordName) {
        try {
            File originalFile = new File(originalPath);
            String extension = obtenerExtension(originalFile.getName());
            String nombreGuardado = generarNombreUnico(recordName, extension);
            Path destino = Paths.get(IMAGES_DIR + "/" + nombreGuardado);
            
            // Copiar la imagen
            Files.copy(originalFile.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            return destino.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return originalPath; // En caso de error, usar la ruta original
        }
    }
    
    // Guardar audio y devolver la ruta guardada
    public String guardarAudio(String originalPath, String recordName, String side) {
        try {
            File originalFile = new File(originalPath);
            String extension = obtenerExtension(originalFile.getName());
            String nombreGuardado = generarNombreUnico(recordName + "_" + side, extension);
            Path destino = Paths.get(AUDIOS_DIR + "/" + nombreGuardado);
            
            // Copiar el audio
            Files.copy(originalFile.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            return destino.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return originalPath; // En caso de error, usar la ruta original
        }
    }
    
    // Guardar todos los records
    public void guardarRecords(List<Map<String, Object>> records) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            // Convertir los records a un formato serializable
            List<Map<String, Serializable>> recordsSerializable = new ArrayList<>();
            
            for (Map<String, Object> record : records) {
                Map<String, Serializable> recordSerializable = new HashMap<>();
                
                for (Map.Entry<String, Object> entry : record.entrySet()) {
                    Object value = entry.getValue();
                    
                    if (value instanceof Serializable) {
                        // Si es un ImageIcon, guardar como ruta
                        if (value instanceof ImageIcon) {
                            ImageIcon icon = (ImageIcon) value;
                            String imagePath = guardarImageIcon(icon, (String) record.get("nombre"));
                            recordSerializable.put(entry.getKey() + "_Path", imagePath);
                        } else {
                            recordSerializable.put(entry.getKey(), (Serializable) value);
                        }
                    } else if (value instanceof Image) {
                        // Convertir Image a ImageIcon y guardar
                        ImageIcon icon = new ImageIcon((Image) value);
                        String imagePath = guardarImageIcon(icon, (String) record.get("nombre"));
                        recordSerializable.put(entry.getKey() + "_Path", imagePath);
                    } else if (value instanceof JLabel) {
                        // Para JLabel con ImageIcon
                        JLabel label = (JLabel) value;
                        if (label.getIcon() instanceof ImageIcon) {
                            ImageIcon icon = (ImageIcon) label.getIcon();
                            String imagePath = guardarImageIcon(icon, (String) record.get("nombre"));
                            recordSerializable.put(entry.getKey() + "_Path", imagePath);
                        }
                    }
                }
                
                recordsSerializable.add(recordSerializable);
            }
            
            oos.writeObject(recordsSerializable);
            System.out.println("Records guardados en: " + CONFIG_FILE);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Cargar records guardados
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> cargarRecords() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CONFIG_FILE))) {
            List<Map<String, Serializable>> recordsSerializable = (List<Map<String, Serializable>>) ois.readObject();
            List<Map<String, Object>> records = new ArrayList<>();
            
            for (Map<String, Serializable> recordSerializable : recordsSerializable) {
                Map<String, Object> record = new HashMap<>();
                
                for (Map.Entry<String, Serializable> entry : recordSerializable.entrySet()) {
                    String key = entry.getKey();
                    
                    // Si es una ruta de imagen, cargar el ImageIcon
                    if (key.endsWith("_Path") && key.startsWith("coverImage")) {
                        String imagePath = (String) entry.getValue();
                        File imageFile = new File(imagePath);
                        if (imageFile.exists()) {
                            ImageIcon icon = new ImageIcon(imagePath);
                            record.put("coverImage", icon);
                        }
                    } else if (!key.endsWith("_Path")) {
                        record.put(key, entry.getValue());
                    }
                }
                
                records.add(record);
            }
            
            System.out.println("Records cargados: " + records.size());
            return records;
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // Método auxiliar para guardar ImageIcon
    String guardarImageIcon(ImageIcon icon, String recordName) {
        try {
            String nombreGuardado = generarNombreUnico(recordName, "png");
            Path destino = Paths.get(IMAGES_DIR + "/" + nombreGuardado);
            
            // Guardar la imagen en formato PNG
            Image image = icon.getImage();
            java.awt.image.BufferedImage bufferedImage = new java.awt.image.BufferedImage(
                icon.getIconWidth(), 
                icon.getIconHeight(), 
                java.awt.image.BufferedImage.TYPE_INT_ARGB
            );
            java.awt.Graphics2D g2d = bufferedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
            
            javax.imageio.ImageIO.write(bufferedImage, "png", destino.toFile());
            return destino.toString();
            
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    // Métodos auxiliares
    private String obtenerExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }
    
    private String generarNombreUnico(String base, String extension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return base.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + "." + extension;
    }
    
    // Cargar imagen desde ruta
    public ImageIcon cargarImagen(String ruta) {
        File imageFile = new File(ruta);
        if (imageFile.exists()) {
            return new ImageIcon(ruta);
        }
        return null;
    }
}