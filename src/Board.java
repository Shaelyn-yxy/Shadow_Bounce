/**
 * Board Class
 *
 * includes loading CSV files and initiating all pegs and powerUp
 */

import bagel.*;
import bagel.util.*;
import java.io.*;
import java.util.*;

public class Board{
    private ArrayList<Peg> bluePegs;
    private ArrayList<Peg> greyPegs;
    private ArrayList<Peg> redPegs;
    private Peg greenPeg;
    private PowerUp powerUp;
    private static final double RED_RATIO = 0.2;


    public Board(String csvFile){
        bluePegs = new ArrayList<Peg>();
        greyPegs = new ArrayList<Peg>();
        redPegs = new ArrayList<Peg>();
        loadFile(csvFile);
        initBoard();
    }

    /**
     * read a csv file to load a new board
     *
     * @param csvFile get from CSV_FILES
     */
    public void loadFile(String csvFile){
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            String oneLine;
            while((oneLine = br.readLine()) != null){
                String[] coordinate = oneLine.split(",");
                // assign the position of peg
                int x = Integer.parseInt(coordinate[1]);
                int y = Integer.parseInt(coordinate[2]);
                Point peg_point = new Point(x,y);
                // assign the image of peg
                if(coordinate[0].equals("blue_peg")){
                    bluePegs.add(new Peg(peg_point,"res/peg.png"));
                }
                if(coordinate[0].equals("grey_peg")){
                    greyPegs.add(new Peg(peg_point,"res/grey-peg.png"));
                }
                if(coordinate[0].equals("blue_peg_horizontal")){
                    bluePegs.add(new Peg(peg_point,"res/horizontal-peg.png"));
                }
                if(coordinate[0].equals("grey_peg_horizontal")){
                    greyPegs.add(new Peg(peg_point,"res/grey-horizontal-peg.png"));
                }
                if(coordinate[0].equals("blue_peg_vertical")){
                    bluePegs.add(new Peg(peg_point,"res/vertical-peg.png"));
                }
                if(coordinate[0].equals("grey_peg_vertical")){
                    greyPegs.add(new Peg(peg_point,"res/grey-vertical-peg.png"));
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     *  initiate a board, which involves generating red pegs and initiate a new turn
     */
    public void initBoard(){
        Random random = new Random();
        int i = 0;
        // choose 1/5 bluePegs randomly, use the position of the point to generate red
        while(i < RED_RATIO * bluePegs.size()){
            int index = random.nextInt(bluePegs.size());
            Peg b_r_peg = bluePegs.get(index);
            String imageSrc = "res/red-"+b_r_peg.getShape()+"peg.png";
            redPegs.add(new Peg(b_r_peg.getRect().centre(), imageSrc));
            bluePegs.remove(b_r_peg);
            i++;
        }
        initTurn();
    }

    /**
     * initiate one turn which involves the generation of greenPeg and powerUp
     */
    public void initTurn(){
        Random random = new Random();
        String imageSrc;
        powerUp = null;
        // the previous greenPeg turn back to blue and randomly generate a new greenPeg from bluePegs
        if(greenPeg != null){
            imageSrc = "res/"+ greenPeg.getShape()+"peg.png";
            Peg g_b_peg = new Peg(greenPeg.getRect().centre(),imageSrc);
            bluePegs.add(g_b_peg);
            generateGreenPeg();
        }
        //generate a green peg at the beginning of the board.
        // if there is no bluePegs on the screen, greenPeg will not be generated.
        else if(bluePegs.size()!= 0){
            generateGreenPeg();
        }
        // 1/10 chance a powerUp will be created at a random position
        int num = random.nextInt(10);
        if(num == 7){
            powerUp = new PowerUp(new Point(Math.random() * Window.getWidth(), Math.random() * Window.getHeight()));
        }


    }

    public void update() {
        for(Peg b_peg : bluePegs){
            b_peg.update();
        }
        for(Peg g_peg : greyPegs){
            g_peg.update();
        }
        for(Peg r_peg : redPegs){
            r_peg.update();
        }
        if(greenPeg != null){
            greenPeg.update();
        }

    }

    /**
     * Choose a random blue peg to become greenPeg
     */
    private void generateGreenPeg() {
        Random random = new Random();
        int index = random.nextInt(bluePegs.size());
        String imageSrc = "res/green-"+ bluePegs.get(index).getShape()+"peg.png";
        greenPeg = new Peg(bluePegs.get(index).getRect().centre(),imageSrc);
        bluePegs.remove(index);

    }


    /**
     * getters and setters
     */

    public ArrayList<Peg> getBluePegs() {
        return bluePegs;
    }

    public ArrayList<Peg> getGreyPegs() {
        return greyPegs;
    }

    public ArrayList<Peg> getRedPegs() {
        return redPegs;
    }

    public Peg getGreenPeg() {
        return greenPeg;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setGreenPeg(Peg greenPeg) {
        this.greenPeg = greenPeg;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }
}

