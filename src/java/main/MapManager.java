package src.java.main;

import src.java.structure.Province;
import src.java.structure.State;
import src.java.utils.Message;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class MapManager {
    public static final int OCEAN_COLOR = 0x000066;
    public static final int LAKE_COLOR = 0x1e5a8c;

    private final BufferedImage provincesImg;
    private BufferedImage statesImg;
    private BufferedImage countriesImg;
    private final ImagePanel imagePanel;
    private final GreatOrganizer greatOrganizer;

    public MapManager(GreatOrganizer greatOrganizer) throws IOException {
        this.greatOrganizer = greatOrganizer;


        Message mess = new Message(getClass());
        mess.sendStartMessage("Images generation in progress...");

        provincesImg = ImageIO.read(greatOrganizer.getProvincesMapFile());
        this.imagePanel = new ImagePanel(provincesImg);
        imagePanel.changeImage(provincesImg);
        imagePanel.repaint();
        mess.sendFormattedMessage("Provinces image generated");

        // TODO think about raster
        generateStatesImage();
        mess.sendFormattedMessage("States image generated");

        generateCountriesImage();
        mess.sendFormattedMessage("Countries image generated");
        mess.sendEndMessage();
    }

    private void generateStatesImage() {
        HashMap<Integer, Province> colorsToProvince = greatOrganizer.getColorToProvince();
        statesImg = new BufferedImage(provincesImg.getWidth(), provincesImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < provincesImg.getWidth(); x++) {
            for (int y = 0; y < provincesImg.getHeight(); y++) {
                if (pixelIsWater(colorsToProvince, x, y, statesImg)) continue;
                statesImg.setRGB(x, y, colorsToProvince.get(provincesImg.getRGB(x, y) & 0xFFFFFF).getState().getColorRGB());
            }
        }
    }

    private void generateCountriesImage() {
        HashMap<Integer, Province> colorsToProvince = greatOrganizer.getColorToProvince();
        HashMap<Integer, State> colorToState = greatOrganizer.getColorToState();
        countriesImg = new BufferedImage(provincesImg.getWidth(), provincesImg.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < provincesImg.getWidth(); x++) {
            for (int y = 0; y < provincesImg.getHeight(); y++) {
                if (pixelIsWater(colorsToProvince, x, y, countriesImg)) continue;
                countriesImg.setRGB(x, y, colorToState.get(statesImg.getRGB(x, y) & 0xFFFFFF).getCountry().getColorRGB());
            }
        }
    }

    private boolean pixelIsWater(HashMap<Integer, Province> colorsToProvince, int x, int y, BufferedImage statesImg) {
        if (colorsToProvince.get(provincesImg.getRGB(x, y) & 0xFFFFFF).getTerrain().equalsIgnoreCase("ocean")) {
            statesImg.setRGB(x, y, OCEAN_COLOR);
            return true;
        }
        if (colorsToProvince.get(provincesImg.getRGB(x, y) & 0xFFFFFF).getType().equalsIgnoreCase("lake")) {
            statesImg.setRGB(x, y, LAKE_COLOR);
            return true;
        }
        return false;
    }

    public void changeToProvinces() {
        imagePanel.changeImage(provincesImg);
    }

    public void changeToStates() {
        imagePanel.changeImage(statesImg);
    }

    public void changeToCountries() {
        imagePanel.changeImage(countriesImg);
    }

    public ImagePanel getImagePanel() {
        return imagePanel;
    }
}
