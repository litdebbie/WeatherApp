import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGUI extends JFrame {
    public WeatherAppGUI() {
        // add title
        setTitle("Weather App");

        // add function to close window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set the size of window
        setSize(450, 650);

        // load gui at the center of the screen
        setLocationRelativeTo(null);

        // make layout manager null to manuallly position our components within the frame
        setLayout(null);

        // make window non resizable
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        // search box
        JTextField searchBoxField = new JTextField();

        // set the location and size of component
        searchBoxField.setBounds(15, 15, 351, 45);

        // change font style and size
        searchBoxField.setFont(new Font("Dialog", Font.PLAIN, 24));

        add(searchBoxField);

        // search button
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        // change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 14, 47, 45);
        add(searchButton);
    }

    // used to create images in our gui components
    private ImageIcon loadImage(String resourcePath) {
        try {
            // read th image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // returns an image icon so that our component can render it
            return new ImageIcon(image);

        } catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("Image not found!");
        return null;
    }
}