import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class AppJava {
    private  JFrame mainFrame;
    private BufferedImage loadedImage;
    private ImageIcon imageIcon;
    private JLabel picLabel;
    private JProgressBar progressBar;
    private File file;
    private  JPanel rightPanel;
    private  JLabel sizeLabel;
    private Filters filter = new Filters();

    void start(){

        mainFrame = new JFrame("My PS");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(600,400);

        initMenuBar();
        initUI();

        mainFrame.setVisible(true);
    }

    private void initUI() {
        Container mainPanel = mainFrame.getContentPane();
        mainPanel.setLayout(new BorderLayout());

        imageIcon = new ImageIcon();
        picLabel = new JLabel(imageIcon);

        mainPanel.add(picLabel);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

        mainPanel.add(progressBar,BorderLayout.SOUTH);

        rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);

        sizeLabel = new JLabel();
        sizeLabel.setForeground(Color.white);
        rightPanel.add(sizeLabel);
        mainPanel.add(rightPanel, BorderLayout.SOUTH);


    }

    private void initMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu=new JMenu("File");
        JMenuItem fileOpenItem= new JMenuItem("Open");
        fileOpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));

        JMenuItem fileSaveItem= new JMenuItem("Save");
        fileSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));

        JMenuItem fileCloseItem= new JMenuItem("Close");
        fileCloseItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));


        JMenu infoMenu=new JMenu("Info");
        JMenuItem infoAboutItem= new JMenuItem("About");


        JMenu zoomMenu=new JMenu("Zoom");
        JMenuItem zoomIn= new JMenuItem("ZoomIn");
        zoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,ActionEvent.CTRL_MASK));

        JMenuItem zoomOut= new JMenuItem("ZoomOut");
        zoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,ActionEvent.CTRL_MASK));

        JMenu filtersMenu = new JMenu("Filters");
        JMenuItem original = new JMenuItem("Original");
        JMenuItem gray = new JMenuItem("Gray");
        JMenuItem blur = new JMenuItem("Blur");
        JMenuItem lighting = new JMenuItem("Lighting");
        JMenuItem histo = new JMenuItem("Histo");





        fileOpenItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileOpenDialog();
            }
        });

        fileSaveItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveImage(loadedImage,"new_"+file.getName());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        fileCloseItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Close pressed");
                mainFrame.dispose();
            }
        });

        infoAboutItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"My PS 1.0. \n Karimov Ikhtiar","About programm",JOptionPane.INFORMATION_MESSAGE);
            }
        });




        zoomIn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomImage(loadedImage,1);
            }
        });

        zoomOut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomImage(loadedImage,0);
            }
        });





        original.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawImg(loadedImage);

            }
        });

        gray.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BufferedImage grayImage = filter.filterBAW(loadedImage);

                drawImg(grayImage);

                try {
                    saveImage(grayImage,"gray_"+file.getName());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,"Error","My PS",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        blur.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BufferedImage blurImage = filter.filterBlur(loadedImage);

                drawImg(blurImage);

                try {
                    saveImage(blurImage,"blur_"+file.getName());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,"Error","My PS",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        lighting.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BufferedImage lightingImage = filter.filterLighting(loadedImage);

                drawImg(lightingImage);

                try {
                    saveImage(lightingImage,"lighting_"+file.getName());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,"Error","My PS",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        histo.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BufferedImage histoImage = filter.histo(loadedImage);

                imageIcon.setImage(histoImage);
                picLabel.repaint();

                try {
                    saveImage(histoImage,"histo_"+file.getName());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,"Error","My PS",JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        fileMenu.add(fileOpenItem);
        fileMenu.add(fileSaveItem);
        fileMenu.add(fileCloseItem);

        infoMenu.add(infoAboutItem);

        zoomMenu.add(zoomIn);
        zoomMenu.add(zoomOut);

        filtersMenu.add(original);
        filtersMenu.add(gray);
        filtersMenu.add(blur);
        filtersMenu.add(lighting);
        filtersMenu.add(histo);


        menuBar.add(fileMenu);
        menuBar.add(infoMenu);
        menuBar.add(zoomMenu);
        menuBar.add(filtersMenu);
        mainFrame.setJMenuBar(menuBar);
    }

    private void showFileOpenDialog(){
        JFileChooser chooser = new JFileChooser();
        int resultCode = chooser.showOpenDialog(mainFrame);
        if(resultCode==JFileChooser.APPROVE_OPTION){
            file = chooser.getSelectedFile();
            openSelectedFile(file);
        }

    }

    private void openSelectedFile(File file) {
        progressBar.setVisible(true);

        try {
            loadedImage = ImageIO.read(file);
            imageIcon.setImage(getScaledImage(loadedImage,picLabel.getWidth(),picLabel.getHeight()));
            picLabel.repaint();
            sizeLabel.setText(loadedImage.getWidth()+"x"+loadedImage.getHeight());
        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error","My PS",JOptionPane.ERROR_MESSAGE);

        }finally {
            progressBar.setVisible(false);
        }
    }

    private Image getScaledImage(BufferedImage srcImg, int width, int height){

        float widthRatio = srcImg.getWidth() / (float)width;
        float heightRatio = srcImg.getHeight()/ (float)height;

        float maxRatio = Math.max(widthRatio,heightRatio);

        int resultWidth = (int)(srcImg.getWidth()/maxRatio);
        int resultHeight = (int)(srcImg.getHeight()/maxRatio);

        BufferedImage resizedImg = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_ARGB);



        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, resultWidth, resultHeight, null);
        g2.dispose();

        return resizedImg;
    }

    private void saveImage(BufferedImage image, String fileName) throws IOException {
        File output=new File(fileName);
        ImageIO.write(image,"jpg",output);

    }

    private void zoomImage(BufferedImage img, int c){
        try {
            imageIcon.setImage(getScaledImage(loadedImage,  img.getWidth(), img.getHeight()));
            if(c==1) {
                imageIcon.setImage(getScaledImage(img, (int) (img.getWidth() * 1.2f), (int) (img.getHeight() * 1.2f)));
            }
            else if(c==0){
                imageIcon.setImage(getScaledImage(img,(int)(img.getWidth()/1.2f),(int)(img.getHeight()/1.2f)));
            }

            picLabel.repaint();
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error","My PS",JOptionPane.ERROR_MESSAGE);
        }


    }

    private void drawImg(BufferedImage image){
        try{
        imageIcon.setImage(getScaledImage(image,picLabel.getWidth(),picLabel.getHeight()));
        picLabel.repaint();}
        catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error","My PS",JOptionPane.ERROR_MESSAGE);
        }
    }
}
