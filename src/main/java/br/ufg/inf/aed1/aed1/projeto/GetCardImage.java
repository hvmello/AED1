package br.ufg.inf.aed1.aed1.projeto;

import br.ufg.inf.aed1.aed1.carta.*;
import br.ufg.inf.aed1.aed1.gameplay.*;
import br.ufg.inf.aed1.aed1.network.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import sun.net.www.http.HttpClient;

public class GetCardImage {

    //paramaetros: objeto Carta, e fonte da imagem da carta, podendo ser um url ou caminho de diretorio local
    // https://stackoverflow.com/questions/27525874/download-images-from-a-https-url-in-java
    public static String httpGetImage(CartaMonstro carta, String src) throws Exception {

        String name = carta.getNome() + ".jpg";
        //String folder = "./";
        String folder = "Files/img_Cartas/";
        String urlImg = src;
        File dir = new File(folder);

        if (!dir.exists()) {
            if (!dir.mkdir()) {
                return null;
            }
        }

        File file = new File(folder + name);

        //checa se src é um arquivo local, e se for, realiza um POST pelo http para o site "yugiohcardmaker.net"
        if (!src.contains(".http") && !src.contains(".com") && !src.contains(".net") && !src.contains("www.")) {
            urlImg = httpPostImage(src);
        }
        String url = "http://www.yugiohcardmaker.net/ycmaker/createcard.php?name=" + carta.getNome()
                + "&cardtype=Monster&subtype=normal&attribute=" + carta.getTipoAtributo()
                + "&level=" + carta.getQuantidadeEstrelas()
                + "&rarity=Common&picture=" + urlImg
                + "&circulation=&set1=" + carta.getSet()
                + "&set2=" + String.format("%03d", carta.getId())
                + "&type=" + carta.getTipoAtributo()
                + "&carddescription=" + carta.getDescricao().replace("\n", "%0A")
                + "&atk=" + carta.getATK()
                + "&def=" + carta.getDEF()
                + "&creator=&year=2018&serial=23849947";

        url = url.replace(" ", "%20"); // em url espaço da problema, colocando o código do espaço ( 20 )
        try {

            URLConnection conn = new URL(url).openConnection();
            conn.connect();

            //pode ser comentado / removido, apenas deixei aqui para fins de teste
            System.out.println("\ndownload: \n");
            System.out.println(">> URL: " + url);
            System.out.println(">> Name: " + name);
            System.out.println(">> size: " + conn.getContentLength() + " bytes");

            InputStream in = conn.getInputStream();
            OutputStream out = new FileOutputStream(file);

            int b = 0;

            while (b != -1) {
                b = in.read();
                if (b != -1) {
                    out.write(b);
                }
            }

            out.close();
            in.close();

            System.out.println("\ncomplete download\n");
        } catch (MalformedURLException e) {
            System.out.println("url: " + url + " invalid");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getPath();
    }

    public static String httpGetImage(CartaMagica cartaMagica, String src) throws Exception {

        String name = cartaMagica.getNome() + ".jpg";
        String cardType = "", trap_spell_Type = "";
        //String folder = "./";
        String folder = "Files/img_Cartas/";
        String urlImg = src;
        File dir = new File(folder);

        if (!dir.exists()) {
            if (!dir.mkdir()) {
                return null;
            }
        }

        File file = new File(folder + name);

        //checa se src é um arquivo local, e se for, realiza um POST pelo http para o site "yugiohcardmaker.net"
        if (!src.contains(".http") && !src.contains(".com") && !src.contains(".net") && !src.contains("www.")) {
            urlImg = httpPostImage(src);
        }
        if (cartaMagica.getTipoEfeitoMagico() == CartaMagica.TipoEfeitoMagico.TRAP) {
            cardType = "Trap";
            trap_spell_Type = "Counter";
        }
        if (cartaMagica.getTipoEfeitoMagico() == CartaMagica.TipoEfeitoMagico.CAMPO) {
            cardType = "Spell";
            trap_spell_Type = "Field";
        }
        String url = "http://www.yugiohcardmaker.net/ycmaker/createcard.php?name=" + cartaMagica.getNome()
                + "&cardtype=" + cardType
                + "&trapmagictype=" + trap_spell_Type
                + "&rarity=Common&picture=" + urlImg
                + "&circulation=&set1=" + cartaMagica.getSet()
                + "&set2=" + String.format("%03d", cartaMagica.getId())
                + "&carddescription=" + cartaMagica.getDescricao().replace("\n", "%0A")
                + "&creator=&year=2018&serial=23849947";

        url = url.replace(" ", "%20"); // em url espaço da problema, colocando o código do espaço ( 20 )
        try {

            URLConnection conn = new URL(url).openConnection();
            conn.connect();

            //pode ser comentado / removido, apenas deixei aqui para fins de teste
            System.out.println("\ndownload: \n");
            System.out.println(">> URL: " + url);
            System.out.println(">> Name: " + name);
            System.out.println(">> size: " + conn.getContentLength() + " bytes");

            InputStream in = conn.getInputStream();
            OutputStream out = new FileOutputStream(file);

            int b = 0;

            while (b != -1) {
                b = in.read();
                if (b != -1) {
                    out.write(b);
                }
            }

            out.close();
            in.close();

            System.out.println("\ncomplete download\n");
        } catch (MalformedURLException e) {
            System.out.println("url: " + url + " invalid");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getPath();
    }

    //https://stackoverflow.com/questions/17173435/send-image-file-using-java-http-post-connections
    private static String httpPostImage(String src) throws Exception {

//        String url = null;
//
//        HttpClient httpclient = new HttpClient();
//        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//
//    
//        //HttpPost httppost = new HttpPost("http://localhost:9000/upload");
//        HttpPost httppost = new HttpPost("https://www.yugiohcardmaker.net/ycmaker/uploadimage.php");
//        File file = new File(src);
//        
//        if( !file.isFile() ) {
//            System.out.println("File \""+ file.getPath() +" \" not found!");
//            return "";
//        }
//
//        MultipartEntity mpEntity = new MultipartEntity();
//        ContentBody cbFile = new FileBody(file, "image/png");
//        mpEntity.addPart("userfile", cbFile);
//
//
//        httppost.setEntity(mpEntity);
//        //System.out.println("executing request " + httppost.getRequestLine());
//        HttpResponse response = httpclient.execute(httppost);
//        HttpEntity resEntity = response.getEntity();
//            
//        if (resEntity != null) {
//            String temp = toString(resEntity);
//         
//            int index = temp.indexOf("document.getElementById(\"picture\").value=");
//            if(index < 0) index = temp.indexOf("document.getElementById('picture').value=");
//            
//            //obtém o endereço url / do servidor de onde foi armazenada (através do método POST) a imagem da carta, ex: "tempimages/051333317.png"
//            url = temp.substring( index+42, temp.indexOf("\";", index+42) );
//        }
//        if (resEntity != null) {
//            resEntity.consumeContent();
//        }
//
//        httpclient.getConnectionManager().shutdown();
//  
//        return url;
        return null;
    }

}
