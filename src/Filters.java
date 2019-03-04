import java.awt.*;
import java.awt.image.BufferedImage;

class Filters {
    BufferedImage filterBAW(BufferedImage image) {

        BufferedImage new_img =new BufferedImage(image.getWidth(),image.getHeight(),image.getType());

        for(int x=0;x<image.getWidth();x++){
            for(int y=0;y<image.getHeight();y++){
                Color color = new Color(image.getRGB(x,y));

                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();


                int gray = (int) (red * 0.299 + green * 0.587 + blue * 0.114);

                Color newColor = new Color(gray, gray, gray);

                new_img.setRGB(x,y,newColor.getRGB());

            }
        }
        return  new_img;
    }

    BufferedImage filterBlur(BufferedImage image){
        BufferedImage newImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int x=1;x<image.getWidth()-1;x++){
            for(int y=1;y<image.getHeight()-1;y++){
                Color color0 = new Color(image.getRGB(x-1,y-1));
                Color color1 = new Color(image.getRGB(x-1,y));
                Color color2 = new Color(image.getRGB(x-1,y+1));
                Color color3 = new Color(image.getRGB(x-1,y));
                Color color4 = new Color(image.getRGB(x,y));
                Color color5 = new Color(image.getRGB(x+1,y));
                Color color6 = new Color(image.getRGB(x+1,y-1));
                Color color7 = new Color(image.getRGB(x+1,y));
                Color color8 = new Color(image.getRGB(x+1,y+1));

                int red = color0.getRed()/9+color1.getRed()/9+color2.getRed()/9+color3.getRed()/9+color4.getRed()/9+color5.getRed()/9+color6.getRed()/9+color7.getRed()/9+color8.getRed()/9;
                int green = color0.getGreen()/9+color1.getGreen()/9+color2.getGreen()/9+color3.getGreen()/9+color4.getGreen()/9+color5.getGreen()/9+color6.getGreen()/9+color7.getGreen()/9+color8.getGreen()/9;
                int blue = color0.getBlue()/9+color1.getBlue()/9+color2.getBlue()/9+color3.getBlue()/9+color4.getBlue()/9+color5.getBlue()/9+color6.getBlue()/9+color7.getBlue()/9+color8.getBlue()/9;

                if(red>255)red=255;
                else if(red<0)red=0;
                if(green>255)green=255;
                else if(green<0)green=0;
                if(blue>255)blue=255;
                else if(blue<0)blue=0;

                Color myColor = new Color(red,green,blue);

                newImage.setRGB(x,y,myColor.getRGB());

            }
        }
        return newImage;
    }

    BufferedImage filterLighting(BufferedImage image){
        float Vmax=0,Vmin=0;


        BufferedImage newImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);

        for(int x=1;x<image.getWidth();x++){
            for(int y=1;y<image.getHeight();y++){

                Color color = new Color(image.getRGB(x,y));

                int red = color.getRed();
                int green =color.getGreen();
                int blue = color.getBlue();


                float R=red/255f;
                float G=green/255f;
                float B=blue/255f;

                float Cmax=Math.max(R,G);
                Cmax=Math.max(Cmax,B);

                float V=Cmax;
                if(Vmax<V)Vmax=V;
                if(Vmin>V)Vmin=V;



            }
        }

        for(int x=1;x<image.getWidth();x++){
            for(int y=1;y<image.getHeight();y++){

                Color color = new Color(image.getRGB(x,y));

                int red = color.getRed();
                int green =color.getGreen();
                int blue = color.getBlue();


                float R=red/255f;
                float G=green/255f;
                float B=blue/255f;

                float Cmax=Math.max(R,G);
                Cmax=Math.max(Cmax,B);

                float Cmin=Math.min(R,G);
                Cmin=Math.min(Cmin,B);

                float delta=Cmax-Cmin;

                float H=0,S,V=Cmax;

                if(delta==0){
                    H=0;
                }
                else if(Cmax==R){
                    H=60*(((G-B)/delta)%6);
                }
                else if(Cmax==G){
                    H=60*(((B-R)/delta)+2);
                }
                else if(Cmax==B){
                    H=60*(((R-G)/delta)+4);
                }

                if(Cmax==0){
                    S=0;
                }
                else {
                    S=delta/Cmax;
                }

                float V0=(V-Vmin)*(1/(Vmax-Vmin));

                float C=V0*S;
                float X=C*(1-Math.abs((H/60)%2-1));
                float m=V0-C;


                if(0<=H && H<60){
                    R=C;
                    G=X;
                    B=0;
                }
                else if (60<=H && H<120){
                    R=X;
                    G=C;
                    B=0;
                }
                else if (120<=H && H<180){
                    R=0;
                    G=C;
                    B=X;
                }
                else if (180<=H && H<240){
                    R=0;
                    G=X;
                    B=C;
                }
                else if (240<=H && H<300){
                    R=X;
                    G=0;
                    B=C;
                }
                else if (300<=H && H<360){
                    R=C;
                    G=0;
                    B=X;
                }
                red=(int)((R+m)*255);
                green=(int)((G+m)*255);
                blue=(int)((B+m)*255);


                if(red>255)red=255;
                else if(red<0)red=0;
                if(green>255)green=255;
                else if(green<0)green=0;
                if(blue>255)blue=255;
                else if(blue<0)blue=0;


                Color myColor = new Color(red,green,blue);

                newImage.setRGB(x,y,myColor.getRGB());

            }
        }

        return newImage;
    }

    BufferedImage histo(BufferedImage image){
        BufferedImage hist_img = new BufferedImage(256,100,BufferedImage.TYPE_INT_RGB);

        int []arr = new int [256];

        for(int x=0;x<256;x++){
            arr[x]=0;
            for(int y=0;y<100;y++){
                Color color = new Color(255,255,255);
                hist_img.setRGB(x,y,color.getRGB());
            }
        }



        for(int x=1;x<image.getWidth();x++){
            for(int y=1;y<image.getHeight();y++){

                Color color = new Color(image.getRGB(x,y));

                int red = color.getRed();
                int green =color.getGreen();
                int blue = color.getBlue();


                float R=red/255f;
                float G=green/255f;
                float B=blue/255f;

                float Cmax=Math.max(R,Math.max(G,B));

                float V= Cmax;
                V= V*100;
                arr[(int)V]++;

            }
        }
        int sum=0;

        for(int i=0;i<256;i++)
            sum+=arr[i];

        int ss=0;
        for(int x=0; x<256; x++) {
            for (int y = 99, j = 0; j < (arr[x]*100)/sum; j++, y--) {
                Color color = new Color(0, 0, 0);
                hist_img.setRGB(x, y, color.getRGB());
            }
            ss+=(arr[x]*100)/sum;
        }
        System.out.println(ss);
        return  hist_img;
    }
}
