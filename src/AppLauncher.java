import javax.swing.SwingUtilities;

public class AppLauncher {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // display our weather app gui
                new WeatherAppGUI().setVisible(true);
            }
        });
    }
}
