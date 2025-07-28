import javax.swing.*;

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
    }
}