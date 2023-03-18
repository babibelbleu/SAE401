package fr.weshdev.sae401.teacher.classes;

import javafx.scene.image.Image;

public class ImageObject {
    private static class Content{
        private final Image ImageContenue;
        private Content(Image Contenue) {
        this.ImageContenue = Contenue;
        }
        private Image getImageContenue(){
            return ImageContenue;
        }
    }
    public static class Accesor{
        private Content Content;
        public Accesor(Image Contenue){
           Content=new Content(Contenue);
        }
        public Image getContent(){
            return Content.getImageContenue();
        }
    }
}
