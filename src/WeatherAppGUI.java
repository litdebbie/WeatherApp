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

        // Set the background color of the content pane
        Container contentPane = getContentPane();
        contentPane.setBackground(Color.LIGHT_GRAY);

        addGuiComponents();
    }

    private void addGuiComponents() {
        // SEARCH BOX
        JTextField searchBoxField = new JTextField();
        searchBoxField.setForeground(Color.BLACK);

        // set the location and size of component
        searchBoxField.setBounds(15, 15, 351, 45);

        // change font style and size
        searchBoxField.setFont(new Font("Dialog", Font.PLAIN, 24));

        add(searchBoxField);

        // SEARCH BUTTON
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        // searchButton.setBackground(Color.GREEN);

        // change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 14, 47, 45);
        add(searchButton);

        // WEATHER IMAGE
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        // TEMPERATURE TEXT
        JLabel temperatureText = new JLabel("15 \u00B0C");
        temperatureText.setForeground(Color.BLACK);
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));

        // center the text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        // WEATHER CONDITION DESCRIPTION
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setForeground(Color.BLACK);
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        // HUMIDITY IMAGE
        JLabel humidtyImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidtyImage.setBounds(15, 500, 74, 66);
        add(humidtyImage);

        // HUMIDITY TEXT
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setForeground(Color.BLACK);
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        // WINDSPEED IMAGE
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220, 500, 74, 66);
        add(windspeedImage);

        // WINDSPEED TEXT
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setForeground(Color.BLACK);
        windspeedText.setBounds(310, 500, 85, 55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windspeedText);
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