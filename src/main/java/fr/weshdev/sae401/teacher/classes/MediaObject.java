package fr.weshdev.sae401.teacher.classes;

import javafx.scene.media.Media;

public class MediaObject {
    private static class Content{
        private final Media MediaContenue;
        private Content(Media Contenue) {
        this.MediaContenue = Contenue;
        }
        private Media getMediaContenue(){
            return MediaContenue;
        }
    }
    public static class Accesor{
        private Content Content;
        public Accesor(Media Contenue){
           Content=new Content(Contenue);
        }
        public Media getContent(){
            return Content.getMediaContenue();
        }
    }
}
